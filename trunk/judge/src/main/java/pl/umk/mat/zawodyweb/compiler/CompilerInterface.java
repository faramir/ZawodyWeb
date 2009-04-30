/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.compiler;

import java.util.Properties;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;

/**
 *
 * @author lukash2k
 */
public interface CompilerInterface {

    public void setProperties(Properties properties);

    /**
     * DO NOT USE THIS METHOD. Instead, use Program.runTest()
     */
    public TestOutput runTest(String path, TestInput input);

    /**
     * DO NOT USE THIS METHOD. Instead, use Code.compile()
     */
    public byte[] precompile(byte[] code);

    /**
     * DO NOT USE THIS METHOD. Instead, use Code.compile()
     */
    public Program compile(byte[] code);

    /**
     * DO NOT USE THIS METHOD. Instead, use Code.compile()
     */
    public Program postcompile(Program program);

    /**
     * DO NOT USE THIS METHOD. Instead, use Program.closeProgram()
     */
    public void closeProgram(Program program);
}
