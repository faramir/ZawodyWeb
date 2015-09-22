/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import pl.umk.mat.zawodyweb.database.AliasesDAO;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.database.DAOFactory;
import pl.umk.mat.zawodyweb.database.SubmitsStateEnum;
import pl.umk.mat.zawodyweb.database.hibernate.HibernateUtil;
import pl.umk.mat.zawodyweb.database.pojo.Aliases;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.pojo.Tests;

/**
 *
 * @author slawek
 * @author faramir
 */
public class ELFunctions {

    /**
     * Filter input URI against unwanted characters
     *
     * @param in input URI
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
        return in.replaceAll("(\\r\\n?+|\\n){2}", "<br/>");
    }

    public static String esc(String in) {
        return HtmlEscape.escape(in);
    }

    public static String nlDoubler(String in) {
        return in.replace("\n", "\n\n");
    }

    public static String submitResults(Submits s) {
        if (s.getState() == null || !s.getState().equals(SubmitsStateEnum.DONE.getCode()) || s.getResultss() == null || s.getResultss().isEmpty()) {
            return "";
        }

        int worseResult = Integer.MAX_VALUE;
        String result = "";

        int nowResult;
        for (Results r : s.getResultss()) {
            if (testVisible(r.getTests())) {
                nowResult = 0;
                if (worseResult > ++nowResult && r.getStatus() == ResultsStatusEnum.UNDEF.getCode()) {
                    worseResult = nowResult;
                    result = " (!)";
                } else if (worseResult > ++nowResult && r.getStatus() == ResultsStatusEnum.UNKNOWN.getCode()) {
                    worseResult = nowResult;
                    result = " (?)";
                } else if (worseResult > ++nowResult && r.getStatus() == ResultsStatusEnum.RV.getCode()) {
                    worseResult = nowResult;
                    result = " (RV)";
                } else if (worseResult > ++nowResult && r.getStatus() == ResultsStatusEnum.CTLE.getCode()) {
                    worseResult = nowResult;
                    result = " (CTLE)";
                } else if (worseResult > ++nowResult && r.getStatus() == ResultsStatusEnum.CE.getCode()) {
                    worseResult = nowResult;
                    result = " (CE)";
                } else if (worseResult > ++nowResult && r.getStatus() == ResultsStatusEnum.MLE.getCode()) {
                    worseResult = nowResult;
                    result = " (MLE)";
                } else if (worseResult > ++nowResult && r.getStatus() == ResultsStatusEnum.RE.getCode()) {
                    worseResult = nowResult;
                    result = " (RTE)";
                } else if (worseResult > ++nowResult && r.getStatus() == ResultsStatusEnum.TLE.getCode()) {
                    worseResult = nowResult;
                    result = " (TLE)";
                } else if (worseResult > ++nowResult && r.getStatus() == ResultsStatusEnum.WA.getCode()) {
                    worseResult = nowResult;
                    result = " (WA)";
                } else if (worseResult > ++nowResult && r.getStatus() == ResultsStatusEnum.ACC.getCode()) {
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

        if (s.getState() == null || !s.getState().equals(SubmitsStateEnum.DONE.getCode()) || s.getResultss() == null || s.getResultss().isEmpty()) {
        } else {
            SortedSet<Results> results = s.getResultss();

            for (Results r : results) {
                if (testVisible(r.getTests())) {
                    points += r.getPoints();
                    maxPoints += r.getTests().getMaxpoints();
                    if (r.getStatus() == ResultsStatusEnum.ACC.getCode()) {
                        ++good;
                    }
                    ++size;
                }
            }
        }

        return coloring(points * size + good, maxPoints * size + size, ResultsStatusEnum.ACC.getCode());
    }

    public static String coloring(Integer in1, Integer in2, Integer submitResult) {
        if (in2 == 0) {
            in1 = 1;
            in2 = 1;
            if (submitResult != ResultsStatusEnum.ACC.getCode()) {
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
        if (s.getState() == null || !s.getState().equals(SubmitsStateEnum.DONE.getCode()) || s.getResultss() == null || s.getResultss().isEmpty()) {
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
        if (s.getState() == null || !s.getState().equals(SubmitsStateEnum.DONE.getCode()) || s.getResultss() == null || s.getResultss().isEmpty()) {
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

    public static Date getDate() {
        return new Date();
    }

    public static Boolean submitDateOk(Problems p) {
        Date now = new Date();
        return p.getSeries().getStartdate().before(now) && (p.getSeries().getEnddate() == null || p.getSeries().getEnddate().after(now));
    }

    /* http://stackoverflow.com/questions/4209760/validate-an-ip-address-with-mask */
    private static int getInetInteger(Inet4Address inet) {
        byte[] addr = inet.getAddress();
        return ((addr[0] & 0xFF) << 24)
                | ((addr[1] & 0xFF) << 16)
                | ((addr[2] & 0xFF) << 8)
                | ((addr[3] & 0xFF));
    }

    private static Boolean checkValidIP(String ipMask, String clientIp) {
        if (ipMask.matches("^[^0-9].*$")) {
            AliasesDAO aliasesDAO = DAOFactory.DEFAULT.buildAliasesDAO();
            List<Aliases> aliases = aliasesDAO.findByName(ipMask);
            if (aliases.size() > 0) {
                for (Aliases alias : aliases) {
                    if (isValidIP(alias.getIps(), clientIp)) {
                        return true;
                    }
                }
                return false;
            }
        } else if (ipMask.contains("/")) {
            try {
                int index = ipMask.indexOf("/");
                int bits = Integer.parseInt(ipMask.substring(index + 1));
                if (bits > 0) {

                    InetAddress cinet = InetAddress.getByName(clientIp);
                    InetAddress sinet = InetAddress.getByName(ipMask.substring(0, index));
                    if ((cinet instanceof Inet4Address)
                            && (sinet instanceof Inet4Address)) {
                        int sint = getInetInteger((Inet4Address) sinet);
                        int cint = getInetInteger((Inet4Address) cinet);
                        int mask = ~((1 << (32 - bits)) - 1);

                        return (sint & mask) == (cint & mask);
                    }
                }
            } catch (UnknownHostException | NumberFormatException | IndexOutOfBoundsException ex) {
            }
        } else {
            return clientIp.startsWith(ipMask);
        }
        return null;
    }

    public static boolean isValidIP(String openips, String clientIp) {
        String[] ips = getOpenIps(openips);
        if (ips == null || ips.length == 0) {
            return true;
        }
        Boolean valid;
        boolean r = true;
        for (String ip : ips) {
            valid = checkValidIP(ip, clientIp);
            if (valid != null && valid == true) {
                return true;
            } else if (valid != null && valid == false) {
                r = false;
            }
        }
        return r;
    }

    private static String[] getOpenIps(String ips) {
        if (ips == null) {
            return null;
        }

        List<String> openIPs = new ArrayList<>();
        for (String e : ips.split("[;,\\s]")) {
            e = e.trim();
            if (e.isEmpty() == false) {
                openIPs.add(e);
            }
        }

        return openIPs.toArray(new String[0]);
    }

    public static Boolean submitIpOk(Problems p, String clientIp) {
        return isValidIP(p.getSeries().getOpenips(), clientIp);
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
     * If the first two bytes are hex FE FF, the file is tentatively UTF-16 BE. If the first two
     * bytes are hex FF FE, and the following two bytes are not hex 00 00 , the file is tentatively
     * UTF-16 LE. If the first four bytes are hex 00 00 FE FF, the file is tentatively UTF-32 BE. If
     * the first four bytes are hex FF FE 00 00, the file is tentatively UTF-32 LE.
     *
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

    public static String truncate(String s, Integer limit, String continuationMark) {
        if (s.length() > limit) {
            return s.substring(0, limit) + continuationMark;
        }
        return s;
    }

    public static String getInputPartOfTest(Tests test, Integer limit) {
        return getPartOfTest(test, "input", limit);
    }

    public static String getOutputPartOfTest(Tests test, Integer limit) {
        return getPartOfTest(test, "output", limit);
    }

    private static String getPartOfTest(Tests test, String part, Integer limit) {
        if (test == null || ("input".equals(part) == false && "output".equals(part) == false)) {
            return "";
        }

        String substr = null;

        SessionFactory sf = HibernateUtil.getSessionFactory();
        if (sf instanceof SessionFactoryImplementor) {
            SessionFactoryImplementor sfi = (SessionFactoryImplementor) HibernateUtil.getSessionFactory();
            Dialect dialect = sfi.getDialect();

            if (dialect != null) {
                SQLFunction function = dialect.getFunctions().get("substr");
                if (function != null) {
                    substr = function.render(StringType.INSTANCE, Arrays.asList("input", 0, limit), sfi);
                }

            }
        }

        if (substr == null) {
            substr = String.format("substr('%s',%d,%d)", "input", 1, limit);
        }

        return (String) sf.getCurrentSession()
                .createCriteria(Tests.class)
                .setProjection(
                        Projections.sqlProjection(
                                substr + " as short",
                                new String[]{"short"},
                                new Type[]{StringType.INSTANCE}))
                .add(Restrictions.idEq(test.getId()))
                .uniqueResult();
    }
}
