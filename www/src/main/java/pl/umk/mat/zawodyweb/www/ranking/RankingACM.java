package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ProblemsDAO;
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingACM {

    static private final RankingACM instance = new RankingACM();

    /**
     * @return the instance
     */
    static public RankingACM getInstance() {
        return instance;
    }

    private class MapProblems {

        String problemName;
        String problemAbbrev;

        public MapProblems(String problemName, String problemAbbrev) {
            this.problemName = problemName;
            this.problemAbbrev = problemAbbrev;
        }
    }

    private class ProblemSolutions implements Comparable {

        int problemId;
        int bombs;
        long date;

        public ProblemSolutions(int problemId, int bombs, long date) {
            this.problemId = problemId;
            this.bombs = bombs;
            this.date = date;
        }

        @Override
        public int compareTo(Object o) {
            return (int) (((ProblemSolutions) o).date - this.date);
        }
    }

    private class UserSolutions implements Comparable {

        int userId;
        int points;
        long totalTime;
        TreeSet<ProblemSolutions> problemsSolved;

        public UserSolutions(int userId) {
            this.userId = userId;
            points = 0;
            totalTime = 0;
            problemsSolved = new TreeSet<ProblemSolutions>();
        }

        @Override
        public int compareTo(Object o) {
            if (this.points < ((UserSolutions) o).points) {
                return -1;
            }
            if (this.points == ((UserSolutions) o).points) {
                return (int) (this.totalTime - ((UserSolutions) o).totalTime);
            }
            return 1;
        }
    }

    Vector<RankingEntry> getRanking(int contest_id, Date checkDate, boolean admin) {
        Session hibernateSession = HibernateUtil.getSessionFactory().getCurrentSession();

        SeriesDAO seriesDAO = DAOFactory.DEFAULT.buildSeriesDAO();
        ProblemsDAO problemsDAO = DAOFactory.DEFAULT.buildProblemsDAO();

        Timestamp checkTimestamp = new Timestamp(checkDate.getTime());

        HashMap<Integer, MapProblems> mapProblems = new HashMap<Integer, MapProblems>();
        HashMap<Integer, UserSolutions> mapUsers = new HashMap<Integer, UserSolutions>();

        List<Series> listSeries = seriesDAO.findByContestsid(contest_id);

        /* dla każdej serii z zawodów */
        for (Series series : listSeries) {
            List<Problems> listProblems = problemsDAO.findBySeriesid(series.getId());

            /* dla każdego zadania z serii */
            for (Problems problems : listProblems) {

                HashMap<Integer, Integer> userTries = new HashMap<Integer, Integer>();
                HashMap<Integer, Boolean> userSolved = new HashMap<Integer, Boolean>();

                mapProblems.put(problems.getId(), new MapProblems(problems.getName(), problems.getAbbrev()));

                /*
                 * pobierz rozwiązania, które nastąpiły przed czasem checkDate
                 * i wybierz z nich tylko te, które są widoczne, czyli są widoczne dla admina
                 * albo było wysłane przed zamrożeniem rankingu lub sprawdzamy z czasem po zamrożeniu rankingu
                 */
                Criteria c = hibernateSession.createCriteria(Submits.class);
                c.createCriteria("problems").add(Restrictions.eq("id", problems.getId()));
                c.add(Restrictions.le("sdate", checkTimestamp));
                c.addOrder(Order.asc("sdate"));
                List<Submits> listSubmits = c.list();

                /*
                 * pobierz maksymalną ilość punktów, które aktualnie może zobaczyć user w danym teście,
                 * czyli wszystko zawsze, jeśli jesteśmy adminem,
                 * lub zawsze jeśli test jest widoczny, lub wszystko, gdy seria zakończona
                 */

                /*
                 * każde rozwiązanie testujemy z testami, które należą do tego rozwiązania
                 * jeśli wszystkie widoczne testy mają maksymalną liczbę punktów, to więcej rozwiązań
                 * danego usera nis sprawdzamy, ale zapisujemy to rozwiązanie zadania do listy zadań rozwiązanych przez usera
                 * z czasem rozwiązania zadania (realny czas, a nie od rozpoczęcia tury)
                 * + czas rozwiązywania + penalty * ilość błędnych rozwiązań do tego czasu
                 */
                for (Submits submits : listSubmits) {
                    if (!(admin == true || submits.getSdate().before(series.getFreezedate()) || checkDate.after(series.getUnfreezedate()))) {
                        continue;
                    }
                    int user_id = submits.getUsers().getId();
                    if (userSolved.containsKey(user_id)) {
                        continue;
                    }
                    if (!userTries.containsKey(user_id)) {
                        userTries.put(user_id, 0);
                    }
                    int sum = 0;
                    boolean good = true;
                    for (Results result : submits.getResultss()) { // FIXME: getEnddate() czy getUnfreezedate() ? 
                        if ((admin == true || result.getTests().getVisibility().equals(1) || checkDate.after(series.getUnfreezedate())) && result.getTests().getMaxpoints().equals(result.getPoints()) == false) {
                            good = false;
                            break;
                        } else {
                            sum += result.getPoints();
                        }
                    }
                    if (good) {
                        userSolved.put(user_id, good);
                        if (mapUsers.containsKey(user_id) == false) {
                            mapUsers.put(user_id, new UserSolutions(user_id));
                        }
                        UserSolutions us = mapUsers.get(user_id);
                        us.problemsSolved.add(new ProblemSolutions(problems.getId(), userTries.get(user_id), submits.getSdate().getTime()));
                        us.points += sum;
                        us.totalTime += submits.getSdate().getTime() - series.getStartdate().getTime();
                    } else {
                        userTries.put(user_id, userTries.get(user_id) + 1);
                    }

                }
            }
        }
        /*
         * po przejrzeniu wszystkich serii sortujemy userów po ilości rozwiązanych zadań
         * i całkowitym czasie rozwiązywania zadań
         * wewnątrz usera sortujemy rozwiązania pod kątem rzeczywistej daty rozwiązania,
         * dopisujemy gwiazdki (lub nawiasy) i wszystko zwracamy
         */
        for (Integer i : mapUsers.keySet()) {
            System.out.println("" + i + ": " + mapUsers.get(i).userId);
            System.out.println("" + i + ": " + mapUsers.get(i).points);
            System.out.println("" + i + ": " + mapUsers.get(i).totalTime);
            System.out.println("" + i + ": " + mapUsers.get(i).problemsSolved.size());
        }
        return null;

    }
}
