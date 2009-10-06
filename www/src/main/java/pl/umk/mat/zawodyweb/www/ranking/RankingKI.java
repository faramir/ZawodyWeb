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
        String abbrev;
        String name;
        boolean frozen;

        public ProblemsKI(int id_problem, long series_begin, String abbrev, String name, boolean frozen) {
            this.id_problem = id_problem;
            this.series_begin = series_begin;
            this.abbrev = abbrev;
            this.name = name;
            this.frozen = frozen;
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

            return this.abbrev.compareTo(p2.abbrev);
        }
    }

    private class UserKI implements Comparable {

        String lastname;
        String firstname;
        String login;
        int id_user;
        int points;
        HashMap<Integer, Integer> solutions;

        public UserKI(int id_user, Users users) {
            this.id_user = id_user;
            this.solutions = new HashMap<Integer, Integer>();

            this.login = users.getLogin();
            this.firstname = users.getFirstname();
            this.lastname = users.getLastname();
        }

        String formatName() {
            return String.format("%s %s (%s)", firstname, lastname, login);
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

            int r;
            r = this.lastname.compareToIgnoreCase(u2.lastname);
            if (r != 0) {
                return r;
            }

            r = this.firstname.compareToIgnoreCase(u2.firstname);
            if (r != 0) {
                return r;
            }

            return this.login.compareToIgnoreCase(u2.login);
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
        boolean frozenRanking = false;
        boolean frozenSeria;

        for (Series series : seriesDAO.findByContestsid(contest_id)) {

            if (series_id != null && series.getId() != series_id) {
                continue;
            }

            if (series.getStartdate().after(checkDate)) {
                continue;
            }

            frozenSeria = false;
            checkTimestamp = checkDate;
            allTests = admin;

            if (!admin && series.getFreezedate() != null) {
                if (checkDate.after(series.getFreezedate()) && (series.getUnfreezedate() == null || checkDate.before(series.getUnfreezedate()))) {
                    checkTimestamp = new Timestamp(series.getFreezedate().getTime());
                    if (series.getUnfreezedate() != null) {
                        frozenRanking = true;
                    }
                    frozenSeria = true;
                }
            }

            if (series.getUnfreezedate() != null) {
                if (checkDate.after(series.getUnfreezedate())) {
                    allTests = true;
                }

            }

            for (Problems problems : problemsDAO.findBySeriesid(series.getId())) {
                vectorProblemsKI.add(new ProblemsKI(problems.getId(), series.getStartdate().getTime(), problems.getAbbrev(), problems.getName(), frozenSeria));

                Query query = null;
                if (allTests == true) {
                    query = hibernateSession.createSQLQuery("" +
                            "select usersid, sum(points) " +
                            "from submits,results,tests " +
                            "where submits.problemsid='" + problems.getId() + "' " +
                            "  and submits.id=results.submitsid " +
                            "  and tests.id=results.testsid" +
                            "  and sdate in ( " +
                            "        select max(sdate) " +
                            "	     from submits " +
                            "        where submits.problemsid='" + problems.getId() + "' " +
                            "          and submits.result='" + SubmitsResultEnum.DONE.getCode() + "' " +
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
                            "  and sdate in ( " +
                            "        select max(sdate) " +
                            "	     from submits " +
                            "        where submits.problemsid='" + problems.getId() + "' " +
                            "          and submits.result='" + SubmitsResultEnum.DONE.getCode() + "' " +
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
                        Integer user_id = (Integer) o[0];
                        Users users = usersDAO.getById(user_id);
                        user = new UserKI(user_id, users);
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
            columnsCaptions.add(RankingUtils.formatText(problemsKI.abbrev, problemsKI.name, problemsKI.frozen ? "frozen" : null));
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

            vectorRankingEntry.add(new RankingEntry(place, user.formatName(), v));
        }

        return new RankingTable(columnsCaptions, columnsCSS, vectorRankingEntry, frozenRanking);
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
