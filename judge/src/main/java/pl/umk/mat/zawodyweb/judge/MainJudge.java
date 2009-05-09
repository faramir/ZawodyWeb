/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.judge;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.checker.CheckerInterface;
import pl.umk.mat.zawodyweb.checker.CheckerResult;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.Code;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;
import pl.umk.mat.zawodyweb.compiler.Program;
import pl.umk.mat.zawodyweb.database.DAOFactory;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException, InstantiationException, IllegalAccessException {
        Properties properties = new Properties();
        try {
            properties.loadFromXML(new FileInputStream("configuration.xml"));
        } catch (FileNotFoundException ex) {
            properties.setProperty("PORT", "8888");
            properties.setProperty("HOST", "127.0.0.1");
        }
        Socket sock = new Socket(InetAddress.getByName(properties.getProperty("HOST")),
                Integer.parseInt(properties.getProperty("PORT")));
        BufferedReader input = new BufferedReader(new InputStreamReader(
                sock.getInputStream()));
        PrintWriter output = new PrintWriter(sock.getOutputStream(), true);
        while (15 == 15) {

            while (!input.ready()) {
                Thread.sleep(1000);
            }
            String str = input.readLine();
            int id = Integer.parseInt(str);
            Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            Submits submit = DAOFactory.DEFAULT.buildSubmitsDAO().getById(id);
            
                       
            if (!sock.isConnected()) {
                break;
            }
            byte[] codeText = submit.getCode();
            String filename = submit.getFilename();
            if (!filename.isEmpty()) {
                properties.setProperty("COMPILE_FILENAME", filename);
            }
            Classes compilerClasses = submit.getLanguages().getClasses();
            CompilerInterface compiler = (CompilerInterface) new CompiledClassLoader().loadCompiledClass(compilerClasses.getFilename(),
                    compilerClasses.getCode()).newInstance();
            Classes diffClasses = submit.getProblems().getClasses();
            CheckerInterface checker = (CheckerInterface) new CompiledClassLoader().loadCompiledClass(diffClasses.getFilename(),
                    diffClasses.getCode()).newInstance();
            Code code = new Code(codeText, compiler);
            Program program = code.compile();
            List<Tests> tests = submit.getProblems().getTestss();
            TestInput testInput;
            TestOutput testOutput;
            for (Tests test : tests) {
                testInput = new TestInput(test.getInput(),
                        test.getTimelimit(),submit.getProblems().getMemlimit());
                testOutput = new TestOutput(test.getOutput());
                CheckerResult result = checker.check(program, testInput, testOutput);
                Results dbResult = new Results();
                dbResult.setMemory(testOutput.getMemUsed());
                dbResult.setRuntime(testOutput.getRuntime());
                dbResult.setNotes(result.getDecription());
                dbResult.setPoints(result.getResult());
                dbResult.setSubmits(submit);
                dbResult.setTests(test);
                DAOFactory.DEFAULT.buildResultsDAO().save(dbResult);
            }
            transaction.commit();
        }
        HibernateUtil.getSessionFactory().getCurrentSession().close();
        sock.close();
    }

}
