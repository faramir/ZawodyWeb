package pl.umk.mat.zawodyweb.www.util;

import java.util.List;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ProblemsDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Contests;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;

/**
 *
 * @author Jakub Prabucki
 */
public class ContestsUtils {

    static private final ContestsUtils instance = new ContestsUtils();

    private ContestsUtils() {
    }

    public static ContestsUtils getInstance() {
        return instance;
    }

    public void reJudge(Contests contest) {
        Transaction transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
        List<Series> findByContestsid = DAOFactory.DEFAULT.buildSeriesDAO().findByContestsid(contest.getId());
        transaction.commit();
        for (Series serie : findByContestsid) {
            transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            List<Problems> findBySeriesid = DAOFactory.DEFAULT.buildProblemsDAO().findBySeriesid(serie.getId());
            transaction.commit();
            for (Problems problem : findBySeriesid) {
                ProblemsUtils.getInstance().reJudge(problem);
            }
        }
    }
}
