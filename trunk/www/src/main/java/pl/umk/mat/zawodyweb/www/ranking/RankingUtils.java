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

    public static String generateHtml(RankingTable ranking, boolean showRowNumber) {
        int itc;
        int itr;
        String out = "";

        itr = 1; // <c:set var="itr" value="1" />
        for (RankingEntry entry : ranking.getTable()) { // <c:forEach var="entry" items="#{requestBean.currentContestRanking.table}">
            out += "<tr class=\"" + (itr % 2 == 0 ? "linia1" : "linia2") + "\">"; // <tr class="${itr % 2 == 0 ? 'linia1' : 'linia2'}">
            out += "<td class=\"small\">"; // <td class="small">
            if (showRowNumber) {
                out += "<span title=\"" + itr + "\">" + entry.getPlace() + "</span>"; // <h:outputText value="#{entry.place}" title="#{itr}" rendered="#{requestBean.temporaryAdminBoolean == true}" />
            } else {
                out += entry.getPlace(); // <h:outputText value="#{entry.place}" rendered="#{requestBean.temporaryAdminBoolean == false}" />
            }

            out += "</td><td class=\"big\">" + org.apache.commons.lang.StringEscapeUtils.escapeHtml(entry.getUsername()) + "</td>"; // </td><td class="big"><h:outputText value="#{entry.username}" /></td>

            itc = 0; //<c:set var="itc" value="0" />
            for (String column : entry.getTable()) { // <c:forEach var="column" items="#{entry.table}">
                out += "<td class=\"" + ranking.getColumnsCSS().get(itc) + "\">" + column + "</td>"; // <td class="${requestBean.currentContestRanking.columnsCSS[itc]}"><h:outputText escape="false" value="#{column}" /></td>
                ++itc; // <c:set var="itc" value="#{itc + 1}" />
            } // </c:forEach>
            out += "</tr>"; // </tr>

            ++itr; // <c:set var="itr" value="#{itr + 1}" />
        } // </c:forEach>

        return out;
    }
}
