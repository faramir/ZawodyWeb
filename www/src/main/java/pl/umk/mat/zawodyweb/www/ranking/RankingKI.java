package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;
import org.hibernate.Query;
import org.hibernate.Session;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ProblemsDAO;
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.UsersDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.Users;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingKI implements RankingInteface {

    private final ResourceBundle messages = ResourceBundle.getBundle("pl.umk.mat.zawodyweb.www.Messages");

    private class ProblemsKI implements Comparable {

        int id_problem;
        long series_begin;
        String name;

        public ProblemsKI(int id_problem, long series_begin, String name) {
            this.id_problem = id_problem;
            this.series_begin = series_begin;
            this.name = name;
        }

        @Override
        public int compareTo(Object o) {
            ProblemsKI p2 = (ProblemsKI) o;

            if (this.series_begin < p2.series_begin) {
                return -1;
            }
            if (this.series_begin > p2.series_begin) {
                return 1;
            }

            return this.name.compareTo(p2.name);
        }
    }

    private class UserKI implements Comparable {

        int id_user;
        int points;
        HashMap<Integer, Integer> solutions;

        public UserKI(int id_user) {
            this.id_user = id_user;
            this.solutions = new HashMap<Integer, Integer>();
        }

        void add(int problem_id, int points) {
            this.points += points;
            this.solutions.put(problem_id, points);
        }

        @Override
        public int compareTo(Object o) {
            UserKI u2 = (UserKI) o;

            if (this.points < u2.points) {
                return 1;
            }
            if (this.points > u2.points) {
                return -1;
            }

            return 0;
        }
    }

    private RankingTable getRankingKI(int contest_id, Timestamp checkDate, boolean admin, Integer series_id) {
        Session hibernateSession = HibernateUtil.getSessionFactory().getCurrentSession();

        Timestamp checkTimestamp;

        UsersDAO usersDAO = DAOFactory.DEFAULT.buildUsersDAO();
        SeriesDAO seriesDAO = DAOFactory.DEFAULT.buildSeriesDAO();
        ProblemsDAO problemsDAO = DAOFactory.DEFAULT.buildProblemsDAO();
        HashMap<Integer, UserKI> mapUserKI = new HashMap<Integer, UserKI>();
        Vector<ProblemsKI> vectorProblemsKI = new Vector<ProblemsKI>();

        boolean allTests;

        for (Series series : seriesDAO.findByContestsid(contest_id)) {

            if (series_id != null && series.getId() != series_id) {
                continue;
            }

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
                vectorProblemsKI.add(new ProblemsKI(problems.getId(), series.getStartdate().getTime(), problems.getAbbrev()));

                Query query = null;
                if (allTests == true) {
                    query = hibernateSession.createSQLQuery("" +
                            "select usersid, sum(points) " +
                            "from submits,results,tests " +
                            "where submits.problemsid='" + problems.getId() + "' " +
                            "  and submits.id=results.submitsid " +
                            "  and tests.id=results.testsid" +
                            "  and submits.result='" + SubmitsResultEnum.DONE.getCode() + "' " +
                            "  and sdate in ( " +
                            "        select max(sdate) " +
                            "	     from submits " +
                            "        where submits.problemsid='" + problems.getId() + "' " +
                            "          and sdate <= '" + checkTimestamp.toString() + "' " +
                            //"	       and tests.visibility=1 " +
                            "	     group by usersid " +
                            "      ) " +
                            "group by usersid, submits.id");
                } else {
                    query = hibernateSession.createSQLQuery("" +
                            "select usersid, sum(points) " +
                            "from submits,results,tests " +
                            "where submits.problemsid='" + problems.getId() + "' " +
                            "  and submits.id=results.submitsid " +
                            "  and tests.id=results.testsid" +
                            "  and submits.result='" + SubmitsResultEnum.DONE.getCode() + "' " +
                            "  and sdate in ( " +
                            "        select max(sdate) " +
                            "	     from submits " +
                            "        where submits.problemsid='" + problems.getId() + "' " +
                            "          and sdate <= '" + checkTimestamp.toString() + "' " +
                            "	       and tests.visibility=1 " +
                            "	     group by usersid " +
                            "      ) " +
                            "group by usersid, submits.id");
                }

                for (Object list : query.list()) {
                    Object[] o = (Object[]) list; // 0 - user.id, 1 - sum(points)

                    UserKI user = mapUserKI.get((Integer) o[0]);
                    if (user == null) {
                        user = new UserKI((Integer) o[0]);
                        mapUserKI.put((Integer) o[0], user);
                    }

                    user.add(problems.getId(), ((Number) o[1]).intValue());
                }
            }
        }

        /* Tworzenie rankingu z danych */
        Vector<UserKI> cre = new Vector<UserKI>();
        cre.addAll(mapUserKI.values());
        Collections.sort(cre);

        Collections.sort(vectorProblemsKI);

        /* nazwy kolumn i CSSy */
        Vector<String> columnsCSS = new Vector<String>();
        Vector<String> columnsCaptions = new Vector<String>();
        for (ProblemsKI problemsKI : vectorProblemsKI) {
            columnsCaptions.add(problemsKI.name);
            columnsCSS.add("small");
        }
        columnsCaptions.add(messages.getString("points"));
        columnsCSS.add("small");

        /* tabelka z rankingiem */
        Vector<RankingEntry> vectorRankingEntry = new Vector<RankingEntry>();
        int place = 0;
        int points = Integer.MAX_VALUE;
        for (UserKI user : cre) {
            if (points > user.points) {
                ++place;
                points = user.points;
            }
            Vector<String> v = new Vector<String>();
            for (ProblemsKI problemsKI : vectorProblemsKI) {
                Integer solution_points = user.solutions.get(problemsKI.id_problem);
                if (solution_points == null) {
                    v.add("-");
                } else {
                    v.add(solution_points.toString());
                }
            }
            v.add(Integer.toString(user.points));

            Users users = usersDAO.getById(user.id_user);

            vectorRankingEntry.add(new RankingEntry(place, users.getFirstname() + " " + users.getLastname() + " (" + users.getLogin() + ")", v));
        }

        return new RankingTable(columnsCaptions, columnsCSS, vectorRankingEntry);
    }

    @Override
    public RankingTable getRanking(int contest_id, Timestamp checkDate) {
        return getRankingKI(contest_id, checkDate, false, null);
    }

    @Override
    public RankingTable getRankingForAdmin(int contest_id, Timestamp checkDate) {
        return getRankingKI(contest_id, checkDate, true, null);
    }

    @Override
    public RankingTable getRankingForSeries(int contest_id, int series_id, Timestamp checkDate) {
        return getRankingKI(contest_id, checkDate, false, series_id);
    }

    @Override
    public RankingTable getRankingForSeriesForAdmin(int contest_id, int series_id, Timestamp checkDate) {
        return getRankingKI(contest_id, checkDate, true, series_id);
    }
}
