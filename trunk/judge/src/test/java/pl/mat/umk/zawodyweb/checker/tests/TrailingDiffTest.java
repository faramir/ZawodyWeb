/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mat.umk.zawodyweb.checker.tests;

import org.junit.Test;
import static org.junit.Assert.*;
import pl.umk.mat.zawodyweb.checker.CheckerErrors;
import pl.umk.mat.zawodyweb.checker.CheckerResult;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.checker.classes.NormalDiff;
import pl.umk.mat.zawodyweb.checker.classes.TrailingDiff;
import pl.umk.mat.zawodyweb.compiler.Code;
import pl.umk.mat.zawodyweb.compiler.Program;
import pl.umk.mat.zawodyweb.compiler.classes.LanguageTXT;

/**
 *
 * @author lukash2k
 */
public class TrailingDiffTest {

    public TrailingDiffTest() {
    }

    private int Test(String codeText, String rightText) {
        TestOutput test = new TestOutput(rightText);
        TrailingDiff nd = new TrailingDiff();
        LanguageTXT languageTXT = new LanguageTXT();
        Code code = new Code(codeText, languageTXT);
        Program program = code.compile();
        CheckerResult result =
                nd.check(program, new TestInput("", 0, 0), test);
        return result.getResult();
    }

    @Test
    public void exactTest() {
        int expected = CheckerErrors.ACC;
        assertEquals(expected, Test("15\n", "15\n"));
    }

    @Test
    public void trailingTest1() {
        int expected = CheckerErrors.ACC;
        assertEquals(expected, Test("   15\n", "15\n"));
    }

    @Test
    public void trailingTest2() {
        int expected = CheckerErrors.WA;
        assertEquals(expected, Test("\n15\n", "15\n"));
    }

    @Test
    public void trailingTest3() {
        int expected = CheckerErrors.ACC;
        assertEquals(expected, Test("15", "15\n"));
    }

    @Test
    public void trailingTest4() {
        int expected = CheckerErrors.ACC;
        assertEquals(expected, Test("15\n", "15"));
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}