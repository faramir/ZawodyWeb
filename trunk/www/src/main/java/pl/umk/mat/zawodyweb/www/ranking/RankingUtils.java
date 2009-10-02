package pl.umk.mat.zawodyweb.www.ranking;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public class RankingUtils {

    public static String formatText(String text, String title, String style) {
        return String.format("<abbr%s%s>%s</abbr>", style == null ? "" : " class=\"" + style + "\"", title == null ? "" : " title=\"" + title + "\"", text);
    }
}
