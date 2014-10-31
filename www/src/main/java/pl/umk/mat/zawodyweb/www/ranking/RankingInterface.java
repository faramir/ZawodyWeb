/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www.ranking;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public interface RankingInterface {

    public RankingTable getRanking(int contest_id, Timestamp checkDate, boolean admin);

    public RankingTable getRankingForSeries(int contest_id, int series_id, Timestamp checkDate, boolean admin);

    public List<Integer> getRankingSolutions(int contest_id, Integer series_id, Timestamp checkDate, boolean admin);
}
