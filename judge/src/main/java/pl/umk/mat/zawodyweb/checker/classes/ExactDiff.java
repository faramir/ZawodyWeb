package pl.umk.mat.zawodyweb.checker.classes;

import pl.umk.mat.zawodyweb.database.CheckerErrors;
import pl.umk.mat.zawodyweb.checker.CheckerInterface;
import pl.umk.mat.zawodyweb.checker.CheckerResult;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.Program;

/**
 *
 * @author lukash2k
 */
public class ExactDiff implements CheckerInterface {

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
}
