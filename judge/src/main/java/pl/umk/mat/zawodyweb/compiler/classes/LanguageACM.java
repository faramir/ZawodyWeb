/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.Vector;
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
 *
 * @author Jakub Prabucki
 */
public class LanguageACM implements CompilerInterface {

    private Properties properties;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public TestOutput runTest(String path, TestInput input) {
        TestOutput result = new TestOutput(null);
        String acmSite = "http://uva.onlinejudge.org/";

        String login = properties.getProperty("acm_uva.login");
        String password = properties.getProperty("acm_uva.password");

        HttpClient client = new HttpClient();
        GetMethod logging = new GetMethod(acmSite);
        InputStream firstGet = null;

        HttpClientParams params = client.getParams();
        params.setParameter("http.useragent", "Opera/9.64 (Windows NT 6.0; U; pl) Presto/2.1.1");
        client.setParams(params);

        try {
            client.executeMethod(logging);
            firstGet = logging.getResponseBodyAsStream();
        } catch (HttpException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("HttpException");
            logging.releaseConnection();
            return result;
        } catch (IOException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("IOException");
            logging.releaseConnection();
            return result;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(firstGet, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        String line, name, value;
        Vector<NameValuePair> vectorLoginData = new Vector<NameValuePair>();
        vectorLoginData.addElement(new NameValuePair("username", login));
        vectorLoginData.addElement(new NameValuePair("passwd", password));
        try {
            line = br.readLine();
            while (line != null && !line.matches(".*class=\"mod_login\".*")) {
                line = br.readLine();
            }
            while (line != null && !line.matches("(?i).*submit.*login.*")) {
                if (line.matches(".*hidden.*name=\".*value=\".*")) {
                    name = line.split("name=\"")[1].split("\"")[0];
                    value = line.split("value=\"")[1].split("\"")[0];
                    vectorLoginData.addElement(new NameValuePair(name, value)); // FIXME: check if it's neccesary: URLDecoder.decode(value, "UTF-8"));
                }
                line = br.readLine();
            }
            vectorLoginData.addElement(new NameValuePair("remember", "yes"));
            vectorLoginData.addElement(new NameValuePair("Submit", "Login"));
        } catch (IOException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("IOException");
            logging.releaseConnection();
            return result;
        }
        logging.releaseConnection();
        PostMethod sendAnswer = new PostMethod("http://uva.onlinejudge.org/index.php?option=com_comprofiler&task=login");
        sendAnswer.setRequestHeader("Referer", acmSite);
        NameValuePair[] loginData = new NameValuePair[0];
        loginData = vectorLoginData.toArray(loginData);
        sendAnswer.setRequestBody(loginData);
        try {
            client.executeMethod(sendAnswer);
        } catch (HttpException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("HttpException");
            sendAnswer.releaseConnection();
            return result;
        } catch (IOException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("IOException");
            sendAnswer.releaseConnection();
            return result;
        }
        sendAnswer.releaseConnection();
        sendAnswer = new PostMethod("http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=25&page=save_submission");
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
            new NameValuePair("code", path),
            new NameValuePair("submit", "Submit")
        };
        sendAnswer.setRequestBody(dataSendAnswer);

        int id;
        try {
            client.executeMethod(sendAnswer);
            String location = sendAnswer.getResponseHeader("Location").getValue();
            try {
                id = Integer.parseInt(location.substring(location.lastIndexOf("+") + 1));
            } catch (NumberFormatException ex) {
                result.setResult(CheckerErrors.UNKNOWN);
                result.setResultDesc(URLDecoder.decode(location.substring(location.lastIndexOf("=") + 1), "UTF-8"));
                result.setText("NumberFormatException");
                sendAnswer.releaseConnection();
                return result;
            }
        } catch (HttpException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("HttpException");
            sendAnswer.releaseConnection();
            return result;
        } catch (IOException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("IOException");
            sendAnswer.releaseConnection();
            return result;
        }
        sendAnswer.releaseConnection();
        int limit = 50;
        int limitstart = 0;
        String statusSite = "";
        String stat = "", time = "";
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("InterruptedException");
            return result;
        }
        do {
            logging = new GetMethod("http://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=9&limit=50&limitstart=" + String.valueOf(limitstart));
            firstGet = null;
            try {
                client.executeMethod(logging);
                firstGet = logging.getResponseBodyAsStream();
            } catch (HttpException e) {
                result.setResult(CheckerErrors.UNDEF);
                result.setResultDesc(e.getMessage());
                result.setText("HttpException");
                logging.releaseConnection();
                return result;
            } catch (IOException e) {
                result.setResult(CheckerErrors.UNDEF);
                result.setResultDesc(e.getMessage());
                result.setText("IOException");
                logging.releaseConnection();
                return result;
            }
            try {
                br = new BufferedReader(new InputStreamReader(firstGet, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
            }
            try {
                while ((line = br.readLine()) != null) {
                    if (line.matches(".*<td>" + String.valueOf(id) + "</td>.*")) {
                        statusSite = line;
                        line = br.readLine();
                        while (!line.matches(".*</tr>.*")) {
                            statusSite += line;
                            line = br.readLine();
                        }
                        String[] split = statusSite.split("(<td[^>]*>)|(</td>)");
                        stat = split[7];
                        time = split[11];
                    }
                }
            } catch (IOException e) {
                result.setResult(CheckerErrors.UNDEF);
                result.setResultDesc(e.getMessage());
                result.setText("IOException");
                logging.releaseConnection();
                return result;
            }
            if (!stat.equals("") && !time.equals("")) {
                result.setPoints(0);
                if (!stat.equals("Received") && !stat.equals("Running") && !stat.equals("Sent to judge") && !stat.equals("In judge queue")) {
                    if (stat.matches(".*Accepted.*")) {
                        result.setResult(CheckerErrors.ACC);
                        result.setPoints(input.getMaxPoints());
                        result.setRuntime(Integer.parseInt(time.replaceAll("\\.", "")));
                    } else if (stat.matches(".*Compilation error.*")) {
                        result.setResult(CheckerErrors.CE);
                        result.setRuntime(Integer.parseInt(time.replaceAll("\\.", "")));
                    } else if (stat.matches(".*Presentation error.*")) {
                        result.setResult(CheckerErrors.ACC);
                        result.setPoints(input.getMaxPoints());
                        result.setRuntime(Integer.parseInt(time.replaceAll("\\.", "")));
                    } else if (stat.matches(".*Wrong answer.*")) {
                        result.setResult(CheckerErrors.WA);
                        result.setRuntime(Integer.parseInt(time.replaceAll("\\.", "")));
                    } else if (stat.matches(".*Time limit exceeded.*")) {
                        result.setResult(CheckerErrors.TLE);
                        result.setRuntime(Integer.parseInt(time.replaceAll("\\.", "")));
                    } else if (stat.matches(".*Memory limit exceeded.*")) {
                        result.setResult(CheckerErrors.MLE);
                        result.setRuntime(Integer.parseInt(time.replaceAll("\\.", "")));
                    } else if (stat.matches(".*Runtime error.*")) {
                        result.setResult(CheckerErrors.RE);
                        result.setRuntime(Integer.parseInt(time.replaceAll("\\.", "")));
                    } else {
                        result.setResult(CheckerErrors.UNKNOWN);
                        result.setResultDesc("Unknown status: \"" + stat + "\"");
                    }
                    break;
                } else {
                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        result.setResult(CheckerErrors.UNDEF);
                        result.setResultDesc(e.getMessage());
                        result.setText("InterruptedException");
                        return result;
                    }
                }
            } else {
                limitstart += limit;
            }
        } while (true);
        logging.releaseConnection();
        sendAnswer = new PostMethod("http://uva.onlinejudge.org/index.php?option=logout");
        NameValuePair[] logout = {
            new NameValuePair("op2", "logout"),
            new NameValuePair("return", "http://uva.onlinejudge.org"),
            new NameValuePair("lang", "english"),
            new NameValuePair("message", "0"),
            new NameValuePair("Submit", "Logout")
        };
        sendAnswer.setRequestBody(logout);
        try {
            client.executeMethod(sendAnswer);
        } catch (HttpException e) {
            sendAnswer.releaseConnection();
        } catch (IOException e) {
            sendAnswer.releaseConnection();
        }
        sendAnswer.releaseConnection();
        return result;
    }

    @Override
    public byte[] precompile(
            byte[] code) {
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
