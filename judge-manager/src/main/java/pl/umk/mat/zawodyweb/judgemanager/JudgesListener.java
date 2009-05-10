package pl.umk.mat.zawodyweb.judgemanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class JudgesListener extends Thread {

    public static final Logger logger = Logger.getLogger(JudgesListener.class);
    private Properties properties;
    private ServerSocket judgeSocket;
    private ConcurrentLinkedQueue<Integer> submitsQueue;
    private SubmitsDAO submitsDAO;
    private String[] addresses;

    public JudgesListener(ServerSocket judgeSocket, Properties properties, ConcurrentLinkedQueue<Integer> submitsQueue, SubmitsDAO submitsDAO) {
        this.properties = properties;
        this.judgeSocket = judgeSocket;
        this.submitsQueue = submitsQueue;
        this.submitsDAO = submitsDAO;
        addresses = properties.getProperty("JUDGE_ADDRESSES").split(" ");
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

        Socket judgeClient;

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
            logger.debug("judge - connection from: " + judgeClient.getInetAddress().getHostAddress());
            Integer submit;
            try {
                DataInputStream in = new DataInputStream(judgeClient.getInputStream());
                DataOutputStream out = new DataOutputStream(judgeClient.getOutputStream());

                while (true) {
                    submit = submitsQueue.poll();
                    if (submit == null) {
                        delay(1000);
                    } else {
                        try {
                            if (submitsDAO.getById(submit).getResult().equals(SubmitsResultEnum.WAIT.getCode()) == false) {
                                out.writeInt(submit);
                                out.flush();
                                in.readInt();
                            }
                        } catch (IOException ex) {
                            submitsQueue.add(submit);
                        }
                    }
                }
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket judgeClient = judgeSocket.accept();
                if (isAccepted(judgeClient.getInetAddress().getHostAddress()) == false) {
                    logger.warn("judge - refused connection from: " + judgeClient.getInetAddress().getHostAddress());
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
