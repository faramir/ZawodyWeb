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
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;
import pl.umk.mat.zawodyweb.database.CheckerErrors;

/**
 *
 * @author Jakub Prabucki
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
        String login = "zawodyweb";
        String pass = "zawody.web.2009";
        HttpClient client = new HttpClient();
        PostMethod logging = new PostMethod(loginUrl);
        NameValuePair[] dataLogging = {
            new NameValuePair("login_form_submit", "1"),
            new NameValuePair("login_form_login", login),
            new NameValuePair("login_form_pass", pass)
        };
        logging.setRequestBody(dataLogging);
        try {
            client.executeMethod(logging);
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
        logging.releaseConnection();
        PostMethod sendAnswer = new PostMethod("http://opss.assecobs.pl/?menu=comp&sub=send&comp=0");
        NameValuePair[] dataSendAnswer = {
            new NameValuePair("form_send_submit", "1"),
            new NameValuePair("form_send_comp", ""),
            new NameValuePair("form_send_problem", input.getText()), //tu numer zadania
            new NameValuePair("form_send_lang", properties.getProperty("CODEFILE_EXTENSION")), //tu rozszerzenie
            new NameValuePair("form_send_sourcetext", path), //tu kod zrodlowy
            new NameValuePair("form_send_submittxt", "Wyślij kod")
        };
        sendAnswer.setRequestBody(dataSendAnswer);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("InterruptedException");
            return result;
        }
        InputStream status = null;
        try {
            client.executeMethod(sendAnswer);
            status = sendAnswer.getResponseBodyAsStream();
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
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("IOException");
            sendAnswer.releaseConnection();
            return result;
        }
        sendAnswer.releaseConnection();
        GetMethod answer = new GetMethod("http://opss.assecobs.pl/?menu=comp&sub=stat&comp=0&id=" + submitId);
        try {
            client.executeMethod(answer);
        } catch (HttpException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("HttpException");
            answer.releaseConnection();
            return result;
        } catch (IOException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("IOException");
            answer.releaseConnection();
            return result;
        }
        InputStream answerStream = null;
        try {
            client.executeMethod(answer);
            answerStream = answer.getResponseBodyAsStream();
        } catch (HttpException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("HttpException");
            answer.releaseConnection();
            return result;
        } catch (IOException e) {
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("IOException");
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
            Pattern p2 = Pattern.compile("(<tr class=row0>)|<tr class=stat_ac>");
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
            result.setResult(CheckerErrors.UNDEF);
            result.setResultDesc(e.getMessage());
            result.setText("IOException");
            sendAnswer.releaseConnection();
            return result;
        }
        String[] col = row.split("(<td>)|(</table>)");
        if (col[6].matches(".*Program zaakceptowany.*")) {
            result.setResult(CheckerErrors.ACC);
        } else if (col[6].matches(".*Błąd kompilacji.*")) {
            result.setResult(CheckerErrors.CE);
        } else if (col[6].matches(".*Błąd wykonania.*")) {
            result.setResult(CheckerErrors.RE);
        } else if (col[6].matches(".*Limit czasu przekroczony.*")) {
            result.setResult(CheckerErrors.TLE);
        } else if (col[6].matches(".*Limit pamięci przekroczony.*")) {
            result.setResult(CheckerErrors.MLE);
        } else if (col[6].matches(".*Błędna odpowiedź.*")) {
            result.setResult(CheckerErrors.WA);
        } else if (col[6].matches(".*Niedozwolona funkcja.*")) {
            result.setResult(CheckerErrors.RV);
        } else {
            result.setResult(CheckerErrors.UNKNOWN);
            result.setResultDesc("Unknown status: \"" + col[6] + "\"");
        }
        answer.releaseConnection();
        GetMethod logout = new GetMethod("http://opss.assecobs.pl/?logoff");
        try {
            client.executeMethod(logout);
        } catch (HttpException e) {
            answer.releaseConnection();
        } catch (IOException e) {
            answer.releaseConnection();
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
