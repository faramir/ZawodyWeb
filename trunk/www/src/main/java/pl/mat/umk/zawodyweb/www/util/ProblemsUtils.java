package pl.mat.umk.zawodyweb.www.util;

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
    
    public boolean copySolution(Problems problem, Series serie, boolean copySolution){
        //TODO: implements this
        return false;
    }
    public boolean reJundge() {
        //TODO: implements this
        return false;
    }
}
