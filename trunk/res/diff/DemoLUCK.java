package pl.umk.mat.zawodyweb.checker.classes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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
public class DemoLUCK implements CheckerInterface {

    @Override
    public CheckerResult check(Program program, TestInput input, TestOutput output) {
        TestOutput codeOutput = program.runTest(input);
        if (codeOutput.getResult() != CheckerErrors.UNDEF) {
            return new CheckerResult(codeOutput.getResult(), codeOutput.getResultDesc());
        }
        CheckerResult result = new CheckerResult();
        if (new Random(new Date().getTime()).nextInt(7) == 0) {
            result.setResult(CheckerErrors.ACC);
            result.setPoints(input.getMaxPoints());
        } else {
            result.setResult(CheckerErrors.WA);
        }
        return result;
    }
}
