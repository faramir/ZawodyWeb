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
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;

/**
 *
 * @author faramir
 */
public class ExternalChecker implements CheckerInterface {

    @Override
    public CheckerResult check(Program program, TestInput input, TestOutput output) {
        TestOutput to = program.runTest(input);

        CheckerResult result = new CheckerResult();
        result.setStatus(ResultsStatusEnum.EXTERNAL.getCode());
        result.setDescription(to.getResultDesc());
        result.setMemUsed(0);
        result.setRuntime(0);
        result.setPoints(0);

        return result;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
