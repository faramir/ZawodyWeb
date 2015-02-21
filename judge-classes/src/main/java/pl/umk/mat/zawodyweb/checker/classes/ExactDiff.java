/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.checker.classes;

import java.util.Properties;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.judge.commons.CheckerInterface;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;
import pl.umk.mat.zawodyweb.judge.commons.Program;

/**
 *
 * @author lukash2k
 */
public class ExactDiff implements CheckerInterface {

    @Override
    public TestOutput check(Program program, TestInput input, TestOutput output) {
        TestOutput codeOutput = program.runTest(input);
        if (codeOutput.getStatus() != ResultsStatusEnum.UNDEF.getCode()) {
            return codeOutput;
        }

        String codeText = codeOutput.getOutputText();
        String rightText = output.getOutputText();
        if (diff(codeText, rightText) == 0) {
            codeOutput.setStatus(ResultsStatusEnum.ACC.getCode());
            codeOutput.setPoints(input.getMaxPoints());
        } else {
            codeOutput.setStatus(ResultsStatusEnum.WA.getCode());
            codeOutput.setPoints(0);
        }
        codeOutput.setOutputText(null);
        
        return codeOutput;
    }

    private int diff(String codeText, String rightText) {

        if (codeText == null || rightText == null) {
            if (codeText == null && rightText == null) {
                return 0;
            }
            return 1;
        }

        if (codeText.length() != rightText.length()) {
            return 1;
        }
        int codeTextLength = codeText.length();
        for (int i = 0; i < codeTextLength; i++) {
            if (codeText.charAt(i) != rightText.charAt(i)) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
