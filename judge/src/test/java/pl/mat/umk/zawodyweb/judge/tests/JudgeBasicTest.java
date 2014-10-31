/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.mat.umk.zawodyweb.judge.tests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import pl.umk.mat.zawodyweb.judge.MainJudge;

/**
 *
 * @author lukash2k
 */
public class JudgeBasicTest {

    int id = 0; //PODAC ID SUBMITA DO SPRAWDZENIA :-)
    String configFilePath = ""; // podac sciezke do config file -- niekoniecznie

    public JudgeBasicTest() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

    @Test
    public void basicJudgeTest() {
        try {
            if (id == 0) {
                return;
            }
            ServerSocket ss = new ServerSocket(8888);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        String[] args = {configFilePath};
                        MainJudge.main(args);
                    } catch (Exception ex) {
                        Logger.getLogger(JudgeBasicTest.class).fatal(null, ex);
                    }
                }
            }).start();
            Socket sock = ss.accept();
            //Socket sock = new Socket(InetAddress.getByName("127.0.0.1"), Integer.parseInt("8887"));
            DataOutputStream output = new DataOutputStream(sock.getOutputStream());
            output.writeInt(id);
            output.flush();
            Thread.sleep(15000);
            sock.close();
            ss.close();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(JudgeBasicTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    //ss.close();
    }
}
