package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.hibernate.Transaction;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Contests;
import pl.umk.mat.zawodyweb.database.pojo.Series;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date: 2010-10-05 20:59:31 +0200 (Wt, 05 paź
 * 2010)$
 */
public class Ranking {

    private static final Ranking instance = new Ranking();
    private static final Executor executor = Executors.newFixedThreadPool(8);

    private class RankingWorker implements Runnable {

        Integer contest_id;
        Integer serie_id;
        Integer subcontest_id;

        public RankingWorker(Integer contest_id, Integer series_id, Integer subranking) {
            this.contest_id = contest_id;
            this.serie_id = series_id;
            this.subcontest_id = subranking;
        }

        @Override
        public void run() {
            Transaction transaction = null;
            try {

                transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

                if (contest_id != null) {
                    Contests contest = DAOFactory.DEFAULT.buildContestsDAO().getById(contest_id);
                    contestRankingTableMap.put(contest_id, createRankingTable(contest_id, contest.getType(), new Date(), false));
                    contestWorker.remove(contest_id);
                }
                if (serie_id != null) {
                    Series serie = DAOFactory.DEFAULT.buildSeriesDAO().getById(serie_id);
                    Contests contest = serie.getContests();
                    seriesRankingTableMap.put(serie_id, createRankingTableForSeries(contest.getId(), serie_id, contest.getType(), new Date(), false));
                    seriesWorker.remove(serie_id);
                }

                if (subcontest_id != null) {
                    Contests contest = DAOFactory.DEFAULT.buildContestsDAO().getById(subcontest_id);
                    if (contest.getSubtype() != 0) {
                        contestSubrankingTableMap.put(subcontest_id, createSubrankingTable(subcontest_id, contest.getSubtype(), new Date(), false));
                    } else {
                        contestSubrankingTableMap.remove(contest.getId());
                    }
                    subrankingWorker.remove(subcontest_id);
                }

                transaction.commit();
            } catch (Exception ex) {
            } finally {
                try {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                } catch (Exception e) {
                }

                if (contest_id != null) {
                    contestWorker.remove(contest_id);
                }
                if (serie_id != null) {
                    seriesWorker.remove(serie_id);
                }
                if (subcontest_id != null) {
                    subrankingWorker.remove(subcontest_id);
                }
            }
        }
    }
    private final Map<Integer, RankingTable> contestRankingTableMap = new ConcurrentHashMap<Integer, RankingTable>();
    private final Map<Integer, RankingTable> contestSubrankingTableMap = new ConcurrentHashMap<Integer, RankingTable>();
    private final Map<Integer, RankingTable> seriesRankingTableMap = new ConcurrentHashMap<Integer, RankingTable>();
    private final ConcurrentMap<Integer, Object> contestWorking = new ConcurrentHashMap<Integer, Object>();
    private final ConcurrentMap<Integer, Object> seriesWorking = new ConcurrentHashMap<Integer, Object>();
    private final ConcurrentMap<Integer, Object> subrankingWorking = new ConcurrentHashMap<Integer, Object>();
    private final Set<Integer> contestWorker = Collections.synchronizedSet(new HashSet<Integer>());
    private final Set<Integer> seriesWorker = Collections.synchronizedSet(new HashSet<Integer>());
    private final Set<Integer> subrankingWorker = Collections.synchronizedSet(new HashSet<Integer>());

    private Object getCacheSyncObject(ConcurrentMap<Integer, Object> map, final Integer id) {
        map.putIfAbsent(id, new Object());
        return map.get(id);
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
        RankingTable rankingTable;
        long start = new Date().getTime();

        RankingInterface ranking = getRankingInterface(type, null);

        rankingTable = ranking.getRanking(contest_id, new Timestamp(date.getTime()), admin);

        rankingTable.setType(type);
        rankingTable.setGenerationDate(date);
        rankingTable.setGenerationTime(new Date().getTime() - start);

        return rankingTable;
    }

    private RankingTable createRankingTableForSeries(int contest_id, int series_id, int type, Date date, boolean admin) {
        RankingTable rankingTable;
        long start = new Date().getTime();

        RankingInterface ranking = getRankingInterface(type, null);

        rankingTable = ranking.getRankingForSeries(contest_id, series_id, new Timestamp(date.getTime()), admin);

        rankingTable.setType(type);
        rankingTable.setGenerationDate(date);
        rankingTable.setGenerationTime(new Date().getTime() - start);

        return rankingTable;
    }

    private RankingTable createSubrankingTable(int contest_id, int subtype, Date date, boolean admin) {
        RankingTable rankingTable;
        long start = new Date().getTime();

        RankingInterface ranking = getRankingInterface(null, subtype);

        rankingTable = ranking.getRanking(contest_id, new Timestamp(date.getTime()), admin);

        rankingTable.setType(subtype);
        rankingTable.setGenerationDate(date);
        rankingTable.setGenerationTime(new Date().getTime() - start);

        return rankingTable;
    }

    public RankingTable getRanking(int contest_id, int type, int rankingRefreshRate, Date rankingDate, boolean admin) {
        if (!(type == 0 || type == 1 || type == 2)) {
            return null;
        }

        if (admin == true) {
            return createRankingTable(contest_id, type, rankingDate, admin);
        }

        if (rankingRefreshRate < 0) {
            rankingRefreshRate = 0;
        }
        if (Math.abs(rankingDate.getTime() - new Date().getTime()) > (rankingRefreshRate + 5) * 1000) {
            return createRankingTable(contest_id, type, rankingDate, admin);
        }

        RankingTable rankingTable;
        synchronized (getCacheSyncObject(contestWorking, contest_id)) {
            rankingTable = getCachedRanking(contestRankingTableMap, contest_id, type);

            if (rankingTable == null) {
                rankingTable = createRankingTable(contest_id, type, rankingDate, admin);
                contestRankingTableMap.put(contest_id, rankingTable);
            } else {
                if (!contestWorker.contains(contest_id) && (new Date().getTime() - rankingTable.getGenerationDate().getTime() > (rankingRefreshRate + 1) * 1000)) {
                    contestWorker.add(contest_id);
                    executor.execute(new RankingWorker(contest_id, null, null));
                }
            }
        }

        return rankingTable;
    }

    public RankingTable getRankingForSeries(int contest_id, int series_id, int type, int rankingRefreshRate, Date rankingDate, boolean admin) {
        if (!(type == 0 || type == 1 || type == 2)) {
            return null;
        }

        if (admin == true) {
            return createRankingTableForSeries(contest_id, series_id, type, rankingDate, admin);
        }

        if (rankingRefreshRate < 0) {
            rankingRefreshRate = 0;
        }
        if (Math.abs(rankingDate.getTime() - new Date().getTime()) > (rankingRefreshRate + 5) * 1000) {
            return createRankingTableForSeries(contest_id, series_id, type, rankingDate, admin);
        }

        RankingTable rankingTable;
        synchronized (getCacheSyncObject(seriesWorking, series_id)) {
            rankingTable = getCachedRanking(seriesRankingTableMap, series_id, type);

            if (rankingTable == null) {
                rankingTable = createRankingTableForSeries(contest_id, series_id, type, rankingDate, admin);
                seriesRankingTableMap.put(series_id, rankingTable);
            } else {
                if (!seriesWorker.contains(series_id) && (new Date().getTime() - rankingTable.getGenerationDate().getTime() > (rankingRefreshRate + 1) * 1000)) {
                    seriesWorker.add(series_id);
                    executor.execute(new RankingWorker(null, series_id, null));
                }
            }
        }

        return rankingTable;
    }

    public RankingTable getSubranking(int contest_id, int type, int rankingRefreshRate, Date rankingDate, boolean admin) {
        if (!(type == 1 || type == 2)) {
            return null;
        }

        if (admin == true) {
            return createSubrankingTable(contest_id, type, rankingDate, admin);
        }

        if (rankingRefreshRate < 0) {
            rankingRefreshRate = 0;
        }
        if (Math.abs(rankingDate.getTime() - new Date().getTime()) > (rankingRefreshRate + 5) * 1000) {
            return createSubrankingTable(contest_id, type, rankingDate, admin);
        }

        RankingTable rankingTable;
        synchronized (getCacheSyncObject(subrankingWorking, contest_id)) {
            rankingTable = getCachedRanking(contestSubrankingTableMap, contest_id, type);

            if (rankingTable == null) {
                rankingTable = createSubrankingTable(contest_id, type, rankingDate, admin);
                contestSubrankingTableMap.put(contest_id, rankingTable);
            } else {
                if (!subrankingWorker.contains(contest_id) && (new Date().getTime() - rankingTable.getGenerationDate().getTime() > (rankingRefreshRate + 1) * 1000)) {
                    subrankingWorker.add(contest_id);
                    executor.execute(new RankingWorker(null, null, contest_id));
                }
            }
        }

        return rankingTable;
    }

    public List<Integer> getSolutions(int contest_id, Integer series_id, int type, Date date, boolean admin) {
        RankingInterface ranking = getRankingInterface(type, null);

        if (ranking == null) {
            return null;
        }

        return ranking.getRankingSolutions(contest_id, series_id, new Timestamp(date.getTime()), admin);
    }
}
