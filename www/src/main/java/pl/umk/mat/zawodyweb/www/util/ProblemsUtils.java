package pl.umk.mat.zawodyweb.www.util;

import java.util.List;
import java.util.Vector;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import pl.umk.mat.zawodyweb.database.CheckerErrors;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.LanguagesProblems;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
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
        for (LanguagesProblems oldLanguage : findByProblemsid) {
            LanguagesProblems newLanguage = new LanguagesProblems();
            newLanguage.setLanguages(oldLanguage.getLanguages());
            newLanguage.setProblems(copyProblem);
            DAOFactory.DEFAULT.buildLanguagesProblemsDAO().saveOrUpdate(newLanguage);
        }
        List<Tests> findByProblemsid1 = DAOFactory.DEFAULT.buildTestsDAO().findByProblemsid(problem.getId());
        for (Tests oldTest : findByProblemsid1) {
            Tests newTest = new Tests();
            newTest.setInput(oldTest.getInput());
            newTest.setMaxpoints(oldTest.getMaxpoints());
            newTest.setOutput(oldTest.getOutput());
            newTest.setProblems(copyProblem);
            newTest.setTestorder(oldTest.getTestorder());
            newTest.setTimelimit(oldTest.getTimelimit());
            newTest.setVisibility(oldTest.getVisibility());
            DAOFactory.DEFAULT.buildTestsDAO().saveOrUpdate(newTest);
        }
        if (copySolution) {
            Criteria crit = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Submits.class);
            Query query = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery("" +
                    "SELECT DISTINCT on (submits.usersid) " +
                    "submits.id, submits.usersid, sum(results.points) as suma " +
                    "from submits, results " +
                    "where submits.problemsid ='" + problem.getId() + "' " +
                    "and submits.id = results.submitsid " +
                    "and results.submitresult = '" + CheckerErrors.ACC + "' " +
                    "and submits.result = '" + SubmitsResultEnum.DONE.getCode() + "' " +
                    "group by " +
                    "submits.id, submits.usersid " +
                    "order by " +
                    "submits.usersid ASC, suma DESC");
            Vector<Criterion> vCrit = new Vector<Criterion>();
            for (Object list : query.list()) {
                Object[] o = (Object[]) list;
                vCrit.add(Restrictions.eq("id", o[0]));
            }
            if (vCrit.size() != 0) {
                Criterion c = vCrit.remove(0);
                for (Criterion criterion : vCrit) {
                    c = Restrictions.or(c, criterion);
                }
                crit.add(c);
                List<Submits> lista = crit.list();
                for (Submits oldSubmits : lista) {
                    Submits newSubmit = new Submits();
                    newSubmit.setCode(oldSubmits.getCode());
                    newSubmit.setFilename(oldSubmits.getFilename());
                    newSubmit.setLanguages(oldSubmits.getLanguages());
                    newSubmit.setNotes(oldSubmits.getNotes());
                    newSubmit.setProblems(copyProblem);
                    newSubmit.setResult(oldSubmits.getResult());
                    newSubmit.setSdate(oldSubmits.getSdate());
                    newSubmit.setUsers(oldSubmits.getUsers());
                    DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(newSubmit);
                    List<Results> findBySubmitsid = DAOFactory.DEFAULT.buildResultsDAO().findBySubmitsid(oldSubmits.getId());
                    for (Results oldResults : findBySubmitsid) {
                        Results newResults = new Results();
                        newResults.setMemory(oldResults.getMemory());
                        newResults.setNotes(oldResults.getNotes());
                        newResults.setPoints(oldResults.getPoints());
                        newResults.setRuntime(oldResults.getRuntime());
                        newResults.setSubmitResult(oldResults.getSubmitResult());
                        newResults.setSubmits(newSubmit);
                        newResults.setTests(oldResults.getTests());
                        DAOFactory.DEFAULT.buildResultsDAO().save(newResults);
                    }
                }
            }
        }
        transaction.commit();
        return copyProblem;
    }

    public void reJudge(Problems problem) {
        Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        List<Submits> findByProblemsid = DAOFactory.DEFAULT.buildSubmitsDAO().findByProblemsid(problem.getId());
        for (Submits submit : findByProblemsid) {
            SubmitUtils.getInstance().reJudge(submit);
        }
        transaction.commit();
    }
}
