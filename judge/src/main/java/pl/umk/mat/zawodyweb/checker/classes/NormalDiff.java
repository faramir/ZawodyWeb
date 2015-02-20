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
 * @author lukash2k
 */
public class NormalDiff implements CheckerInterface {

    private int diff(String codeText, String rightText) {
        int i = 0;
        int j = 0;

        if (codeText == null || rightText == null) {
            if (codeText == null && rightText == null) {
                return 0;
            }
            return 1;
        }
        int rightTextLength = rightText.length();
        int codeTextLength = codeText.length();
        while (i < rightTextLength && j < codeTextLength) {
            if ((Character.isWhitespace(rightText.charAt(i))
                    && Character.isWhitespace(codeText.charAt(j)))
                    || (i == 0
                    && Character.isWhitespace(codeText.charAt(j)))
                    || (j == 0
                    && Character.isWhitespace(rightText.charAt(i)))) {
                while (i < rightTextLength
                        && Character.isWhitespace(rightText.charAt(i))) {
                    i++;
                }

                while (j < codeTextLength
                        && Character.isWhitespace(codeText.charAt(j))) {
                    j++;
                }

            } else if (rightText.charAt(i) == codeText.charAt(j)) {
                i++;
                j++;
            } else {
                return 1;
            }
        }
        if (i == rightTextLength && j == codeTextLength) {
            return 0;
        } else if (i == rightTextLength
                && Character.isWhitespace(codeText.charAt(j))) {
            while (j < codeTextLength
                    && Character.isWhitespace(codeText.charAt(j))) {
                j++;
            }
        } else if (j == codeTextLength
                && Character.isWhitespace(rightText.charAt(i))) {
            while (i < rightTextLength
                    && Character.isWhitespace(rightText.charAt(i))) {
                i++;
            }
        }
        if (i == rightTextLength && j == codeTextLength) {
            return 0;
        } else if (i == rightTextLength || j == codeTextLength) {
            return 1;
        }
        return -1;

    }

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

    @Override
    public void setProperties(Properties properties) {
    }
}
