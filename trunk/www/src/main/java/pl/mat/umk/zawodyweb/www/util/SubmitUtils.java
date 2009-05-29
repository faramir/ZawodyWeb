package pl.mat.umk.zawodyweb.www.util;

import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 *
 * @author Jakub Prabucki
 */
public class SubmitUtils {

    static private final SubmitUtils instance = new SubmitUtils();

    private SubmitUtils() {
    }

    public static SubmitUtils getInstance() {
        return instance;
    }

    public boolean reJundge() {
        //TODO: implements this
        return false;
    }

    public boolean deleteSubmit(Submits submit){
        //TODO: implements this
        return false;
    }
}
