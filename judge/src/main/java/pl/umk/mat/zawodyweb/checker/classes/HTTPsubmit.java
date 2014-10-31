/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.checker.classes;

import java.util.Properties;
import pl.umk.mat.zawodyweb.checker.CheckerInterface;
import pl.umk.mat.zawodyweb.checker.CheckerResult;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.Program;

/**
 *
 * @author Jakub Prabucki
 */
public class HTTPsubmit implements CheckerInterface {

    @Override
    public CheckerResult check(Program program, TestInput input, TestOutput output) {
        TestOutput to = program.runTest(input);
        CheckerResult result = new CheckerResult();
        result.setResult(to.getResult());
        result.setMemUsed(to.getMemUsed());
        result.setRuntime(to.getRuntime());
        result.setDescription(to.getResultDesc());
        result.setPoints(to.getPoints());
        return result;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
