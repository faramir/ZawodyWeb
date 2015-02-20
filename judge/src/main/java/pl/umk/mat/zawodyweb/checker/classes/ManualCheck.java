/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.checker.classes;

import java.util.Properties;
import pl.umk.mat.zawodyweb.commons.CheckerInterface;
import pl.umk.mat.zawodyweb.commons.TestInput;
import pl.umk.mat.zawodyweb.commons.TestOutput;
import pl.umk.mat.zawodyweb.commons.Program;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;

/**
 *
 * @author Jakub Prabucki
 */
public class ManualCheck implements CheckerInterface {

    @Override
    public TestOutput check(Program program, TestInput input, TestOutput output) {
        TestOutput result = new TestOutput();
        result.setStatus(ResultsStatusEnum.MANUAL.getCode());
        result.setMemUsed(0);
        result.setRuntime(0);
        result.setNotes("");
        result.setPoints(0);
        return result;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
