package pl.umk.mat.zawodyweb.www.util;

import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
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

    public void reJudge() {
        //TODO: implement this
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
