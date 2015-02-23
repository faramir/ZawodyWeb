/*
 * Copyright (c) 2014-2015, ICM UW
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.edu.icm.zawodyweb.unicore;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.checker.classes.ExternalChecker;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.judge.commons.CheckerInterface;
import pl.umk.mat.zawodyweb.judge.commons.Code;
import pl.umk.mat.zawodyweb.judge.commons.CompilerInterface;
import pl.umk.mat.zawodyweb.judge.commons.Program;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;

/**
 * @author Marek Nowicki /faramir/
 */
public class UnicoreLanguageMainTest {

    public static void main(String[] args) throws IOException {
        Logger.getRootLogger().setLevel(Level.TRACE);
        
        Properties properties = new Properties();
        properties.setProperty("UNICORECC_PROPERTIES", "");
        properties.setProperty("UNICORECC_BIN_PATH", "C:\\UNICORE\\ucc-distribution-7.1.0\\bin\\ucc.bat");
        properties.setProperty("UNICORECC_JOB_TEMPLATE", "src\\main\\resources\\job_template.u");
        properties.setProperty("UNICORECC_SCRIPT_TEMPLATE", "src\\main\\resources\\script_template.sh");
        properties.setProperty("UNICORECC_GRANT_ID", "plgpcj2014");
        properties.setProperty("UNICORECC_JOB_NAME", "ZW-Unicore");
        properties.setProperty("UNICORECC_NODES_COUNT", "2");
        properties.setProperty("UNICORECC_CPU_PER_NODE", "3");
        properties.setProperty("UNICORECC_NODES_PROPERTY", "istanbul");
        properties.setProperty("UNICORECC_QUEUE", "plgrid");
        properties.setProperty("UNICORECC_RESERVATION", "PCJ");
        properties.setProperty("UNICORECC_SITE", "ICM-HYDRA");
        properties.setProperty("CODE_DIR", "temp");

        Path currentRelativePath = Paths.get(properties.getProperty("CODE_DIR"));
        Path libPath = Paths.get("src\\main\\resources\\lib");
        properties.setProperty("UNICORECC_LIB_PATH", currentRelativePath.toAbsolutePath().relativize(libPath.toAbsolutePath()).toString().replace("\\", "\\\\"));

        byte[] codeText = ("import org.pcj.*;\n"
                + "        \n"
                + "public class MyTestClass extends Storage implements StartPoint {\n"
                + "\n"
                + "    public void main() {\n"
                + "        PCJ.log(\"myId: \" + PCJ.myId());\n"
                + "//        byte[] bytes = new byte[512*1024*1024];\n"
                + "//        System.out.println(\"size: \" + bytes.length);\n"
                + "//        if (true) while(true) {\n"
                + "//            System.out.print(\".\");\n"
                + "//            System.out.flush();\n"
                + "//        }\n"
                + "//        try { Thread.sleep(30000); } catch (Throwable t) {}\n"
                + "//        System.exit(42);\n"
                + "        PCJ.log(\"threadCount: \" + PCJ.threadCount());\n"
                + "    }\n"
                + "}").getBytes();
        String filename = "MyTestClass.java";

        Properties submissionProperties = new Properties(properties);
        submissionProperties.setProperty("COMPILED_FILENAME", filename);
        submissionProperties.setProperty("CODE_FILENAME", filename);
        submissionProperties.setProperty("CODEFILE_EXTENSION", "java");

        CompilerInterface compiler = new UnicoreLanguage();
        compiler.setProperties(submissionProperties);

        CheckerInterface checker = new ExternalChecker();
        checker.setProperties(submissionProperties);

        Code code = new Code(codeText, compiler);
        Program program = code.compile();

        TestInput testInput = new TestInput("1 2 3", 100, 60000, 128, new Properties());
        TestOutput testOutput = new TestOutput("6");

        TestOutput result = checker.check(program, testInput, testOutput);
        System.out.println("status:  " + ResultsStatusEnum.getByCode(result.getStatus()));
        System.out.println("notes:   " + result.getNotes());
        System.out.println("output:  " + result.getOutputText());
        
        program.closeProgram();
    }
}
