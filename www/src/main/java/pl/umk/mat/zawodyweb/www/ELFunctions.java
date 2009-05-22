/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www;

import pl.umk.mat.zawodyweb.database.pojo.Problems;
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
}
