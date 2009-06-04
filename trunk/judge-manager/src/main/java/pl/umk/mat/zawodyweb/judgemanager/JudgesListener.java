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
    private String[] addresses;
    private long queueDelayTime;

    public JudgesListener(ServerSocket judgeSocket, Properties properties, ConcurrentLinkedQueue<Integer> submitsQueue) {
        super();
        this.judgeSocket = judgeSocket;
        this.submitsQueue = submitsQueue;
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
                        logger.debug("submit_id from queue: " + submitId);
                        try {
                            transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

                            Submits s = DAOFactory.DEFAULT.buildSubmitsDAO().getById(submitId);
                            if (s != null && s.getResult().equals(SubmitsResultEnum.WAIT.getCode()) == true) {
                                out.writeInt(submitId);
                                logger.info("Send submit(" + submitId + ") to Judge: " + judgeHost);
                                out.flush();
                                in.readInt();
                            }

                            transaction.commit();
                        } catch (IOException ex) {
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
