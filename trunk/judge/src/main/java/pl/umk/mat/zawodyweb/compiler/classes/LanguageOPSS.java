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
        //properties.getProperty("CODEFILE_EXTENSION"); properties
        
        String loginUrl = "http://opss.assecobs.pl/?&login";
        String login = "";
        String pass = "";
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
        } catch (IOException e) {
        }
        logging.releaseConnection();
        PostMethod sendAnswer = new PostMethod("http://opss.assecobs.pl/?menu=comp&sub=send&comp=0");
        NameValuePair[] dataSendAnswer = {
            new NameValuePair("form_send_submit", "1"),
            new NameValuePair("form_send_comp", ""),
            new NameValuePair("form_send_problem", "1005"), //tu numer zadania
            new NameValuePair("form_send_lang", "c"),
            new NameValuePair("form_send_sourcetext", "int main(){printf(\"\\n\");return 0;}"),
            new NameValuePair("form_send_submittxt", "Wyślij kod")
        };
        sendAnswer.setRequestBody(dataSendAnswer);
        try {
            client.executeMethod(sendAnswer);
        } catch (HttpException e) {
        } catch (IOException e) {
        }
        sendAnswer.releaseConnection();
        return new TestOutput(null);
    //zakladam, ze jestem zalogowany
    }

    @Override
    public byte[] precompile(byte[] code) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String compile(byte[] code) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String postcompile(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void closeProgram(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
