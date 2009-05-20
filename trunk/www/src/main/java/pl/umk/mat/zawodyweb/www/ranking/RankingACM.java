package pl.umk.mat.zawodyweb.www.ranking;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.ProblemsDAO;
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.pojo.Tests;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingACM {

    static private final RankingACM instance = new RankingACM();

    /**
     * @return the instance
     */
    static public RankingACM getInstance() {
        return instance;
    }

    private class MapProblems {

        String problemName;
        String problemAbbrev;
        int maxPoints;

        public MapProblems(String problemName, String problemAbbrev, int maxPoints) {
            this.problemName = problemName;
            this.problemAbbrev = problemAbbrev;
            this.maxPoints = maxPoints;
        }
    }

    Vector<RankingEntry> getRanking(int contest_id, Date checkDate, boolean admin) {
        SeriesDAO seriesDAO = DAOFactory.DEFAULT.buildSeriesDAO();
        ProblemsDAO problemsDAO = DAOFactory.DEFAULT.buildProblemsDAO();

        HashMap<Integer, MapProblems> mapProblems = new HashMap<Integer, MapProblems>();
        HashMap<Integer, Boolean> mapTestsVisible = new HashMap<Integer, Boolean>();

        List<Series> listSeries = seriesDAO.findByContestsid(contest_id);

        for (Series series : listSeries) {
            List<Problems> listProblems = problemsDAO.findBySeriesid(series.getId());
            for (Problems problems : listProblems) {
                int maxPoints = 0;
                for (Tests tests : problems.getTestss()) {
                    if (admin == true || tests.getVisibility().equals(1) || checkDate.before(series.getFreezedate()) || checkDate.after(series.getUnfreezedate())) {
                        maxPoints += tests.getMaxpoints();
                        mapTestsVisible.put(tests.getId(), true);
                    } else {
                        mapTestsVisible.put(tests.getId(), false);
                    }
                }
                mapProblems.put(problems.getId(), new MapProblems(problems.getName(), problems.getAbbrev(), maxPoints));
                problems.getSubmitss();
            }
        }
        return null;

    }
}
