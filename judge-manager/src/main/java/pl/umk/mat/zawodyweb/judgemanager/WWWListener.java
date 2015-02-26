/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
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

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class WWWListener extends Thread {

    private static final Logger logger = Logger.getLogger(WWWListener.class);
    private ServerSocket wwwSocket;
    private ConcurrentLinkedQueue<Integer> submitsQueue;
    private String[] addresses;
    private int soTimeout;

    public WWWListener(ServerSocket wwwSocket, Properties properties, ConcurrentLinkedQueue<Integer> submitsQueue) {
        super("WWW Listener Thread");
        this.wwwSocket = wwwSocket;
        this.submitsQueue = submitsQueue;
        addresses = properties.getProperty("WWW_ADDRESSES").split("[ ]+");

        try {
            soTimeout = Integer.parseInt(properties.getProperty("WWW_TIMEOUT", "10000"));
        } catch (NumberFormatException ex) {
            soTimeout = 10000;
        }
    }

    private boolean isAccepted(String address) {
        for (String a : addresses) {
            if (address.equals(a)) {
                return true;
            }
        }
        return false;
    }

    private class WWWWaiter extends Thread {

        Socket wwwClient;

        public WWWWaiter(Socket wwwClient) {
            super();
            this.wwwClient = wwwClient;
        }

        @Override
        public void run() {
            try {
                wwwClient.setSoTimeout(soTimeout);

                logger.info("WWW connected from: " + wwwClient.getInetAddress().getHostAddress());

                DataInputStream in = new DataInputStream(wwwClient.getInputStream());
                DataOutputStream out = new DataOutputStream(wwwClient.getOutputStream());

                int submitId = in.readInt();

                logger.info("WWW gets submit_id = " + submitId);

                submitsQueue.add(submitId);

                out.writeInt(submitId);
                out.flush();
            } catch (IOException ex) {
                logger.info("Exception reading submit_id from WWW", ex);
            } finally {
                try {
                    wwwClient.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Override
    public void run() {
        logger.info("Listening for connection from WWW...");
        while (wwwSocket.isClosed() == false) {
            try {
                Socket wwwClient = wwwSocket.accept();
                if (isAccepted(wwwClient.getInetAddress().getHostAddress()) == false) {
                    logger.warn("Refused WWW connection from: " + wwwClient.getInetAddress().getHostAddress());

                    wwwClient.close();

                    continue;
                }

                new WWWWaiter(wwwClient).start();
            } catch (SocketException ex) {
                logger.info("WWWListener: " + ex.getMessage());
            } catch (IOException ex) {
                logger.error("Exception occurs: ", ex);
            }
        }
    }
}
