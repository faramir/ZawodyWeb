package pl.umk.mat.zawodyweb.judgemanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class WWWListener extends Thread {

    public static final Logger logger = Logger.getLogger(WWWListener.class);
    private Properties properties;
    private ServerSocket wwwSocket;
    private ConcurrentLinkedQueue<Integer> submitsQueue;
    private String[] addresses;
    //private int soTimeout;

    public WWWListener(ServerSocket wwwSocket, Properties properties, ConcurrentLinkedQueue<Integer> submitsQueue) {
        super();
        this.properties = properties;
        this.wwwSocket = wwwSocket;
        this.submitsQueue = submitsQueue;
        addresses = properties.getProperty("WWW_ADDRESSES").split("[ ]+");
        /*
        try {
            soTimeout = Integer.parseInt(properties.getProperty("WWW_TIMEOUT", "10000"));
        } catch (NumberFormatException ex) {
            soTimeout = 10000;
        }
        */
    }

    private boolean isAccepted(String address) {
        for (String a : addresses) {
            if (address.equals(a)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket wwwClient = wwwSocket.accept();
                if (isAccepted(wwwClient.getInetAddress().getHostAddress()) == false) {
                    logger.warn("Refused WWW connection from: " + wwwClient.getInetAddress().getHostAddress());

                    wwwClient.close();

                    continue;
                }
                // wwwClient.setSoTimeout(soTimeout);

                logger.debug("WWW connected from: " + wwwClient.getInetAddress().getHostAddress());

                DataInputStream in = new DataInputStream(wwwClient.getInputStream());
                DataOutputStream out = new DataOutputStream(wwwClient.getOutputStream());

                int submitId = in.readInt();

                logger.debug("WWW gets submit_id = " + submitId);

                submitsQueue.add(submitId);

                out.writeInt(submitId);
                out.flush();

                wwwClient.close();
            } catch (IOException ex) {
                logger.error("Exception occurs: ", ex);
            }
        }
    }
}
