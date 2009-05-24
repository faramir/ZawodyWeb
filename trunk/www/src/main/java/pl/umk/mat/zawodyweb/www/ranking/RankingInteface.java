package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.Vector;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public interface RankingInteface {

    public Vector<RankingEntry> getRanking(int contest_id, Timestamp checkDate);

    public Vector<RankingEntry> getRankingForAdmin(int contest_id, Timestamp checkDate);
}
