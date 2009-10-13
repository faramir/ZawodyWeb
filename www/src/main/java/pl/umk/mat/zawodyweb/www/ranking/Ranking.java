package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Contests;
import pl.umk.mat.zawodyweb.database.pojo.Series;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class Ranking {

    private static final Logger logger = Logger.getLogger(Ranking.class);
    static private final Ranking instance = new Ranking();
    private Map<Integer, RankingWorker> contestRankingWorker = new ConcurrentHashMap<Integer, RankingWorker>();
    private Map<Integer, RankingTable> contestRankingTableMap = new ConcurrentHashMap<Integer, RankingTable>();
    private Map<Integer, RankingTable> seriesRankingTableMap = new ConcurrentHashMap<Integer, RankingTable>();

    class RankingWorker extends Thread {

        int contest_id;
        long start_date;

        public RankingWorker(int contest_id) {
            this.contest_id = contest_id;
            start_date = new Date().getTime();
        }

        public void setStartDate(long start_date) {
            this.start_date = start_date;
        }

        @Override
        public void run() {
            contestRankingWorker.put(contest_id, this);
            Transaction transaction = null;
            try {
                Contests contest = null;
                while (start_date > (new Date().getTime() - 3 * 60 * 60 * 1000)) {
                    transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
                    contest = DAOFactory.DEFAULT.buildContestsDAO().getById(contest_id);

                    if (contest == null) {
                        break;
                    }

                    if (contest.getRankingrefreshrate() == 0) {
                        break;
                    }

                    contestRankingTableMap.put(contest.getId(), createRankingTable(contest.getId(), contest.getType(), new Date(), false));
                    for (Series serie : DAOFactory.DEFAULT.buildSeriesDAO().findByContestsid(contest.getId())) {
                        seriesRankingTableMap.put(serie.getId(), createRankingTableForSeries(contest.getId(), serie.getId(), contest.getType(), new Date(), false));
                    }

                    transaction.commit();

                    Thread.sleep(contest.getRankingrefreshrate() * 1000);
                }
            } catch (Exception ex) {
                logger.fatal("Fatal exception", ex);
                try {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } catch (Exception e) {
                }
            }
            contestRankingWorker.remove(contest_id);
        }
    }

    private Ranking() {
    }

    /**
     * @return the instance
     */
    static public Ranking getInstance() {
        return instance;
    }

    private RankingTable getCachedRankingWithExpirationTime(Map<Integer, RankingTable> map, int key, int type, Date data, int rankingRefreshRate) {
        RankingTable ranking = map.get(key);
        if (ranking == null) {
            return null;
        }
        if (ranking.getType() != type) {
            return null;
        }
        if (ranking.getGenerationDate().getTime() <= data.getTime() - rankingRefreshRate) {
            return null;
        }
        return ranking;
    }

    private RankingTable getCachedRanking(Map<Integer, RankingTable> map, int key, int type) {
        RankingTable ranking = map.get(key);
        if (ranking == null) {
            return null;
        }
        if (ranking.getType() != type) {
            return null;
        }
        return ranking;
    }

    private RankingTable createRankingTable(int contest_id, int type, Date date, boolean admin) {
        RankingTable rankingTable = null;
        long start = new Date().getTime();

        RankingInteface ranking = null;

        if (type == 0) { // ACM
            ranking = new RankingACM();
        } else if (type == 1) { // PA
            ranking = new RankingPA();
        } else if (type == 2) { // KI
            ranking = new RankingKI();
        }

        if (admin == true) {
            rankingTable = ranking.getRankingForAdmin(contest_id, new Timestamp(date.getTime()));
        } else {
            rankingTable = ranking.getRanking(contest_id, new Timestamp(date.getTime()));
        }

        rankingTable.setType(type);
        rankingTable.generateHtml(admin);
        rankingTable.setGenerationDate(date);
        rankingTable.setGenerationTime(new Date().getTime() - start);

        return rankingTable;
    }

    private RankingTable createRankingTableForSeries(int contest_id, int series_id, int type, Date date, boolean admin) {
        RankingTable rankingTable = null;
        long start = new Date().getTime();

        RankingInteface ranking = null;

        if (type == 0) { // ACM
            ranking = new RankingACM();
        } else if (type == 1) { // PA
            ranking = new RankingPA();
        } else if (type == 2) { // KI
            ranking = new RankingKI();
        }

        if (admin == true) {
            rankingTable = ranking.getRankingForSeriesForAdmin(contest_id, series_id, new Timestamp(date.getTime()));
        } else {
            rankingTable = ranking.getRankingForSeries(contest_id, series_id, new Timestamp(date.getTime()));
        }

        rankingTable.setType(type);
        rankingTable.generateHtml(admin);
        rankingTable.setGenerationDate(date);
        rankingTable.setGenerationTime(new Date().getTime() - start);

        return rankingTable;
    }

    public RankingTable getRanking(int contest_id, int type, int rankingRefreshRate, Date date, boolean admin) {
        if (!(type == 0 || type == 1 || type == 2)) {
            return null;
        }

        if (contestRankingWorker.get(contest_id) == null) {
            contestRankingTableMap.remove(contest_id);
            new RankingWorker(contest_id).start();
        } else {
            contestRankingWorker.get(contest_id).setStartDate(new Date().getTime());
        }

        if (admin == true) {
            return createRankingTable(contest_id, type, date, admin);
        }

        RankingTable rankingTable = getCachedRanking(contestRankingTableMap, contest_id, type);
        if (rankingTable == null) {
            rankingTable = createRankingTable(contest_id, type, date, admin);
            contestRankingTableMap.put(contest_id, rankingTable);
        }

        return rankingTable;
    }

    public RankingTable getRankingForSeries(int contest_id, int series_id, int type, int rankingRefreshRate, Date date, boolean admin) {
        if (!(type == 0 || type == 1 || type == 2)) {
            return null;
        }

        boolean startWorker = false;
        if (contestRankingWorker.get(contest_id) == null) {
            contestRankingTableMap.remove(contest_id);
            new RankingWorker(contest_id).start();
            startWorker = true;
        } else {
            contestRankingWorker.get(contest_id).setStartDate(new Date().getTime());
        }

        if (admin == true) {
            return createRankingTableForSeries(contest_id, series_id, type, date, admin);
        }

        RankingTable rankingTable;

        if (startWorker) {
            rankingTable = getCachedRankingWithExpirationTime(seriesRankingTableMap, series_id, type, date, rankingRefreshRate * 1000);
        } else {
            rankingTable = getCachedRanking(seriesRankingTableMap, series_id, type);
        }

        if (rankingTable == null) {
            rankingTable = createRankingTableForSeries(contest_id, series_id, type, date, admin);
            seriesRankingTableMap.put(series_id, rankingTable);
        }

        return rankingTable;
    }
}
