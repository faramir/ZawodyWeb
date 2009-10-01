package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class Ranking {

    static private final Ranking instance = new Ranking();

    private Ranking() {
    }

    /**
     * @return the instance
     */
    static public Ranking getInstance() {
        return instance;
    }

    public RankingTable getRanking(int contest_id, int type, Date date, boolean admin) {
        RankingInteface ranking = null;

        if (type == 0) { // ACM
            ranking = new RankingACM();
        } else if (type == 1) { // FIXME: Tu powinien być ranking PA
            ranking = new RankingKI();
        } else if (type == 2) { // KI
            ranking = new RankingKI();
        }

        if (ranking == null) {
            return null;
        }

        if (admin == true) {
            return ranking.getRankingForAdmin(contest_id, new Timestamp(date.getTime()));
        } else {
            return ranking.getRanking(contest_id, new Timestamp(date.getTime()));
        }
    }

    public RankingTable getRankingForSeries(int contest_id, int series_id, int type, Date date, boolean admin) {
        RankingInteface ranking = null;

        if (type == 0) { // ACM
            ranking = new RankingACM();
        } else if (type == 1) { // FIXME: Tu powinno być PA
            ranking = new RankingKI();
        } else if (type==2) { // KI
            ranking = new RankingKI();
        }

        if (ranking == null) {
            return null;
        }

        if (admin == true) {
            return ranking.getRankingForSeriesForAdmin(contest_id, series_id, new Timestamp(date.getTime()));
        } else {
            return ranking.getRankingForSeries(contest_id, series_id, new Timestamp(date.getTime()));
        }
    }
}
