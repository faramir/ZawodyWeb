/*
 * Copyright (c) 2009-2015, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.judge;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.database.SubmitsStateEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Classes;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.pojo.Tests;
import pl.umk.mat.zawodyweb.judge.commons.BinaryClassLoader;
import pl.umk.mat.zawodyweb.judge.commons.CheckerInterface;
import pl.umk.mat.zawodyweb.judge.commons.ClassInfo;
import pl.umk.mat.zawodyweb.judge.commons.Code;
import pl.umk.mat.zawodyweb.judge.commons.CompilerInterface;
import pl.umk.mat.zawodyweb.judge.commons.Program;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;

/**
 *
 * @author lukash2k
 * @author faramir
 */
public class MainJudge {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(MainJudge.class);
    private static final List<ClassInfo> classes = new ArrayList<>();
    private static final Properties properties = new Properties();
    private static long delayConnect;

    private static void connectToJudgeManager() throws IOException, InstantiationException, IllegalAccessException {
        /*
         * connecting to JudgeManager
         */
        Socket sock = null;
        try {
            sock = new Socket(InetAddress.getByName(properties.getProperty("JUDGEMANAGER_HOST")), Integer.parseInt(properties.getProperty("JUDGEMANAGER_PORT")));
            DataInputStream input = new DataInputStream(sock.getInputStream());
            DataOutputStream output = new DataOutputStream(sock.getOutputStream());
            logger.info("Connection with JudgeManager on " + properties.getProperty("JUDGEMANAGER_HOST") + ":" + properties.getProperty("JUDGEMANAGER_PORT") + "...");

            while (true) {
                /*
                 * receiving submit_id
                 */
                int id;
                try {
                    id = input.readInt();
                    if (id == 0) {
                        continue; // FIXME: brzydkie, bo brzydkie, ale przynajmniej w ten sposób można sprawdzić, czy połączenie jest utrzymane...
                    }
                    logger.info("Received submit id: " + id);
                } catch (IOException ex) {
                    logger.error("Connection to JudgeManager closed, shutting down Judge...");
                    break;
                }

                Transaction transaction = null;
                try {
                    Properties submissionProperties = new Properties(properties);

                    /*
                     * change submit status to PROCESS
                     */
                    logger.info("Setting submit status to PROCESS...");

                    transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
                    Submits submit = DAOFactory.DEFAULT.buildSubmitsDAO().getById(id);
                    submit.setState(SubmitsStateEnum.PROCESS.getCode());
                    DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
                    transaction.commit();

                    /*
                     * getting submit
                     */
                    transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
                    //if (!sock.isConnected()) { // FIXME: po co to było?!
                    //    logger.error("Connection to JudgeManager closed, shutting down Judge...");
                    //    break;
                    //}

                    submit = DAOFactory.DEFAULT.buildSubmitsDAO().getById(id);
                    byte[] codeText = submit.getCode();
                    String filename = submit.getFilename();
                    if (filename != null && !filename.isEmpty()) {
                        submissionProperties.setProperty("COMPILED_FILENAME", filename);
                        submissionProperties.setProperty("CODE_FILENAME", filename);
                    }
                    Classes compilerClasses = submit.getLanguages().getClasses();

                    /*
                     * downloading compiler class
                     */
                    logger.info("Downloading compiler class...");

                    int iVectorClassInfo;
                    boolean found = false;
                    for (iVectorClassInfo = 0; iVectorClassInfo < classes.size(); ++iVectorClassInfo) {
                        if (classes.get(iVectorClassInfo).getId() == compilerClasses.getId()) {
                            found = true;
                            break;
                        }
                    }

                    CompilerInterface compiler;
                    if (found) {
                        if (classes.get(iVectorClassInfo).getVersion() >= compilerClasses.getVersion()) {
                            compiler = (CompilerInterface) new BinaryClassLoader().loadCompiledClass(classes.get(iVectorClassInfo).getFilename(), classes.get(iVectorClassInfo).getCode()).newInstance();
                        } else {
                            classes.get(iVectorClassInfo).setVersion(compilerClasses.getVersion());
                            classes.get(iVectorClassInfo).setCode(compilerClasses.getCode());
                            compiler = (CompilerInterface) new BinaryClassLoader().loadCompiledClass(classes.get(iVectorClassInfo).getFilename(), classes.get(iVectorClassInfo).getCode()).newInstance();
                        }
                    } else {
                        classes.add(new ClassInfo(compilerClasses.getId(), compilerClasses.getFilename(), compilerClasses.getCode(), compilerClasses.getVersion()));
                        compiler = (CompilerInterface) new BinaryClassLoader().loadCompiledClass(compilerClasses.getFilename(), compilerClasses.getCode()).newInstance();
                    }
                    submissionProperties.setProperty("CODEFILE_EXTENSION", submit.getLanguages().getExtension());

                    submissionProperties.putAll(submit.getLanguages().loadProperties());
                    submissionProperties.putAll(submit.getProblems().loadProperties());

                    compiler.setProperties(submissionProperties);

                    /*
                     * downloading diff class
                     */
                    logger.info("Downloading diff class...");
                    Classes diffClasses = submit.getProblems().getClasses();
                    found = false;
                    for (iVectorClassInfo = 0; iVectorClassInfo < classes.size(); iVectorClassInfo++) {
                        if (classes.get(iVectorClassInfo).getId() == diffClasses.getId()) {
                            found = true;
                            break;
                        }
                    }

                    CheckerInterface checker;
                    if (found) {
                        if (classes.get(iVectorClassInfo).getVersion() >= diffClasses.getVersion()) {
                            checker = (CheckerInterface) new BinaryClassLoader().loadCompiledClass(classes.get(iVectorClassInfo).getFilename(), classes.get(iVectorClassInfo).getCode()).newInstance();
                        } else {
                            classes.get(iVectorClassInfo).setVersion(diffClasses.getVersion());
                            classes.get(iVectorClassInfo).setCode(diffClasses.getCode());
                            checker = (CheckerInterface) new BinaryClassLoader().loadCompiledClass(classes.get(iVectorClassInfo).getFilename(), classes.get(iVectorClassInfo).getCode()).newInstance();
                        }
                    } else {
                        classes.add(new ClassInfo(diffClasses.getId(), diffClasses.getFilename(), diffClasses.getCode(), diffClasses.getVersion()));
                        checker = (CheckerInterface) new BinaryClassLoader().loadCompiledClass(diffClasses.getFilename(), diffClasses.getCode()).newInstance();
                    }
                    checker.setProperties(submissionProperties);

                    /*
                     * compilation
                     */
                    Code code = new Code(codeText, compiler);
                    logger.info("Trying to compile the code...");
                    Program program = code.compile();

                    /*
                     * downloading tests
                     */
                    logger.info("Downloading tests...");
                    Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Tests.class);
                    c.add(Restrictions.eq("problems.id", submit.getProblems().getId()));
                    c.addOrder(Order.asc("testorder"));
                    List<Tests> tests = c.list();

                    TestInput testInput;
                    TestOutput testOutput;
                    boolean undefinedResult = false;
                    boolean manualResult = false;
                    boolean externalResult = false;

                    /*
                     * TESTING
                     */
                    logger.info("Starting tests...");
                    for (Tests test : tests) {
                        logger.info("Test " + test.getTestorder() + " started.");
                        testInput = new TestInput(test.getInput(), test.getMaxpoints(), test.getTimelimit(), submit.getProblems().getMemlimit(), test.loadProperties());
                        testOutput = new TestOutput(test.getOutput());
                        /*
                         * tutaj przydaloby sie wykonywanie w petli, poki
                         * checker.check() nie skonczy liczyc, ustawianie
                         * statusu na progress (np. co 10 sekund - to
                         * ustawienia)..
                         *
                         * ale czy mozna otworzyc druga transakcje?
                         */
                        TestOutput result = checker.check(program, testInput, testOutput);
                        if (result.getStatus() == ResultsStatusEnum.UNDEF.getCode()) {
                            undefinedResult = true;
                            logger.info("Test " + test.getTestorder() + " has result UNDEFINDED. Stopping.");
                            break;
                        } else if (result.getStatus() == ResultsStatusEnum.EXTERNAL.getCode()) {
                            externalResult = true;
                        } else if (result.getStatus() == ResultsStatusEnum.MANUAL.getCode()) {
                            manualResult = true;
                        }

                        /*
                         * saving result to database
                         */
                        Results dbResult = new Results();
                        dbResult.setMemory(result.getMemUsed());
                        dbResult.setRuntime(result.getRuntime());
                        if (result.getNotes() != null) {
                            dbResult.setNotes(result.getNotes().replaceAll("[\000-\007]", " "));
                        }
                        dbResult.setPoints(result.getPoints());
                        dbResult.setStatus(result.getStatus());
                        dbResult.setSubmits(submit);
                        dbResult.setTests(test);
                        DAOFactory.DEFAULT.buildResultsDAO().save(dbResult);
                        logger.info("Test " + test.getTestorder() + " finished with result " + result.getStatus() + "(" + result.getNotes() + ").");
                    }

                    /*
                     * finish testing
                     */
                    logger.info("All tests finished. Closing program.");
                    program.closeProgram();

                    /*
                     * successfully completed?
                     */
                    if (undefinedResult == true) {
                        logger.error("Some of the tests got UNDEFINED result -- this should not happend.");
                        transaction.rollback();
                    } else if (externalResult == true) {
                        logger.info("Saving results to database (with external).");
                        submit.setState(SubmitsStateEnum.EXTERNAL.getCode());
                        DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
                        transaction.commit();
                    } else if (manualResult == true) {
                        logger.info("Saving results to database (with manual).");
                        submit.setState(SubmitsStateEnum.MANUAL.getCode());
                        DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
                        transaction.commit();
                    } else {
                        logger.info("Saving results to database.");
                        submit.setState(SubmitsStateEnum.DONE.getCode());
                        DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
                        transaction.commit();
                    }
                } catch (Exception e) {
                    logger.error("Exception occurs -- rolling back.", e);
                    if (transaction != null) {
                        transaction.rollback();
                    }
                }

                /*
                 * send ACK to JudgeManager
                 */
                logger.info("Processing SubmitID: " + id + " finished.");
                output.writeInt(id);
            }
            // dalton said: "never": HibernateUtil.getSessionFactory().getCurrentSession().close();
        } finally {
            if (sock != null) {
                sock.close();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String configFile = MainJudge.class.getResource("").getPath() + "configuration.xml";
        if (args.length == 1 && !args[0].isEmpty()) {
            configFile = args[0];
        }

        /*
         * Default settings for properties
         */
        properties.setProperty("JUDGEMANAGER_HOST", "127.0.0.1");
        properties.setProperty("JUDGEMANAGER_PORT", "8088");
        properties.setProperty("JUDGEMANAGER_DELAY_CONNECT", "10000");

        properties.setProperty("COMPILED_DIR", "");
        properties.setProperty("CODE_DIR", "");

        properties.setProperty("COMPILED_FILENAME", "a");
        properties.setProperty("CODE_FILENAME", "a");

        properties.setProperty("COMPILE_TIMEOUT", "60000");

        properties.setProperty("JAVA_POLICY", "");

        try {
            logger.info("Reading configuration file from " + configFile + "...");
            properties.loadFromXML(new FileInputStream(configFile));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        try {
            delayConnect = Integer.parseInt(properties.getProperty("JUDGEMANAGER_DELAY_CONNECT"));
        } catch (NumberFormatException ex) {
            delayConnect = 10 * 1000;
        }

        /* displaying properties */
        for (String propertyName : properties.stringPropertyNames()) {
            logger.info(propertyName + " = " + properties.getProperty(propertyName));
        }
        
//        logger.info("JUDGEMANAGER_HOST = " + properties.getProperty("JUDGEMANAGER_HOST"));
//        logger.info("JUDGEMANAGER_PORT = " + properties.getProperty("JUDGEMANAGER_PORT"));
//        logger.info("JUDGEMANAGER_DELAY_CONNECT = " + properties.getProperty("JUDGEMANAGER_DELAY_CONNECT"));
//
//        logger.info("COMPILED_DIR = " + properties.getProperty("COMPILED_DIR"));
//        logger.info("CODE_DIR = " + properties.getProperty("CODE_DIR"));
//
//        logger.info("COMPILED_FILENAME = " + properties.getProperty("COMPILED_FILENAME"));
//        logger.info("CODE_FILENAME = " + properties.getProperty("CODE_FILENAME"));
//
//        logger.info("COMPILE_TIMEOUT = " + properties.getProperty("COMPILE_TIMEOUT"));
//
//        logger.info("JAVA_POLICY = " + properties.getProperty("JAVA_POLICY"));

        while (true) {
            try {
                connectToJudgeManager();
            } catch (Exception ex) {
                logger.error("Exception occurs: ", ex);
                try {
                    Thread.sleep(delayConnect);
                } catch (InterruptedException ex1) {
                }
            } catch (Error error) {
                logger.error("Error occurs: ", error);
                try {
                    Thread.sleep(delayConnect);
                } catch (InterruptedException ex1) {
                }
            }
        }
    }
}
