package pl.umk.mat.zawodyweb.www.util;

import java.util.List;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;

/**
 *
 * @author Jakub Prabucki
 */
public class SeriesUtils {

    static private final SeriesUtils instance = new SeriesUtils();

    private SeriesUtils() {
    }

    public static SeriesUtils getInstance() {
        return instance;
    }

    public void reJudge(Series serie) {
        List<Problems> findBySeriesid = DAOFactory.DEFAULT.buildProblemsDAO().findBySeriesid(serie.getId());
        for (Problems problem : findBySeriesid){
            ProblemsUtils.getInstance().reJudge(problem);
        }
    }
}
