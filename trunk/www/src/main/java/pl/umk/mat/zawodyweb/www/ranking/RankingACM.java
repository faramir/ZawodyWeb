package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;
import org.hibernate.Criteria;
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
                System.out.println("maxPoints = " + maxPoints);

                /*
                 * select submits.id,usersid,min(sdate)
                 * from submits,results,tests
                where submits.problemsid='7'
                and submits.id=results.submitsid
                and tests.id = results.testsid
                and tests.visibility=1
                group by submits.id,usersid
                having sum(points)= maxPoints
                 */
                Criteria c = hibernateSession.createCriteria(Submits.class);
                c.setProjection(Projections.min("sdate")).add(Restrictions.eq("problems.id", problems.getId()));
                c.createCriteria("resultss").setProjection(Projections.sum("resultss.points").as("sumPoints")).add(Restrictions.eq("sumPoints", maxPoints));
                c.createCriteria("resultss.tests").add(Restrictions.eq("visibility", "1"));
                for (Object l : c.list()) {
                    System.out.println("l = " + l);
                }
                //List<Submits> listSubmits = hibernateSession.createCriteria(Submits.class).add(Restrictions.and(Restrictions.eq("problems.id", problems.getId()), Restrictions.lt("sdate", checkTimestamp))).addOrder(Order.asc("sdate")).list();
                /*
                 * submitsDAO.findByCriteria(
                 *      Restrictions.and(
                 *      Restrictions.eq("problems.id", problems.getId()),
                 *      Restrictions.lt("sdate", checkTimestamp)))
                 */
                /*for (Submits submit : listSubmits) {
                acceptedSolution = true;
                for (Results results : submit.getResultss()) {
                if ((allTests == true || results.getTests().getVisibility().equals(1)) && results.getPoints().equals(results.getTests().getMaxpoints()) == false) {
                acceptedSolution = false;
                break;
                }
                }
                if (acceptedSolution == true) {
                }
                }*/
            }
        }
        System.out.println("date2 : " + new Date());

        // 1. pobieranie userów, którzy brali udział w konkursie przd checkDate
        // 2. pobieranie problemów, które są w seriach otwartych przed checkDate
        // 3. stworzenie mapy wszystkich userów, gdzie kluczem jest id_user
        // 4. dla każdego zadania wybieramy rozwiązania wszystkie z sumą punktów,
        //    gdzie punkty są widoczne itp. to jest najtrudniejsze :/
        //    w skrócie - wybieramy te rozwiązania, które dostały max punktów
        //    posortowane rosnąco według czasu
        //    a. liczymy ilość rozwiązań usera danego zadania do czasu rozwiązania
        //       zadania i dodajemy takie dane do zmapowanej klasy
        // 6. ze zmapowanych klas robimy wektor rozwiązań
        // 7. i zwracamy
        return null;
    }

    @Override
    public Vector<RankingEntry> getRankingForAdmin(int contest_id, Timestamp checkDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
