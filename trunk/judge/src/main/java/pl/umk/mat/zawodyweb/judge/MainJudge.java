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
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

    public static final org.apache.log4j.Logger logger = Logger.getLogger(MainJudge.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException, InstantiationException, IllegalAccessException {
        Properties properties = new Properties();
        String configFile = MainJudge.class.getResource(".").getPath() +"configuration.xml";
        if (args.length == 1) {
            configFile = args[0];
        }

        // Default settings for properties
        properties.setProperty("PORT", "8888");
        properties.setProperty("HOST", "127.0.0.1");
        properties.setProperty("COMPILED_DIR", "");
        properties.setProperty("CODE_DIR", "");
        properties.setProperty("COMPILED_FILENAME", "a");
        properties.setProperty("CODE_FILENAME", "a");
        properties.setProperty("COMPILE_TIMEOUT", "30000");
        properties.setProperty("acm_uva.login", "spamz");
        properties.setProperty("acm_uva.password", "spamz2");
        properties.setProperty("opss.login", "zawodyweb");
        properties.setProperty("opss.password", "zawody.web.2009");

        try {
            logger.debug("Reading configuration file from " + configFile + "...");
            properties.loadFromXML(new FileInputStream(configFile));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        logger.debug("Connection with JudgeManager on " + properties.getProperty("HOST") +
                ":" + properties.getProperty("PORT") + "...");
        Socket sock = new Socket(InetAddress.getByName(properties.getProperty("HOST")),
                Integer.parseInt(properties.getProperty("PORT")));
        DataInputStream input = new DataInputStream(
                sock.getInputStream());
        DataOutputStream output = new DataOutputStream(sock.getOutputStream());
        while (15 == 15) {
            int id;
            try {
                id = input.readInt();
                logger.info("Received submit id: " + id);
            } catch (IOException ex) {
                logger.error("Connection to JudgeManager closed, shutting down Judge...");
                break;
            }
            logger.debug("Setting submit status to PROCESS...");
            Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            Submits submit = DAOFactory.DEFAULT.buildSubmitsDAO().getById(id);
            submit.setResult(SubmitsResultEnum.PROCESS.getCode());
            DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
            transaction.commit();
            transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            if (!sock.isConnected()) {
                logger.error("Connection to JudgeManager closed, shutting down Judge...");
                break;
            }
            byte[] codeText = submit.getCode();
            String filename = submit.getFilename();
            if (filename != null && !filename.isEmpty()) {
                properties.setProperty("COMPILED_FILENAME", filename);
                properties.setProperty("CODE_FILENAME", filename);
            }
            logger.debug("Downloading compiler class...");
            Classes compilerClasses = submit.getLanguages().getClasses();
            CompilerInterface compiler = (CompilerInterface) new CompiledClassLoader().loadCompiledClass(compilerClasses.getFilename(),
                    compilerClasses.getCode()).newInstance();
            properties.setProperty("CODEFILE_EXTENSION", submit.getLanguages().getExtension());
            compiler.setProperties(properties);
            logger.debug("Downloading diff class...");
            Classes diffClasses = submit.getProblems().getClasses();
            CheckerInterface checker = (CheckerInterface) new CompiledClassLoader().loadCompiledClass(diffClasses.getFilename(),
                    diffClasses.getCode()).newInstance();
            Code code = new Code(codeText, compiler);
            Program program = code.compile();
            logger.debug("Downloading tests...");
            List<Tests> tests = DAOFactory.DEFAULT.buildTestsDAO().
                    findByProblemsid(submit.getProblems().getId());
            TestInput testInput;
            TestOutput testOutput;
            boolean undefinedResult = false;
            logger.debug("Starting tests...");
            for (Tests test : tests) {
                testInput = new TestInput(test.getInput(),
                        test.getTimelimit(), submit.getProblems().getMemlimit());
                testOutput = new TestOutput(test.getOutput());
                CheckerResult result = checker.check(program, testInput, testOutput);
                if (result.getResult() == CheckerErrors.UNDEF) {
                    undefinedResult = true;
                    break;
                }
                Results dbResult = new Results();
                dbResult.setMemory(result.getMemUsed());
                dbResult.setRuntime(result.getRuntime());
                dbResult.setNotes(result.getDecription());
                dbResult.setPoints(result.getResult());
                dbResult.setSubmits(submit);
                dbResult.setTests(test);
                DAOFactory.DEFAULT.buildResultsDAO().save(dbResult);
                logger.debug("Test finished.");
            }
            logger.debug("All tests finished. Closing program.");
            program.closeProgram();
            if (!undefinedResult) {
                logger.debug("Saving results to database.");
                submit.setResult(SubmitsResultEnum.DONE.getCode());
                DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
                transaction.commit();
            } else {
                logger.error("Some of the tests got UNDEFINED result -- this should not happend.");
                transaction.rollback();
            }
            logger.info("Processing SubmitID: " + id + " finished.");
            output.writeInt(id);
        }
        HibernateUtil.getSessionFactory().getCurrentSession().close();
        sock.close();
    }
}
