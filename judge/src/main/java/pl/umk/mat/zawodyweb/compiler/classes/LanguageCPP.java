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
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;
import pl.umk.mat.zawodyweb.database.CheckerErrors;
import pl.umk.mat.zawodyweb.judge.InterruptTimer;

/**
 *
 * @author lukash2k
 */
public class LanguageCPP implements CompilerInterface {

    public static final org.apache.log4j.Logger logger = Logger.getLogger(LanguageCPP.class);
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
            command = Arrays.asList("bash", "-c", "ulimit -v " + (input.getMemoryLimit() * 1024) + " && '" + path + "'");
        } else {
            logger.error("OS without bash: " + System.getProperty("os.name") + ". Memory Limit check is off.");
        }
        try {
            InterruptTimer timer = new InterruptTimer();
            Process p = new ProcessBuilder(command).start();
            long time = new Date().getTime();
            timer.schedule(Thread.currentThread(), input.getTimeLimit());
            try {
                inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                outputStream.write(input.getText());
                //outputStream.flush();
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
            p.destroy();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return output;
    }

    @Override
    public byte[] precompile(byte[] code) {
        String str = new String(code);
        String forbiddenCalls = "__asm__ __asm asm access acct alarm brk chdir chown chroot clearerr clearerr_unlocked close" +
                "confstr crypt ctermid daemon dup2 dup encrypt endusershell euidaccess execl execle execlp" +
                "execv execve execvp _exit fchdir fchown fcloseall fclose fdatasync fdopen feof feof_unlocked" +
                "ferror ferror_unlocked fexecve fflush fflush_unlocked fgetc fgetc_unlocked fgetpos64 fgetpos" +
                "fgets fgets_unlocked fileno fileno_unlocked flockfile fmemopen fopen64 fopen fopencookie fork" +
                "fpathconf fprintf fputc fputc_unlocked fputs fputs_unlocked fread fread_unlocked freopen64" +
                "freopen fscanf fseek fseeko64 fseeko fsetpos64 fsetpos ftell ftello64 ftello ftruncate64" +
                "ftruncate ftrylockfile funlockfile fwrite fwrite_unlocked getc getc_unlocked" +
                "get_current_dir_name getcwd __getdelim getdelim getdomainname getegid geteuid getgid getgroups" +
                "gethostid gethostname getline getlogin getlogin_r getpagesize getpass __getpgid getpgid" +
                "getpgrp getpid getppid getsid getuid getusershell getw getwd group_member isatty lchown link" +
                "lockf64 lockf lseek nice __off64t open open_memstream pathconf pause pclose pipe popen pread64" +
                "pread profil pthread_atfork pthread_ putc putc_unlocked putw pwrite64 pwrite read readlink" +
                "remove rename revoke rewind rmdir sbrk setbuf setbuffer setdomainname setegid seteuid setgid" +
                "sethostid sethostname setlinebuf setlogin setpgid setpgrp setregid setreuid setsid setuid" +
                "setusershell setvbuf signal sleep swab symlink sync sysconf tcgetpgrp tcsetpgrp tempnam" +
                "tmpfile64 tmpfile tmpnam tmpnam_r truncate64 truncate ttyname ttyname_r ttyslot ualarm ungetc" +
                "unlink usleep vfork vfprintf vfscanf vhangup write system";
        String strWithoutComments = new String();
        int len = str.length() - 1;
        for (int i = 0; i < len; i++) {
            if (str.charAt(i) == '/' && str.charAt(i) == '*') {
                while (str.charAt(i) != '*' || str.charAt(i + 1) != '/') {
                    i++;
                }
                i += 2;
            }
            if (str.charAt(i) == '/' && str.charAt(i + 1) == '/') {
                while (str.charAt(i) != '\n') {
                    i++;
                }
                i++;
            }
            strWithoutComments = strWithoutComments + str.charAt(i);
        }
        str = strWithoutComments;
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
            return new String();
        }
        try {
            String line, codefile, compileddir, codedir;
            compilefile = properties.getProperty("COMPILED_FILENAME");
            codefile = properties.getProperty("CODE_FILENAME");
            compileddir = properties.getProperty("COMPILED_DIR");
            codedir = properties.getProperty("CODE_DIR");
            codefile = codefile.replaceAll("\\.cpp$", "");
            compilefile = compilefile.replaceAll("\\.exe$", "");
            codedir = codedir.replaceAll(File.separator + "$", "");
            compileddir = compileddir.replaceAll(File.separator + "$", "");
            codefile = codedir + File.separator + codefile + ".cpp";
            compilefile = compileddir + File.separator + compilefile + ".exe";
            OutputStream is = new FileOutputStream(codefile);
            is.write(code);
            is.close();
            System.gc();
            Process p = null;
            try {
                p = new ProcessBuilder("g++", "-O2", "-static", "-o", compilefile, codefile, "-lm").start();
            } catch (Exception ex) {
                logger.error("No g++ found.");
                compileResult = CheckerErrors.UNKNOWN;
                compileDesc = "No g++ found";
                return compilefile;
            }
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            InterruptTimer timer = new InterruptTimer();
            timer.schedule(Thread.currentThread(), Integer.parseInt(properties.getProperty("COMPILE_TIMEOUT")));
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
                while ((line = input.readLine()) != null) {
                    line = line.replaceAll("^.*" + Pattern.quote(codefile), properties.getProperty("CODE_FILENAME"));
                    compileDesc = compileDesc + line + "\n";
                }
                input.close();
            }
            new File(codefile).delete();
            p.destroy();
        } catch (Exception err) {
            err.printStackTrace();
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
