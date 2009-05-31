package pl.umk.mat.zawodyweb.www.util;

import java.util.List;
import java.util.ListIterator;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.LanguagesProblems;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.Tests;

/**
 *
 * @author Jakub Prabucki
 */
public class ProblemsUtils {

    static private final ProblemsUtils instance = new ProblemsUtils();

    private ProblemsUtils() {
    }

    public static ProblemsUtils getInstance() {
        return instance;
    }

    public Problems copySolution(Problems problem, Series serie, boolean copySolution) {
        Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        Problems copyProblem = new Problems();
        copyProblem.setAbbrev(problem.getAbbrev());
        copyProblem.setClasses(problem.getClasses());
        copyProblem.setMemlimit(problem.getMemlimit());
        copyProblem.setName(problem.getName());
        copyProblem.setPDF(problem.getPDF());
        copyProblem.setSeries(serie);
        copyProblem.setText(problem.getText());
        DAOFactory.DEFAULT.buildProblemsDAO().saveOrUpdate(copyProblem);
        List<LanguagesProblems> findByProblemsid = DAOFactory.DEFAULT.buildLanguagesProblemsDAO().findByProblemsid(problem.getId());
        ListIterator<LanguagesProblems> iterator = findByProblemsid.listIterator();
        while(iterator.hasNext()){
            LanguagesProblems newLanguage = new LanguagesProblems();
            LanguagesProblems oldLanguage = null;
            oldLanguage = iterator.next();
            newLanguage.setLanguages(oldLanguage.getLanguages());
            newLanguage.setProblems(copyProblem);
            DAOFactory.DEFAULT.buildLanguagesProblemsDAO().saveOrUpdate(newLanguage);
        }
        List<Tests> findByProblemsid1 = DAOFactory.DEFAULT.buildTestsDAO().findByProblemsid(problem.getId());
        ListIterator<Tests> iterator1 = findByProblemsid1.listIterator();
        while(iterator1.hasNext()){
            Tests newTest = new Tests();
            Tests oldTest = null;
            oldTest = iterator1.next();
            newTest.setInput(oldTest.getInput());
            newTest.setMaxpoints(oldTest.getMaxpoints());
            newTest.setOutput(oldTest.getOutput());
            newTest.setProblems(copyProblem);
            newTest.setTestorder(oldTest.getTestorder());
            newTest.setTimelimit(oldTest.getTimelimit());
            newTest.setVisibility(oldTest.getVisibility());
            DAOFactory.DEFAULT.buildTestsDAO().saveOrUpdate(newTest);
        }
        transaction.commit();
        return copyProblem;
    }

    public void reJudge() {
        //TODO: implement this
    }
}
