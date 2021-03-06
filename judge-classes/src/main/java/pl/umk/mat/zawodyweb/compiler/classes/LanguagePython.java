/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.judge.commons.CompilerInterface;
import pl.umk.mat.zawodyweb.judge.commons.InterruptTimer;
import pl.umk.mat.zawodyweb.judge.commons.ReaderEater;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;
import pl.umk.mat.zawodyweb.judge.commons.WriterFeeder;

/**
 * @author Marek Nowicki /faramir/
 */
public class LanguagePython implements CompilerInterface {

    public static final org.apache.log4j.Logger logger = Logger.getLogger(LanguagePython.class);
    public static final String FAKE_IMPORTER =
            "# ---------------------------------------------------------------------------\n"
                    + "class FakeImporter(object):\n"
                    + "    def __init__(self):\n"
                    + "        from imp import new_module\n"
                    + "        self.new_module = new_module\n"
                    + "        import sys\n"
                    + "        self.sys = sys\n"
                    + "        sys.meta_path = [self]\n"
                    + "#        modules = [mod for mod in sys.modules.keys()\n"
                    + "#                       if not mod.startswith('_')\n"
                    + "#                      and not mod.startswith('encodings')\n"
                    + "#                      and not mod.startswith('exceptions')\n"
                    + "#                      and not mod.startswith('sre')]\n"
                    + "        modules = [mod for mod in sys.modules.keys()\n"
                    + "                       if mod.startswith('zipimport')\n"
                    + "                      and mod.startswith('os')\n"
                    + "                      and mod.startswith('atexit')\n"
                    + "                      and mod.startswith('dis')\n"
                    + "                      and mod.startswith('site')\n"
                    + "                      and mod.startswith('opcode')]\n"
                    + "        for mod in modules: del sys.modules[mod]\n"
                    + "    def find_module(self, fullname, path=None): return self\n"
                    + "    def load_module(self, fullname):\n"
                    + "        mod = self.new_module(fullname)\n"
                    + "        mod.__loader__ = self\n"
                    + "        self.sys.modules[fullname] = mod\n"
                    + "        mod.__file__ = '[fake module %r]' % fullname\n"
                    + "        mod.__path__ = []\n"
                    + "        return mod\n" // lub `throw ImportError`
                    + "\n"
                    + "FakeImporter()\n"
                    + "\n"
                    + "# __builtins__.open = None\n"
                    + "del __builtins__.open\n"
                    + "del __builtins__.execfile\n"
                    + "del __builtins__.file\n"
                    + "del __builtins__.compile\n"
                    + "del __builtins__.reload\n"
                    + "# ---------------------------------------------------------------------------\n";
    private Properties properties;
    private int compileResult = ResultsStatusEnum.UNDEF.getCode();
    private String compileDesc = "";

    /**
     * Sets properties for using compiler like:
     * <code>_CODE_DIR</code>,
     * <code>_CODE_FILENAME</code>,
     * <code>_CODEFILE_EXTENSION</code>,
     * <code>_COMPILED_DIR</code>,
     * <code>_COMPILED_FILENAME</code>,
     * <code>_COMPILE_TIMEOUT</code>,
     * and other user properties
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Run user program (using path) on input generating output
     *
     * @param path  path to file to execute
     * @param input input values
     * @return output values
     */
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
        List<String> command = Arrays.asList("python", path);
        if (!System.getProperty("os.name").toLowerCase().matches("(?s).*windows.*")) {
            command = Arrays.asList("bash", "-c", "ulimit -v " + (input.getMemoryLimit() * 1024) + " -t " + (5 + input.getTimeLimit() / 1000) + " && python '" + path + "'");
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
                p.destroy();
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
                    logger.debug("Abnormal Program termination.\nExit status: " + p.exitValue() + "\n" + outputText);
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

    /**
     * Modifing code before compilation.
     * <p>After first line comments inserts FakeImporter to
     * disable importing modules that are not standard and disabling open
     * builtin function</p>
     *
     * @param bytes user solution source code
     * @return modified source code
     */
    @Override
    public byte[] precompile(byte[] bytes) {
        String fullCode = new String(bytes);
        Pattern pattern = Pattern.compile("(^(?:#[^\n]*\n)*)([^#].*$)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fullCode);
        if (!matcher.matches()) {//|| matcher.groupCount() != 2
            return (FAKE_IMPORTER + "\n" + fullCode).getBytes();
        }

        String header = matcher.group(1);
        String code = matcher.group(2);

        return (header + "\n" + FAKE_IMPORTER + "\n" + code).getBytes();
    }

    /**
     * Compiling code.
     * <p>Creating .py file</p>
     *
     * @param bytes user solution code
     * @return generated executable file
     */
    @Override
    public String compile(byte[] bytes) {
        String compilefile = null;
        if (compileResult != ResultsStatusEnum.UNDEF.getCode()) {
            return "";
        }
        try {
            String codefile, compileddir;

            codefile = properties.getProperty("CODE_FILENAME");
            compileddir = properties.getProperty("COMPILED_DIR");

            codefile = codefile.replaceAll("\\.py$", "");
            compileddir = compileddir.replaceAll(File.separator + "$", "");

            codefile = compileddir + File.separator + codefile + ".py";

            OutputStream is = new FileOutputStream(codefile);
            is.write(bytes);
            is.close();
            System.gc();

            compilefile = codefile;
        } catch (Exception err) {
            logger.error("Exception when compiling", err);
        }
        return compilefile;
    }

    /**
     * Modifing path to execute after compiling
     *
     * @param path compiled file path
     * @return modified compled file path
     */
    @Override
    public String postcompile(String path) {
        return path;
    }

    /**
     * Clearing after executing all tests.
     *
     * @param path compiled file path
     */
    @Override
    public void closeProgram(String path) {
        if (!path.isEmpty()) {
            new File(path).delete();
        }
    }
}
