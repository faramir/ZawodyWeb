/*
 * Copyright (c) 2014-2015, ICM UW
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.edu.icm.zawodyweb.unicore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.externalchecker.ExternalInterface;
import pl.umk.mat.zawodyweb.judge.commons.ReaderEater;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;

/**
 * @author Marek Nowicki /faramir/
 */
public class UnicoreExternal implements ExternalInterface {

    private static final Logger logger = Logger.getLogger(UnicoreExternal.class);
    private Properties properties;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
        addDefaultPropertyValue("UNICORECC_BIN_PATH", "C:\\UNICORE\\ucc-distribution-7.1.0\\bin\\ucc.bat");
    }

    private void addDefaultPropertyValue(String propertyName, String value) {
        if (this.properties.containsKey(propertyName) == false) {
            this.properties.setProperty(propertyName, value);
        }
    }

    @Override
    public String getPrefix() {
        return "UNICORE";
    }

    @Override
    public TestOutput check(String externalId, TestInput testInput, TestOutput testOutput) {
        logger.info("Checking external: " + externalId);
        List<String> command = Arrays.asList(properties.getProperty("UNICORECC_BIN_PATH"), "job-status", "-v", externalId);

        Thread threadReaderEater = null;
        Process p = null;
        String outputText = null;

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            p = pb.start();
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));

            ReaderEater readerEater = new ReaderEater(inputStream);
            threadReaderEater = new Thread(readerEater);
            threadReaderEater.start();

            p.waitFor();
            threadReaderEater.join();

            outputText = readerEater.getOutputText();
        } catch (InterruptedException | IOException ex) {
            logger.fatal("Error in executing UCC command", ex);
            return null;
        } finally {
            if (p != null) {
                p.destroy();
            }
            if (threadReaderEater != null) {
                threadReaderEater.interrupt();
            }
        }

        if (outputText == null) {
            logger.fatal("Output from UCC is null");
            return null;
        }

        if (outputText.contains("javax.xml.ws.WebServiceException")) {
            logger.error("Network error?");
            return null;
        }

        // QUEUED
        // RUNNING
        // FAILED
        // SUCCESSFUL exit code: NNN
        for (String line : outputText.split("[\\r\\n]+")) {
            logger.trace("> " + line);
            if (line.startsWith(externalId) == true) {
                String status = line.substring(externalId.length()).trim();
                if (status.startsWith("QUEUED") || status.startsWith("RUNNING")) {
                    logger.debug("Job not finished: " + line);
                    return null;
                } else if (status.startsWith("FAILED")) {
                    logger.debug("Job finished with status FAILED: " + line);
                    TestOutput output = new TestOutput();
                    updateOutputWhenFailed(output, externalId);
                    return output;
                } else if (status.startsWith("SUCCESSFUL")) {
                    logger.debug("Job finished with status SUCCESSFUL: " + line);
                    TestOutput output = new TestOutput();
                    String exitCodeString = status.substring("SUCCESSFUL exit code: ".length());
                    int exitCode = Integer.parseInt(exitCodeString);
                    logger.info("Exit code: " + exitCode);

                    switch (exitCode) {
                        case 0:
                            updateOutputWhenSuccess(output, externalId, testInput, testOutput);
                            return output;
                        case 1:
                            output.setStatus(ResultsStatusEnum.RE.getCode());
                            updateOutputWhenRuntimeError(output, externalId);
                            return output;
                        case 2:
                            logger.info("Compile error");
                            output.setStatus(ResultsStatusEnum.CE.getCode());
                            updateOutputWhenCompileError(output, externalId);
                            return output;
                        case 3:
                            logger.error("Module error");
                            output.setStatus(ResultsStatusEnum.UNKNOWN.getCode());
                            updateOutputWhenModuleError(output, externalId);
                            return output;
                        default:
                            logger.fatal("Unknown exit code");
                            return null;

                    }
                } else {
                    logger.info("Unknown status: " + status + " -- " + line);
                }
            }
        }

        logger.error("EPR not found: " + externalId);
        
        return null;
    }

    private List<String> getOutputFiles(String externalId) {
        List<String> command = new ArrayList<>(Arrays.asList(properties.getProperty("UNICORECC_BIN_PATH"), "get-output", "-v"));

        Pattern pattern = Pattern.compile("[A-Za-z0-9]{8}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{12}");
        Matcher matcher = pattern.matcher(externalId);

        String jobId;
        if (matcher.find() == false) {
            jobId = "";
            command.add("-b");
        } else {
            jobId = matcher.group();
        }
        command.add(externalId);

        logger.info("Getting output files using: " + externalId + " and jobId: " + jobId);

        Thread threadReaderEater = null;
        Process p = null;
        String outputText = null;

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            p = pb.start();
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(p.getInputStream()));

            ReaderEater readerEater = new ReaderEater(inputStream);
            threadReaderEater = new Thread(readerEater);
            threadReaderEater.start();

            p.waitFor();
            threadReaderEater.join();

            outputText = readerEater.getOutputText();
        } catch (InterruptedException | IOException ex) {
            logger.fatal("Error in executing UCC command", ex);
            return null;
        } finally {
            if (p != null) {
                p.destroy();
            }
            if (threadReaderEater != null) {
                threadReaderEater.interrupt();
            }
        }

        if (outputText == null) {
            logger.fatal("Output from UCC is null");
            return null;
        }

        System.out.println("output: " + outputText);

        List<String> files = new ArrayList<>();
        try {
            Files.list(Paths.get(jobId)).forEach(path -> files.add(path.toFile().toString()));
        } catch (IOException ex) {
            logger.fatal("Unable to read from directory", ex);
            return null;
        }

        return files;
    }

    private void updateOutputWhenFailed(TestOutput output, String externalId) {
        List<String> files = getOutputFiles(externalId);
        String outputText = null;
        for (String stdoutPath : files) {
            if (stdoutPath.contains("stderr")) {
                try {
                    outputText = new String(Files.readAllBytes(Paths.get(stdoutPath)), StandardCharsets.UTF_8);
                    break;
                } catch (IOException ex) {
                    logger.error("Unable read stderr file", ex);
                }
            }
        }
        if (outputText == null) {
            logger.info("No readable stderr file?");
            output.setStatus(ResultsStatusEnum.UNKNOWN.getCode());
            return;
        }

        String[] lines = outputText.split("[\\r\\n]+");
        int i = lines.length;
        while (i > 0) {
            String line = lines[--i];
            if (line.startsWith("slurmstepd:") == false) {
                ++i;
                break;
            }
        }
        String notes = "";
        output.setStatus(ResultsStatusEnum.UNKNOWN.getCode());
        while (i < lines.length) {
            String line = lines[i++];
            logger.trace(line);
            if (line.contains("exceeded memory limit")) {
                logger.debug("Job exceeded memory limit");
                output.setStatus(ResultsStatusEnum.MLE.getCode());
            } else if (line.contains("DUE TO TIME LIMIT")) {
                logger.debug("Job exceeded time limit");
                output.setStatus(ResultsStatusEnum.TLE.getCode());
            }
            notes += line + "\n";
        }
        output.setNotes(notes);
    }

    private void updateOutputWhenSuccess(TestOutput output, String externalId, TestInput testInput, TestOutput testOutput) {
        List<String> files = getOutputFiles(externalId);
        String stdoutText = null;
        String stderrText = null;
        for (String filePath : files) {
            if (stdoutText == null && filePath.contains("stdout")) {
                try {
                    stdoutText = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    logger.error("Unable read stdout file", ex);
                }
            }
            if (stderrText == null && filePath.contains("stderr")) {
                try {
                    stderrText = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    logger.error("Unable read stderr file", ex);
                }
            }
        }
        if (stdoutText == null) {
            logger.info("No readable stdout file?");
            stdoutText = "";
        }
        if (stderrText == null) {
            logger.info("No readable stderr file?");
            stderrText = "";
        }

        String[] lines = stderrText.split("[\\r\\n]+");
        int i = lines.length;
        Pattern pattern = Pattern.compile("^### ELAPSED TIME IN NANOS: (?<nanos>[0-9]+) ###$");
        while (i > 0) {
            String line = lines[--i];
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String nanos = matcher.group("nanos");
                output.setRuntime((int) (Long.parseLong(nanos) / 1_000_000));
                break;
            }
        }

        Scanner stdoutScanner = new Scanner(stdoutText);
        Scanner testScanner = new Scanner(testOutput.getOutputText());
        while (stdoutScanner.hasNext() && testScanner.hasNext()) {
            String stdout = stdoutScanner.next();
            String test = testScanner.next();
            if (stdout.equals(test) == false) {
                output.setStatus(ResultsStatusEnum.WA.getCode());
                output.setPoints(0);
                return;
            }
        }
        if (stdoutScanner.hasNext() == testScanner.hasNext()) {
            output.setStatus(ResultsStatusEnum.ACC.getCode());
            output.setPoints(testInput.getMaxPoints());
        } else {
            output.setStatus(ResultsStatusEnum.WA.getCode());
            output.setPoints(0);

        }
    }

    private void updateOutputWhenRuntimeError(TestOutput output, String externalId) {
        List<String> files = getOutputFiles(externalId);
        String stderrText = null;
        for (String filePath : files) {
            if (filePath.contains("stderr")) {
                try {
                    stderrText = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
                    break;
                } catch (IOException ex) {
                    logger.error("Unable read stderr file", ex);
                }
            }
        }
        if (stderrText == null) {
            logger.info("No readable stderr file?");
            stderrText = "";
        }

        String[] lines = stderrText.split("[\\r\\n]+");
        int i = lines.length;
        Pattern pattern = Pattern.compile("^### ELAPSED TIME IN NANOS: (?<nanos>[0-9]+) ###$");
        while (i > 0) {
            String line = lines[--i];
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String nanos = matcher.group("nanos");
                output.setRuntime((int) (Long.parseLong(nanos) / 1_000_000));
                break;
            }
        }
    }

    private void updateOutputWhenCompileError(TestOutput output, String externalId) {
        List<String> files = getOutputFiles(externalId);
        String stderrText = null;
        for (String filePath : files) {
            if (filePath.contains("stderr")) {
                try {
                    stderrText = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
                    break;
                } catch (IOException ex) {
                    logger.error("Unable read stderr file", ex);
                }
            }
        }
        if (stderrText == null) {
            logger.info("No readable stderr file?");
            stderrText = "";
        }

        String[] lines = stderrText.split("[\\r\\n]+");
        int stop = -1;
        int i = lines.length;
        Pattern pattern = Pattern.compile("^### ERROR: COMPILER_ERROR: (?:[0-9]+) ###$");
        while (i > 0) {
            String line = lines[--i];
            if (stop < 0) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    stop = i;
                }
            } else {
                if ("### COMPILE FILES ###".equals(line)) {
                    String notes = "";
                    for (int j = i + 1; j < stop; ++j) {
                        notes += lines[j] + "\n";
                    }
                    output.setNotes(notes);
                    return;
                }
            }
        }
    }

    private void updateOutputWhenModuleError(TestOutput output, String externalId) {
        List<String> files = getOutputFiles(externalId);
        String stderrText = null;
        for (String filePath : files) {
            if (filePath.contains("stderr")) {
                try {
                    stderrText = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
                    break;
                } catch (IOException ex) {
                    logger.error("Unable read stderr file", ex);
                }
            }
        }
        if (stderrText == null) {
            logger.info("No readable stderr file?");
            stderrText = "";
        }

        String[] lines = stderrText.split("[\\r\\n]+");
        int stop = -1;
        int i = lines.length;
        Pattern pattern = Pattern.compile("^### ERROR: MODULE_ERROR: (?:[0-9]+) ###$");
        while (i > 0) {
            String line = lines[--i];
            if (stop < 0) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    stop = i;
                }
            } else {
                if ("### MODULE LOAD ###".equals(line)) {
                    String notes = "";
                    for (int j = i + 1; j < stop; ++j) {
                        notes += lines[j] + "\n";
                    }
                    output.setNotes(notes);
                    return;
                }
            }
        }
    }
}
