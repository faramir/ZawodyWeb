
package pl.umk.mat.zawodyweb.compiler.classes;

import java.util.Properties;
import pl.umk.mat.zawodyweb.checker.TestInput;
import pl.umk.mat.zawodyweb.checker.TestOutput;
import pl.umk.mat.zawodyweb.compiler.CompilerInterface;

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
    public String compile(byte[] code) {
        return new String(code);
    }

    @Override
    public String postcompile(String path) {
        return path;
    }

    @Override
    public void setProperties(Properties properties) {
    }

    @Override
    public void closeProgram(String path) {
    }

}
