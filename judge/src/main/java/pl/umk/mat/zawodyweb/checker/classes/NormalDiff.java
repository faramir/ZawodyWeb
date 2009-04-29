package pl.umk.mat.zawodyweb.checker.classes;

import pl.umk.mat.zawodyweb.checker.*;
import pl.umk.mat.zawodyweb.compiler.Code;
import pl.umk.mat.zawodyweb.compiler.Program;

/**
 *
 * @author lukash2k
 */
public class NormalDiff implements CheckerInterface {

    /**
     * UWAGA: UZYWAC TYLKO DO JUnit TESTOW!!
     * W KODZIE UZYWAC TYLKO metody check()
     * @param codeText
     * @param rightText
     * @return diff(codeText,rightText)
     */
    public int pubDiff(String codeText, String rightText) {
        return diff(codeText, rightText);
    }

    private int diff(String codeText, String rightText) {
        int i = 0;
        int j = 0;

        while (i < rightText.length() && j < codeText.length()) {
            if ((Character.isWhitespace(rightText.charAt(i)) &&
                    Character.isWhitespace(codeText.charAt(j))) ||
                    (i==0 &&
                    Character.isWhitespace(codeText.charAt(j))) ||
                    (j==0 &&
                    Character.isWhitespace(rightText.charAt(i)))
                    
                    ){
                while (i < rightText.length() &&
                        Character.isWhitespace(rightText.charAt(i))) {
                    i++;
                }

                while (j < codeText.length() &&
                        Character.isWhitespace(codeText.charAt(j))) {
                    j++;
                }

            } else if (rightText.charAt(i) == codeText.charAt(j)) {
                i++;
                j++;
            } else {
                return 1;
            }
        }
        if (i == rightText.length() && j == codeText.length()) {
            return 0;
        } else if (i == rightText.length() &&
                Character.isWhitespace(codeText.charAt(j))) {
            while (Character.isWhitespace(codeText.charAt(j)) &&
                    j < codeText.length()) {
                j++;
            }
        } else if (j == codeText.length() &&
                Character.isWhitespace(rightText.charAt(i))) {
            while (Character.isWhitespace(rightText.charAt(i)) &&
                    i < rightText.length()) {
                i++;
            }
        }
        if (i == rightText.length() && j == codeText.length()) {
            return 0;
        } else if (i == rightText.length() || j == codeText.length()) {
            return 1;
        }
        return -1;

    }

    @Override
    public CheckerResult check(Code code, TestInput input, TestOutput output) {
        Program program = code.compile();
        TestOutput codeOutput = code.getCompiler().runTest(program, input);
        if (codeOutput.getResult() != CheckerErrors.UNDEF) {
            return new CheckerResult(codeOutput.getResult(), codeOutput.getResultDesc());
        }
        CheckerResult result = new CheckerResult();
        String codeText = codeOutput.getText();
        String rightText = output.getText();
        if (diff(codeText, rightText) == 0) {
            result.setResult(CheckerErrors.ACC);

        } else {
            result.setResult(CheckerErrors.WA);

        }
        return result;
    }
}
