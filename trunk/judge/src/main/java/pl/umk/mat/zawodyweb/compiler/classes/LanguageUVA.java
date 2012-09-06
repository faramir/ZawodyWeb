package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;
import pl.umk.mat.zawodyweb.database.CheckerErrors;

/**
 * Copied from LanguageLA, acmSite = http://uva.onlinejudge.org/
 *
 * @author faramir
 */
public class LanguageUVA implements CompilerInterface {

    public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LanguageUVA.class);
    private Properties properties;
    private HttpClient client;
    private String acmSite = "http://uva.onlinejudge.org/";

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    //http://livearchive.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=19 - last 50 submissions
    private boolean checkSubmissionsStatus() throws HttpException, IOException {
        List<Map<String, String>> results = getResults(50);

        int inQueue = 0;
        for (Map<String, String> result : results) {
            String status = result.get("status");
            if ("".equals(status)
                    || "Sent to judge".equals(status)
                    || "In judge queue".equals(status)) {
                ++inQueue;
            }
        }

        return inQueue <= 32;
    }

    private void logIn(String login, String password) throws HttpException, IOException {
        ArrayList<NameValuePair> vectorLoginData;
        GetMethod get = new GetMethod(acmSite);

        try {
            client.executeMethod(get);
            InputStream firstGet = get.getResponseBodyAsStream();
            BufferedReader br = null;

            try {
                br = new BufferedReader(new InputStreamReader(firstGet, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }

            String line, name, value;
            vectorLoginData = new ArrayList<NameValuePair>();
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

    private int sendSolution(String code, TestInput input) throws NumberFormatException, HttpException, IOException {
        PostMethod post = new PostMethod(acmSite + "index.php?option=com_onlinejudge&Itemid=25&page=save_submission");
        try {

            String lang = properties.getProperty("CODEFILE_EXTENSION");
            if (lang.equals("c")) {
                lang = "1";
            } else if (lang.equals("java")) {
                lang = "2";
            } else if (lang.equals("cpp")) {
                lang = "3";
            } else if (lang.equals("pas")) {
                lang = "4";
            }
            NameValuePair[] dataSendAnswer = {
                new NameValuePair("problemid", ""),
                new NameValuePair("category", ""),
                new NameValuePair("localid", input.getText()),
                new NameValuePair("language", lang),
                new NameValuePair("code", code),
                new NameValuePair("submit", "Submit")
            };
            post.setRequestBody(dataSendAnswer);

            client.executeMethod(post);
            String location = post.getResponseHeader("Location").getValue();
            return Integer.parseInt(location.substring(location.lastIndexOf("+") + 1));
        } finally {
            post.releaseConnection();
        }
    }

    private String getCompilationError(int id) throws HttpException, IOException {
        GetMethod get = new GetMethod(acmSite + "index.php?option=com_onlinejudge&Itemid=9&page=show_compilationerror&submission=" + id);

        try {
            client.executeMethod(get);
            InputStream firstGet = get.getResponseBodyAsStream();
            BufferedReader br = null;

            try {
                br = new BufferedReader(new InputStreamReader(firstGet, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }

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
        List<Map<String, String>> results = new ArrayList<Map<String, String>>();

        while ((line = br.readLine()) != null) {
            if (line.matches(".*<td>[0-9]+</td>.*")) {
                sb = new StringBuilder(line);
                line = br.readLine();
                while (!line.matches(".*</tr>.*")) {
                    sb.append(line.trim());
                    line = br.readLine();
                }
                String[] split = sb.toString().split("(<td[^>]*>)|(</td>)");

                Map<String, String> result = new HashMap<String, String>();
                result.put("id", split[1].trim());
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
                logger.info(String.format("%.1f minutes without answer. Destroy!", maxTime / 60));
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
                    result.setResult(CheckerErrors.ACC);
                    result.setPoints(input.getMaxPoints());
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Compilation error".equals(status)) {
                    result.setResult(CheckerErrors.CE);
                    result.setResultDesc(getCompilationError(id));
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Presentation error".equals(status)) {
                    result.setResult(CheckerErrors.ACC);
                    result.setPoints(input.getMaxPoints());
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Wrong answer".equals(status)) {
                    result.setResult(CheckerErrors.WA);
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Time limit exceeded".equals(status)) {
                    result.setResult(CheckerErrors.TLE);
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Memory limit exceeded".equals(status)) {
                    result.setResult(CheckerErrors.MLE);
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else if ("Runtime error".equals(status)) {
                    result.setResult(CheckerErrors.RE);
                    result.setRuntime(Integer.parseInt(time));
                    break;
                } else {
                    result.setResult(CheckerErrors.UNKNOWN);
                    result.setResultDesc("Unknown status: \"" + status + "\"");
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

        String login = properties.getProperty("livearchive.login");
        String password = properties.getProperty("livearchive.password");
        long maxTime;
        try {
            maxTime = Long.parseLong(properties.getProperty("livearchive.max_time"));
        } catch (NumberFormatException e) {
            maxTime = 10L * 60;
        }

        prepareHttpClient();

        try {
            logIn(login, password);
            logger.info("Logged to UVa-ACM");

            if (checkSubmissionsStatus() == false) {
                result.setResult(CheckerErrors.UNDEF);
                result.setResultDesc("More than 32 submissions in queue.");
                result.setText("UVa-ACM judge broken?");
                return result;
            }

            int id = sendSolution(path, input);
            logger.info("UVa-ACM Submit id = " + id);

            checkResults(id, maxTime, input, result);

            logOut();
            logger.info("Logged out from UVa-ACM");
        } catch (Exception e) {
            logger.info("Exception: ", e);
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText(e.getClass().getName());
            return result;
        }

        return result;
    }

    private void prepareHttpClient() {
        client = new HttpClient();
        HttpClientParams params = client.getParams();
        params.setParameter("http.useragent", "Opera/9.80 (X11; Linux x86_64; U; pl) Presto/2.10.289 Version/12.00");
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
}
