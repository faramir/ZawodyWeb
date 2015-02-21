/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.checker.classes;

import java.util.Properties;
import pl.umk.mat.zawodyweb.judge.commons.CheckerInterface;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;
import pl.umk.mat.zawodyweb.judge.commons.Program;

/**
 *
 * @author Jakub Prabucki
 */
public class HTTPsubmit implements CheckerInterface {

    @Override
    public TestOutput check(Program program, TestInput input, TestOutput output) {
        TestOutput result = program.runTest(input);
        return result;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
