/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.compiler.classes;

import java.io.IOException;
import java.util.Properties;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
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
