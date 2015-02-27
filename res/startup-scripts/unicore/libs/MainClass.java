/*
 * Copyright (c) 2014-2015, ICM UW
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pcj.PCJ;
import org.pcj.StartPoint;
import org.pcj.Storage;

/**
 * @author Marek Nowicki /faramir/
 */
public class MainClass {

    public static void main(String[] args) {
        // arg0: startpoint
        // arg1: storage
        // arg2: plik z hostami
//        args = new String[]{
//            "TestPcjApp",
//            "TestPcjApp",
//            "hostfile.lst"
//        };
        if (args.length != 3) {
            throw new RuntimeException("Invalid number of parameters");
        }
        try {
            String startPointString = args[0];
            String storageString = args[1];
            String hostsFile = args[2];

            if (startPointString.endsWith(".java")) {
                startPointString = startPointString.substring(0, startPointString.length() - 5 /*".java".length()*/);
            }
            if (storageString.endsWith(".java")) {
                storageString = storageString.substring(0, storageString.length() - 5 /*".java".length()*/);
            }

            Class<StartPoint> startPoint = (Class<StartPoint>) Class.forName(startPointString, false, MainClass.class.getClassLoader());
            Class<Storage> storage = (Class<Storage>) Class.forName(storageString, false, MainClass.class.getClassLoader());

//            long time = System.currentTimeMillis();
            PCJ.start(startPoint, storage, hostsFile);
//            long elapsedTime = System.currentTimeMillis() - time;
//            System.err.println("### ELAPSED TIME: " + elapsedTime + " ms");
        } catch (Throwable ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
