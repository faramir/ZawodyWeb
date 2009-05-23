package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ProblemsDAO;
import pl.umk.mat.zawodyweb.database.SeriesDAO;
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
        int maxPoints;

        public MapProblems(String problemName, String problemAbbrev) {
            this.problemName = problemName;
            this.problemAbbrev = problemAbbrev;
        }
    }

    Vector<RankingEntry> getRanking(int contest_id, Date checkDate, boolean admin) {
        Session hibernateSession = HibernateUtil.getSessionFactory().getCurrentSession();

        SeriesDAO seriesDAO = DAOFactory.DEFAULT.buildSeriesDAO();
        ProblemsDAO problemsDAO = DAOFactory.DEFAULT.buildProblemsDAO();

        Timestamp checkTimestamp = new Timestamp(checkDate.getTime());

        HashMap<Integer, MapProblems> mapProblems = new HashMap<Integer, MapProblems>();
        HashMap<Integer, Integer> mapTestsVisible = new HashMap<Integer, Integer>();

        List<Series> listSeries = seriesDAO.findByContestsid(contest_id);

        /* dla każdej serii z zawodów */
        for (Series series : listSeries) {
            List<Problems> listProblems = problemsDAO.findBySeriesid(series.getId());

            /* dla każdego zadania z serii */
            for (Problems problems : listProblems) {

                mapProblems.put(problems.getId(), new MapProblems(problems.getName(), problems.getAbbrev()));

                /*
                 * pobierz maksymalną ilość punktów, które aktualnie może zobaczyć user w danym teście,
                 * czyli wszystko zawsze, jeśli jesteśmy adminem,
                 * lub zawsze jeśli test jest widoczny, lub wszystko, gdy seria zakończona
                 */

                for (Tests tests : problems.getTestss()) {
                    if (admin == true || tests.getVisibility().equals(1) || checkDate.after(series.getEnddate())) {
                        mapTestsVisible.put(tests.getId(), tests.getMaxpoints());
                    } else {
                        mapTestsVisible.put(tests.getId(), null);
                    }
                }

                /*
                 * pobierz rozwiązania, które nastąpiły przed czasem checkDate
                 * i wybierz z nich tylko te, które są widoczne, czyli są widoczne dla admina
                 * albo jesteśmy przed lub po zamrożeniu rankingu
                 */
                Criteria c = hibernateSession.createCriteria(Submits.class);
                c.createCriteria("problems").add(Restrictions.eq("id", problems.getId()));
                c.add(Restrictions.le("sdate", checkTimestamp));
                c.addOrder(Order.asc("sdate"));
                List<Submits> listSubmits = c.list();

                /*
                 * każde rozwiązanie testujemy z testami, które należą do tego rozwiązania
                 * jeśli wszystkie widoczne testy mają maksymalną liczbę punktów, to więcej rozwiązań
                 * danego usera nis sprawdzamy, ale zapisujemy to rozwiązanie zadania do listy zadań rozwiązanych przez usera
                 * z czasem rozwiązania zadania (realny czas, a nie od rozpoczęcia tury)
                 * + czas rozwiązywania + penalty * ilość błędnych rozwiązań do tego czasu
                 */
                for (Submits s : listSubmits) {
                    if (admin == true || checkDate.before(series.getFreezedate()) || checkDate.after(series.getUnfreezedate())) {
                        System.out.println(" s = " + s.getSdate() + ", " + s.getFilename());
                    }
                }
            }
        }

        /*
         * po przejrzeniu wszystkich serii sortujemy userów po ilości rozwiązanych zadań
         * i całkowitym czasie rozwiązywania zadań
         * wewnątrz usera sortujemy rozwiązania pod kątem rzeczywistej daty rozwiązania,
         * dopisujemy gwiazdki (lub nawiasy) i wszystko przesyłamy jako wynik działania programu
         */

        return null;

    }
}
