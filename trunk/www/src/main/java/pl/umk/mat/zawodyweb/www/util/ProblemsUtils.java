package pl.umk.mat.zawodyweb.www.util;

import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;

/**
 *
 * @author Jakub Prabucki
 */
public class ProblemsUtils {

    static private final ProblemsUtils instance = new ProblemsUtils();

    private ProblemsUtils() {
    }
    
    public static ProblemsUtils getInstance(){
        return instance;
    }
    
    public Problems copySolution(Problems problem, Series serie, boolean copySolution){
        //TODO: implement this
        return null;
    }
    public void reJudge() {
        //TODO: implement this
    }
}
