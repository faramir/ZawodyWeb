/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mat.umk.zawodyweb.judge.tests;

import java.util.Properties;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.classes.LanguageUVA;

/**
 *
 * @author faramir
 */
public class LMainTest {

    public static void main(String[] args) {
        LanguageUVA l = new LanguageUVA();
        Properties properties = new Properties();
        properties.setProperty("uva.login","spamz");
        properties.setProperty("uva.password","spamz2");
        properties.setProperty("uva.max_time","1000000");
        properties.setProperty("CODEFILE_EXTENSION","java");
        properties.setProperty("CODE_FILENAME","MyFile.java");
        
        l.setProperties(properties);
        TestOutput o = l.runTest("public class MyFile {"
                + "  public static void main(String[] args) {"
                + "    System.out.println(\"4\\n1 2 3 4\");"
                + "  }"
                + "}", new TestInput("11031", 1, 0, 0));

        System.out.println("text:"+o.getText());
        System.out.println("desc:"+o.getResultDesc());
        System.out.println("pnts:"+o.getPoints());
        System.out.println("rtme:"+o.getRuntime());
        System.out.println("rslt:"+o.getResult());
    }
}
