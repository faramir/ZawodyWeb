/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.mat.umk.zawodyweb.checker.tests;

import org.junit.Test;
import pl.umk.mat.zawodyweb.database.CheckerErrors;
import pl.umk.mat.zawodyweb.checker.CheckerResult;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.checker.classes.NormalDiff;
import pl.umk.mat.zawodyweb.compiler.Code;
import pl.umk.mat.zawodyweb.compiler.Program;
import pl.umk.mat.zawodyweb.compiler.classes.LanguageTXT;
import static org.junit.Assert.*;

/**
 *
 * @author lukash2k
 */
public class NormalDiffTest {

    public NormalDiffTest() {
    }

    private int Test(String codeText, String rightText) {
        TestOutput test = new TestOutput(rightText);
        NormalDiff nd = new NormalDiff();
        LanguageTXT languageTXT = new LanguageTXT();
        Code code = new Code(codeText, languageTXT);
        Program program = code.compile();
        CheckerResult result =
                nd.check(program, new TestInput("", 0, 0, 0), test);
        return result.getResult();
    }

    @Test
    public void exactTest() {
        int expected = CheckerErrors.ACC;
        assertEquals(expected, Test("15\n", "15\n"));
    }

    @Test
    public void codeLongerTest() {
        int expected = CheckerErrors.ACC;
        String codeText = "\n      15\n     \n";
        String rightText = "15\n";
        assertEquals(expected, Test(codeText, rightText));
    }

    @Test
    public void rightLongerTest() {
        int expected = CheckerErrors.ACC;
        String codeText = "15\n";
        String rightText = "\n 15\n\n";
        assertEquals(expected, Test(codeText, rightText));
    }

    @Test
    public void whitespaceTest() {
        int expected = CheckerErrors.ACC;
        String codeText = "15 13\n";
        String rightText = "\n 15\n13\n";
        assertEquals(expected, Test(codeText, rightText));
    }

    @Test
    public void differentTest1() {
        int expected = CheckerErrors.WA;
        String codeText = "15 12\n";
        String rightText = "\n 15\n13\n";
        assertEquals(expected, Test(codeText, rightText));
    }

    @Test
    public void differentTest2() {
        int expected = CheckerErrors.WA;
        String codeText = "1512\n";
        String rightText = "\n 15\n12\n";
        assertEquals(expected, Test(codeText, rightText));
    }

    @Test
    public void differentTest3() {
        int expected = CheckerErrors.WA;
        String codeText = "151 2\n";
        String rightText = "\n 15\n12\n";
        assertEquals(expected, Test(codeText, rightText));
    }

    @Test
    public void standardDifferentTest3() {
        int expected = CheckerErrors.WA;
        String codeText = "15 12\n";
        String rightText = "15 13\n";
        assertEquals(expected, Test(codeText, rightText));
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
