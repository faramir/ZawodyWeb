package pl.umk.mat.zawodyweb.www;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class JudgeManagerConnector {

    private static final Logger logger = Logger.getLogger(JudgeManagerConnector.class);
    private static JudgeManagerConnector INSTANCE;
    private String host;
    private String port;

    private JudgeManagerConnector() {
        INSTANCE = this;
    }

    public static JudgeManagerConnector getInstance() {
        return INSTANCE;
    }

    public void sentToJudgeManager(Integer submitId) {
        if (submitId == null) {
            return;
        }
        new Connector(submitId).start();
    }

    private class Connector extends Thread {

        Integer submitId = null;

        public Connector(Integer submitId) {
            super();
            this.submitId = submitId;
        }

        @Override
        public void run() {
            Socket jmSocket = null;
            try {
                logger.debug("Sending (" + submitId + ") to JudgeManager");

                jmSocket = new Socket(InetAddress.getByName(host), Integer.parseInt(port));

                DataOutputStream output = new DataOutputStream(jmSocket.getOutputStream());
                DataInputStream input = new DataInputStream(jmSocket.getInputStream());

                output.writeInt(submitId);
                output.flush();

                Integer submitRecv = input.readInt();

                if (submitId.equals(submitRecv) == false) {
                    logger.debug("Something goes wrong (" + submitId + "!=" + submitRecv + ")");
                } else {
                    logger.debug("Successfully send submit(" + submitId + ")!");
                }


            } catch (IOException ex) {
                logger.error("Exception occurs: ", ex);
            } finally {
                try {
                    jmSocket.close();
                } catch (IOException ex) {
                    logger.error("Exception occurs: ", ex);
                }
            }
        }
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }
}
