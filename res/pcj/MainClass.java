
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.umk.mat.pcj.PCJ;
import pl.umk.mat.pcj.StartPoint;
import pl.umk.mat.pcj.Storage;

public class MainClass {

    public static void main(String[] args) {
        // arg0: startpoint
        // arg1: storage
        // arg2: liczba watkow
//        args = new String[]{
//            "pl.mat.umk.zawodyweb.pcj.TestPcjApp",
//            "pl.mat.umk.zawodyweb.pcj.TestPcjApp",
//            "3"
//        };
        try {
            if (args.length != 3) {
                throw new RuntimeException("Invalid number of parameters");
            }
            Class<StartPoint> startPoint = (Class<StartPoint>) Class.forName(args[0], false, MainClass.class.getClassLoader());
            Class<Storage> storage = (Class<Storage>) Class.forName(args[1], false, MainClass.class.getClassLoader());
            int threads = Integer.parseInt(args[2]);
            String[] hosts = new String[threads];

            for (int i = 0; i < threads; ++i) {
                hosts[i] = "localhost";
            }

            PCJ.deploy(startPoint, storage, hosts);
        } catch (Throwable ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
