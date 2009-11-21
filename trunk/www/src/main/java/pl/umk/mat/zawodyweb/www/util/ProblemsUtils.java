package pl.umk.mat.zawodyweb.www.util;

import java.util.List;
import java.util.Vector;
import org.hibernate.Criteria;
import org.hibernate.Query;
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

    public Problems copyProblem(Problems sourceProblem, Series destinationSerie, boolean copySolutions) {
        Problems destinationProblem = new Problems();
        destinationProblem.setAbbrev(sourceProblem.getAbbrev());
        destinationProblem.setClasses(sourceProblem.getClasses());
        destinationProblem.setMemlimit(sourceProblem.getMemlimit());
        destinationProblem.setName(sourceProblem.getName());
        destinationProblem.setPDF(sourceProblem.getPDF());
        destinationProblem.setSeries(destinationSerie);
        destinationProblem.setText(sourceProblem.getText());
        DAOFactory.DEFAULT.buildProblemsDAO().saveOrUpdate(destinationProblem);
        List<LanguagesProblems> findByProblemsid = DAOFactory.DEFAULT.buildLanguagesProblemsDAO().findByProblemsid(sourceProblem.getId());
        for (LanguagesProblems oldLanguage : findByProblemsid) {
            LanguagesProblems newLanguage = new LanguagesProblems();
            newLanguage.setLanguages(oldLanguage.getLanguages());
            newLanguage.setProblems(destinationProblem);
            DAOFactory.DEFAULT.buildLanguagesProblemsDAO().saveOrUpdate(newLanguage);
        }
        List<Tests> findByProblemsid1 = DAOFactory.DEFAULT.buildTestsDAO().findByProblemsid(sourceProblem.getId());
        for (Tests oldTest : findByProblemsid1) {
            Tests newTest = new Tests();
            newTest.setInput(oldTest.getInput());
            newTest.setMaxpoints(oldTest.getMaxpoints());
            newTest.setOutput(oldTest.getOutput());
            newTest.setProblems(destinationProblem);
            newTest.setTestorder(oldTest.getTestorder());
            newTest.setTimelimit(oldTest.getTimelimit());
            newTest.setVisibility(oldTest.getVisibility());
            DAOFactory.DEFAULT.buildTestsDAO().saveOrUpdate(newTest);
        }
        if (copySolutions) {
            Criteria crit = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Submits.class);
            Query query = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery("" +
                    "SELECT DISTINCT ON (submits.usersid) " +
                    "      submits.id, submits.usersid, SUM(results.points) AS suma " +
                    "   FROM submits, results " +
                    "      WHERE submits.problemsid ='" + sourceProblem.getId() + "' " +
                    "        AND submits.id = results.submitsid " +
                    "        AND results.submitresult = '" + CheckerErrors.ACC + "' " +
                    "        AND submits.result = '" + SubmitsResultEnum.DONE.getCode() + "' " +
                    "      GROUP BY " +
                    "        submits.id, submits.usersid " +
                    "   ORDER BY " +
                    "        submits.usersid ASC, suma DESC");
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
                    newSubmit.setProblems(destinationProblem);
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
        return destinationProblem;
    }

    public void reJudge(Problems problem) {
        List<Submits> findByProblemsid = DAOFactory.DEFAULT.buildSubmitsDAO().findByProblemsid(problem.getId());
        for (Submits submit : findByProblemsid) {
            SubmitsUtils.getInstance().reJudge(submit);
        }
    }
}
