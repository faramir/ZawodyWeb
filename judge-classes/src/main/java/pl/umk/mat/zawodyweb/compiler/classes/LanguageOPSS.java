/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.judge.commons.CompilerInterface;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;

/**
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

        RequestConfig requestConfig = RequestConfig.custom()
                                              .setCookieSpec(CookieSpecs.DEFAULT)
                                              .build();

        HttpClient client = HttpClients.custom()
                                    .setDefaultRequestConfig(requestConfig)
                                    .setUserAgent("Opera/9.80 (X11; Linux x86_64; U) Presto/2.12.388 Version/12.11")
                                    .build();

        HttpPost logging = new HttpPost(loginUrl);
        List<NameValuePair> dataLogging = Arrays.asList(new NameValuePair[]{
                new BasicNameValuePair("login_form_submit", "1"),
                new BasicNameValuePair("login_form_login", login),
                new BasicNameValuePair("login_form_pass", password)
        });
        try {
            logging.setEntity(new UrlEncodedFormEntity(dataLogging));
            client.execute(logging);
        } catch (Exception e) {
            result.setStatus(ResultsStatusEnum.UNDEF.getCode());
            result.setNotes(e.getMessage());
            result.setOutputText(e.getClass().getName());
            logging.releaseConnection();
            return result;
        }
        logging.releaseConnection();

        HttpPost sendAnswer = new HttpPost("http://opss.assecobs.pl/?menu=comp&sub=send&comp=0");
        List<NameValuePair> dataSendAnswer = Arrays.asList(new NameValuePair[]{
                new BasicNameValuePair("form_send_submit", "1"),
                new BasicNameValuePair("form_send_comp", ""),
                new BasicNameValuePair("form_send_problem", input.getInputText()),
                new BasicNameValuePair("form_send_lang", properties.getProperty("CODEFILE_EXTENSION")),
                new BasicNameValuePair("form_send_sourcetext", path),
                new BasicNameValuePair("form_send_submittxt", "Wyślij kod")
        });
        InputStream status = null;
        try {
            sendAnswer.setEntity(new UrlEncodedFormEntity(dataSendAnswer));
            HttpResponse response = client.execute(sendAnswer);
            HttpEntity entity = response.getEntity();
            status = entity.getContent();
        } catch (Exception e) {
            result.setStatus(ResultsStatusEnum.UNDEF.getCode());
            result.setNotes(e.getMessage());
            result.setOutputText(e.getClass().getName());
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
        } catch (Exception e) {
            result.setStatus(ResultsStatusEnum.UNDEF.getCode());
            result.setNotes(e.getMessage());
            result.setOutputText(e.getClass().getName());
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
            HttpGet answer = new HttpGet("http://opss.assecobs.pl/?menu=comp&sub=stat&comp=0&id=" + submitId);
            InputStream answerStream = null;
            try {
                HttpResponse response = client.execute(answer);
                HttpEntity entity = response.getEntity();
                answerStream = entity.getContent();
            } catch (Exception e) {
                result.setStatus(ResultsStatusEnum.UNDEF.getCode());
                result.setNotes(e.getMessage());
                result.setOutputText(e.getClass().getName());
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
        HttpGet logout = new HttpGet("http://opss.assecobs.pl/?logoff");
        try {
            client.execute(logout);
        } catch (Exception e) {
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
