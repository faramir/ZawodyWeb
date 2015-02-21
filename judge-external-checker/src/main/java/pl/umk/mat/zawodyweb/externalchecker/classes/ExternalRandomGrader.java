/*
 * Copyright (c) 2015, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.externalchecker.classes;

import java.util.Properties;
import java.util.Random;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.externalchecker.ExternalInterface;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;

/**
 *
 * @author faramir
 */
public class ExternalRandomGrader implements ExternalInterface {

    final private static Random random = new Random();

    @Override
    public void setProperties(Properties properties) {
    }

    @Override
    public String getPrefix() {
        return "random";
    }

    @Override
    public TestOutput check(String externalId, TestInput testInput, TestOutput testOutput) {
        TestOutput output = new TestOutput();
        switch (random.nextInt(10)) {
            case 0:
                output.setStatus(ResultsStatusEnum.RE.getCode());
                output.setPoints(0);
                break;
            case 1:
                output.setStatus(ResultsStatusEnum.TLE.getCode());
                output.setPoints(0);
                break;
            case 2:
                output.setStatus(ResultsStatusEnum.ACC.getCode());
                output.setPoints(testInput.getMaxPoints());
                break;
            case 3:
                output.setStatus(ResultsStatusEnum.WA.getCode());
                output.setPoints(random.nextInt(testInput.getMaxPoints()));
                break;
            default:
                return null;
        }
        return output;
    }
}
