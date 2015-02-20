package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.commons.TestInput;
import pl.umk.mat.zawodyweb.commons.TestOutput;
import pl.umk.mat.zawodyweb.commons.CompilerInterface;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.commons.InterruptTimer;
import pl.umk.mat.zawodyweb.commons.ReaderEater;
import pl.umk.mat.zawodyweb.commons.WriterFeeder;

/**
 *
 * @author lukash2k (modified by faramir)
 */
public class LanguagePCJ implements CompilerInterface {

    public static final org.apache.log4j.Logger logger = Logger.getLogger(LanguagePCJ.class);
    private Properties properties;
    private int compileResult = ResultsStatusEnum.UNDEF.getCode();
    private String compileDesc = "";

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public TestOutput runTest(String path, TestInput input) {
        TestOutput output = new TestOutput(null);
        if (compileResult != ResultsStatusEnum.UNDEF.getCode()) {
            output.setStatus(compileResult);
            if (!compileDesc.isEmpty()) {
                output.setNotes(compileDesc);
            }
            return output;
        }

        String security = properties.getProperty("JAVA_POLICY");

        System.gc();
        List<String> command = new ArrayList<String>(Arrays.asList(System.getProperty("java.home") + "/bin/java", "-Xmx" + input.getMemoryLimit() + "m",
                "-Xms" + input.getMemoryLimit() + "m", "-Xss" + input.getMemoryLimit() + "m"
        ));

        if (security != null && !security.isEmpty()) {
            command.add("-Djava.security.manager");
            command.add("-Djava.security.policy=" + security);
        }

        String libraryPath = properties.getProperty("pcj.library");
        String mainClassFile = properties.getProperty("pcj.main_class");
        String codedir = properties.getProperty("CODE_DIR");
        command.add("-cp");
        command.add(codedir + File.pathSeparator + libraryPath);

        command.add(mainClassFile.substring(mainClassFile.lastIndexOf(File.separator) + 1, mainClassFile.lastIndexOf(".")));
        command.add(path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf(".")));
        command.add(path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf(".")));

        String[] threadStrings = input.getProperty().getProperty("threadCounts").split("[, ]");

        if (output.getOutputText() == null) {
            output.setOutputText("");
        }
        if (output.getNotes() == null) {
            output.setNotes("");
        }
        for (String threadString : threadStrings) {
            threadString = threadString.trim();
            if (threadString.isEmpty()) {
                continue;
            }
            try {
                int threadCount = Integer.parseInt(threadString);
                TestOutput partialOutput = execute(new ArrayList<>(command), threadCount, input);
                output.setMemUsed(Math.max(output.getMemUsed(), partialOutput.getMemUsed()));
                output.setRuntime(Math.max(output.getRuntime(), partialOutput.getRuntime()));
                output.setOutputText(output.getOutputText() + "\n>>> ---v--- " + threadCount + " ---v---\n" + partialOutput.getOutputText());
                if (partialOutput.getNotes() == null || partialOutput.getNotes().isEmpty()) {
                    partialOutput.setNotes("");
                } else {
                    partialOutput.setNotes(partialOutput.getNotes() + "\n");
                }
                partialOutput.setNotes(partialOutput.getNotes() + "time: " + partialOutput.getRuntime());

                output.setNotes(output.getNotes() + "\n>>> ---v--- " + threadCount + " ---v---\n" + partialOutput.getNotes());

                int worseResult = output.getStatus();
                int nowResult = 0;

                if (worseResult > ++nowResult && partialOutput.getStatus() == ResultsStatusEnum.UNDEF.getCode()) {
                    worseResult = nowResult;
                } else if (worseResult > ++nowResult && partialOutput.getStatus() == ResultsStatusEnum.UNKNOWN.getCode()) {
                    worseResult = nowResult;
                } else if (worseResult > ++nowResult && partialOutput.getStatus() == ResultsStatusEnum.RV.getCode()) {
                    worseResult = nowResult;
                } else if (worseResult > ++nowResult && partialOutput.getStatus() == ResultsStatusEnum.CTLE.getCode()) {
                    worseResult = nowResult;
                } else if (worseResult > ++nowResult && partialOutput.getStatus() == ResultsStatusEnum.CE.getCode()) {
                    worseResult = nowResult;
                } else if (worseResult > ++nowResult && partialOutput.getStatus() == ResultsStatusEnum.MLE.getCode()) {
                    worseResult = nowResult;
                } else if (worseResult > ++nowResult && partialOutput.getStatus() == ResultsStatusEnum.RE.getCode()) {
                    worseResult = nowResult;
                } else if (worseResult > ++nowResult && partialOutput.getStatus() == ResultsStatusEnum.TLE.getCode()) {
                    worseResult = nowResult;
                } else if (worseResult > ++nowResult && partialOutput.getStatus() == ResultsStatusEnum.WA.getCode()) {
                    worseResult = nowResult;
                } else if (worseResult > ++nowResult && partialOutput.getStatus() == ResultsStatusEnum.ACC.getCode()) {
                    worseResult = nowResult;
                }
                output.setStatus(worseResult);

            } catch (NumberFormatException ex) {
                logger.debug("Unable to parse thradCount: '" + threadString + "'.", ex);
            }
        }
        output.setOutputText(output.getOutputText().trim());
        output.setNotes(output.getNotes().trim());

        return output;
    }

    private TestOutput execute(List<String> command, int threadCount, TestInput input) {
        TestOutput output = new TestOutput(null);

        command.add(String.valueOf(threadCount));

        if (!System.getProperty("os.name").toLowerCase().matches("(?s).*windows.*")) {
            StringBuilder sb = new StringBuilder();
            for (String s : command) {
                sb.append("'");
                sb.append(s);
                sb.append("' ");
            }
            command = Arrays.asList("bash", "-c", "ulimit -t " + (5 + input.getTimeLimit() / 1000) + " && " + sb.toString() + "");
            //command = Arrays.asList("bash", "-c", "ulimit -v " + (input.getMemoryLimit() * 1024) + " -t " + (5 + input.getTimeLimit() / 1000) + " && " + sb.toString() + "");
        } else {
            logger.error("OS without bash: " + System.getProperty("os.name") + ". Memory Limit check is off.");
        }
        /*
         * {
         * StringBuilder sb = new StringBuilder(); for (String s : command) {
         * sb.append(s); sb.append(" "); } logger.error("command: " +
         * sb.toString()); }
         */
        boolean exception = false;
        try {
            InterruptTimer timer = new InterruptTimer();
            Thread threadReaderEater = null;
            Thread threadErrorEater = null;
            Thread threadWriterFeeder = null;
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(false);
            Process p = pb.start();
            long time = System.currentTimeMillis();
            String outputText = "";
            try {
                timer.schedule(Thread.currentThread(), input.getTimeLimit());
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                BufferedReader errorStream = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                ReaderEater readerEater = new ReaderEater(inputStream);
                threadReaderEater = new Thread(readerEater);
                threadReaderEater.start();

                ReaderEater errorEater = new ReaderEater(errorStream);
                threadErrorEater = new Thread(errorEater);
                threadErrorEater.start();

                WriterFeeder writerFeeder = new WriterFeeder(outputStream, input.getInputText());
                threadWriterFeeder = new Thread(writerFeeder);
                threadWriterFeeder.start();

                logger.debug("Waiting for program after " + (System.currentTimeMillis() - time) + "ms.");

                p.waitFor();
                threadReaderEater.join();
                threadWriterFeeder.join();
                threadErrorEater.join();

                outputText = readerEater.getOutputText();
            } catch (InterruptedException ex) {
                output.setRuntime(input.getTimeLimit());
                output.setStatus(ResultsStatusEnum.TLE.getCode());
                logger.debug("TLE after " + (System.currentTimeMillis() - time) + "ms.", ex);
                return output;
            } catch (Exception ex) {
                logger.fatal("Fatal Exception", ex);
                exception = true;
            } finally {
                timer.cancel();
                if (p != null) {
                    p.destroy();
                }
                if (threadReaderEater != null) {
                    threadReaderEater.interrupt();
                }
                if (threadErrorEater != null) {
                    threadErrorEater.interrupt();
                }
                if (threadWriterFeeder != null) {
                    threadWriterFeeder.interrupt();
                }
            }
            long currentTime = System.currentTimeMillis();
            if ((int) (currentTime - time) < input.getTimeLimit()) {
                output.setRuntime((int) (currentTime - time));
            } else {
                if (exception && (int) (currentTime - time) >= input.getTimeLimit()) {
                    output.setRuntime(input.getTimeLimit());
                    output.setStatus(ResultsStatusEnum.TLE.getCode());
                    logger.debug("TLE after " + (currentTime - time) + "ms with Exception");
                } else if (input.getTimeLimit() > 0) {
                    output.setRuntime(input.getTimeLimit() - 1);
                }
            }
            try {
                if (p.exitValue() != 0 && output.getStatus() != ResultsStatusEnum.TLE.getCode()) {
                    output.setStatus(ResultsStatusEnum.RE.getCode());
                    output.setNotes("Abnormal Program termination.\nExit status: " + p.exitValue() + "\n");
                    return output;
                }
            } catch (java.lang.IllegalThreadStateException ex) {
                logger.fatal("Fatal Exception", ex);
                output.setStatus(ResultsStatusEnum.RE.getCode());
                output.setNotes("Abnormal Program termination.");
                return output;
            }
            output.setOutputText(outputText);
        } catch (Exception ex) {
            logger.fatal("Fatal Exception (timer may not be canceled)", ex);
        }
        return output;
    }

    @Override
    public byte[] precompile(byte[] code) {
        return code;
    }

    @Override
    public String compile(byte[] code) {
        if (compileResult != ResultsStatusEnum.UNDEF.getCode()) {
            return "";
        }
        try {
            String codefile = properties.getProperty("CODE_FILENAME");
            String codedir = properties.getProperty("CODE_DIR");
            String mainClassFile = properties.getProperty("pcj.main_class");
            String libraryPath = properties.getProperty("pcj.library");

            String mainClassPath = mainClassFile.substring(0, mainClassFile.lastIndexOf(File.separator));

            codefile = codefile.replaceAll("\\.java$", "");
            codedir = codedir.replaceAll(File.separator + "$", "");
            codefile = codedir + File.separator + codefile + ".java";

            OutputStream is = null;
            try {
                is = new FileOutputStream(codefile);
                is.write(code);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            System.gc();
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            ByteArrayOutputStream err = new ByteArrayOutputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if (compiler.run(new ByteArrayInputStream("".getBytes()), out, err,
                    "-cp", "." + File.pathSeparator + libraryPath + File.pathSeparator + mainClassPath,
                    "-d", codedir,
                    mainClassFile,
                    codefile) != 0) {
                compileResult = ResultsStatusEnum.CE.getCode();
                for (String line : err.toString().split("\n")) {
                    line = line.replaceAll("^.*" + Pattern.quote(codefile), properties.getProperty("CODE_FILENAME"));
                    compileDesc = compileDesc + line + "\n";
                }
            }

            new File(codefile).delete();
            return codefile.replaceAll("\\.java$", ".class");
        } catch (Exception err) {
            logger.error("Exception when compiling", err);
        }
        return "";
    }

    @Override
    public String postcompile(String path) {
        return path;
    }

    @Override
    public void closeProgram(String path) {
        if (!path.isEmpty()) {
            new File(path).delete();
        }
    }

    public static void main(String[] args) {
        LanguagePCJ language = new LanguagePCJ();
        Properties properties = new Properties();
        properties.setProperty("CODE_FILENAME", "TestPcjApp");
        properties.setProperty("CODE_DIR", "C:\\Users\\UMK\\Documents\\codes\\java\\ZawodyWeb\\branches\\pcj_zw\\res\\temp");
        properties.setProperty("pcj.library", "C:\\Users\\UMK\\Documents\\codes\\java\\ZawodyWeb\\branches\\pcj_zw\\res\\pcj\\PCJ.jar");
        properties.setProperty("pcj.main_class", "C:\\Users\\UMK\\Documents\\codes\\java\\ZawodyWeb\\branches\\pcj_zw\\res\\pcj\\MainClass.java");

        language.setProperties(properties);
//        C:\Users\UMK\Documents\codes\java\ZawodyWeb\branches\pcj_zw\res\temp
        String outputPath = language.compile(("import java.util.Scanner;\n"
                + "import pl.umk.mat.pcj.PCJ;\n"
                + "import pl.umk.mat.pcj.StartPoint;\n"
                + "import pl.umk.mat.pcj.Storage;\n"
                + "\n"
                + "/**\n"
                + " *\n"
                + " * @author faramir\n"
                + " */\n"
                + "public class TestPcjApp extends Storage implements StartPoint {\n"
                + "\n"
                + "    @Override\n"
                + "    public void main() throws Throwable {\n"
                //+ "        System.out.println(\"My id: \" + PCJ.myId());\n"
                + "        if (PCJ.myId() == 0) {\n"
                + "            Scanner scanner = new Scanner(System.in);\n"
                + "            System.out.println(scanner.nextInt() * 2);\n"
                + "        }\n"
                + "    }\n"
                + "\n"
                + "}\n"
                + "").getBytes());
        System.out.println("" + language.compileResult);
        System.out.println("" + language.compileDesc);
        System.out.println("" + outputPath);
        Properties testProperty = new Properties();
        testProperty.setProperty("threadCounts", "1,2,3,4");

        TestInput testInput = new TestInput("1",
                1, 10000, 1000, testProperty);
        TestOutput runOutput = language.runTest(outputPath, testInput);
        System.out.println("" + runOutput.getOutputText() + ", " + runOutput.getPoints());
        System.out.println("" + runOutput.getNotes());
    }
}
