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
            return result; // FIXME: tak miało być?
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(firstGet, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        String line, name, value;

        NameValuePair[] loginData = new NameValuePair[12]; // FIXME: a bit non-kosher thing - try to use Vector<NVP> instead of constant-size array
        loginData[0] = new NameValuePair("username", login);
        loginData[1] = new NameValuePair("passwd", password);
        int noData = 2;
        try {
            line = br.readLine();
            while (line != null && !line.matches(".*class=\"mod_login\".*")) {
                line = br.readLine();
            }
            while (line != null && !line.matches("(?i).*submit.*login.*")) {
                if (line.matches(".*hidden.*name=\".*value=\".*")) {
                    name = line.split("name=\"")[1].split("\"")[0];
                    value = line.split("value=\"")[1].split("\"")[0];
                    loginData[noData++] = new NameValuePair(name, value); // FIXME: check if it's neccesary: URLDecoder.decode(value, "UTF-8"));
                }
                line = br.readLine();
            }
            loginData[noData++] = new NameValuePair("remember", "yes");
            loginData[noData++] = new NameValuePair("Submit", "Login");
        } catch (IOException e) {
        }

        PostMethod sendAnswer = new PostMethod("http://uva.onlinejudge.org/index.php?option=com_comprofiler&task=login");
        sendAnswer.setRequestHeader("Referer", acmSite);

        sendAnswer.setRequestBody(loginData);
        try {
            client.executeMethod(sendAnswer);
            br = new BufferedReader(new InputStreamReader(sendAnswer.getResponseBodyAsStream(), "UTF-8"));
        } catch (HttpException e) {
            sendAnswer.releaseConnection();
        } catch (IOException e) {
            sendAnswer.releaseConnection();
        }

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
