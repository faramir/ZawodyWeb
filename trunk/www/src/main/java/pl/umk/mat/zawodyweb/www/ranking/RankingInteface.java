package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public interface RankingInteface {

    public RankingTable getRanking(int contest_id, Timestamp checkDate, boolean admin);

    public RankingTable getRankingForSeries(int contest_id, int series_id, Timestamp checkDate, boolean admin);

    public int[] getRankingSolutions(int contest_id, Integer series_id, Timestamp checkDate, boolean admin);
}
