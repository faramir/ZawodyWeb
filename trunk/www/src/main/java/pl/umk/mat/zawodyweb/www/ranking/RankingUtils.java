package pl.umk.mat.zawodyweb.www.ranking;

import java.text.Collator;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public class RankingUtils {

    private static Collator collator;

    static {
        collator = Collator.getInstance();
        collator.setStrength(Collator.CANONICAL_DECOMPOSITION);
    }

    public static String formatText(String text, String title, String style) {
        return String.format("<abbr%s%s>%s</abbr>", style == null ? "" : " class=\"" + style + "\"", title == null ? "" : " title=\"" + title + "\"", text);
    }

    public static int compareStrings(String str1, String str2) {
        return collator.compare(str1, str2);
    }
}
