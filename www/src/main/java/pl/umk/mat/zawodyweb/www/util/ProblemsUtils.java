package pl.umk.mat.zawodyweb.www.util;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import pl.umk.mat.zawodyweb.database.CheckerErrors;
import pl.umk.mat.zawodyweb.database.DAOFactory;
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
            crit.add(Restrictions.eq("problems.id", problem.getId()));
            crit.addOrder(Order.asc("users.id"));
            int actualUser = 0;
            List<Submits> findByProblemsid2 = crit.list();
            //List<Submits> findByProblemsid2 = DAOFactory.DEFAULT.buildSubmitsDAO().findByProblemsid(problem.getId());
            for (Submits oldSubmits : findByProblemsid2) {
                //if oldSubmits.getUsers().getId() nie zostal juz dodany{
                List<Results> findBySubmitsid = DAOFactory.DEFAULT.buildResultsDAO().findBySubmitsid(oldSubmits.getId());
                boolean add = true;
                for (Results oldResults : findBySubmitsid) {
                    if (oldResults.getSubmitResult() != CheckerErrors.ACC) {
                        add = false;
                        break;
                    }
                }
                if (add) {
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
                // dodac oldSubmits.getUsers().getId() do zaakceptowanych uzytkownikow
                }


            //}
            }
        }
        transaction.commit();
        return copyProblem;
    }

    public void reJudge() {
        //TODO: implement this
    }
}
