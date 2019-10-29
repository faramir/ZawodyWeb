/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;
import pl.umk.mat.zawodyweb.judge.commons.CompilerInterface;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;

/**
 * Copied from LanguageLA, acmSite = http://uva.onlinejudge.org/
 *
 * @author faramir
 */
public class LanguageUVA implements CompilerInterface {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LanguageUVA.class);
    private Properties properties;
    private HttpClient client;
    private final String acmSite = "https://onlinejudge.org/";

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    //http://livearchive.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=19 - last 50 submissions
    private int checkInQueueStatus(String problemId) throws HttpException, IOException {
        List<Map<String, String>> results = getResults(50);

        int inQueue = 0;
        for (Map<String, String> result : results) {
            if (problemId.equals(result.get("problemsid"))) {
                String status = result.get("status");
                if ("".equals(status)
                        || "Sent to judge".equals(status)
                        || "In judge queue".equals(status)) {
                    ++inQueue;
                }
            }
        }

        return inQueue;
    }

    private void logIn(String login, String password) throws HttpException, IOException {
        List<NameValuePair> vectorLoginData;
        GetMethod get = new GetMethod(acmSite);

        try {
            client.executeMethod(get);
            InputStream firstGet = get.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(firstGet, StandardCharsets.UTF_8));

            String line, name, value;
            vectorLoginData = new ArrayList<>();
            vectorLoginData.add(new NameValuePair("username", login));
            vectorLoginData.add(new NameValuePair("passwd", password));

            line = br.readLine();
            while (line != null && !line.matches(".*class=\"mod_login\".*")) {
                line = br.readLine();
            }
            while (line != null && !line.matches("(?i).*submit.*login.*")) {
                if (line.matches(".*hidden.*name=\".*value=\".*")) {
                    name = line.split("name=\"")[1].split("\"")[0];
                    value = line.split("value=\"")[1].split("\"")[0];
                    vectorLoginData.add(new NameValuePair(name, value)); // FIXME: check if it's neccesary: URLDecoder.decode(value, "UTF-8"));
                }
                line = br.readLine();
            }
        } finally {
            get.releaseConnection();
        }
        vectorLoginData.add(new NameValuePair("remember", "yes"));
        vectorLoginData.add(new NameValuePair("Submit", "Login"));

        PostMethod post = new PostMethod(acmSite + "index.php?option=com_comprofiler&task=login");
        post.setRequestHeader("Referer", acmSite);
        NameValuePair[] loginData = new NameValuePair[0];
        loginData = vectorLoginData.toArray(loginData);
        post.setRequestBody(loginData);

        client.executeMethod(post);

        post.releaseConnection();
    }

    private String sendSolution(String code, TestInput input) throws HttpException, IOException {
        PostMethod post = new PostMethod(acmSite + "index.php?option=com_onlinejudge&Itemid=25&page=save_submission");
        try {

            String langId = properties.getProperty("uva.languageId");
            if (langId != null) {
                if (langId.equals("1") || langId.equalsIgnoreCase("C")) {
                    langId = "1";
                } else if (langId.equals("2") || langId.equalsIgnoreCase("JAVA")) {
                    langId = "2";
                } else if (langId.equals("3") || langId.equalsIgnoreCase("C++")) {
                    langId = "3";
                } else if (langId.equals("4") || langId.equalsIgnoreCase("PASCAL")) {
                    langId = "4";
                } else if (langId.equals("5") || langId.equalsIgnoreCase("C++11")) {
                    langId = "5";
                } else {
                    langId = null;
                }
            }

            if (langId == null) {
                String fileExt = properties.getProperty("CODEFILE_EXTENSION");
                if (fileExt.equals("c")) {
                    langId = "1";
                } else if (fileExt.equals("java")) {
                    langId = "2";
                } else if (fileExt.equals("cpp")) {
                    langId = "3";
                } else if (fileExt.equals("pas")) {
                    langId = "4";
                } else if (fileExt.equals("cpp11")) {
                    langId = "5";
                }
            }
            NameValuePair[] dataSendAnswer = {
                new NameValuePair("problemid", ""),
                new NameValuePair("category", ""),
                new NameValuePair("localid", input.getInputText()),
                new NameValuePair("language", langId),
                new NameValuePair("code", code),
                new NameValuePair("submit", "Submit")
            };
            post.setRequestBody(dataSendAnswer);

            client.executeMethod(post);
            String location = post.getResponseHeader("Location").getValue();

            String msg = location.substring(location.lastIndexOf("msg=") + 4);
            if (msg.contains("Submission+received")) {
                return msg.substring(msg.lastIndexOf("+") + 1);
            } else {
                return java.net.URLDecoder.decode(msg, "UTF-8");
            }
        } finally {
            post.releaseConnection();
        }
    }

    private String getCompilationError(int id) throws HttpException, IOException {
        GetMethod get = new GetMethod(acmSite + "index.php?option=com_onlinejudge&Itemid=9&page=show_compilationerror&submission=" + id);

        try {
            client.executeMethod(get);
            InputStream firstGet = get.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(firstGet, StandardCharsets.UTF_8));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            String[] split = sb.toString().split("(<pre>)|(</pre>)");
            if (split.length == 3) {
                return split[1].trim();
            }
        } finally {
            get.releaseConnection();
        }
        return "";
    }

    /**
     *
     * @param br BufferedReader
     * @return List of Map (id, status, time)
     * @throws IOException
     */
    private List<Map<String, String>> processResults(BufferedReader br) throws IOException {
        String line;
        StringBuilder sb;
        List<Map<String, String>> results = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            if (line.matches(".*<td>[0-9]+</td>.*")) {
                sb = new StringBuilder(line);
                line = br.readLine();
                while (!line.matches(".*</tr>.*")) {
                    sb.append(line.trim());
                    line = br.readLine();
                }
                String[] split = sb.toString().split("(<td[^>]*>)|(</td>)");

                Map<String, String> result = new HashMap<>();
                result.put("id", split[1].trim());
                result.put("problemid", (split[3].replaceAll("<[^>]*>", "")).trim());
                result.put("status", (split[7].replaceAll("<[^>]*>", "")).trim());
                result.put("time", split[11].replace(".", "").trim());

                results.add(result);
            }
        }
        return results;
    }

    private List<Map<String, String>> getResults(int limitOnPage) throws HttpException, IOException {
        BufferedReader br = null;
        GetMethod get = new GetMethod(acmSite + "index.php?option=com_onlinejudge&Itemid=9&limit=" + limitOnPage + "&limitstart=0");
        try {
            client.executeMethod(get);
            InputStream firstGet = get.getResponseBodyAsStream();

            try {
                br = new BufferedReader(new InputStreamReader(firstGet, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
            }

            return processResults(br);
        } finally {
            get.releaseConnection();
        }
    }

    private void checkResults(int id, long maxTime, TestInput input, TestOutput result) throws InterruptedException,
            TimeoutException, HttpException, IOException {

        int limitRise = 50;
        int limitOnPage = 50;

        Random random = new Random();

        Thread.sleep(7000 + (Math.abs(random.nextInt()) % 3000));

        long start_time = System.currentTimeMillis();
        do {
            if (System.currentTimeMillis() - start_time > maxTime * 1000L) {
                logger.info(String.format("%.1f minutes without answer. Destroy!", maxTime / 60.));
                throw new TimeoutException("Too slow to answer.. destroy");
            }

            logger.info("Checking answer on UVa-ACM");
            List<Map<String, String>> results = getResults(limitOnPage);

            String sid = String.valueOf(id);
            Map<String, String> map = null;
            for (Map<String, String> m : results) {
                if (sid.equals(m.get("id"))) {
                    map = m;
                    break;
                }
            }

            if (map != null) {
                String status = map.get("status");
                String time = map.get("time");
                result.setPoints(0);
                if ("".equals(status)
                        || "Received".equals(status)
                        || "Running".equals(status)
                        || "Sent to judge".equals(status)
                        || "In judge queue".equals(status)
                        || "Compiling".equals(status)
                        || "Linking".equals(status)) {
                    Thread.sleep(7000);
                } else if ("Accepted".equals(status)) {
                    result.setStatus(ResultsStatusEnum.ACC.getCode());
                    result.setPoints(input.getMaxPoints());
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Compilation error".equals(status)) {
                    result.setStatus(ResultsStatusEnum.CE.getCode());
                    result.setNotes(getCompilationError(id));
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Presentation error".equals(status)) {
                    result.setStatus(ResultsStatusEnum.ACC.getCode());
                    result.setPoints(input.getMaxPoints());
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Wrong answer".equals(status)) {
                    result.setStatus(ResultsStatusEnum.WA.getCode());
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Time limit exceeded".equals(status)) {
                    result.setStatus(ResultsStatusEnum.TLE.getCode());
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Memory limit exceeded".equals(status)) {
                    result.setStatus(ResultsStatusEnum.MLE.getCode());
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Runtime error".equals(status)) {
                    result.setStatus(ResultsStatusEnum.RE.getCode());
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else {
                    result.setStatus(ResultsStatusEnum.UNKNOWN.getCode());
                    result.setNotes("Unknown status: \"" + status + "\"");
                    logger.info("Unknown status: \"" + status + "\"");
                    break;
                }
            } else {
                limitOnPage += limitRise;
            }
            Thread.sleep(3000 + (Math.abs(random.nextInt()) % 3000));
        } while (true);
    }

    private void logOut() throws HttpException, IOException {
        PostMethod post = new PostMethod(acmSite + "index.php?option=logout");
        try {
            NameValuePair[] logout = {
                new NameValuePair("op2", "logout"),
                new NameValuePair("return", acmSite),
                new NameValuePair("lang", "english"),
                new NameValuePair("message", "0"),
                new NameValuePair("Submit", "Logout")
            };
            post.setRequestBody(logout);

            client.executeMethod(post);
        } finally {
            post.releaseConnection();
        }
    }

    @Override
    public TestOutput runTest(String path, TestInput input) {
        TestOutput result = new TestOutput(null);

        Random random = new Random();

        String login = properties.getProperty("uva.login");
        String password = properties.getProperty("uva.password");

        int inQueue;
        try {
            inQueue = Integer.parseInt(properties.getProperty("uva.inqueue"));
        } catch (NumberFormatException e) {
            inQueue = 4;
        }

        long maxTime;
        try {
            maxTime = Long.parseLong(properties.getProperty("uva.max_time"));
        } catch (NumberFormatException e) {
            maxTime = 10L * 60;
        }

        prepareHttpClient();

        try {
            logIn(login, password);
            logger.info("Logged to UVa-ACM");

            for (int i = 3; i >= 0; --i) {
                if (checkInQueueStatus(input.getInputText()) > inQueue) {
                    if (i == 0) {
                        result.setStatus(ResultsStatusEnum.UNDEF.getCode());
                        result.setNotes("More than " + inQueue + " submissions of " + input.getInputText() + " in queue.");
                        result.setOutputText("UVa-ACM judge broken?");
                        logger.info(result.getNotes());
                        return result;
                    } else {
                        Thread.sleep(10000 + (Math.abs(random.nextInt()) % 5000));
                        continue;
                    }
                }
                break;
            }

            String msg = sendSolution(path, input);
            int id = 0;
            try {
                id = Integer.parseInt(msg);
            } catch (NumberFormatException ex) {
                logger.info("Unable to get UVa-ACM Submit id. Message: " + msg);
            }

            if (id == 0) {
                result.setStatus(ResultsStatusEnum.RV.getCode());
                result.setNotes(msg);
                return result;
            } else {
                logger.info("UVa-ACM Submit id = " + id);

                checkResults(id, maxTime, input, result);
            }
            logOut();
            logger.info("Logged out from UVa-ACM");
        } catch (Exception e) {
            logger.info("Exception: ", e);
            result.setStatus(ResultsStatusEnum.UNDEF.getCode());
            result.setNotes(e.getMessage());
            result.setOutputText(e.getClass().getName());
            return result;
        }

        return result;
    }

    private void prepareHttpClient() {
        client = new HttpClient();
        HttpClientParams params = client.getParams();
        params.setParameter(HttpClientParams.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        params.setParameter("http.useragent", "Opera/9.80 (X11; Linux x86_64; U) Presto/2.12.388 Version/12.11");
        client.setParams(params);
    }

    @Override
    public byte[] precompile(byte[] code) {
        return code;
    }

    @Override
    public String compile(byte[] code) {
        return new String(code);
    }

    @Override
    public String postcompile(String path) {
        return path;
    }

    @Override
    public void closeProgram(String path) {
    }

    public static void main(String[] args) {
        CompilerInterface language = new LanguageUVA();
        Properties properties = new Properties();

        properties.setProperty("uva.languageId", "3");
        properties.setProperty("CODEFILE_EXTENSION","cpp");
        properties.setProperty("uva.login", "spamz");
        properties.setProperty("uva.password", "spamz2");
        properties.setProperty("uva.inqueue", "100");
        properties.setProperty("uva.max_time", "60");

        
        
        language.setProperties(properties);
        
        TestOutput output = language.runTest("#include<iostream>\n" +
                "\n" +
                "#define max(a,b) ((a)<(b) ? (b):(a))\n" +
                "\n" +
                "using namespace std;\n" +
                "\n" +
                "int main(){\n" +
                "    int n, m, caseN = 0;\n" +
                "    int n_tower[100+10], m_tower[100+10], c[100+10][100+10];\n" +
                "    cin >> n >> m;\n" +
                "    while (m != 0 && n != 0){\n" +
                "        for(int i = 0; i < 110; ++i){\n" +
                "            for(int j = 0; j < 110; ++j){\n" +
                "                c[i][j] = 0;\n" +
                "            }\n" +
                "        }\n" +
                "        for(int i = 1; i <= n; ++i)\n" +
                "            cin >> n_tower[i];\n" +
                "        for(int i = 1; i <= m; ++i)\n" +
                "            cin >> m_tower[i];\n" +
                "        for(int i = 1; i <= n; ++i){\n" +
                "            for(int j = 1; j <= m; ++j){\n" +
                "                if(n_tower[i] == m_tower[j])\n" +
                "                    c[i][j] = c[i-1][j-1] + 1;\n" +
                "                else\n" +
                "                    c[i][j] = max(c[i][j-1], c[i-1][j]);\n" +
                "            }\n" +
                "        }\n" +
                "        cout << \"Twin Towers #\" << ++caseN << endl;\n" +
                "        cout << \"Number of Tiles : \" << c[n][m] << endl << endl;\n" +
                "        cin >> n >> m;\n" +
                "    }\n" +
                "\n" +
                "    return 0;\n" +
                "}", new TestInput("10066", 1, 0, 0, null));
        
        System.out.println("Notes: "+output.getNotes());
        System.out.println("Output text: "+output.getOutputText());
        System.out.println("Points: "+output.getPoints());
    }
}
