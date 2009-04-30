package pl.umk.mat.zawodyweb.compiler;

import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;

/**
 *
 * @author lukash2k
 */
public class Program {

    private String path;
    private CompilerInterface compiler;

    public void closeProgram() {
        compiler.closeProgram(this);
    }

    public TestOutput runTest(TestInput input) {
        return compiler.runTest(path, input);
    }

    public CompilerInterface getCompiler() {
        return compiler;
    }

    public void setCompiler(CompilerInterface compiler) {
        this.compiler = compiler;
    }

    public Program(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
