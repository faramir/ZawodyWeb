package pl.umk.mat.zawodyweb.www.util;

import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 *
 * @author Jakub Prabucki
 */
public class SubmitUtils {

    static private final SubmitUtils instance = new SubmitUtils();

    private SubmitUtils() {
    }

    public static SubmitUtils getInstance() {
        return instance;
    }

    public void reJudge(Submits submit) {
        Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        submit.setResult(SubmitsResultEnum.WAIT.getCode());
        DAOFactory.DEFAULT.buildSubmitsDAO().saveOrUpdate(submit);
        DAOFactory.DEFAULT.buildResultsDAO().deleteById(submit.getId());
        transaction.commit();
        //poinformować JM
    }

    public boolean deleteSubmit(Submits submit) {
        try {
            Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            DAOFactory.DEFAULT.buildSubmitsDAO().delete(submit);
            transaction.commit();
            return true;
        } catch (Exception ex){
            return false;
        }
    }
}
