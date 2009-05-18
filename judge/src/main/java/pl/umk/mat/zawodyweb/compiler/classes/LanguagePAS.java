package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;
import pl.umk.mat.zawodyweb.database.CheckerErrors;

/**
 *
 * @author lukash2k
 */
public class LanguagePAS implements CompilerInterface {

    public static final org.apache.log4j.Logger logger = Logger.getLogger(LanguagePAS.class);
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
        List<String> command = Arrays.asList(path);
        if (!System.getProperty("os.name").toLowerCase().matches("(?s).*windows.*")) {
            command = Arrays.asList("bash", "-c", "ulimit -v " + input.getMemoryLimit() + " && " + path);
        } else {
            logger.error("OS without bash: " + System.getProperty("os.name") + ". Memory Limit check is off.");
        }
        try {
            Timer timer = new Timer();
            Process p = new ProcessBuilder(command).start();
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
                logger.debug("Waiting for program after " + (new Date().getTime() - time) + "ms.");
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
            logger.error(ex.getMessage());
        }
        return output;
    }

    @Override
    public byte[] precompile(byte[] code) {
        String str = new String(code);
        if (str.matches(".*uses\\s+crt.*")) {
            compileResult = CheckerErrors.RV;
        }
        String forbiddenCalls = "append asm assign blockread blockwrite close eof eoln erase filepos filesize flush getdir " +
                "mkdir reset rewrite rmdir seek seekeof seekeoln seekof seekoln truncate uses access acct alarm " +
                "ork chdir chown chroot clearerr clearerr_unlocked close confstr crypt ctermid daemon dup2 dup " +
                "encrypt endusershell euidaccess execl execle execlp execv execve execvp _exit fchdir fchown " +
                "fcloseall fclose fdatasync fdopen feof feof_unlocked ferror ferror_unlocked fexecve fflush " +
                "fflush_unlocked fgetc fgetc_unlocked fgetpos64 fgetpos fgets fgets_unlocked fileno " +
                "fileno_unlocked flockfile fmemopen fopen64 fopen fopencookie fork fpathconf fprintf fputc " +
                "fputc_unlocked fputs fputs_unlocked fread fread_unlocked freopen64 freopen fscanf fseek " +
                "fseeko64 fseeko fsetpos64 fsetpos ftell ftello64 ftello ftruncate64 ftruncate ftrylockfile " +
                "funlockfile fwrite fwrite_unlocked getc getc_unlocked get_current_dir_name getcwd __getdelim " +
                "getdelim getdomainname getegid geteuid getgid getgroups gethostid gethostname getline getlogin " +
                "getlogin_r getpagesize getpass __getpgid getpgid getpgrp getpid getppid getsid getuid " +
                "getusershell getw getwd group_member isatty lchown link lockf64 lockf lseek nice __off64t open " +
                "open_memstream pathconf pause pclose pipe popen pread64 pread profil pthread_atfork pthread_ " +
                "putc putc_unlocked putw pwrite64 pwrite readlink remove rename revoke rewind rmdir sbrk setbuf " +
                "setbuffer setdomainname setegid seteuid setgid sethostid sethostname setlinebuf setlogin " +
                "setpgid setpgrp setregid setreuid setsid setuid setusershell setvbuf signal sleep swab symlink " +
                "sync sysconf tcgetpgrp tcsetpgrp tempnam tmpfile64 tmpfile tmpnam tmpnam_r truncate64 truncate " +
                "ttyname ttyname_r ttyslot ualarm ungetc unlink usleep vfork vfprintf vfscanf vhangup";
        String strWithoutComments = new String();
        int len = str.length() - 1;
        for (int i = 0; i < len; i++) {
            if (str.charAt(i) == '{') {
                while (str.charAt(i) != '}') {
                    i++;
                }
                i++;
            }
            if (str.charAt(i) == '(' && str.charAt(i + 1) == '*') {
                while (str.charAt(i) != '*' || str.charAt(i + 1) != ')') {
                    i++;
                }
                i += 2;
            }
            strWithoutComments = strWithoutComments + str.charAt(i);
        }
        str = strWithoutComments;
        System.out.println(str);
        String regexp1_on = "(?s).*[^A-Za-z0-9_](" + forbiddenCalls.replaceAll(" ", "|") + ")\\s*\\([^\\)]\\).*";
        String regexp2_on = "(?s).*&(" + forbiddenCalls.replaceAll(" ", "|") + ").*";
        if (str.matches(regexp1_on) || str.matches(regexp2_on)) {
            compileResult = CheckerErrors.RV;
        }
        return code;
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
        if (ofile != null) {
            new File(ofile).delete();
        }
        return path;
    }

    @Override
    public void closeProgram(String path) {
        if (!path.isEmpty()) {
            new File(path).delete();
        }
    }
}
