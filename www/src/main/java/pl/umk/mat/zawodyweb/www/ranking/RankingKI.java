package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.management.Query;
import org.hibernate.Session;
import pl.umk.mat.zawodyweb.database.CheckerErrors;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ProblemsDAO;
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.www.ranking.RankingACM.UserACM;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingKI implements RankingInteface {

    private final ResourceBundle messages = ResourceBundle.getBundle("pl.umk.mat.zawodyweb.www.Messages");

    @Override
    public RankingTable getRanking(int contest_id, Timestamp checkDate) {
        Session hibernateSession = HibernateUtil.getSessionFactory().getCurrentSession();

        Timestamp checkTimestamp;

        UsersDAO usersDAO = DAOFactory.DEFAULT.buildUsersDAO();
        SeriesDAO seriesDAO = DAOFactory.DEFAULT.buildSeriesDAO();
        ProblemsDAO problemsDAO = DAOFactory.DEFAULT.buildProblemsDAO();
        HashMap<Integer, UserACM> mapUserACM = new HashMap<Integer, UserACM>();

        boolean allTests;

        for (Series series : seriesDAO.findByContestsid(contest_id)) {

            checkTimestamp = checkDate;
            allTests = false;

            if (series.getFreezedate() != null && series.getUnfreezedate() != null) {
                if (checkDate.after(series.getFreezedate()) && checkDate.before(series.getUnfreezedate())) {
                    checkTimestamp = new Timestamp(series.getFreezedate().getTime());
                }

                if (checkDate.after(series.getUnfreezedate())) {
                    allTests = true;
                }

            }

            for (Problems problems : problemsDAO.findBySeriesid(series.getId())) {
                Query query = null;
/*                if (allTests == true) {
                    query = hibernateSession.createSQLQuery("select usersid,submits.id as sid,min(sdate) as mdate " +
                            " from submits,results,tests " +
                            " where submits.problemsid='" + problems.getId() + "' " +
                            "	and submits.id=results.submitsid " +
                            "	and tests.id = results.testsid " +
                            "   and results.submitresult='" + CheckerErrors.ACC + "' " +
                            "   and submits.result='" + SubmitsResultEnum.DONE.getCode() + "' " +
                            "   and sdate <= '" + checkTimestamp.toString() + "' " +
                            " group by submits.id,usersid " +
                            " having sum(points)='" + maxPoints + "' " +
                            "    and count(points)='" + noTests + "'")
                }*/
            }
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RankingTable getRankingForAdmin(int contest_id, Timestamp checkDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
