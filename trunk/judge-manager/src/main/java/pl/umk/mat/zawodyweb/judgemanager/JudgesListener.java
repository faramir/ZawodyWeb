package pl.umk.mat.zawodyweb.judgemanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class JudgesListener extends Thread {

    private static final Logger logger = Logger.getLogger(JudgesListener.class);
    private ServerSocket judgeSocket;
    private ConcurrentLinkedQueue<Integer> submitsQueue;
    private CompilerErrorHandler compilerErrorHandler;
    private String[] addresses;
    private long queueDelayTime;

    public JudgesListener(ServerSocket judgeSocket, Properties properties,
            ConcurrentLinkedQueue<Integer> submitsQueue,
            CompilerErrorHandler compilerErrorHandler) {
        this.judgeSocket = judgeSocket;
        this.submitsQueue = submitsQueue;
        this.compilerErrorHandler = compilerErrorHandler;
        addresses = properties.getProperty("JUDGE_ADDRESSES").split("[ ]+");
        queueDelayTime = Long.parseLong(properties.getProperty("JUDGE_DELAY"));
    }

    private boolean isAccepted(String address) {
        for (String a : addresses) {
            if (address.equals(a)) {
                return true;
            }
        }
        return false;
    }

    private class JudgeWaiter extends Thread {

        private Socket judgeClient;

        public JudgeWaiter(Socket judgeClient) {
            super();
            this.judgeClient = judgeClient;
        }

        private void delay(long time) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException ex) {
            }
        }

        @Override
        public void run() {
            Integer submitId;
            Transaction transaction;
            String judgeHost = judgeClient.getInetAddress().getHostAddress();

            logger.info("Judge connected from: " + judgeHost);
            try {
                DataInputStream in = new DataInputStream(judgeClient.getInputStream());
                DataOutputStream out = new DataOutputStream(judgeClient.getOutputStream());

                while (true) {
                    submitId = submitsQueue.poll();

                    if (submitId == null) {
                        out.writeInt(0); // FIXME: brzydkie, bo brzydkie... ciąg dalszy opisu w Judge
                        delay(queueDelayTime);
                    } else {
                        logger.info("submit_id from queue: " + submitId);
                        try {
                            transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

                            Submits s = DAOFactory.DEFAULT.buildSubmitsDAO().getById(submitId);
                            if (s != null) {
                                if (s.getResult().equals(SubmitsResultEnum.WAIT.getCode()) == true) {
                                    int compilerId = s.getLanguages().getClasses().getId();

                                    /*
                                     * TODO: sprawdzanie, czy dany compiler dziala dobrze
                                     *       compiler to tez uruchamiacz - nie sprawdzamy porownywacza
                                     *       ...
                                     * Nalezy sprawdzic, czy w ostatnich M minutach (w calym JudgeManager)
                                     * wystepowalo P problemow z danym compilerem:
                                     * - jesli tak, to nie procesujemy zadania i nie wrzucamy na liste,
                                     *   bo i tak po 10 minutach (DELAY_PROCESS) pojawi sie znowu
                                     * - jesli nie bylo problemow to normalnie
                                     */
                                    if (compilerErrorHandler.canUseCompiler(compilerId) == false) {
                                        logger.info("There were problems with compiler(" + compilerId + "). Not sending submit(" + submitId + ").");
                                        continue;
                                    }

                                    out.writeInt(submitId);
                                    logger.info("Sending submit(" + submitId + ") to Judge: " + judgeHost);
                                    out.flush();
                                    in.readInt();

                                    /*
                                     * TODO: po sprawdzeniu rozwiazania, gdy wynikiem nie jest DONE i MANUAL
                                     *       dodajemy informacje do JudgeManager o problemie z kompilatorem K
                                     *       i wyswietlamy stosowny komunikat
                                     */

                                    s = DAOFactory.DEFAULT.buildSubmitsDAO().getById(submitId);
                                    Integer result = s.getResult();

                                    if (result.equals(SubmitsResultEnum.DONE.getCode())
                                            || result.equals(SubmitsResultEnum.MANUAL.getCode())) {
                                        logger.info("Checked submit(" + submitId + ") by Judge: " + judgeHost);
                                    } else {
                                        logger.error("Checked submit(" + submitId + ") with ERROR(" + result + ") by Judge: " + judgeHost);
                                        compilerErrorHandler.addCompilerError(compilerId);
                                    }
                                } else {
                                    logger.info("Submit(" + submitId + ") don't have WAIT(" + SubmitsResultEnum.WAIT.getCode() + ") status - it have (" + s.getResult() + ")");
                                }
                            } else {
                                logger.info("Error getting submit(" + submitId + ")");
                            }

                            transaction.commit();
                        } catch (SocketException ex) {
                            logger.info("Exception occurs. Add submit(" + submitId + ") to queue", ex);
                            submitsQueue.add(submitId);
                            return;
                        } catch (IOException ex) {
                            logger.info("Exception occurs. Add submit(" + submitId + ") to queue", ex);
                            submitsQueue.add(submitId);
                        }
                    }
                }
            } catch (IOException ex) {
            } finally {
                try {
                    logger.info("Judge disconnected: " + judgeHost);
                    judgeClient.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Override
    public void run() {
        logger.info("Listening for connection from Judges...");
        while (judgeSocket.isClosed() == false) {
            try {
                Socket judgeClient = judgeSocket.accept();
                if (isAccepted(judgeClient.getInetAddress().getHostAddress()) == false) {
                    logger.warn("Refused judge connection from: " + judgeClient.getInetAddress().getHostAddress());

                    judgeClient.close();

                    continue;
                }

                new JudgeWaiter(judgeClient).start();
            } catch (SocketException ex) {
                logger.info("JudgesListener: " + ex.getMessage());
            } catch (IOException ex) {
                logger.error("Exception occurs: ", ex);
            }
        }
    }
}
