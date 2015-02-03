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
 * @author Jakub Prabucki
 */
public class ManualCheck implements CheckerInterface {

    @Override
    public CheckerResult check(Program program, TestInput input, TestOutput output) {
        CheckerResult result = new CheckerResult();
        result.setStatus(ResultsStatusEnum.MANUAL.getCode());
        result.setMemUsed(0);
        result.setRuntime(0);
        result.setDescription("");
        result.setPoints(0);
        return result;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
