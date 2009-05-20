package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
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

    Vector<RankingEntry> getRanking(int contest_id, Timestamp date, boolean admin) {
        ContestsDAO contestsDAO = DAOFactory.DEFAULT.buildContestsDAO();

        if (contestsDAO.getById(contest_id).getType().equals(0)) {
            return RankingACM.getInstance().getRanking(contest_id, date, admin);
        } else if (contestsDAO.getById(contest_id).getType().equals(1)) {
            return null;
        }
        return null;
    }
}
