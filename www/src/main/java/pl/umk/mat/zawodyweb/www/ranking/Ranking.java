package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class Ranking {

    static private final Ranking instance = new Ranking();
    private Map<Integer, RankingTable> contestRankingTableMap = new ConcurrentHashMap<Integer, RankingTable>();
    private Map<Integer, RankingTable> seriesRankingTableMap = new ConcurrentHashMap<Integer, RankingTable>();

    private Ranking() {
    }

    /**
     * @return the instance
     */
    static public Ranking getInstance() {
        return instance;
    }

    private RankingTable getCachedRanking(Map<Integer, RankingTable> map, int key, int type, Date data, int rankingRefreshRate) {
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

    private void clearCachedRankingTable(Map<Integer, RankingTable> map, int key) {
        map.remove(key);
    }

    public void clearRankingTable(int contest_id) {
        clearCachedRankingTable(contestRankingTableMap, contest_id);
    }

    public void clearRankingTableForSeries(int contest_id) {
        clearCachedRankingTable(seriesRankingTableMap, contest_id);
    }

    public RankingTable getRanking(int contest_id, int type, int rankingRefreshRate, Date date, boolean admin) {
        if (!(type == 0 || type == 1 || type == 2)) {
            return null;
        }

        RankingTable rankingTable = getCachedRanking(contestRankingTableMap, contest_id, type, date, rankingRefreshRate * 1000);

        if (admin == true || rankingTable == null) {
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

                contestRankingTableMap.put(contest_id, rankingTable);
            }
            
            rankingTable.setType(type);
            rankingTable.setGenerationDate(date);
            rankingTable.setGenerationTime(new Date().getTime() - start);
        }

        return rankingTable;
    }

    public RankingTable getRankingForSeries(int contest_id, int series_id, int type, int rankingRefreshRate, Date date, boolean admin) {
        if (!(type == 0 || type == 1 || type == 2)) {
            return null;
        }

        RankingTable rankingTable = getCachedRanking(seriesRankingTableMap, series_id, type, date, rankingRefreshRate * 1000);

        if (admin == true || rankingTable == null) {
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
                seriesRankingTableMap.put(series_id, rankingTable);
            }

            rankingTable.setType(type);
            rankingTable.setGenerationDate(date);
            rankingTable.setGenerationTime(new Date().getTime() - start);
        }

        return rankingTable;
    }
}
