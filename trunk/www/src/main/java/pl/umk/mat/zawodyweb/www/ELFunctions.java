/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www;

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
}
