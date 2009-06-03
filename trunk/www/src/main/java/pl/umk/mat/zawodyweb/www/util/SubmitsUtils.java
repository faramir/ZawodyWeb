package pl.umk.mat.zawodyweb.www.util;

import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
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
        DAOFactory.DEFAULT.buildResultsDAO().deleteById(submit.getId());
        transaction.commit();

        JudgeManagerConnector.getInstance().sentToJudgeManager(submit.getId());
    }

    public boolean deleteSubmit(Submits submit) {
        try {
            Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            DAOFactory.DEFAULT.buildSubmitsDAO().delete(submit);
            transaction.commit();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
