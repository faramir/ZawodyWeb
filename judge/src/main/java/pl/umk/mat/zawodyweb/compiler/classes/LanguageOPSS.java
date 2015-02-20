/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import pl.umk.mat.zawodyweb.commons.TestInput;
import pl.umk.mat.zawodyweb.commons.TestOutput;
import pl.umk.mat.zawodyweb.commons.CompilerInterface;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;

/**
 *
 * @author Jakub Prabucki (modified by faramir)
 */
public class LanguageOPSS implements CompilerInterface {

    private Properties properties;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public TestOutput runTest(String path, TestInput input) {
        TestOutput result = new TestOutput(null);

        String loginUrl = "http://opss.assecobs.pl/?&login";
        String login = properties.getProperty("opss.login");
        String password = properties.getProperty("opss.password");

        HttpClient client = new HttpClient();

        HttpClientParams params = client.getParams();
        params.setParameter("http.useragent", "Opera/9.64 (Windows NT 6.0; U; pl) Presto/2.1.1");
        client.setParams(params);

        PostMethod logging = new PostMethod(loginUrl);
        NameValuePair[] dataLogging = {
            new NameValuePair("login_form_submit", "1"),
            new NameValuePair("login_form_login", login),
            new NameValuePair("login_form_pass", password)
        };
        logging.setRequestBody(dataLogging);
        try {
            client.executeMethod(logging);
        } catch (HttpException e) {
            result.setStatus(ResultsStatusEnum.UNDEF.getCode());
            result.setNotes(e.getMessage());
            result.setOutputText("HttpException");
            logging.releaseConnection();
            return result;
        } catch (IOException e) {
            result.setStatus(ResultsStatusEnum.UNDEF.getCode());
            result.setNotes(e.getMessage());
            result.setOutputText("IOException");
            logging.releaseConnection();
            return result;
        }
        logging.releaseConnection();
        PostMethod sendAnswer = new PostMethod("http://opss.assecobs.pl/?menu=comp&sub=send&comp=0");
        NameValuePair[] dataSendAnswer = {
            new NameValuePair("form_send_submit", "1"),
            new NameValuePair("form_send_comp", ""),
            new NameValuePair("form_send_problem", input.getInputText()),
            new NameValuePair("form_send_lang", properties.getProperty("CODEFILE_EXTENSION")),
            new NameValuePair("form_send_sourcetext", path),
            new NameValuePair("form_send_submittxt", "Wyślij kod")
        };
        sendAnswer.setRequestBody(dataSendAnswer);
        InputStream status = null;
        try {
            client.executeMethod(sendAnswer);
            status = sendAnswer.getResponseBodyAsStream();
        } catch (HttpException e) {
            result.setStatus(ResultsStatusEnum.UNDEF.getCode());
            result.setNotes(e.getMessage());
            result.setOutputText("HttpException");
            sendAnswer.releaseConnection();
            return result;
        } catch (IOException e) {
            result.setStatus(ResultsStatusEnum.UNDEF.getCode());
            result.setNotes(e.getMessage());
            result.setOutputText("IOException");
            sendAnswer.releaseConnection();
            return result;
        }
        String submitId = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(status, "iso-8859-2"));
        } catch (UnsupportedEncodingException e) {
        }
        String line;
        try {
            Pattern p = Pattern.compile("<a[^>]*href=[^>]*sub=stat[^>]*id=\\d*[^>]*>");
            line = br.readLine();
            Matcher m = p.matcher(line);
            while (!m.find() && line != null) {
                line = br.readLine();
                m = p.matcher(line);
            }
            Pattern p1 = Pattern.compile("id=\\d*");
            Matcher m1 = p1.matcher(m.group());
            m1.find();
            submitId = m1.group().split("=")[1];
        } catch (IOException e) {
            result.setStatus(ResultsStatusEnum.UNDEF.getCode());
            result.setNotes(e.getMessage());
            result.setOutputText("IOException");
            sendAnswer.releaseConnection();
            return result;
        }
        sendAnswer.releaseConnection();
        do {
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                result.setStatus(ResultsStatusEnum.UNDEF.getCode());
                result.setNotes(e.getMessage());
                result.setOutputText("InterruptedException");
                return result;
            }
            GetMethod answer = new GetMethod("http://opss.assecobs.pl/?menu=comp&sub=stat&comp=0&id=" + submitId);
            InputStream answerStream = null;
            try {
                client.executeMethod(answer);
                answerStream = answer.getResponseBodyAsStream();
            } catch (HttpException e) {
                result.setStatus(ResultsStatusEnum.UNDEF.getCode());
                result.setNotes(e.getMessage());
                result.setOutputText("HttpException");
                answer.releaseConnection();
                return result;
            } catch (IOException e) {
                result.setStatus(ResultsStatusEnum.UNDEF.getCode());
                result.setNotes(e.getMessage());
                result.setOutputText("IOException");
                answer.releaseConnection();
                return result;
            }
            try {
                br = new BufferedReader(new InputStreamReader(answerStream, "iso-8859-2"));
            } catch (UnsupportedEncodingException e) {
            }
            String row = "";
            try {
                line = br.readLine();
                Pattern p2 = Pattern.compile("(<tr class=row0>)|(<tr class=stat_ac>)|(<tr class=stat_run>)");
                Matcher m2 = p2.matcher(line);
                while (!m2.find() && line != null) {
                    line = br.readLine();
                    m2 = p2.matcher(line);
                }
                row = "";
                for (int i = 0; i < 19; i++) {
                    row += line;
                    line = br.readLine();
                }
            } catch (IOException e) {
                result.setStatus(ResultsStatusEnum.UNDEF.getCode());
                result.setNotes(e.getMessage());
                result.setOutputText("IOException");
                sendAnswer.releaseConnection();
                return result;
            }
            String[] col = row.split("(<td>)|(</table>)");
            result.setPoints(0);
            if (col[6].matches(".*Program zaakceptowany.*")) {
                result.setStatus(ResultsStatusEnum.ACC.getCode());
                result.setPoints(input.getMaxPoints());
                result.setRuntime(10 * Integer.parseInt(col[8].replaceAll("\\.", "")));
                result.setMemUsed(Integer.parseInt(col[9].replaceAll("\\s", "")));
                break;
            } else if (col[6].matches(".*Błąd kompilacji.*")) {
                result.setStatus(ResultsStatusEnum.CE.getCode());
                break;
            } else if (col[6].matches(".*Błąd wykonania.*")) {
                result.setStatus(ResultsStatusEnum.RE.getCode());
                break;
            } else if (col[6].matches(".*Limit czasu przekroczony.*")) {
                result.setStatus(ResultsStatusEnum.TLE.getCode());
                break;
            } else if (col[6].matches(".*Limit pamięci przekroczony.*")) {
                result.setStatus(ResultsStatusEnum.MLE.getCode());
                break;
            } else if (col[6].matches(".*Błędna odpowiedź.*")) {
                result.setStatus(ResultsStatusEnum.WA.getCode());
                break;
            } else if (col[6].matches(".*Niedozwolona funkcja.*")) {
                result.setStatus(ResultsStatusEnum.RV.getCode());
            } else if (col[6].matches(".*Uruchamianie.*")) {
            } else if (col[6].matches(".*Kompilacja.*")) {
            } else {
                result.setStatus(ResultsStatusEnum.UNKNOWN.getCode());
                result.setNotes("Unknown status: \"" + col[6] + "\"");
                break;
            }
            answer.releaseConnection();
        } while (true);
        GetMethod logout = new GetMethod("http://opss.assecobs.pl/?logoff");
        try {
            client.executeMethod(logout);
        } catch (HttpException e) {
            logout.releaseConnection();
        } catch (IOException e) {
            logout.releaseConnection();
        }
        logout.releaseConnection();
        return result;
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
