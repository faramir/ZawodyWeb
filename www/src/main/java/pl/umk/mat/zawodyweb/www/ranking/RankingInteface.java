package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public interface RankingInteface {

    public RankingTable getRanking(int contest_id, Timestamp checkDate);

    public RankingTable getRankingForAdmin(int contest_id, Timestamp checkDate);
}
