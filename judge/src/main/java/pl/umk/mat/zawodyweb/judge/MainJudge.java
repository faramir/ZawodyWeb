/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import pl.umk.mat.zawodyweb.checker.CheckerInterface;
import pl.umk.mat.zawodyweb.checker.CheckerResult;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.Code;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;
import pl.umk.mat.zawodyweb.compiler.Program;
import pl.umk.mat.zawodyweb.database.CheckerErrors;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Classes;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.pojo.Tests;

/**
 *
 * @author lukash2k
 */
public class MainJudge {

    private static final org.apache.log4j.Logger logger = Logger.getLogger(MainJudge.class);
    private static ArrayList<ClassInfo> classes = new ArrayList<ClassInfo>();
    private static Properties properties = new Properties();
    private static long delayConnect;

    private static void connectToJudgeManager() throws IOException, InstantiationException, IllegalAccessException {
        Socket sock = null;
        /* connecting to JudgeManager */
        try {
            sock = new Socket(InetAddress.getByName(properties.getProperty("JUDGEMANAGER_HOST")), Integer.parseInt(properties.getProperty("JUDGEMANAGER_PORT")));
            DataInputStream input = new DataInputStream(sock.getInputStream());
            DataOutputStream output = new DataOutputStream(sock.getOutputStream());
            logger.info("Connection with JudgeManager on " + properties.getProperty("JUDGEMANAGER_HOST") + ":" + properties.getProperty("JUDGEMANAGER_PORT") + "...");

            while (15 == 15) {
                /* receiving submit_id */
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
                    /* change submit status to PROCESS */
                    logger.info("Setting submit status to PROCESS...");

                    transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
                    Submits submit = DAOFactory.DEFAULT.buildSubmitsDAO().getById(id);
                    submit.setResult(SubmitsResultEnum.PROCESS.getCode());
                    DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
                    transaction.commit();

                    /* getting submit */
                    transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
                    //if (!sock.isConnected()) { // FIXME: po co to było?!
                    //    logger.error("Connection to JudgeManager closed, shutting down Judge...");
                    //    break;
                    //}

                    submit = DAOFactory.DEFAULT.buildSubmitsDAO().getById(id);
                    byte[] codeText = submit.getCode();
                    String filename = submit.getFilename();
                    if (filename != null && !filename.isEmpty()) {
                        properties.setProperty("COMPILED_FILENAME", filename);
                        properties.setProperty("CODE_FILENAME", filename);
                    }
                    Classes compilerClasses = submit.getLanguages().getClasses();

                    /* downloading compiler class */
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
                            compiler = (CompilerInterface) new CompiledClassLoader().loadCompiledClass(classes.get(iVectorClassInfo).getFilename(), classes.get(iVectorClassInfo).getCode()).newInstance();
                        } else {
                            classes.get(iVectorClassInfo).setVersion(compilerClasses.getVersion());
                            classes.get(iVectorClassInfo).setCode(compilerClasses.getCode());
                            compiler = (CompilerInterface) new CompiledClassLoader().loadCompiledClass(classes.get(iVectorClassInfo).getFilename(), classes.get(iVectorClassInfo).getCode()).newInstance();
                        }
                    } else {
                        classes.add(new ClassInfo(compilerClasses.getId(), compilerClasses.getFilename(), compilerClasses.getCode(), compilerClasses.getVersion()));
                        compiler = (CompilerInterface) new CompiledClassLoader().loadCompiledClass(compilerClasses.getFilename(), compilerClasses.getCode()).newInstance();
                    }
                    properties.setProperty("CODEFILE_EXTENSION", submit.getLanguages().getExtension());
                    compiler.setProperties(properties);

                    /* downloading diff class */
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
                            checker = (CheckerInterface) new CompiledClassLoader().loadCompiledClass(classes.get(iVectorClassInfo).getFilename(), classes.get(iVectorClassInfo).getCode()).newInstance();
                        } else {
                            classes.get(iVectorClassInfo).setVersion(diffClasses.getVersion());
                            classes.get(iVectorClassInfo).setCode(diffClasses.getCode());
                            checker = (CheckerInterface) new CompiledClassLoader().loadCompiledClass(classes.get(iVectorClassInfo).getFilename(), classes.get(iVectorClassInfo).getCode()).newInstance();
                        }
                    } else {
                        classes.add(new ClassInfo(diffClasses.getId(), diffClasses.getFilename(), diffClasses.getCode(), diffClasses.getVersion()));
                        checker = (CheckerInterface) new CompiledClassLoader().loadCompiledClass(diffClasses.getFilename(), diffClasses.getCode()).newInstance();
                    }

                    /* compilation */
                    Code code = new Code(codeText, compiler);
                    logger.info("Trying to compile the code...");
                    Program program = code.compile();

                    /* downloading tests */
                    logger.info("Downloading tests...");
                    Criteria c = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Tests.class);
                    c.add(Restrictions.eq("problems.id", submit.getProblems().getId()));
                    c.addOrder(Order.asc("testorder"));
                    List<Tests> tests = c.list();

                    TestInput testInput;
                    TestOutput testOutput;
                    boolean undefinedResult = false;
                    boolean manualResult = false;

                    /* TESTING */
                    logger.info("Starting tests...");
                    for (Tests test : tests) {
                        logger.info("Test " + test.getTestorder() + " started.");
                        testInput = new TestInput(test.getInput(), test.getMaxpoints(), test.getTimelimit(), submit.getProblems().getMemlimit());
                        testOutput = new TestOutput(test.getOutput());
                        CheckerResult result = checker.check(program, testInput, testOutput);
                        if (result.getResult() == CheckerErrors.UNDEF) {
                            undefinedResult = true;
                            break;
                        }
                        if (result.getResult() == CheckerErrors.MANUAL) {
                            manualResult = true;
                        }

                        /* saving result to database */
                        Results dbResult = new Results();
                        dbResult.setMemory(result.getMemUsed());
                        dbResult.setRuntime(result.getRuntime());
                        if (result.getDescription() != null) {
                            dbResult.setNotes(result.getDescription().replaceAll("[\000-\007]", " "));
                        }
                        dbResult.setPoints(result.getPoints());
                        dbResult.setSubmitResult(result.getResult());
                        dbResult.setSubmits(submit);
                        dbResult.setTests(test);
                        DAOFactory.DEFAULT.buildResultsDAO().save(dbResult);
                        logger.info("Test " + test.getTestorder() + " finished.");
                    }

                    /* finish testing */
                    logger.info("All tests finished. Closing program.");
                    program.closeProgram();

                    /* successfully completed? */
                    if (undefinedResult == true) {
                        logger.error("Some of the tests got UNDEFINED result -- this should not happend.");
                        transaction.rollback();
                    } else if (manualResult == true) {
                        logger.info("Saving results to database (with manual).");
                        submit.setResult(SubmitsResultEnum.MANUAL.getCode());
                        DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
                        transaction.commit();
                    } else {
                        logger.info("Saving results to database.");
                        submit.setResult(SubmitsResultEnum.DONE.getCode());
                        DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
                        transaction.commit();
                    }
                } catch (Exception e) {
                    logger.error("Exception occurs -- rolling back.", e);
                    if (transaction != null) {
                        transaction.rollback();
                    }
                }

                /* send ACK to JudgeManager */
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
        String configFile = MainJudge.class.getResource(".").getPath() + "configuration.xml";
        if (args.length == 1 && !args[0].isEmpty()) {
            configFile = args[0];
        }

        /* Default settings for properties */
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

        logger.info("JUDGEMANAGER_HOST = " + properties.getProperty("JUDGEMANAGER_HOST"));
        logger.info("JUDGEMANAGER_PORT = " + properties.getProperty("JUDGEMANAGER_PORT"));
        logger.info("JUDGEMANAGER_DELAY_CONNECT = " + properties.getProperty("JUDGEMANAGER_DELAY_CONNECT"));

        logger.info("COMPILED_DIR = " + properties.getProperty("COMPILED_DIR"));
        logger.info("CODE_DIR = " + properties.getProperty("CODE_DIR"));

        logger.info("COMPILED_FILENAME = " + properties.getProperty("COMPILED_FILENAME"));
        logger.info("CODE_FILENAME = " + properties.getProperty("CODE_FILENAME"));

        logger.info("COMPILE_TIMEOUT = " + properties.getProperty("COMPILE_TIMEOUT"));

        logger.info("JAVA_POLICY = " + properties.getProperty("JAVA_POLICY"));

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
