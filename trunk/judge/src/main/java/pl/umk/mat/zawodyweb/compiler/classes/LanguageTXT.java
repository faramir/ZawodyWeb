
package pl.umk.mat.zawodyweb.compiler.classes;

import java.util.Properties;
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
    public TestOutput runTest(String path, TestInput input) {
        return new TestOutput(path);
    }

    @Override
    public byte[] precompile(byte[] code) {
        return code;
    }

    @Override
    public Program compile(byte[] code) {
        return new Program(new String(code));
    }

    @Override
    public Program postcompile(Program program) {
        return program;
    }

    @Override
    public void setProperties(Properties properties) {
    }

    @Override
    public void closeProgram(Program program) {
    }

}
