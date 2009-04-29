/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.compiler;

import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;

/**
 *
 * @author lukash2k
 */
public interface CompilerInterface {

    public TestOutput runTest(Program program, TestInput input);

    public String precompile(String code);

    public Program compile(String code);

    public Program postcompile(Program program);
}
