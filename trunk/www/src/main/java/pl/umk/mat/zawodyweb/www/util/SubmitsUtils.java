package pl.umk.mat.zawodyweb.www.util;

import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ResultsDAO;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.www.JudgeManagerConnector;

/**
 *
 * @author Jakub Prabucki
 */
public class SubmitsUtils {

    static private final SubmitsUtils instance = new SubmitsUtils();

    private SubmitsUtils() {
    }

    public static SubmitsUtils getInstance() {
        return instance;
    }

    public void reJudge(Submits submit) {
        Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        submit.setResult(SubmitsResultEnum.WAIT.getCode());
        DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
        ResultsDAO resultsDAO = DAOFactory.DEFAULT.buildResultsDAO();
        for (Results r : submit.getResultss()) { //resultsDAO.findBySubmitsid(submit.getId())) {
            resultsDAO.delete(r);
        }
        transaction.commit();

        JudgeManagerConnector.getInstance().sentToJudgeManager(submit.getId());
    }
}
