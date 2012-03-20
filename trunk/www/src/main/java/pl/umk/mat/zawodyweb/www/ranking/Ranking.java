package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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
 * @version $Rev$ Date: $Date: 2010-10-05 20:59:31 +0200 (Wt, 05 paź 2010)
 * $
 */
public class Ranking {

    private static final Logger logger = Logger.getLogger(Ranking.class);
    static private final Ranking instance = new Ranking();
    private static final long rankingWorkerLive = 3 * 60 * 60 * 1000; //3h
    private Map<Integer, RankingWorker> contestRankingWorker = new ConcurrentHashMap<Integer, RankingWorker>();
    private Map<Integer, RankingTable> contestRankingTableMap = new ConcurrentHashMap<Integer, RankingTable>();
    private Map<Integer, RankingTable> contestSubrankingTableMap = new ConcurrentHashMap<Integer, RankingTable>();
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
            Transaction transaction = null;
            try {
                logger.warn("[" + contest_id + "] Adding RankingWorker to Map...");
                contestRankingWorker.put(contest_id, this);
                Contests contest = null;
                while (start_date > (new Date().getTime() - rankingWorkerLive)) {
                    transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
                    contest = DAOFactory.DEFAULT.buildContestsDAO().getById(contest_id);

                    if (contest == null) {
                        logger.warn("[" + contest_id + "] Contest not found in database, ending RankingWorker...");
                        break;
                    }

                    logger.warn("[" + contest_id + "] Creating RankingTable for contest: " + contest.getName() + " (" + contest.getId() + ")");

                    contestRankingTableMap.put(contest.getId(), createRankingTable(contest.getId(), contest.getType(), new Date(), false));

                    for (Series serie : DAOFactory.DEFAULT.buildSeriesDAO().findByContestsid(contest.getId())) {
                        logger.warn("[" + contest_id + "] Creating RankingTable for serie: " + serie.getName() + " (" + serie.getId() + ")");
                        seriesRankingTableMap.put(serie.getId(), createRankingTableForSeries(contest.getId(), serie.getId(), contest.getType(), new Date(), false));
                    }

                    if (contest.getSubtype() != 0) {
                        logger.warn("[" + contest_id + "] Creating RankingTable for contests' subranking: " + contest.getName() + " (" + contest.getId() + ")");
                        contestSubrankingTableMap.put(contest.getId(), createSubrankingTable(contest.getId(), contest.getSubtype(), new Date(), false));
                    } else {
                        contestSubrankingTableMap.remove(contest.getId());
                    }

                    transaction.commit();

                    if (contest.getRankingrefreshrate() <= 0) {
                        logger.warn("[" + contest_id + "] Contest RefreshRate <= 0, ending RankingWorker...");
                        break;
                    }

                    logger.warn("[" + contest_id + "] Waiting " + contest.getRankingrefreshrate() + " seconds...");
                    Thread.sleep(contest.getRankingrefreshrate() * 1000);
                }
                logger.warn("[" + contest_id + "] RankingWorker live time elapsed (" + new Date(start_date) + " >  (" + new Date() + ")");
            } catch (Exception ex) {
                logger.fatal("[" + contest_id + "] Fatal exception", ex);
            } finally {
                logger.warn("[" + contest_id + "] Removing RankingWorker from Map...");
                contestRankingWorker.remove(contest_id);
                try {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private Ranking() {
    }

    private RankingInterface getRankingInterface(Integer type, Integer subtype) {
        if (subtype != null) {
            switch (subtype) {
                case 1:
                    return new SubrankingKI("s1");
                case 2:
                    return new SubrankingKI("s2");
            }
        }
        if (type != null) {
            switch (type) {
                case 0:
                    return new RankingACM();
                case 1:
                    return new RankingPA();
                case 2:
                    return new RankingKI();
            }
        }
        return null;
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

        RankingInterface ranking = getRankingInterface(type, null);

        rankingTable = ranking.getRanking(contest_id, new Timestamp(date.getTime()), admin);

        rankingTable.setType(type);
        rankingTable.generateHtml(admin);
        rankingTable.setGenerationDate(date);
        rankingTable.setGenerationTime(new Date().getTime() - start);

        return rankingTable;
    }

    private RankingTable createRankingTableForSeries(int contest_id, int series_id, int type, Date date, boolean admin) {
        RankingTable rankingTable = null;
        long start = new Date().getTime();

        RankingInterface ranking = getRankingInterface(type, null);

        rankingTable = ranking.getRankingForSeries(contest_id, series_id, new Timestamp(date.getTime()), admin);

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

        if (rankingRefreshRate < 0) {
            rankingRefreshRate = 0;
        }

        if (contestRankingWorker.get(contest_id) == null) {
            if (rankingRefreshRate > 0) {
                contestRankingTableMap.remove(contest_id);
                new RankingWorker(contest_id).start();
            }
        } else {
            if (rankingRefreshRate > 0) {
                contestRankingWorker.get(contest_id).setStartDate(new Date().getTime());
            } else {
                contestRankingWorker.get(contest_id).interrupt();
            }
        }

        if (admin == true) {
            return createRankingTable(contest_id, type, date, admin);
        }
        if (Math.abs(date.getTime() - new Date().getTime()) > (rankingRefreshRate + 1) * 1000) {
            return createRankingTable(contest_id, type, date, admin);
        }

        RankingTable rankingTable = null;
        if (rankingRefreshRate > 0) {
            rankingTable = getCachedRanking(contestRankingTableMap, contest_id, type);
        }
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

        if (rankingRefreshRate < 0) {
            rankingRefreshRate = 0;
        }

        if (contestRankingWorker.get(contest_id) == null) {
            if (rankingRefreshRate > 0) {
                contestRankingTableMap.remove(contest_id);
                new RankingWorker(contest_id).start();
            }
        } else {
            if (rankingRefreshRate > 0) {
                contestRankingWorker.get(contest_id).setStartDate(new Date().getTime());
            } else {
                contestRankingWorker.get(contest_id).interrupt();
            }
        }

        if (admin == true) {
            return createRankingTableForSeries(contest_id, series_id, type, date, admin);
        }
        if (Math.abs(date.getTime() - new Date().getTime()) > (rankingRefreshRate + 1) * 1000) {
            return createRankingTableForSeries(contest_id, series_id, type, date, admin);
        }

        RankingTable rankingTable = null;

        if (rankingRefreshRate > 0) {
            rankingTable = getCachedRanking(seriesRankingTableMap, series_id, type);
        }

        if (rankingTable == null) {
            rankingTable = createRankingTableForSeries(contest_id, series_id, type, date, admin);
            seriesRankingTableMap.put(series_id, rankingTable);
        }

        return rankingTable;
    }

    private RankingTable createSubrankingTable(int contest_id, int subtype, Date date, boolean admin) {
        RankingTable rankingTable = null;
        long start = new Date().getTime();

        RankingInterface ranking = getRankingInterface(null, subtype);

        rankingTable = ranking.getRanking(contest_id, new Timestamp(date.getTime()), admin);

        rankingTable.setType(subtype);
        rankingTable.generateHtml(admin);
        rankingTable.setGenerationDate(date);
        rankingTable.setGenerationTime(new Date().getTime() - start);

        return rankingTable;
    }

    public RankingTable getSubranking(int contest_id, int type, int rankingRefreshRate, Date date, boolean admin) {
        if (!(type == 1 || type == 2)) {
            return null;
        }

        if (rankingRefreshRate < 0) {
            rankingRefreshRate = 0;
        }

        if (contestRankingWorker.get(contest_id) == null) {
            if (rankingRefreshRate > 0) {
                contestRankingTableMap.remove(contest_id);
                new RankingWorker(contest_id).start();
            }
        } else {
            if (rankingRefreshRate > 0) {
                contestRankingWorker.get(contest_id).setStartDate(new Date().getTime());
            } else {
                contestRankingWorker.get(contest_id).interrupt();
            }
        }

        if (admin == true) {
            return createSubrankingTable(contest_id, type, date, admin);
        }
        if (Math.abs(date.getTime() - new Date().getTime()) > (rankingRefreshRate + 1) * 1000) {
            return createSubrankingTable(contest_id, type, date, admin);
        }

        RankingTable rankingTable = null;
        if (rankingRefreshRate > 0) {
            rankingTable = getCachedRanking(contestSubrankingTableMap, contest_id, type);
        }
        if (rankingTable == null) {
            rankingTable = createSubrankingTable(contest_id, type, date, admin);
            contestSubrankingTableMap.put(contest_id, rankingTable);
        }

        return rankingTable;
    }

    public List<Integer> getRankingSolutions(int contest_id, Integer series_id, Integer type, Integer subtype, Timestamp checkDate, boolean admin) {
        RankingInterface ranking = getRankingInterface(type, subtype);

        if (ranking == null) {
            return null;
        }

        return ranking.getRankingSolutions(contest_id, series_id, checkDate, admin);
    }
}
