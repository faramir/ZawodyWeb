package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;
import pl.umk.mat.zawodyweb.database.CheckerErrors;

/**
 *
 * @author lukash2k
 */
public class LanguagePAS implements CompilerInterface {

    Properties properties;
    String ofile;
    int compileResult = CheckerErrors.UNDEF;
    String compileDesc = new String();

    public class InterruptThread extends TimerTask {

        private Thread thread;

        public InterruptThread(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            thread.interrupt();
        }
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public TestOutput runTest(String path, TestInput input) {
        TestOutput output = new TestOutput(null);
        if (compileResult != CheckerErrors.UNDEF) {
            output.setResult(compileResult);
            if (!compileDesc.isEmpty()) {
                output.setResultDesc(compileDesc);
            }
            return output;
        }
        BufferedReader inputStream;
        System.gc();
        try {
            Process p = new ProcessBuilder(path).start();
            Timer timer = new Timer();
            long time = new Date().getTime();
            timer.schedule(new InterruptThread(Thread.currentThread()),
                    input.getTimeLimit());
            try {
                inputStream =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                outputStream.write(input.getText());
                outputStream.flush();
                outputStream.close();
                p.waitFor();
            } catch (InterruptedException ex) {
                p.destroy();
                output.setResult(CheckerErrors.TLE);
                return output;
            }
            long currentTime = new Date().getTime();
            timer.cancel();
            if (p.exitValue() != 0) {
                output.setResult(CheckerErrors.RE);
                output.setResultDesc("Abnormal Program termination.\nExit status: " + p.exitValue() + "\n");
                return output;
            }
            output.setRuntime((int) (currentTime - time));
            String outputText = new String();
            String line;
            while ((line = inputStream.readLine()) != null) {
                outputText = outputText + line + "\n";
            }
            output.setText(outputText);
        } catch (Exception ex) {
        }
        return output;
    }

    @Override
    public byte[] precompile(byte[] code) {
        String str = new String(code);
        if (str.matches(".*uses\\s+crt.*")) {
            compileResult = CheckerErrors.RV;
        }
        return str.getBytes();
    }

    @Override
    public String compile(byte[] code) {
        String compilefile = null;
        if (compileResult != CheckerErrors.UNDEF) {
            return new String();
        }
        try {
            String line, codefile, compileddir, codedir;
            compilefile = properties.getProperty("COMPILED_FILENAME");
            codefile = properties.getProperty("CODE_FILENAME");
            compileddir = properties.getProperty("COMPILED_DIR");
            codedir = properties.getProperty("CODE_DIR");
            codefile = codefile.replaceAll("\\.pas$", "");
            compilefile = compilefile.replaceAll("\\.exe$", "");
            codedir = codedir.replaceAll("\\\\$", "");
            compileddir = compileddir.replaceAll("\\\\$", "");
            ofile = compileddir + "\\" + codefile + ".o";
            codefile = codedir + "\\" + codefile + ".pas";
            compilefile = compileddir + "\\" + compilefile + ".exe";
            OutputStream is = new FileOutputStream(codefile);
            is.write(code);
            is.close();
            System.gc();
            Process p = new ProcessBuilder("ppc386", "-O2", "-XS", "-Xt", "-o" + compilefile, codefile).start();
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            Timer timer = new Timer();
            timer.schedule(new InterruptThread(Thread.currentThread()),
                    Integer.parseInt(properties.getProperty("COMPILE_TIMEOUT")));
            try {
                p.waitFor();
            } catch (InterruptedException ex) {
                p.destroy();
                compileResult = CheckerErrors.CTLE;
                return compilefile;
            }
            timer.cancel();
            if (p.exitValue() != 0) {

                compileResult = CheckerErrors.CE;
                int i = 1;
                while ((line = input.readLine()) != null) {
                    if (i > 4) {
                        compileDesc = compileDesc + line + "\n";
                    }
                    i++;
                }
                input.close();
            }
            new File(codefile).delete();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return compilefile;
    }

    @Override
    public String postcompile(String path) {
        new File(ofile).delete();
        return path;
    }

    @Override
    public void closeProgram(String path) {
        if (!path.isEmpty()) {
            new File(path).delete();
        }
    }
}