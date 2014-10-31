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
import pl.umk.mat.zawodyweb.database.CheckerErrors;

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
    public CheckerResult check(Program program, TestInput input, TestOutput output) {
        TestOutput codeOutput = program.runTest(input);
        if (codeOutput.getResult() != CheckerErrors.UNDEF) {
            return new CheckerResult(codeOutput.getResult(), codeOutput.getResultDesc());
        }
        CheckerResult result = new CheckerResult();
        String codeText = codeOutput.getText();
        String rightText = output.getText();
        if (diff(codeText, rightText) == 0) {
            result.setResult(CheckerErrors.ACC);
            result.setPoints(input.getMaxPoints());

        } else {
            result.setResult(CheckerErrors.WA);
            result.setPoints(0);

        }
        result.setRuntime(codeOutput.getRuntime());
        result.setMemUsed(codeOutput.getMemUsed());
        return result;
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
