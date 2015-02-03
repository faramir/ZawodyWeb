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
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.judge.InterruptTimer;
import pl.umk.mat.zawodyweb.judge.ReaderEater;
import pl.umk.mat.zawodyweb.judge.WriterFeeder;

/**
 *
 * @author lukash2k (modified by faramir, jb)
 */
public class LanguageANSIC implements CompilerInterface {

    public static final org.apache.log4j.Logger logger = Logger.getLogger(LanguageC.class);
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
            output.setResult(compileResult);
            if (!compileDesc.isEmpty()) {
                output.setResultDesc(compileDesc);
            }
            return output;
        }

		String testInput = properties.getProperty("test.input.asargs");

        System.gc();
//        List<String> command = Arrays.asList(path);
        List<String> command;

        if (testInput != null && !testInput.isEmpty() 
		    && !testInput.equals("0") && !testInput.equalsIgnoreCase("false")) {
			   command = Arrays.asList(path + " " + input.getText());
		} else command = Arrays.asList(path);

        if (!System.getProperty("os.name").toLowerCase().matches("(?s).*windows.*")) {
            command = Arrays.asList("bash", "-c", "ulimit -v " + (input.getMemoryLimit() * 1024) + " -t " + (5 + input.getTimeLimit() / 1000) + " && '" + path + "'");

            if (testInput != null && !testInput.isEmpty() 
		        && !testInput.equals("0") && !testInput.equalsIgnoreCase("false")) {
			  command = Arrays.asList("bash", "-c", "ulimit -v " + (input.getMemoryLimit() * 1024) + " -t " + (5 + input.getTimeLimit() / 1000) + " && '" + path + "' " + input.getText().trim());
		    } else {
			  command = Arrays.asList("bash", "-c", "ulimit -v " + (input.getMemoryLimit() * 1024) + " -t " + (5 + input.getTimeLimit() / 1000) + " && '" + path + "'");
 		    }

        } else {
            logger.error("OS without bash: " + System.getProperty("os.name") + ". Memory Limit check is off.");
        }

        boolean exception = false;
        try {
/* wstawka dla plikow - poczatek (jb) */
            String testFilePath = properties.getProperty("test.file.path");
            String testFileContent = properties.getProperty("test.file.content");

 		    try {
              if (testFilePath != null && !testFilePath.isEmpty()
                && testFileContent != null && !testFileContent.isEmpty()) {
                  testFileContent = testFileContent.replaceAll("%n", "\n");
                  testFileContent = testFileContent.replaceAll("%r", "\r");
                  testFileContent = testFileContent.replaceAll("%f", "\f");

                  OutputStream tf = new FileOutputStream(testFilePath);
                  tf.write(testFileContent.getBytes());
                  tf.close();
		      }
            }
		    catch (Exception e) { }
		
            String testFilePath2 = properties.getProperty("test.file.path2");
            String testFileContent2 = properties.getProperty("test.file.content2");

		    try {
              if (testFilePath2 != null && !testFilePath2.isEmpty()
                && testFileContent2 != null && !testFileContent2.isEmpty()) {
                  testFileContent2 = testFileContent2.replaceAll("%n", "\n");
                  testFileContent2 = testFileContent2.replaceAll("%r", "\r");
                  testFileContent2 = testFileContent2.replaceAll("%f", "\f");

                  OutputStream tf = new FileOutputStream(testFilePath2);
                  tf.write(testFileContent2.getBytes());
                  tf.close();
		      }
            }
		    catch (Exception e) { }
/* wstawka dla plikow - koniec (jb) */

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
/* wstawka dla plikow - poczatek (jb) */
				if (testFilePath != null && !testFilePath.isEmpty()
				  && testFileContent != null && !testFileContent.isEmpty()) {
					new File(testFilePath).delete();
				}
				if (testFilePath2 != null && !testFilePath2.isEmpty()
				  && testFileContent2 != null && !testFileContent2.isEmpty()) {
					new File(testFilePath2).delete();
				}
/* wstawka dla plikow - koniec (jb) */
                output.setRuntime(input.getTimeLimit());
                output.setResult(ResultsStatusEnum.TLE.getCode());
                logger.debug("TLE after " + (System.currentTimeMillis() - time) + "ms.", ex);
                return output;
//            } catch (IOException ex) {
//                logger.fatal("IOException", ex);
//                p.destroy();
//            } catch (NullPointerException ex) {
//                logger.fatal("NullPointer Exception",ex);
//                output.setResult(ResultsStatusEnum.UNDEF);
//                return output;
            } catch (Exception ex) {
                logger.fatal("Fatal Exception", ex);
                exception = true;
            } finally {
/* wstawka dla plikow - poczatek (jb) */
				if (testFilePath != null && !testFilePath.isEmpty()
				  && testFileContent != null && !testFileContent.isEmpty()) {
					new File(testFilePath).delete();
				}
				if (testFilePath2 != null && !testFilePath2.isEmpty()
				  && testFileContent2 != null && !testFileContent2.isEmpty()) {
					new File(testFilePath2).delete();
				}
/* wstawka dla plikow - koniec (jb) */
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
                    output.setResult(ResultsStatusEnum.TLE.getCode());
                    logger.debug("TLE after " + (currentTime - time) + "ms with Exception");
                } else if (input.getTimeLimit() > 0) {
                    output.setRuntime(input.getTimeLimit() - 1);
                }
            }

            try {
				String testExitCode = properties.getProperty("test.output.exitcode");
                if (testExitCode != null && !testExitCode.isEmpty() 
		            && !testExitCode.equals("0") && !testExitCode.equalsIgnoreCase("false")) {
		           output.setText("" + p.exitValue());
                   return output;
			    }
                if (p.exitValue() != 0) {
                    output.setResult(ResultsStatusEnum.RE.getCode());
                    output.setResultDesc("Abnormal Program termination.\nExit status: " + p.exitValue() + "\n");
                    return output;
                }
            } catch (java.lang.IllegalThreadStateException ex) {
                logger.fatal("Fatal Exception", ex);
                output.setResult(ResultsStatusEnum.RE.getCode());
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
/*
		File zlyPlik = new File("/home/konkinf/judges/submits/2/compiled/\"main_file.c\"");
		if (zlyPlik.exists())
		   zlyPlik.delete();
*/				
        String allowBinaryDescRead = properties.getProperty("gcc.allowBinaryDescRead");
        if (allowBinaryDescRead != null && !allowBinaryDescRead.isEmpty()) {
           forbiddenCalls = forbiddenCalls.replaceAll(" read ", " ");
           forbiddenCalls = forbiddenCalls.replaceAll(" open ", " ");
           forbiddenCalls = forbiddenCalls.replaceAll(" close ", " ");
        }

        String allowBinaryStreamRead = properties.getProperty("gcc.allowBinaryStreamRead");
        if (allowBinaryStreamRead != null && !allowBinaryStreamRead.isEmpty()) {
           forbiddenCalls = forbiddenCalls.replaceAll(" fread ", " ");
           forbiddenCalls = forbiddenCalls.replaceAll(" fopen ", " ");
           forbiddenCalls = forbiddenCalls.replaceAll(" fclose ", " ");
        }

        String allowTextStreamRead = properties.getProperty("gcc.allowTextStreamRead");
        if (allowTextStreamRead != null && !allowTextStreamRead.isEmpty()) {
           forbiddenCalls = forbiddenCalls.replaceAll(" fgetc ", " ");
           forbiddenCalls = forbiddenCalls.replaceAll(" fscanf ", " ");
           forbiddenCalls = forbiddenCalls.replaceAll(" fopen ", " ");
           forbiddenCalls = forbiddenCalls.replaceAll(" fclose ", " ");
        }

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
            codefile = codefile.replaceAll("\\.c$", "");
            compilefile = compilefile.replaceAll("\\.exe$", "");
            codedir = codedir.replaceAll(File.separator + "$", "");
            compileddir = compileddir.replaceAll(File.separator + "$", "");
            codefile = codedir + File.separator + codefile + ".c";
            compilefile = compileddir + File.separator + compilefile + ".exe";

            String includeFileName = properties.getProperty("gcc.asinclude");
            String mainFile = properties.getProperty("gcc.mainfile");
            String includeFile = null;
            
            if (includeFileName != null && !includeFileName.isEmpty()
                && mainFile != null && !mainFile.isEmpty()) {
              includeFile = compileddir + File.separator + includeFileName;

              mainFile = mainFile.replaceAll("%n", "\n");
               
              OutputStream is = new FileOutputStream(includeFile);
              is.write(code);
              is.close();

              OutputStream cf = new FileOutputStream(codefile);
              cf.write(mainFile.getBytes());
              cf.close();

            } else {
              OutputStream is = new FileOutputStream(codefile);
              is.write(code);
              is.close();
            }

            String addedIncludeFileName = properties.getProperty("gcc.addinclude");
            String includeFileContent = properties.getProperty("gcc.includefilecontent");
            //String includeFile = null;
            
            if (addedIncludeFileName != null && !addedIncludeFileName.isEmpty()
                && includeFileContent != null && !includeFileContent.isEmpty()) {
              includeFile = compileddir + File.separator + addedIncludeFileName;

              includeFileContent = includeFileContent.replaceAll("%n", "\n");

              OutputStream ifile = new FileOutputStream(includeFile);
              ifile.write(includeFileContent.getBytes());
              ifile.close();

            }

            String addedModuleFileName = properties.getProperty("gcc.addmodule");
            String moduleFileContent = properties.getProperty("gcc.modulefilecontent");
            String moduleFile = null;

            if (addedModuleFileName != null && !addedModuleFileName.isEmpty()
                && moduleFileContent != null && !moduleFileContent.isEmpty()) {
              moduleFile = compileddir + File.separator + addedModuleFileName;

              moduleFileContent = moduleFileContent.replaceAll("%n", "\n");

              OutputStream mf = new FileOutputStream(moduleFile);
              mf.write(moduleFileContent.getBytes());
              mf.close();
            }

            System.gc();

            Process p = null;
            Thread threadReaderEater = null;
            InterruptTimer timer = new InterruptTimer();
            try {
                List<String> command = new ArrayList<String>(
                        Arrays.asList("gcc", "-O2", "-static", "-o", compilefile, codefile, "-lm"));

                if (moduleFile != null) {
                    command.addAll(Arrays.asList(moduleFile));
                }

                if (includeFile != null) {
                    command.addAll(Arrays.asList("-I", compileddir));
                }

                String args = properties.getProperty("gcc.args");
                if (args != null && args.isEmpty() == false) {
                    command.addAll(Arrays.asList(args.split(" ")));
                } else {
                    command.addAll(Arrays.asList("-ansi", "-Wall", "-pedantic"));
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
                logger.error("No gcc found.");
                compileResult = ResultsStatusEnum.UNKNOWN.getCode();
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
                compileResult = ResultsStatusEnum.CE.getCode();
                compileDesc = compileDesc.replaceAll("(?m)^.*" + Pattern.quote(codefile), Matcher.quoteReplacement(properties.getProperty("CODE_FILENAME")));
            }
            new File(codefile).delete();
            if (moduleFile != null) {
                new File(moduleFile).delete();
            }
			if (includeFile != null && !includeFile.isEmpty()) {
                new File(includeFile).delete();
            }
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
