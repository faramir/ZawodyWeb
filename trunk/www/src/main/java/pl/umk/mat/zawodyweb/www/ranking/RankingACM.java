package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;
import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ProblemsDAO;
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.pojo.Tests;
import pl.umk.mat.zawodyweb.database.pojo.Users;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingACM implements RankingInteface {

    private final ResourceBundle messages = ResourceBundle.getBundle("pl.umk.mat.zawodyweb.www.Messages");

    class SolutionACM implements Comparable {

        String name;
        long date;
        long time;
        int bombs;

        public SolutionACM(String name, long date, long time, int bombs) {
            this.name = name;
            this.date = date;
            this.time = time;
            this.bombs = bombs;
        }

        @Override
        public int compareTo(Object o) {
            if (this.date < ((SolutionACM) o).date) {
                return 1;
            }
            if (this.date > ((SolutionACM) o).date) {
                return -1;
            }
            return 0;
        }
    }

    class UserACM implements Comparable {

        int id_user;
        int points;
        long totalTime;
        Vector<SolutionACM> solutions;

        public UserACM(int id_user) {
            this.id_user = id_user;
            this.points = 0;
            this.totalTime = 0;
            this.solutions = new Vector<SolutionACM>();
        }

        void add(int points, SolutionACM solutionACM) {
            this.points += points;
            this.totalTime += solutionACM.time/1000;
            this.solutions.add(solutionACM);
        }

        String getSolutionsForRanking() {
            String s = "";
            Collections.sort(this.solutions);

            for (SolutionACM solutionACM : solutions) {
                s += solutionACM.name;
                if (solutionACM.bombs > 5) {
                    s += "(" + solutionACM.bombs + ")";
                } else {
                    for (int i = 0; i < solutionACM.bombs; ++i) {
                        s += "*";
                    }
                }
                s += " ";
            }
            return s.trim();
        }

        @Override
        public int compareTo(Object o) {
            UserACM u2 = (UserACM) o;
            if (this.points > u2.points) {
                return 1;
            }
            if (this.points < u2.points) {
                return -1;
            }
            if (this.totalTime < u2.totalTime) {
                return 1;
            }
            if (this.totalTime > u2.totalTime) {
                return -1;
            }
            return 0;
        }
    }

    private String parseTime(long time) {
        long d, h, m, s;
        d = time / (24 * 60 * 60);
        time %= (24 * 60 * 60);
        h = time / (60 * 60);
        time %= (60 * 60);
        m = time / 60;
        time %= 60;
        s = time;

        return String.format("%dd %02d:%02d:%02d", d, h, m, s);
    }

    private Vector<RankingEntry> getRankingACM(int contest_id, Timestamp checkDate, boolean admin) {
        Session hibernateSession = HibernateUtil.getSessionFactory().getCurrentSession();


        Timestamp checkTimestamp;

        UsersDAO usersDAO = DAOFactory.DEFAULT.buildUsersDAO();
        SeriesDAO seriesDAO = DAOFactory.DEFAULT.buildSeriesDAO();
        ProblemsDAO problemsDAO = DAOFactory.DEFAULT.buildProblemsDAO();
        HashMap<Integer, UserACM> mapUserACM = new HashMap<Integer, UserACM>();

        boolean allTests;

        for (Series series : seriesDAO.findByContestsid(contest_id)) {

            checkTimestamp = checkDate;
            allTests = admin;

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
                            "   and sdate <= '" + checkTimestamp.toString() + "' " +
                            " group by submits.id,usersid " +
                            " having sum(points)='" + maxPoints + "'");
                } else {
                    query = hibernateSession.createSQLQuery("select usersid,submits.id as sid,min(sdate) as mdate " +
                            " from submits,results,tests " +
                            " where submits.problemsid='" + problems.getId() + "' " +
                            "	and submits.id=results.submitsid " +
                            "	and tests.id = results.testsid " +
                            "	and tests.visibility=1 " +
                            "   and sdate <= '" + checkTimestamp.toString() + "' " +
                            " group by submits.id,usersid " +
                            " having sum(points)='" + maxPoints + "'");
                }

                for (Object list : query.list()) { // tu jest zwrócona lista "zaakceptowanych" w danym momencie rozwiązań zadania
                    Object[] o = (Object[]) list; // 0 - user.id, 1 - submits.id, 2 - sdate
                    Number bombs = (Number) hibernateSession.createCriteria(Submits.class).setProjection(Projections.rowCount()).add(Restrictions.eq("problems.id", (Number) problems.getId())).add(Restrictions.eq("users.id", (Number) o[0])).add(Restrictions.lt("sdate", (Timestamp) o[2])).uniqueResult();

                    if (bombs == null) {
                        bombs = -1;
                    }

                    UserACM user = mapUserACM.get((Integer) o[0]);
                    if (user == null) {
                        user = new UserACM((Integer) o[0]);
                        mapUserACM.put((Integer) o[0], user);
                    }

                    user.add(maxPoints.intValue(),
                            new SolutionACM(problems.getAbbrev(),
                            ((Timestamp) o[2]).getTime(),
                            ((Timestamp) o[2]).getTime() - series.getStartdate().getTime() + series.getPenaltytime() * bombs.longValue(), bombs.intValue()));
                }
            }

        }
        Vector<UserACM> cre = new Vector<UserACM>();
        cre.addAll(mapUserACM.values());
        Collections.sort(cre);

        Vector<RankingEntry> vectorRankingEntry = new Vector<RankingEntry>();
        int place = 0;
        long totalTime = -1;
        int points = Integer.MAX_VALUE;
        for (UserACM user : cre) {
            if (points > user.points || (points == user.points && totalTime < user.totalTime)) {
                ++place;
                points = user.points;
                totalTime = user.totalTime;
            }
            Vector<KeyValue> v = new Vector<KeyValue>();
            v.add(new DefaultKeyValue(messages.getString("points"), user.points));
            v.add(new DefaultKeyValue(messages.getString("solutions"), user.getSolutionsForRanking()));
            v.add(new DefaultKeyValue(messages.getString("time"), parseTime(user.totalTime)));

            Users users = usersDAO.getById(user.id_user);

            vectorRankingEntry.add(new RankingEntry(place, users.getFirstname() + " " + users.getLastname() + " (" + users.getLogin() + ")", v));

//            System.out.println(place + " : " + user.id_user + " " + usersDAO.getById(user.id_user).getLogin() + " " + " : " + user.points + " : " + user.totalTime + " : " + user.getSolutionsForRanking());
        }

        return vectorRankingEntry;
    }

    @Override
    public Vector<RankingEntry> getRanking(int contest_id, Timestamp checkDate) {
        return getRankingACM(contest_id, checkDate, false);
    }

    @Override
    public Vector<RankingEntry> getRankingForAdmin(int contest_id, Timestamp checkDate) {
        return getRankingACM(contest_id, checkDate, true);
    }
}
