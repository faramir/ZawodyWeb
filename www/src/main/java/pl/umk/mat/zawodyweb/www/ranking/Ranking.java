package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Date;
import pl.umk.mat.zawodyweb.database.ContestsDAO;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.pojo.Contests;

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

    public RankingTable getRanking(Contests contest, Date date, boolean admin) {
        RankingInteface ranking = null;

        if (contest.getType().equals(0)) {
            ranking = new RankingACM();
        } else if (contest.getType().equals(1)) {
            ranking = new RankingKI();
        }

        if (ranking == null) {
            return null;
        }

        if (admin == true) {
            return ranking.getRankingForAdmin(contest.getId(), new Timestamp(date.getTime()));
        } else {
            return ranking.getRanking(contest.getId(), new Timestamp(date.getTime()));
        }
    }

    public RankingTable getRankingForSeries(Contests contest, int series_id, Date date, boolean admin) {
        RankingInteface ranking = null;

        if (contest.getType().equals(0)) {
            ranking = new RankingACM();
        } else if (contest.getType().equals(1)) {
            ranking = new RankingKI();
        }

        if (ranking == null) {
            return null;
        }

        if (admin == true) {
            return ranking.getRankingForSeriesForAdmin(contest.getId(), series_id, new Timestamp(date.getTime()));
        } else {
            return ranking.getRankingForSeries(contest.getId(), series_id, new Timestamp(date.getTime()));
        }
    }
}
