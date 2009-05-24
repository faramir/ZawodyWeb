package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;
import pl.umk.mat.zawodyweb.database.ContestsDAO;
import pl.umk.mat.zawodyweb.database.DAOFactory;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class Ranking {

    static private final Ranking instance = new Ranking();

    /**
     * @return the instance
     */
    static public Ranking getInstance() {
        return instance;
    }

    Vector<RankingEntry> getRanking(int contest_id, Date date, boolean admin) {
        ContestsDAO contestsDAO = DAOFactory.DEFAULT.buildContestsDAO();

        RankingInteface ranking = null;

        if (contestsDAO.getById(contest_id).getType().equals(0)) {
            ranking = new RankingACM();
        } else if (contestsDAO.getById(contest_id).getType().equals(1)) {
            ranking = new RankingACM();
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
}
