/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.checker.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.commons.CheckerInterface;
import pl.umk.mat.zawodyweb.commons.TestInput;
import pl.umk.mat.zawodyweb.commons.TestOutput;
import pl.umk.mat.zawodyweb.commons.Program;

/**
 *
 * @author lukash2k
 */
public class TrailingDiff implements CheckerInterface {

    @Override
    public TestOutput check(Program program, TestInput input, TestOutput output) {
        TestOutput codeOutput = program.runTest(input);
        if (codeOutput.getStatus() != ResultsStatusEnum.UNDEF.getCode()) {
            return codeOutput;
        }

        String codeText = codeOutput.getOutputText();
        String rightText = output.getOutputText();
        try {
            if (diff(codeText, rightText) == 0) {
                codeOutput.setStatus(ResultsStatusEnum.ACC.getCode());
                codeOutput.setPoints(input.getMaxPoints());
            } else {
                codeOutput.setStatus(ResultsStatusEnum.WA.getCode());
                codeOutput.setPoints(0);
            }
        } catch (IOException ex) {
            //System.err.println("IOException at TrailingDiff." + ex.getMessage());
        }
        codeOutput.setOutputText(null);

        return codeOutput;
    }

    private int diff(String codeText, String rightText) throws IOException {
        BufferedReader right = new BufferedReader(new StringReader(rightText));
        BufferedReader code = new BufferedReader(new StringReader(codeText));

        String codeLine = code.readLine();
        String rightLine = right.readLine();
        while (codeLine != null && rightLine != null) {
            if (codeLine.trim().compareTo(rightLine.trim()) != 0) {
                return 1;
            }
            codeLine = code.readLine();
            rightLine = right.readLine();
        }
        if (codeLine != null) {
            while (codeLine != null && codeLine.trim().isEmpty()) {
                codeLine = code.readLine();
            }
        }
        if (rightLine != null) {
            while (rightLine != null && rightLine.trim().isEmpty()) {
                rightLine = right.readLine();
            }
        }
        if (codeLine == null && rightLine == null) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
