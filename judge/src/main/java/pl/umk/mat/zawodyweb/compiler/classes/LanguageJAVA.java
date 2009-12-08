package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;
import pl.umk.mat.zawodyweb.database.CheckerErrors;
import pl.umk.mat.zawodyweb.judge.InterruptTimer;
import pl.umk.mat.zawodyweb.judge.ReaderEater;

/**
 *
 * @author lukash2k
 */
public class LanguageJAVA implements CompilerInterface {

    public static final org.apache.log4j.Logger logger = Logger.getLogger(LanguageJAVA.class);
    Properties properties;
    int compileResult = CheckerErrors.UNDEF;
    String compileDesc = new String();

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public TestOutput runTest(String path, TestInput input) {
        TestOutput output = new TestOutput(null);
        String security = properties.getProperty("JAVA_POLICY");
        if (compileResult != CheckerErrors.UNDEF) {
            output.setResult(compileResult);
            if (!compileDesc.isEmpty()) {
                output.setResultDesc(compileDesc);
            }
            return output;
        }
        BufferedReader inputStream = null;
        System.gc();
        ArrayList<String> command = new ArrayList<String>(Arrays.asList("java", "-Xmx" + input.getMemoryLimit() + "m",
                "-Xms" + input.getMemoryLimit() + "m", "-Xss" + input.getMemoryLimit() + "m"));
        if (!security.isEmpty()) {
            command.add("-Djava.security.manager");
            command.add("-Djava.security.policy=" + security);
        }
        command.add("-cp");
        command.add(path.substring(0, path.lastIndexOf(File.separator)));
        command.add(path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf(".")));
        InterruptTimer timer = null;
        try {
            timer = new InterruptTimer();
            Process p = new ProcessBuilder(command).start();
            long time = new Date().getTime();
            String outputText = "";
            try {
                timer.schedule(Thread.currentThread(), input.getTimeLimit());
                inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));

                ReaderEater readerEater = new ReaderEater(inputStream);
                Thread threadReaderEater = new Thread(readerEater);
                threadReaderEater.start();

                BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                outputStream.write(input.getText());
                outputStream.close();
                logger.debug("Waiting for program after " + (new Date().getTime() - time) + "ms.");

                p.waitFor();
                threadReaderEater.join();

                if (readerEater.getException() != null) {
                    throw readerEater.getException();
                }

                outputText = readerEater.getOutputText();
            } catch (InterruptedException ex) {
                timer.cancel();
                p.destroy();
                output.setRuntime(input.getTimeLimit());
                output.setResult(CheckerErrors.TLE);
                logger.debug("TLE after " + (new Date().getTime() - time) + "ms.", ex);
                return output;
            } catch (IOException ex) {
                logger.fatal("IOException", ex);
                p.destroy();
            } catch (Exception ex) {
                logger.fatal("Fatal Exception", ex);
                p.destroy();
            }
            long currentTime = new Date().getTime();
            timer.cancel();

            if ((int) (currentTime - time) < input.getTimeLimit()) {
                output.setRuntime((int) (currentTime - time));
            } else {
                if (input.getTimeLimit() > 0) {
                    output.setRuntime(input.getTimeLimit() - 1);
                }
            }

            try {
                if (p.exitValue() != 0) {
                    output.setResult(CheckerErrors.RE);
                    output.setResultDesc("Abnormal Program termination.\nExit status: " + p.exitValue() + "\n");
                    return output;
                }
            } catch (java.lang.IllegalThreadStateException ex) {
                logger.fatal("Fatal Exception", ex);
                p.destroy();
                output.setResult(CheckerErrors.RE);
                output.setResultDesc("Abnormal Program termination.");
                return output;
            }

            output.setText(outputText);

            p.destroy();
        } catch (Exception ex) {
            logger.fatal("Fatal Exception (timer may not be canceled)", ex);
        } finally {
            if (timer != null) {
                timer.cancel();
            }
        }
        return output;
    }

    @Override
    public byte[] precompile(byte[] code) {
        return code;
    }

    @Override
    public String compile(byte[] code) {
        String codefile = null;
        if (compileResult != CheckerErrors.UNDEF) {
            return new String();
        }
        try {
            String codedir;
            codefile = properties.getProperty("CODE_FILENAME");
            codedir = properties.getProperty("CODE_DIR");
            codefile = codefile.replaceAll("\\.java$", "");
            codedir = codedir.replaceAll(File.separator + "$", "");
            codefile = codedir + File.separator + codefile + ".java";
            OutputStream is = new FileOutputStream(codefile);
            is.write(code);
            is.close();
            System.gc();
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            ByteArrayOutputStream err = new ByteArrayOutputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if (compiler.run(new ByteArrayInputStream(new String().getBytes()), out, err, codefile) != 0) {
                compileResult = CheckerErrors.CE;
                for (String line : err.toString().split("\n")) {
                    line = line.replaceAll("^.*" + Pattern.quote(codefile), properties.getProperty("CODE_FILENAME"));
                    compileDesc = compileDesc + line + "\n";
                }
            }
        } catch (Exception err) {
            logger.error("Exception when compiling", err);
        }
        new File(codefile).delete();
        return codefile.replaceAll("\\.java$", ".class");
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
