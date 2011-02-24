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
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;
import pl.umk.mat.zawodyweb.database.CheckerErrors;
import pl.umk.mat.zawodyweb.judge.InterruptTimer;
import pl.umk.mat.zawodyweb.judge.ReaderEater;
import pl.umk.mat.zawodyweb.judge.WriterFeeder;

/**
 *
 * @author lukash2k
 */
public class LanguageC implements CompilerInterface {

    public static final org.apache.log4j.Logger logger = Logger.getLogger(LanguageC.class);
    Properties properties;
    int compileResult = CheckerErrors.UNDEF;
    String compileDesc = "";

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

        System.gc();
        List<String> command = Arrays.asList(path);
        if (!System.getProperty("os.name").toLowerCase().matches("(?s).*windows.*")) {
            command = Arrays.asList("bash", "-c", "ulimit -v " + (input.getMemoryLimit() * 1024) + " -t " + (5 + input.getTimeLimit() / 1000) + " && '" + path + "'");
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

                WriterFeeder writerFeeder = new WriterFeeder(outputStream, input.getText());
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
                output.setResult(CheckerErrors.TLE);
                logger.debug("TLE after " + (System.currentTimeMillis() - time) + "ms.", ex);
                return output;
//            } catch (IOException ex) {
//                logger.fatal("IOException", ex);
//                p.destroy();
//            } catch (NullPointerException ex) {
//                logger.fatal("NullPointer Exception",ex);
//                output.setResult(CheckerErrors.UNDEF);
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
                    output.setResult(CheckerErrors.TLE);
                    logger.debug("TLE after " + (currentTime - time) + "ms with Exception");
                } else if (input.getTimeLimit() > 0) {
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
                output.setResult(CheckerErrors.RE);
                output.setResultDesc("Abnormal Program termination.");
                return output;
            }

            output.setText(outputText);
        } catch (Exception ex) {
            logger.fatal("Fatal Exception (timer may not be canceled)", ex);
        }

        return output;
    }

    @Override
    public byte[] precompile(byte[] code) {
        String str = new String(code);
        String forbiddenCalls = "__asm__ __asm asm access acct alarm brk chdir chown chroot clearerr clearerr_unlocked close "
                + "confstr crypt ctermid daemon dup2 dup encrypt endusershell euidaccess execl execle execlp "
                + "execv execve execvp _exit fchdir fchown fcloseall fclose fdatasync fdopen feof_unlocked "
                + "ferror ferror_unlocked fexecve fflush_unlocked fgetc fgetc_unlocked fgetpos64 fgetpos "
                + "fgets_unlocked fileno fileno_unlocked flockfile fmemopen fopen64 fopen fopencookie fork "
                + "fpathconf fprintf fputc fputc_unlocked fputs fputs_unlocked fread fread_unlocked freopen64 "
                + "freopen fscanf fseek fseeko64 fseeko fsetpos64 fsetpos ftell ftello64 ftello ftruncate64 "
                + "ftruncate ftrylockfile funlockfile fwrite fwrite_unlocked getc getc_unlocked "
                + "get_current_dir_name getcwd __getdelim getdelim getdomainname getegid geteuid getgid getgroups "
                + "gethostid gethostname getlogin getlogin_r getpagesize getpass __getpgid getpgid "
                + "getpgrp getpid getppid getsid getuid getusershell getw getwd group_member isatty lchown link "
                + "lockf64 lockf lseek nice __off64t open open_memstream pathconf pause pclose pipe popen pread64 "
                + "pread profil pthread_atfork pthread_ putc putc_unlocked putw pwrite64 pwrite read readlink "
                + "remove rename revoke rewind rmdir sbrk setbuf setbuffer setdomainname setegid seteuid setgid "
                + "sethostid sethostname setlinebuf setlogin setpgid setpgrp setregid setreuid setsid setuid "
                + "setusershell setvbuf signal sleep swab symlink sync sysconf tcgetpgrp tcsetpgrp tempnam "
                + "tmpfile64 tmpfile tmpnam tmpnam_r truncate64 truncate ttyname ttyname_r ttyslot ualarm ungetc "
                + "unlink usleep vfork vfprintf vfscanf vhangup write system "
                + "mkfifo";
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
            compileResult = CheckerErrors.RV;
        }
        return code;
    }

    @Override
    public String compile(byte[] code) {
        String compilefile = null;
        if (compileResult != CheckerErrors.UNDEF) {
            return "";
        }
        try {
            String line, codefile, compileddir, codedir;
            compilefile = properties.getProperty("COMPILED_FILENAME");
            codefile = properties.getProperty("CODE_FILENAME");
            compileddir = properties.getProperty("COMPILED_DIR");
            codedir = properties.getProperty("CODE_DIR");
            codefile = codefile.replaceAll("\\.c$", "");
            compilefile = compilefile.replaceAll("\\.exe$", "");
            codedir = codedir.replaceAll(File.separator + "$", "");
            compileddir = compileddir.replaceAll(File.separator + "$", "");
            codefile = codedir + File.separator + codefile + ".c";
            compilefile = compileddir + File.separator + compilefile + ".exe";
            OutputStream is = new FileOutputStream(codefile);
            is.write(code);
            is.close();

            System.gc();

            Process p = null;
            Thread threadReaderEater = null;
            InterruptTimer timer = new InterruptTimer();
            try {
                p = new ProcessBuilder("gcc", "-O2", "-static", "-o", compilefile, codefile, "-lm").start();
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
                compileResult = CheckerErrors.CTLE;
                return compilefile;
            } catch (Exception ex) {
                logger.error("No gcc found.");
                compileResult = CheckerErrors.UNKNOWN;
                compileDesc = "No gcc found";
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
                compileResult = CheckerErrors.CE;
                compileDesc = compileDesc.replaceAll("(?m)^.*" + Pattern.quote(codefile),  Matcher.quoteReplacement(properties.getProperty("CODE_FILENAME")));
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
