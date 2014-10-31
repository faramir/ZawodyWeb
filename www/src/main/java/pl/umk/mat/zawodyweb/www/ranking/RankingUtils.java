/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www.ranking;

import java.text.Collator;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date: 2009-12-08 15:16:30 +0100 (Wt, 08 gru 2009)
 * $
 */
public class RankingUtils {

    private static final Collator collator;

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

    public static String generateHtml(RankingTable ranking, boolean loggedIn) {
        int itc;
        int itr;
        StringBuilder out = new StringBuilder();

        itr = 1; // <c:set var="itr" value="1" />
        for (RankingEntry entry : ranking.getTable()) { // <c:forEach var="entry" items="#{requestBean.currentContestRanking.table}">
            out.append("<tr class=\"").append(itr % 2 == 0 ? "linia1" : "linia2").append("\">"); // <tr class="${itr % 2 == 0 ? 'linia1' : 'linia2'}">
            out.append("<td class=\"small\">"); // <td class="small">
            out.append("<span title=\"").append(itr).append("\">").append(entry.getPlace()).append("</span>"); // <h:outputText value="#{entry.place}" title="#{itr}" rendered="#{requestBean.temporaryAdminBoolean == true}" />

            out.append("</td><td class=\"big\">").append(org.apache.commons.lang.StringEscapeUtils.escapeHtml(entry.getUsername(loggedIn))).append("</td>"); // </td><td class="big"><h:outputText value="#{entry.username}" /></td>

            itc = 0; //<c:set var="itc" value="0" />
            for (String column : entry.getTable()) { // <c:forEach var="column" items="#{entry.table}">
                out.append("<td class=\"").append(ranking.getColumnsCSS().get(itc)).append("\">").append(column).append("</td>"); // <td class="${requestBean.currentContestRanking.columnsCSS[itc]}"><h:outputText escape="false" value="#{column}" /></td>
                ++itc; // <c:set var="itc" value="#{itc + 1}" />
            } // </c:forEach>
            out.append("</tr>"); // </tr>

            ++itr; // <c:set var="itr" value="#{itr + 1}" />
        } // </c:forEach>

        return out.toString();
    }
}
