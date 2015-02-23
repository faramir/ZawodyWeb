/*
 * Copyright (c) 2014-2015, ICM UW
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.edu.icm.zawodyweb.unicore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.judge.commons.CompilerInterface;
import pl.umk.mat.zawodyweb.judge.commons.ReaderEater;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;

/**
 * @author Marek Nowicki /faramir/
 */
public class UnicoreLanguage implements CompilerInterface {

    private static final Logger logger = Logger.getLogger(UnicoreLanguage.class);
    private static final Random random = new Random();
    private Properties properties;

    /**
     * <code>UNICORECC_PROPERTIES</code>,
     *
     * <code>UNICORECC_BIN_PATH</code>,
     *
     * <code>UNICORECC_JOB_TEMPLATE</code>,
     *
     * <code>UNICORECC_SCRIPT_TEMPLATE</code>,
     *
     * <code>CODE_DIR</code>,
     *
     * <code>CODE_FILENAME</code>,
     *
     * <code>UNICORECC_CPU_PER_NODE</code>
     *
     * <code>UNICORECC_GRANT_ID</code>
     *
     * <code>UNICORECC_JOB_NAME</code>
     *
     * <code>UNICORECC_LIB_PATH</code>
     *
     * <code>UNICORECC_NODES_COUNT</code>
     *
     * <code>UNICORECC_NODES_PROPERTY</code>
     *
     * <code>UNICORECC_QUEUE</code>
     *
     * <code>UNICORECC_RESERVATION</code>
     *
     * <code>UNICORECC_SITE</code>
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
        addDefaultPropertyValue("UNICORECC_JOB_NAME", "ZawodyWeb-submission");
        addDefaultPropertyValue("UNICORECC_BIN_PATH", "C:\\UNICORE\\ucc-distribution-7.1.0\\bin\\ucc.bat");
    }

    private void addDefaultPropertyValue(String propertyName, String value) {
        if (this.properties.containsKey(propertyName) == false) {
            this.properties.setProperty(propertyName, value);
        }
    }

    private String generateFilename(String prefix, String suffix) {
        long n = random.nextLong();
        if (n == Long.MIN_VALUE) {
            n = 0;
        } else {
            n = Math.abs(n);
        }
        return prefix + Long.toHexString(n) + suffix;
    }

    @Override
    public TestOutput runTest(String code, TestInput input) {
        TestOutput output = new TestOutput(null);

        String propertiesFile = properties.getProperty("UNICORECC_PROPERTIES");

        String codeDir = properties.getProperty("CODE_DIR");
        codeDir = codeDir.replaceAll(File.separator + "$", "") + File.separator;

        String codeFile = properties.getProperty("CODE_FILENAME");
        String inputFile = generateFilename("input", "");
        String jobFile = generateFilename("job", ".u");
        String scriptFile = generateFilename("script", ".sh");

        /* create JOB description */
        String jobtemplateFile = properties.getProperty("UNICORECC_JOB_TEMPLATE");
        if (jobtemplateFile == null || jobtemplateFile.isEmpty()) {
            logger.fatal("Empty or unknown UNICORECC_JOB_TEMPLATE parameter");
            return output;
        }

        try {
            String jobfileText = new String(Files.readAllBytes(Paths.get(jobtemplateFile)), StandardCharsets.UTF_8);

            for (String propertyName : properties.stringPropertyNames()) {
                if (propertyName.startsWith("UNICORECC_") == false) {
                    continue;
                }
                String value = properties.getProperty(propertyName);
                if (value == null || value.isEmpty()) {
                    continue;
                }
                propertyName = propertyName.substring("UNICORECC_".length());
                jobfileText = jobfileText.replace("${" + propertyName + "}", value);
            }

            jobfileText = jobfileText.replace("${TIME_LIMIT}", (1 + input.getTimeLimit() / 1000) + "");
            jobfileText = jobfileText.replace("${MEMORY_LIMIT}", (input.getMemoryLimit()) + "M");
            jobfileText = jobfileText.replace("${CODE_FILE}", codeFile);
            jobfileText = jobfileText.replace("${INPUT_FILE}", inputFile);
            jobfileText = jobfileText.replace("${SCRIPT_FILE}", scriptFile);
            jobfileText = jobfileText.replaceAll("\\$\\{[-_A-Za-z0-9]*\\}", "");

            try (BufferedWriter out = new BufferedWriter(new FileWriter(codeDir + jobFile))) {
                out.write(jobfileText);
            } catch (IOException ex) {
                logger.fatal("Error writing job file", ex);
                return output;
            }
        } catch (IOException ex) {
            logger.fatal("Unable to read job file template: " + jobtemplateFile, ex);
            return output;
        }


        /* create SCRIPT file */
        String scripttemplateFile = properties.getProperty("UNICORECC_SCRIPT_TEMPLATE");
        if (scripttemplateFile == null || scripttemplateFile.isEmpty()) {
            logger.fatal("Empty or unknown UNICORECC_SCRIPT_TEMPLATE parameter");
            return output;
        }

        try {
            String scriptfileText = new String(Files.readAllBytes(Paths.get(scripttemplateFile)), StandardCharsets.UTF_8);

            for (String propertyName : properties.stringPropertyNames()) {
                if (propertyName.startsWith("UNICORECC_") == false) {
                    continue;
                }
                String value = properties.getProperty(propertyName);
                if (value == null || value.isEmpty()) {
                    continue;
                }
                propertyName = propertyName.substring("UNICORECC_".length());
                scriptfileText = scriptfileText.replace("${" + propertyName + "}", value);
            }

            scriptfileText = scriptfileText.replace("${TIME_LIMIT}", (15 + input.getTimeLimit() / 1000) + "");
            scriptfileText = scriptfileText.replace("${MEMORY_LIMIT}", (input.getMemoryLimit()) + "M");
            scriptfileText = scriptfileText.replace("${CODE_FILE}", codeFile);
            scriptfileText = scriptfileText.replace("${INPUT_FILE}", inputFile);
            scriptfileText = scriptfileText.replaceAll("\\$\\{[-_A-Za-z0-9]*\\}", "");

            try (BufferedWriter out = new BufferedWriter(new FileWriter(codeDir + scriptFile))) {
                out.write(scriptfileText);
            } catch (IOException ex) {
                logger.fatal("Error writing script file", ex);
                return output;
            }
        } catch (IOException ex) {
            logger.fatal("Unable to read script file template: " + scripttemplateFile, ex);
            return output;
        }

        /* prepare code */
        try (BufferedWriter out = new BufferedWriter(new FileWriter(codeDir + codeFile))) {
            out.write(code);
        } catch (IOException ex) {
            logger.fatal("Error writing code file", ex);
            return output;
        }

        /* prepare input */
        try (BufferedWriter out = new BufferedWriter(new FileWriter(codeDir + inputFile))) {
            out.write(input.getInputText());
        } catch (IOException ex) {
            logger.fatal("Error writing input file", ex);
            return output;
        }

        /* run JOB using UNICORE Commandline Client */
        List<String> command = new ArrayList<>();
        command.addAll(Arrays.asList(properties.getProperty("UNICORECC_BIN_PATH"), "run", "-v", "-a"));
        if (propertiesFile != null && propertiesFile.isEmpty() == false) {
            command.add("-c");
            command.add(propertiesFile);
        }
        command.add(jobFile);

        Thread threadReaderEater = null;
        Process p = null;
        String outputText = null;

        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(Paths.get(codeDir).toFile());
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
            return output;
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
            return output;
        }

        /* save result (EPR from job file) in Description/Notes */
        for (String line : outputText.split("[\\r\\n]+")) { //"\\r?\\n"
            line = line.trim();
            if (!line.endsWith(".job")) {
                continue;
            }

            try (InputStream jobInputStream = new BufferedInputStream(new FileInputStream(line))) {

                JsonReader jsonReader = Json.createReader(jobInputStream);
                JsonObject json = jsonReader.readObject();
                String eprPath = json.getString("epr");

                output.setStatus(ResultsStatusEnum.EXTERNAL.getCode());
                output.setNotes("UNICORE:" + eprPath);

                return output;
            } catch (IOException ex) {
                logger.fatal("Error in reading job file: " + line, ex);
                return output;
            }
        }
        logger.error("UCC does not return EPR:");
        logger.trace(outputText);

        return output;
    }

    @Override
    public byte[] precompile(byte[] code) {
        return code;
    }

    @Override
    public String compile(byte[] code) {
        return new String(code);
    }

    @Override
    public String postcompile(String path) {
        return path;
    }

    @Override
    public void closeProgram(String path) {
        String codeDir = properties.getProperty("CODE_DIR");
        codeDir = codeDir.replaceAll(File.separator + "$", "") + File.separator;
        logger.debug("Clearing CODE_DIR: " + codeDir);
        Arrays.stream(Paths.get(codeDir).toFile().listFiles())
                .forEach((File f) -> {
                    logger.trace("deleting: " + f.toString());
                    f.delete();
                });
    }

}
