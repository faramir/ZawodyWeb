/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mat.umk.zawodyweb.checker.tests;

import org.junit.Test;
import pl.umk.mat.zawodyweb.checker.CheckerErrors;
import pl.umk.mat.zawodyweb.checker.CheckerResult;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.checker.classes.NormalDiff;
import pl.umk.mat.zawodyweb.compiler.Code;
import pl.umk.mat.zawodyweb.compiler.classes.LanguageTXT;
import static org.junit.Assert.*;

/**
 *
 * @author Właściciel
 */
public class NormalDiffTest {

    public NormalDiffTest() {
    }

    @Test
    public void exactTest() {
        int expected = CheckerErrors.ACC;
        String codeText = "15\n";
        String rightText = "15\n";
        TestOutput test = new TestOutput(rightText);
        NormalDiff nd = new NormalDiff();
        CheckerResult result =
                nd.check(new Code(codeText, new LanguageTXT()), new TestInput("", 0, 0), test);
        assertEquals(expected, result.getResult());
    }

    @Test
    public void codeLongerTest() {
        int expected = CheckerErrors.ACC;
        String codeText = "\n      15\n     \n";
        String rightText = "15\n";
        TestOutput test = new TestOutput(rightText);
        NormalDiff nd = new NormalDiff();
        CheckerResult result =
                nd.check(new Code(codeText, new LanguageTXT()), new TestInput("", 0, 0), test);
        assertEquals(expected, result.getResult());
    }

    @Test
    public void rightLongerTest() {
        int expected = CheckerErrors.ACC;
        String codeText = "15\n";
        String rightText = "\n 15\n\n";
        TestOutput test = new TestOutput(rightText);
        NormalDiff nd = new NormalDiff();
        CheckerResult result =
                nd.check(new Code(codeText, new LanguageTXT()), new TestInput("", 0, 0), test);
        assertEquals(expected, result.getResult());
    }

    @Test
    public void whitespaceTest() {
        int expected = CheckerErrors.ACC;
        String codeText = "15 13\n";
        String rightText = "\n 15\n13\n";
        TestOutput test = new TestOutput(rightText);
        NormalDiff nd = new NormalDiff();
        CheckerResult result =
                nd.check(new Code(codeText, new LanguageTXT()), new TestInput("", 0, 0), test);
        assertEquals(expected, result.getResult());
    }

    @Test
    public void differentTest1() {
        int expected = CheckerErrors.WA;
        String codeText = "15 12\n";
        String rightText = "\n 15\n13\n";
        TestOutput test = new TestOutput(rightText);
        NormalDiff nd = new NormalDiff();
        CheckerResult result =
                nd.check(new Code(codeText, new LanguageTXT()), new TestInput("", 0, 0), test);
        assertEquals(expected, result.getResult());
    }

    @Test
    public void differentTest2() {
        int expected = CheckerErrors.WA;
        String codeText = "1512\n";
        String rightText = "\n 15\n13\n";
        TestOutput test = new TestOutput(rightText);
        NormalDiff nd = new NormalDiff();
        CheckerResult result =
                nd.check(new Code(codeText, new LanguageTXT()), new TestInput("", 0, 0), test);
        assertEquals(expected, result.getResult());
    }

    @Test
    public void differentTest3() {
        int expected = CheckerErrors.WA;
        String codeText = "151 2\n";
        String rightText = "\n 15\n13\n";
        TestOutput test = new TestOutput(rightText);
        NormalDiff nd = new NormalDiff();
        CheckerResult result =
                nd.check(new Code(codeText, new LanguageTXT()), new TestInput("", 0, 0), test);
        assertEquals(expected, result.getResult());
    }

    @Test
    public void standardDifferentTest3() {
        int expected = CheckerErrors.WA;
        String codeText = "15 12\n";
        String rightText = "15 13\n";
        TestOutput test = new TestOutput(rightText);
        NormalDiff nd = new NormalDiff();
        CheckerResult result =
                nd.check(new Code(codeText, new LanguageTXT()), new TestInput("", 0, 0), test);
        assertEquals(expected, result.getResult());
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}