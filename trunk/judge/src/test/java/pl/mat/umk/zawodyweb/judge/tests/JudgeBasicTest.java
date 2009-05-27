package pl.mat.umk.zawodyweb.judge.tests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
    public void basicJudgeTest() throws IOException, InterruptedException, IOException {
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
        DataOutputStream output = new DataOutputStream(sock.getOutputStream());
        output.writeInt(id);
        Thread.sleep(15000);
        sock.close();
        ss.close();
    }
}
