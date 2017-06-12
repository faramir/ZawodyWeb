/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;
import pl.umk.mat.zawodyweb.judge.commons.CompilerInterface;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.judge.commons.InterruptTimer;
import pl.umk.mat.zawodyweb.judge.commons.ReaderEater;
import pl.umk.mat.zawodyweb.judge.commons.WriterFeeder;

/**
 *
 * @author lukash2k (modified by faramir, jb)
 */
public class LanguageCS implements CompilerInterface {

    public static final org.apache.log4j.Logger logger = Logger.getLogger(LanguageCS.class);
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

        System.gc();
        List<String> command = Arrays.asList("mono", path);
        if (!System.getProperty("os.name").toLowerCase().matches("(?s).*windows.*")) {
            command = Arrays.asList("bash", "-c", "ulimit -v " + (input.getMemoryLimit() * 1024) + " -t " + (5 + input.getTimeLimit() / 1000) + " && '" + "mono" + " " + path + "'");
        } else {
            logger.error("OS without bash: " + System.getProperty("os.name") + ". Memory Limit check is off.");
        }

        boolean exception = false;
        try {
            InterruptTimer timer = new InterruptTimer();
            Thread threadReaderEater = null;
            Thread threadWriterFeeder = null;

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process p = pb.start();
            long time = System.currentTimeMillis();
            String outputText = "";
            try {
                timer.schedule(Thread.currentThread(), input.getTimeLimit());
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

                ReaderEater readerEater = new ReaderEater(inputStream);
                threadReaderEater = new Thread(readerEater);
                threadReaderEater.start();

                WriterFeeder writerFeeder = new WriterFeeder(outputStream, input.getInputText());
                threadWriterFeeder = new Thread(writerFeeder);
                threadWriterFeeder.start();

                logger.debug("Waiting for program after " + (System.currentTimeMillis() - time) + "ms.");

                p.waitFor();
                threadReaderEater.join();
                threadWriterFeeder.join();

//                if (readerEater.getException() != null) {
//                    throw readerEater.getException();
//                }

                outputText = readerEater.getOutputText();
            } catch (InterruptedException ex) {
                output.setRuntime(input.getTimeLimit());
                output.setStatus(ResultsStatusEnum.TLE.getCode());
                logger.debug("TLE after " + (System.currentTimeMillis() - time) + "ms.", ex);
                return output;
//            } catch (IOException ex) {
//                logger.fatal("IOException", ex);
//                p.destroy();
//            } catch (NullPointerException ex) {
//                logger.fatal("NullPointer Exception",ex);
//                output.setStatus(ResultsStatusEnum.UNDEF);
//                return output;
            } catch (Exception ex) {
                logger.fatal("Fatal Exception", ex);
                exception = true;
            } finally {
                if (timer != null) {
                    timer.cancel();
                }
                if (p != null) {
                    p.destroy();
                }
                if (threadReaderEater != null) {
                    threadReaderEater.interrupt();
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
                if (p.exitValue() != 0) {
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
        String str = new String(code);
        String forbiddenCalls = "unsafe File FileInfo Directory DirectoryInfo SystemInfo DriveInfo "
                + "FileSystemWatcher "
                + "StreamReader StreamWriter FileStream FileStreamReader FileStreamWriter "
				+ "OpenStandardError OpenStandardInput OpenStandardOutput"
                + "TcpListener TcpClient Socket SocketStream MemoryStream " 
				+ "Reflection Process " // te klasy moga na za wiele pozwalac
				+ "Thread ThreadPool " // rozwazylbym odkomentowanie (zwlaszcza Thread)
                + "ApplicationDomain ConfigurationManager ";
				// trzeba sporo dopisac
        StringBuilder strWithoutComments = new StringBuilder();
        int len = str.length() - 1;
        try {
            for (int i = 0; i < len; i++) {
                if (str.charAt(i) == '"') {
                    do {
                        strWithoutComments.append(str.charAt(i));
                        ++i;
                    } while (str.charAt(i) != '"' && str.charAt(i) != '\n');
                    strWithoutComments.append(str.charAt(i));
                    continue;
                }
                if (str.charAt(i) == '/' && str.charAt(i + 1) == '*') {
                    while (str.charAt(i) != '*' || str.charAt(i + 1) != '/') {
                        ++i;
                    }
                    ++i;
                    continue;
                }
                if (str.charAt(i) == '/' && str.charAt(i + 1) == '/') {
                    while (str.charAt(i) != '\n') {
                        ++i;
                    }
                    strWithoutComments.append(str.charAt(i));
                    continue;
                }
                strWithoutComments.append(str.charAt(i));
            }
        } catch (StringIndexOutOfBoundsException ex) {
        }
        str = strWithoutComments.toString();
        String regexp1_on = "(?s).*\\W(" + forbiddenCalls.replaceAll(" ", "|") + ")\\W.*";
        if (str.matches(regexp1_on)) {
            compileResult = ResultsStatusEnum.RV.getCode();
        }
        return code;
    }

    @Override
    public String compile(byte[] code) {
        String compilefile = null;
        if (compileResult != ResultsStatusEnum.UNDEF.getCode()) {
            return "";
        }
        try {
            String line, codefile, compileddir, codedir;
            compilefile = properties.getProperty("COMPILED_FILENAME");
            codefile = properties.getProperty("CODE_FILENAME");
            compileddir = properties.getProperty("COMPILED_DIR");
            codedir = properties.getProperty("CODE_DIR");
            codefile = codefile.replaceAll("\\.cs$", "");
            compilefile = compilefile.replaceAll("\\.exe$", "");
            codedir = codedir.replaceAll(File.separator + "$", "");
            compileddir = compileddir.replaceAll(File.separator + "$", "");
            codefile = codedir + File.separator + codefile + ".cs";
            compilefile = compileddir + File.separator + compilefile + ".exe";
            OutputStream is = new FileOutputStream(codefile);
            is.write(code);
            is.close();

            System.gc();

            Process p = null;
            Thread threadReaderEater = null;
            InterruptTimer timer = new InterruptTimer();
            try {
                List<String> command = new ArrayList<String>(
                        Arrays.asList("mcs", "-optimalize+", "-debug-", "-unsafe-", 
						"-sdk:4.5", "-out:" + compilefile, codefile));

                String args = properties.getProperty("gcc.args");
                if (args != null && args.isEmpty() == false) {
                    command.addAll(Arrays.asList(args.split(" ")));
                }

                p = new ProcessBuilder(command).start();
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                timer.schedule(Thread.currentThread(), Integer.parseInt(properties.getProperty("COMPILE_TIMEOUT")));

                ReaderEater readerEater = new ReaderEater(inputStream);
                threadReaderEater = new Thread(readerEater);
                threadReaderEater.start();

                p.waitFor();
                threadReaderEater.interrupt();

                compileDesc = readerEater.getOutputText();
            } catch (InterruptedException ex) {
                logger.error("Compile Time Limit Exceeded", ex);
                compileResult = ResultsStatusEnum.CTLE.getCode();
                return compilefile;
            } catch (Exception ex) {
                logger.error("No mcs found.");
                compileResult = ResultsStatusEnum.UNKNOWN.getCode();
                compileDesc = "No mcs found";
                return compilefile;
            } finally {
                if (timer != null) {
                    timer.cancel();
                }
                if (p != null) {
                    p.destroy();
                }
                if (threadReaderEater != null) {
                    threadReaderEater.interrupt();
                }
            }

            if (p.exitValue() != 0) {
                compileResult = ResultsStatusEnum.CE.getCode();
                compileDesc = compileDesc.replaceAll("(?m)^.*" + Pattern.quote(codefile), Matcher.quoteReplacement(properties.getProperty("CODE_FILENAME")));
            }
            new File(codefile).delete();
        } catch (Exception err) {
            logger.fatal("Fatal Exception (timer may not be canceled)", err);
        }
        return compilefile;
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
}
