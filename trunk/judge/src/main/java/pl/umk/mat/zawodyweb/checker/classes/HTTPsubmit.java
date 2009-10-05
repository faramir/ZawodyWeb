/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.checker.classes;

import pl.umk.mat.zawodyweb.checker.CheckerInterface;
import pl.umk.mat.zawodyweb.checker.CheckerResult;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.Program;

/**
 *
 * @author Jakub Prabucki
 */
public class HTTPsubmit implements CheckerInterface {

    @Override
    public CheckerResult check(Program program, TestInput input, TestOutput output) {
        TestOutput to = program.runTest(input);
        CheckerResult result = new CheckerResult();
        result.setResult(to.getResult());
        result.setMemUsed(to.getMemUsed());
        result.setRuntime(to.getRuntime());
        result.setDescription(to.getResultDesc());
        result.setPoints(to.getPoints());
        return result;
    }
}
