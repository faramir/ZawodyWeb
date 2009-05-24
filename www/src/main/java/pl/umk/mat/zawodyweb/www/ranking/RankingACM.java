package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ProblemsDAO;
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.pojo.Tests;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingACM implements RankingInteface {

    @Override
    public Vector<RankingEntry> getRanking(int contest_id, Timestamp checkDate) {
        Session hibernateSession = HibernateUtil.getSessionFactory().getCurrentSession();


        Timestamp checkTimestamp;

        SubmitsDAO submitsDAO = DAOFactory.DEFAULT.buildSubmitsDAO();
        SeriesDAO seriesDAO = DAOFactory.DEFAULT.buildSeriesDAO();
        ProblemsDAO problemsDAO = DAOFactory.DEFAULT.buildProblemsDAO();

        boolean allTests;
        boolean acceptedSolution;

        System.out.println("date : " + new Date());

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

                // select sum(maxpoints) from tests where problemsid='7' and visibility=1
                Number maxPoints = null;
                if (allTests) {
                    maxPoints = (Number) hibernateSession.createCriteria(Tests.class).setProjection(Projections.sum("maxpoints")).add(Restrictions.eq("problems.id", problems.getId())).uniqueResult();
                } else {
                    maxPoints = (Number) hibernateSession.createCriteria(Tests.class).setProjection(Projections.sum("maxpoints")).add(Restrictions.and(Restrictions.eq("problems.id", problems.getId()), Restrictions.eq("visibility", 1))).uniqueResult();
                }
                if (maxPoints == null) {
                    maxPoints = 0; // To nie powinno się nigdy zdarzyć ;).. chyba, że nie ma testu przy zadaniu?
                }

                // FIXME: wrócimy tu, gdy Hibernate będzie obsługiwał klauzulę having ;-)
                /*
                 * Criteria c = hibernateSession.createCriteria(Submits.class);
                 * c.setProjection(Projections.min("sdate")).add(Restrictions.eq("problems.id", problems.getId()));
                 * c.createCriteria("resultss", "r").setProjection(Projections.sum("r.points").as("sumPoints").add(Restrictions.eq("sum(r.points)", maxPoints));
                 * c.createCriteria("r.tests").add(Restrictions.eq("visibility", 1));
                 * // c.add(Restrictions.sqlRestriction("1=1 having sumPoints=" + maxPoints));
                 */

                // FIXME: BAAAAAAAAAAAAAAAAAAARDZO NIEKOSZERNIE!
                Query query = null;
                System.out.println("problems.getId() = " + problems.getId());
                System.out.println("allTests = " + allTests);
                if (allTests == true) {
                    query = hibernateSession.createSQLQuery("select usersid,submits.id as sid,min(sdate) as mdate " +
                            " from submits,results,tests " +
                            " where submits.problemsid='" + problems.getId() + "' " +
                            "	and submits.id=results.submitsid " +
                            "	and tests.id = results.testsid " +
                            " group by submits.id,usersid " +
                            " having sum(points)='" + maxPoints + "'");
                } else {
                    query = hibernateSession.createSQLQuery("select usersid,submits.id as sid,min(sdate) as mdate " +
                            " from submits,results,tests " +
                            " where submits.problemsid='" + problems.getId() + "' " +
                            "	and submits.id=results.submitsid " +
                            "	and tests.id = results.testsid " +
                            "	and tests.visibility=1 " +
                            " group by submits.id,usersid " +
                            " having sum(points)='" + maxPoints + "'");
                }

                for (Object list : query.list()) { // tu jest zwrócona lista "zaakceptowanych" w danym momencie rozwiązań zadania
                    Object[] o = (Object[]) list;
                    System.out.println("user.id    : " + o[0]);
                    System.out.println("submits.id : " + o[1]);
                    System.out.println("min(sdate) : " + o[2] + " -> " + o[2].getClass().getName());
                    Number bombs = (Number) hibernateSession.createCriteria(Submits.class).setProjection(Projections.rowCount()).add(Restrictions.eq("problems.id", (Number) problems.getId())).add(Restrictions.eq("users.id", (Number) o[0])).add(Restrictions.lt("sdate", (Timestamp) o[2])).uniqueResult();
                    if (bombs == null) {
                        bombs = -1;
                    }
                    System.out.println("bombs = " + bombs.intValue());
                }
            }
        }
        System.out.println("date2 : " + new Date());

        return null;
    }

    @Override
    public Vector<RankingEntry> getRankingForAdmin(int contest_id, Timestamp checkDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
