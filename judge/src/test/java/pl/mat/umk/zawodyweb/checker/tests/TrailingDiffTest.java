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
import pl.umk.mat.zawodyweb.commons.TestInput;
import pl.umk.mat.zawodyweb.commons.TestOutput;
import pl.umk.mat.zawodyweb.checker.classes.TrailingDiff;
import pl.umk.mat.zawodyweb.compiler.Code;
import pl.umk.mat.zawodyweb.commons.Program;
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
        TestOutput result =
                nd.check(program, new TestInput("", 0, 0, 0, null), test);
        return result.getStatus();
    }

    @Test
    public void exactTest() {
        int expected = ResultsStatusEnum.ACC.getCode();
        assertEquals(expected, Test("15\n", "15\n"));
    }

    @Test
    public void trailingTest1() {
        int expected = ResultsStatusEnum.ACC.getCode();
        assertEquals(expected, Test("   15\n", "15\n"));
    }

    @Test
    public void trailingTest2() {
        int expected = ResultsStatusEnum.WA.getCode();
        assertEquals(expected, Test("\n15\n", "15\n"));
    }

    @Test
    public void trailingTest3() {
        int expected = ResultsStatusEnum.ACC.getCode();
        assertEquals(expected, Test("15", "15\n"));
    }

    @Test
    public void trailingTest4() {
        int expected = ResultsStatusEnum.ACC.getCode();
        assertEquals(expected, Test("15\n", "15"));
    }

    @Test
    public void trailingTest5() {
        int expected = ResultsStatusEnum.ACC.getCode();
        assertEquals(expected, Test("     15 13\n\n", "15 13\n"));
    }
    @Test
    public void trailingTest6() {
        int expected = ResultsStatusEnum.WA.getCode();
        assertEquals(expected, Test("     15  13\n\n", "15 13\n"));
    }
    
    @Test
    public void trailingTest7() {
        int expected = ResultsStatusEnum.WA.getCode();
        assertEquals(expected, Test("     15\n13\n\n", "15 13\n"));
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
