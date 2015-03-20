/*
 * Copyright (c) 2014-2015, ICM UW
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.edu.icm.zawodyweb.unicore;

import java.util.Arrays;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.externalchecker.ExternalInterface;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;

/**
 * @author Marek Nowicki /faramir/
 */
public class UnicoreExternalMainTest {

    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.TRACE);
        ExternalInterface external = new UnicoreExternal();
        String[] jobIds = {
            "fda081e3-36b2-40c4-a9c5-508ad66b19ce",
//            "50804c1b-d3a5-4e90-99fa-680950bcdde1", // doesn't exists
            //            "8b695710-8e23-4eab-86a1-12352f5ce139", // module error
            //            "a2570852-04dd-49ad-bd15-d02de494d7c2", // compile error
            //            "2282599f-bf77-4442-bd6d-0daee3cc3d24", // runtime error (no MainClass)
            //            "5ff1b1e4-f47d-4058-891e-941120b6cee5", // runtime error (System.exit(42))
            //            "ca77ef37-b23d-4726-9c69-a2a2a873c6ea", // MLE
//            "ea05b21c-5b13-48a4-865e-33ddb1f74686", // TLE
            //            "3eb5be7f-afca-49c8-9e52-82f20e3d85f4", // TLE (big stdout)
//            "140bfe03-ac85-41f7-a995-f011e8920f68", // OK (0 sekund)
//            "0c6e1ac2-bea5-4629-9918-e17458cd8d31", // OK (1 sekunda)
//            "8ded8779-7af9-45b6-bc7a-abca17fa9f0b", // OK (2 sekundy)
        //            "de9bafc7-3474-49dc-8e68-26f57996559f", // OK (0 sekund)
        };
        for (String jobId : jobIds) {
            System.out.println("===============================================================");
            String epr = String.format("https://hyx.grid.icm.edu.pl:8080/ICM-HYDRA/services/JobManagement?res=%s", jobId);
            TestInput testInput = new TestInput("", 10, 100, 10000, new java.util.Properties());
            TestOutput testOutput = new TestOutput("0 > myId: 0\n2 > myId: 2\n5 > myId: 5\n"
                    + "3 > myId: 3\n1 > myId: 1\n6 > myId: 6\n7 > myId: 7\n4 > myId: 4\n"
                    + "9 > myId: 9\n8 > myId: 8\n"
                    + "0 > threadCount: 10\n2 > threadCount: 10\n5 > threadCount: 10\n"
                    + "3 > threadCount: 10\n6 > threadCount: 10\n1 > threadCount: 10\n"
                    + "4 > threadCount: 10\n7 > threadCount: 10\n9 > threadCount: 10\n"
                    + "8 > threadCount: 10");
            external.setProperties(new Properties());
            TestOutput output = external.check(epr,
                    testInput,
                    testOutput);
            if (output != null) {
                System.out.println("status:  " + ResultsStatusEnum.getByCode(output.getStatus()));
                System.out.println("points:  " + output.getPoints());
                System.out.println("runtime: " + output.getRuntime());
                System.out.println("memused: " + output.getMemUsed());
                System.out.println("notes:   ");
                Arrays.stream(output.getNotes().split("[\\r\\n]+"))
                        .forEach(s -> System.out.println("notes:   " + s));
            } else {
                System.out.println("output == null");
            }
        }
    }

}
