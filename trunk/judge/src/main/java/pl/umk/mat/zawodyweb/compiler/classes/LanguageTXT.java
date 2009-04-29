
package pl.umk.mat.zawodyweb.compiler.classes;

import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;
import pl.umk.mat.zawodyweb.compiler.Program;

/**
 *
 * @author lukash2k
 */
public class LanguageTXT implements CompilerInterface{

    public LanguageTXT() {
    }

    @Override
    public TestOutput runTest(Program program, TestInput input) {
        return new TestOutput(program.getPath());
    }

    @Override
    public String precompile(String code) {
        return code;
    }

    @Override
    public Program compile(String code) {
        return new Program(code, "");
    }

    @Override
    public Program postcompile(Program program) {
        return program;
    }

}
