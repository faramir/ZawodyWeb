package pl.mat.umk.zawodyweb.www.util;

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

    public boolean reJundge() {
        //TODO: implements this
        return false;
    }
}
