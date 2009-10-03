package pl.umk.mat.zawodyweb.www.util;

import java.util.List;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.pojo.Contests;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;

/**
 *
 * @author Jakub Prabucki
 */
public class ContestsUtils {

    static private final ContestsUtils instance = new ContestsUtils();

    private ContestsUtils() {
    }

    public static ContestsUtils getInstance() {
        return instance;
    }

    public void reJudge(Contests contest) {
        List<Series> findByContestsid = DAOFactory.DEFAULT.buildSeriesDAO().findByContestsid(contest.getId());
        for (Series serie : findByContestsid) {
            List<Problems> findBySeriesid = DAOFactory.DEFAULT.buildProblemsDAO().findBySeriesid(serie.getId());
            for (Problems problem : findBySeriesid) {
                ProblemsUtils.getInstance().reJudge(problem);
            }
        }
    }
}
