package pl.umk.mat.zawodyweb.www.util;

import java.util.List;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.LanguagesProblemsDAO;
import pl.umk.mat.zawodyweb.database.TestsDAO;
import pl.umk.mat.zawodyweb.database.pojo.LanguagesProblems;
import pl.umk.mat.zawodyweb.database.pojo.PDF;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
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

    public Problems copyProblem(Problems sourceProblem, Series destinationSerie, String abbrev, String name/*, boolean copyUsersSolutions*/) {
        Problems destinationProblem = new Problems();
        if (abbrev == null || abbrev.isEmpty()) {
            destinationProblem.setAbbrev(sourceProblem.getAbbrev());
        } else {
            destinationProblem.setAbbrev(abbrev);
        }
        if (name == null || name.isEmpty()) {
            destinationProblem.setName(sourceProblem.getName());
        } else {
            destinationProblem.setName(name);
        }
        destinationProblem.setClasses(sourceProblem.getClasses());
        destinationProblem.setMemlimit(sourceProblem.getMemlimit());
        destinationProblem.setCodesize(sourceProblem.getCodesize());

        PDF pdf = sourceProblem.getPDF();
        if (pdf != null) {
            PDF newPdf = new PDF();
            newPdf.setPdf(pdf.getPdf());
            DAOFactory.DEFAULT.buildPDFDAO().saveOrUpdate(newPdf);
            destinationProblem.setPDF(newPdf);
        }

        destinationProblem.setSeries(destinationSerie);
        destinationProblem.setText(sourceProblem.getText());
        DAOFactory.DEFAULT.buildProblemsDAO().saveOrUpdate(destinationProblem);
        List<LanguagesProblems> findByProblemsid = DAOFactory.DEFAULT.buildLanguagesProblemsDAO().findByProblemsid(sourceProblem.getId());
        LanguagesProblemsDAO languagesProblemsDAO = DAOFactory.DEFAULT.buildLanguagesProblemsDAO();
        for (LanguagesProblems oldLanguage : findByProblemsid) {
            LanguagesProblems newLanguage = new LanguagesProblems();
            newLanguage.setLanguages(oldLanguage.getLanguages());
            newLanguage.setProblems(destinationProblem);
            languagesProblemsDAO.saveOrUpdate(newLanguage);
        }
        List<Tests> findByProblemsid1 = DAOFactory.DEFAULT.buildTestsDAO().findByProblemsid(sourceProblem.getId());
        TestsDAO testsDAO = DAOFactory.DEFAULT.buildTestsDAO();
        for (Tests oldTest : findByProblemsid1) {
            Tests newTest = new Tests();
            newTest.setInput(oldTest.getInput());
            newTest.setMaxpoints(oldTest.getMaxpoints());
            newTest.setOutput(oldTest.getOutput());
            newTest.setProblems(destinationProblem);
            newTest.setTestorder(oldTest.getTestorder());
            newTest.setTimelimit(oldTest.getTimelimit());
            newTest.setVisibility(oldTest.getVisibility());
            testsDAO.saveOrUpdate(newTest);
        }
        /*
         * FIXME: Connection: Tests <--> Results is broken in new Result(?)
         */
//         if (copyUsersSolutions) {
//            Criteria crit = HibernateUtil.getSessionFactory().getCurrentSession().createCriteria(Submits.class);
//            Query query = HibernateUtil.getSessionFactory().getCurrentSession().createSQLQuery("" +
//                    "SELECT DISTINCT ON (submits.usersid) " +
//                    "      submits.id, submits.usersid, SUM(results.points) AS suma " +
//                    "   FROM submits, results " +
//                    "      WHERE submits.problemsid ='" + sourceProblem.getId() + "' " +
//                    "        AND submits.id = results.submitsid " +
//                    "        AND results.submitresult = '" + CheckerErrors.ACC + "' " +
//                    "        AND submits.result = '" + SubmitsResultEnum.DONE.getCode() + "' " +
//                    "      GROUP BY " +
//                    "        submits.id, submits.usersid " +
//                    "   ORDER BY " +
//                    "        submits.usersid ASC, suma DESC");
//            Vector<Criterion> vCrit = new Vector<Criterion>();
//            for (Object list : query.list()) {
//                Object[] o = (Object[]) list;
//                vCrit.add(Restrictions.eq("id", o[0]));
//            }
//            if (vCrit.size() != 0) {
//                Criterion c = vCrit.remove(0);
//                for (Criterion criterion : vCrit) {
//                    c = Restrictions.or(c, criterion);
//                }
//                crit.add(c);
//                List<Submits> lista = crit.list();
//                for (Submits oldSubmits : lista) {
//                    Submits newSubmit = new Submits();
//                    newSubmit.setCode(oldSubmits.getCode());
//                    newSubmit.setFilename(oldSubmits.getFilename());
//                    newSubmit.setLanguages(oldSubmits.getLanguages());
//                    newSubmit.setNotes(oldSubmits.getNotes());
//                    newSubmit.setProblems(destinationProblem);
//                    newSubmit.setResult(oldSubmits.getResult());
//                    newSubmit.setSdate(oldSubmits.getSdate());
//                    newSubmit.setUsers(oldSubmits.getUsers());
//                    DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(newSubmit);
//                    List<Results> findBySubmitsid = DAOFactory.DEFAULT.buildResultsDAO().findBySubmitsid(oldSubmits.getId());
//                    for (Results oldResults : findBySubmitsid) {
//                        Results newResults = new Results();
//                        newResults.setMemory(oldResults.getMemory());
//                        newResults.setNotes(oldResults.getNotes());
//                        newResults.setPoints(oldResults.getPoints());
//                        newResults.setRuntime(oldResults.getRuntime());
//                        newResults.setSubmitResult(oldResults.getSubmitResult());
//                        newResults.setSubmits(newSubmit);
//                        newResults.setTests(oldResults.getTests()); // FIXME: THIS LINE 
//                        DAOFactory.DEFAULT.buildResultsDAO().save(newResults);
//                    }
//                }
//            }
//        }
        return destinationProblem;
    }

    public void reJudge(Problems problem) {
        List<Submits> findByProblemsid = DAOFactory.DEFAULT.buildSubmitsDAO().findByProblemsid(problem.getId());
        for (Submits submit : findByProblemsid) {
            SubmitsUtils.getInstance().reJudge(submit);
        }
    }
}
