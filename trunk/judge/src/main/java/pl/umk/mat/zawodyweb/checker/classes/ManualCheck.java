/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Jakub Prabucki
 */
public class ManualCheck implements CheckerInterface {

    @Override
    public CheckerResult check(Program program, TestInput input, TestOutput output) {
        CheckerResult result = new CheckerResult();
        result.setResult(CheckerErrors.MANUAL);
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
