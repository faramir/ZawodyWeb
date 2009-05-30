/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www;

import java.util.Calendar;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.pojo.Tests;

/**
 *
 * @author slawek
 */
public class ELFunctions {

    /**
     * Filter input URI against unwanted characters
     *
     * @param in
     *            input URI
     * @return filtered URI
     */
    public static String filterUri(String in) {
        String out = in.replaceAll("[^A-Za-z0-9]", "-");
        out = out.substring(0, Math.min(20, out.length())).toLowerCase().replaceAll("-+", "-").replaceAll("-$", "").replaceAll("^-", "");
        if (out.length() == 0) {
            out = "-";
        }
        return out;
    }

    public static String nlToBr(String in) {
        return in.replace("\n", "<br />");
    }

    public static String esc(String in) {
        return HtmlEscape.escape(in);
    }

    public static String nlDoubler(String in) {
        return in.replace("\n", "\n\n");
    }

    public static String coloring(Float in) {
        String out = Integer.toHexString((int)((1-in)*0xDF)+32);
        String out2;

            out2 = Integer.toHexString((int)((in)*0xDF)+32)+"61";

            while(out2.length()<4)
                out2 = "0"+out2;

        out = out + out2;
        
        while(out.length()<6)
            out = "0"+out;

        return "background: #"+out;
    }

    public static Boolean isNullOrZero(Integer a) {
        return a == null || a == 0;
    }

    public static Integer maxLimitTime(Problems p) {
        if (p.getTestss() != null) {
            Integer res = 0;
            for (Tests t : p.getTestss()) {
                res = Math.max(res, t.getTimelimit());
            }
            return res;
        } else {
            return null;
        }
    }

    public static Boolean testVisible(Tests t){
        return (t.getVisibility() != null && t.getVisibility().equals(1)) ||
               (t.getProblems().getSeries().getEnddate() != null && t.getProblems().getSeries().getEnddate().before(Calendar.getInstance().getTime()));
    }

    public static Boolean hasMaxResult(Submits s) {
        Integer a = maxPoints(s);
        Integer b = points(s);

        return a.equals(b) && !a.equals(-1) && !b.equals(-1);
    }

    public static Integer maxPoints(Submits s) {
        if (s.getResult() == null || !s.getResult().equals(SubmitsResultEnum.DONE.getCode()) || s.getResultss() == null || s.getResultss().size() == 0) {
            return -1;
        }
        int maxPoints = 0;

        for (Results r : s.getResultss()) {
            if (testVisible(r.getTests())) {
                maxPoints += r.getTests().getMaxpoints();
            }
        }

        return maxPoints;
    }

    public static Integer points(Submits s){
        if (s.getResult() == null || !s.getResult().equals(SubmitsResultEnum.DONE.getCode()) || s.getResultss() == null || s.getResultss().size() == 0) {
            return -1;
        }

        int points = 0;

        for (Results r : s.getResultss()) {
            if (testVisible(r.getTests())) {
                points += r.getPoints();
            }
        }

        return points;
    }
}
