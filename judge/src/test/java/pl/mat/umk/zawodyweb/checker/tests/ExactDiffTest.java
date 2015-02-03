/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.mat.umk.zawodyweb.checker.tests;

import org.junit.Test;
import static org.junit.Assert.*;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.checker.CheckerResult;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.checker.classes.ExactDiff;
import pl.umk.mat.zawodyweb.compiler.Code;
import pl.umk.mat.zawodyweb.compiler.Program;
import pl.umk.mat.zawodyweb.compiler.classes.LanguageTXT;

/**
 *
 * @author lukash2k
 */
public class ExactDiffTest {

    public ExactDiffTest() {
    }

    private int Test(String codeText, String rightText) {
        TestOutput test = new TestOutput(rightText);
        ExactDiff nd = new ExactDiff();
        LanguageTXT languageTXT = new LanguageTXT();
        Code code = new Code(codeText, languageTXT);
        Program program = code.compile();
        CheckerResult result =
                nd.check(program, new TestInput("", 0, 0, 0, null), test);
        return result.getStatus();
    }

    @Test
    public void trailingTest3() {
        int expected = ResultsStatusEnum.WA.getCode();
        assertEquals(expected, Test("15", "15\n"));
    }

    @Test
    public void trailingTest4() {
        int expected = ResultsStatusEnum.WA.getCode();
        assertEquals(expected, Test("15\n", "15"));
    }

    @Test
    public void exactTest1() {
        int expected = ResultsStatusEnum.ACC.getCode();
        assertEquals(expected, Test("15", "15"));
    }

    @Test
    public void exactTest2() {
        int expected = ResultsStatusEnum.ACC.getCode();
        assertEquals(expected, Test("15\n", "15\n"));
    }

    @Test
    public void exactTest3() {
        int expected = ResultsStatusEnum.WA.getCode();
        assertEquals(expected, Test("\n15\n", "15\n"));
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
