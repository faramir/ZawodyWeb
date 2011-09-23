/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import pl.umk.mat.zawodyweb.database.CheckerErrors;
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
        //return in.replaceAll("\n\r*\n\r*", "<br/>\n"); // wymaga kilku(dziesięciu) modyfikacji na bazie...
        return in.replace("\n", "<br />");
    }

    public static String esc(String in) {
        return HtmlEscape.escape(in);
    }

    public static String nlDoubler(String in) {
        return in.replace("\n", "\n\n");
    }

    public static String submitResults(Submits s) {
        if (s.getResult() == null || !s.getResult().equals(SubmitsResultEnum.DONE.getCode()) || s.getResultss() == null || s.getResultss().isEmpty()) {
            return "";
        }

        int worseResult = Integer.MAX_VALUE;
        String result = "";

        int nowResult;
        for (Results r : s.getResultss()) {
            if (testVisible(r.getTests())) {
                nowResult = 0;
                if (worseResult > ++nowResult && r.getSubmitResult() == CheckerErrors.UNDEF) {
                    worseResult = nowResult;
                    result = " (!)";
                } else if (worseResult > ++nowResult && r.getSubmitResult() == CheckerErrors.UNKNOWN) {
                    worseResult = nowResult;
                    result = " (?)";
                } else if (worseResult > ++nowResult && r.getSubmitResult() == CheckerErrors.RV) {
                    worseResult = nowResult;
                    result = " (RV)";
                } else if (worseResult > ++nowResult && r.getSubmitResult() == CheckerErrors.CTLE) {
                    worseResult = nowResult;
                    result = " (CTLE)";
                } else if (worseResult > ++nowResult && r.getSubmitResult() == CheckerErrors.CE) {
                    worseResult = nowResult;
                    result = " (CE)";
                } else if (worseResult > ++nowResult && r.getSubmitResult() == CheckerErrors.MLE) {
                    worseResult = nowResult;
                    result = " (MLE)";
                } else if (worseResult > ++nowResult && r.getSubmitResult() == CheckerErrors.RE) {
                    worseResult = nowResult;
                    result = " (RTE)";
                } else if (worseResult > ++nowResult && r.getSubmitResult() == CheckerErrors.TLE) {
                    worseResult = nowResult;
                    result = " (TLE)";
                } else if (worseResult > ++nowResult && r.getSubmitResult() == CheckerErrors.WA) {
                    worseResult = nowResult;
                    result = " (WA)";
                } else if (worseResult > ++nowResult && r.getSubmitResult() == CheckerErrors.ACC) {
                    worseResult = nowResult;
                    result = "";
                }
            }
        }

        return result;
    }

    public static String coloring2(Submits s) {
        int good = 0;
        int size = 0;
        int points = 0;
        int maxPoints = 0;

        if (s.getResult() == null || !s.getResult().equals(SubmitsResultEnum.DONE.getCode()) || s.getResultss() == null || s.getResultss().isEmpty()) {
        } else {
            List<Results> results = s.getResultss();

            for (Results r : results) {
                if (testVisible(r.getTests())) {
                    points += r.getPoints();
                    maxPoints += r.getTests().getMaxpoints();
                    if (r.getSubmitResult() == CheckerErrors.ACC) {
                        ++good;
                    }
                    ++size;
                }
            }
        }

        return coloring(points * size + good, maxPoints * size + size, CheckerErrors.ACC);
    }

    public static String coloring(Integer in1, Integer in2, Integer submitResult) {
        if (in2 == 0) {
            in1 = 1;
            in2 = 1;
            if (submitResult != CheckerErrors.ACC) {
                in1 = 0;
            }
        }
        Float in = (float) in1 / (float) in2;
        String out = Integer.toHexString((int) ((1 - in) * 0x55) + 0xaa);
        String out2 = Integer.toHexString((int) ((in) * 0x55) + 0xaa) + "aa";

        return "background: #" + out + out2;
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

    public static Boolean testVisible(Tests t) {
        return (t.getVisibility() != null && t.getVisibility().equals(1))
                || (t.getProblems().getSeries().getEnddate() != null && t.getProblems().getSeries().getEnddate().before(Calendar.getInstance().getTime()));
    }

    public static Boolean hasMaxResult(Submits s) {
        Integer a = maxPoints(s);
        Integer b = points(s);

        return a.equals(b) && !a.equals(-1) && !b.equals(-1);
    }

    public static Integer maxPoints(Submits s) {
        if (s.getResult() == null || !s.getResult().equals(SubmitsResultEnum.DONE.getCode()) || s.getResultss() == null || s.getResultss().isEmpty()) {
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

    public static Integer points(Submits s) {
        if (s.getResult() == null || !s.getResult().equals(SubmitsResultEnum.DONE.getCode()) || s.getResultss() == null || s.getResultss().isEmpty()) {
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

    public static Boolean submitDateOk(Problems p) {
        Date now = new Date();
        return p.getSeries().getStartdate().before(now) && (p.getSeries().getEnddate() == null || p.getSeries().getEnddate().after(now));
    }

    public static boolean isValidIP(String[] ips, String clientIp) {
        if (ips == null || ips.length == 0) {
            return true;
        }
        for (String ip : ips) {
            if (clientIp.startsWith(ip)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean submitIpOk(Problems p, String clientIp) {
        return isValidIP(p.getSeries().getOpenips(false), clientIp);
    }

    public static String dateAndHour(Timestamp t) {
        if (t == null) {
            return "";
        }
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(t);
    }

    public static String formatDateAndTime(Date d, String format) {
        if (d == null || format == null) {
            return "";
        }
        return new SimpleDateFormat(format).format(d);
    }

    /**
     * http://stackoverflow.com/questions/277521/how-to-identify-the-file-content-is-in-ascii-or-binary/277568#277568
     *
     * If the first two bytes are hex FE FF, the file is tentatively UTF-16 BE.
     * If the first two bytes are hex FF FE, and the following two bytes are not hex 00 00 , the file is tentatively UTF-16 LE.
     * If the first four bytes are hex 00 00 FE FF, the file is tentatively UTF-32 BE.
     * If the first four bytes are hex FF FE 00 00, the file is tentatively UTF-32 LE.
     * @param text
     * @return true if file is binary
     */
    public static Boolean isBinarySubmit(Submits submits) {
        byte[] data = submits.getCode();

        if (data[0] == 0xFE && data[1] == 0xFF) {
            return false;
        } else if (data[0] == 0xFF && data[1] == 0xFE) {
            return false;
        } else if (data[0] == 0x00 && data[1] == 0x00 && data[2] == 0xFE && data[3] == 0xFF) {
            return false;
        } else if (data[0] == 0xFF && data[1] == 0xFE && data[2] == 0x00 && data[3] == 0x00) {
            return false;
        }
        for (byte b : data) {
            if (b == 0) {
                return true;
            }
        }
        return false;
    }

    public static String getSubmitCodeString(Submits submits) {
        return new String(submits.getCode());
    }

    public static Integer getStringLength(String s) {
        return s.length();
    }

    public static String getPrefix(String s, Integer length) {
        if (length > s.length()) {
            return s;
        }
        return s.substring(0, length);
    }
}
