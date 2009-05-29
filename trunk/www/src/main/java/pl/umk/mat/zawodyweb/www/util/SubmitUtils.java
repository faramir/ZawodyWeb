package pl.umk.mat.zawodyweb.www.util;

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

    public void reJudge() {
        //TODO: implement this
    }

    public void deleteSubmit(Submits submit) {
        //TODO: implement this
    }
}
