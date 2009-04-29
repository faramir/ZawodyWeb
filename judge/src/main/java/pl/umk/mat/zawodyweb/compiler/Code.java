package pl.umk.mat.zawodyweb.compiler;

/**
 *
 * @author lukash2k
 */
public class Code {

    private String code;
    private CompilerInterface compiler;

    public Code(String code, CompilerInterface compiler) {
        this.code = code;
        this.compiler = compiler;
    }

    public Program compile() {
        String precompiledCode = compiler.precompile(code);
        Program program = compiler.compile(precompiledCode);
        return compiler.postcompile(program);
    }

    public CompilerInterface getCompiler() {
        return compiler;
    }

    public String getCode() {
        return code;
    }

    public void setCompiler(CompilerInterface compiler) {
        this.compiler = compiler;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
