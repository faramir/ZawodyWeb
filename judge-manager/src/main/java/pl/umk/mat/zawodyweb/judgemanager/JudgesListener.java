package pl.umk.mat.zawodyweb.judgemanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class JudgesListener extends Thread {

    public static final Logger logger = Logger.getLogger(JudgesListener.class);
    private ServerSocket judgeSocket;
    private ConcurrentLinkedQueue<Integer> submitsQueue;
    private SubmitsDAO submitsDAO;
    private String[] addresses;

    public JudgesListener(ServerSocket judgeSocket, Properties properties, ConcurrentLinkedQueue<Integer> submitsQueue, SubmitsDAO submitsDAO) {
        super();
        this.judgeSocket = judgeSocket;
        this.submitsQueue = submitsQueue;
        this.submitsDAO = submitsDAO;
        addresses = properties.getProperty("JUDGE_ADDRESSES").split("[ ]+");
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

            logger.debug("Judge connected from: " + judgeHost);

            try {
                DataInputStream in = new DataInputStream(judgeClient.getInputStream());
                DataOutputStream out = new DataOutputStream(judgeClient.getOutputStream());

                while (true) {
                    submitId = submitsQueue.poll();
                    logger.debug("submit_id from queue: " + submitId);
                    if (submitId == null) {
                        delay(1000); // TODO config it
                    } else {
                        try {
                            transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

                            Submits s = submitsDAO.getById(submitId);

                            if (s != null && s.getResult().equals(SubmitsResultEnum.WAIT.getCode()) == false) {
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
                    judgeClient.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket judgeClient = judgeSocket.accept();
                if (isAccepted(judgeClient.getInetAddress().getHostAddress()) == false) {
                    logger.warn("Refused judge connection from: " + judgeClient.getInetAddress().getHostAddress());

                    judgeClient.close();

                    continue;
                }

                new JudgeWaiter(judgeClient).start();
            } catch (IOException ex) {
                logger.error("Exception occurs: ", ex);
            }
        }
    }
}
