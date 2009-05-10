package pl.umk.mat.zawodyweb.judgemanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
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

    public static void main(String[] args) {
        logger.info("JudgeManager start at " + sdf.format(new Date()));

        /* getting properties */
        Properties properties = new Properties();

        properties.setProperty("WWW_PORT", "8887");
        properties.setProperty("WWW_LISTEN_ADDRESS", "127.0.0.1");
        properties.setProperty("WWW_POOL", "8");
        properties.setProperty("WWW_ADDRESS", "127.0.0.1");

        properties.setProperty("JUDGE_PORT", "8888");
        properties.setProperty("JUDGE_LISTEN_ADDRESS", "");
        properties.setProperty("JUDGE_POOL", "8");
        properties.setProperty("JUDGE_ADDRESSES", "127.0.0.1 158.75.12.138");

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
        logger.debug("WWW_ADDRESS = " + properties.getProperty("WWW_ADDRESS"));

        logger.debug("JUDGE_PORT = " + properties.getProperty("JUDGE_PORT"));
        logger.debug("JUDGE_LISTEN_ADDRESS = " + properties.getProperty("JUDGE_LISTEN_ADDRESS"));
        logger.debug("JUDGE_POOL = " + properties.getProperty("JUDGE_POOL"));
        logger.debug("JUDGE_ADDRESSES = " + properties.getProperty("JUDGE_ADDRESSES"));

        /* checking database */
        logger.info("Checking database for waiting submits...");

        Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        SubmitsDAO dao = DAOFactory.DEFAULT.buildSubmitsDAO();

        List<Submits> listSubmits = dao.findByResult(SubmitsResultEnum.WAIT.getCode());

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
        /* Listening for connection from WWW */
        while (true) {
            try {
                Socket wwwClient = wwwSocket.accept();
                if (wwwClient.getInetAddress().getHostAddress().equals(properties.getProperty("WWW_ADDRESS")) == false) {
                    logger.warn("www - refused connection from: " + wwwClient.getInetAddress().getHostAddress());
                    wwwClient.close();
                    continue;
                }
                logger.debug("www - connection from: " + wwwClient.getInetAddress().getHostAddress());

                DataInputStream in = new DataInputStream(wwwClient.getInputStream());
                DataOutputStream out = new DataOutputStream(wwwClient.getOutputStream());

                int submitId = in.readInt();
                out.writeInt(submitId);

                wwwClient.close();
            } catch (IOException ex) {
                logger.error("Exception occurs: ", ex);
            }
        }
    }
}
