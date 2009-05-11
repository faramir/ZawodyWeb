package pl.umk.mat.zawodyweb.judgemanager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class MainJudgeManager {

    public static final Logger logger = Logger.getLogger(MainJudgeManager.class);
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static ConcurrentLinkedQueue<Integer> submitsQueue;

    public static void main(String[] args) {
        int delayProcess;
        logger.info("JudgeManager start at " + sdf.format(new Date()));

        /* getting properties */
        Properties properties = new Properties();

        properties.setProperty("WWW_PORT", "8887");
        properties.setProperty("WWW_LISTEN_ADDRESS", "127.0.0.1");
        properties.setProperty("WWW_POOL", "8");
        properties.setProperty("WWW_ADDRESSES", "127.0.0.1");
        // properties.setProperty("WWW_TIMEOUT", "10000");

        properties.setProperty("JUDGE_PORT", "8888");
        properties.setProperty("JUDGE_LISTEN_ADDRESS", "");
        properties.setProperty("JUDGE_POOL", "16");
        properties.setProperty("JUDGE_ADDRESSES", "127.0.0.1 158.75.12.138");

        properties.setProperty("DELAY_PROCESS", "1800000");

        try {
            String configFile = "configuration.xml";
            if (args.length == 1) {
                configFile = args[0];
            }
            logger.info("Reading configuration file: " + configFile);
            properties.loadFromXML(new FileInputStream(configFile));
        } catch (FileNotFoundException ex) {
            logger.info("Failed to read configuration file!");
        } catch (IOException ex) {
            logger.info("Failed to read configuration file:", ex);
        }

        /* displaying properties */
        logger.debug("WWW_PORT = " + properties.getProperty("WWW_PORT"));
        logger.debug("WWW_LISTEN_ADDRESS = " + properties.getProperty("WWW_LISTEN_ADDRESS"));
        logger.debug("WWW_POOL = " + properties.getProperty("WWW_POOL"));
        logger.debug("WWW_ADDRESSES = " + properties.getProperty("WWW_ADDRESSES"));
        // logger.debug("WWW_TIMEOUT = " + properties.getProperty("WWW_TIMEOUT"));

        logger.debug("JUDGE_PORT = " + properties.getProperty("JUDGE_PORT"));
        logger.debug("JUDGE_LISTEN_ADDRESS = " + properties.getProperty("JUDGE_LISTEN_ADDRESS"));
        logger.debug("JUDGE_POOL = " + properties.getProperty("JUDGE_POOL"));
        logger.debug("JUDGE_ADDRESSES = " + properties.getProperty("JUDGE_ADDRESSES"));

        logger.debug("DELAY_PROCESS = " + properties.getProperty("DELAY_PROCESS"));

        try {
            delayProcess = Integer.parseInt(properties.getProperty("DELAY_PROCESS"));
        } catch (NumberFormatException ex) {
            delayProcess = 30 * 60 * 1000;
        }

        /* checking database */
        logger.info("Checking database for waiting submits...");

        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        SubmitsDAO submitsDAO = DAOFactory.DEFAULT.buildSubmitsDAO();

        submitsQueue = new ConcurrentLinkedQueue<Integer>();

        for (Submits s : submitsDAO.findByResult(SubmitsResultEnum.WAIT.getCode())) {
            submitsQueue.add(s.getId());
        }

        /* opening ports */
        logger.info("Opening ports...");
        ServerSocket wwwSocket = null;
        ServerSocket judgeSocket = null;
        try {
            InetAddress wwwIA = null;
            if (properties.getProperty("WWW_LISTEN_ADDRESS").isEmpty() == false) {
                wwwIA = InetAddress.getByName(properties.getProperty("WWW_LISTEN_ADDRESS"));
            }

            wwwSocket = new ServerSocket(
                    Integer.parseInt(properties.getProperty("WWW_PORT")),
                    Integer.parseInt(properties.getProperty("WWW_POOL")),
                    wwwIA);

            InetAddress judgeIA = null;
            if (properties.getProperty("JUDGE_LISTEN_ADDRESS").isEmpty() == false) {
                judgeIA = InetAddress.getByName(properties.getProperty("JUDGE_LISTEN_ADDRESS"));
            }

            judgeSocket = new ServerSocket(
                    Integer.parseInt(properties.getProperty("JUDGE_PORT")),
                    Integer.parseInt(properties.getProperty("JUDGE_POOL")),
                    judgeIA);
        } catch (IOException ex) {
            logger.fatal("Failed to bind address: ", ex);
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new ExitHookThread(wwwSocket, judgeSocket));
        logger.info("Listening...");

        /* Listening for connection from Judges*/
        new JudgesListener(judgeSocket, properties, submitsQueue, submitsDAO).start();

        /* Listening for connection from WWW */
        new WWWListener(wwwSocket, properties, submitsQueue).start();

        /* Check database for unchecked solutions in state PROCESS... */
        Integer[] prev = new Integer[0];

        while (true) {
            try {
                Thread.sleep(delayProcess);

                boolean used;

                List<Submits> submits = submitsDAO.findByResult(SubmitsResultEnum.PROCESS.getCode());
                Vector<Integer> now = new Vector<Integer>();

                for (Submits submit : submits) {
                    used = false;

                    for (Integer i : prev) {
                        if (submit.getId().equals(i)) {
                            submitsQueue.add(submit.getId());
                            used = true;

                            break;
                        }
                    }

                    if (used == false) {
                        now.add(submit.getId());
                    }
                }

                prev = (Integer[]) now.toArray();
            } catch (InterruptedException ex) {
            }
        }
    }

    static class ExitHookThread extends Thread {

        ServerSocket wwwSocket = null;
        ServerSocket judgeSocket = null;

        public ExitHookThread(ServerSocket wwwSocket, ServerSocket judgeSocket) {
            super();
            this.wwwSocket = wwwSocket;
            this.judgeSocket = judgeSocket;
        }

        @Override
        public void run() {
            try {
                if (wwwSocket != null) {
                    wwwSocket.close();
                }
                if (judgeSocket != null) {
                    judgeSocket.close();
                }
            } catch (IOException ex) {
            }
            HibernateUtil.getSessionFactory().getCurrentSession().close();

            logger.info("JudgeManager stop at " + sdf.format(new Date()));
        }
    }
}
