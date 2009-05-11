package pl.umk.mat.zawodyweb.compiler;

/**
 *
 * @author lukash2k
 */
public class Code {

    private byte[] code;
    private CompilerInterface compiler;

    public Code(byte[] code, CompilerInterface compiler) {
        this.code = code;
        this.compiler = compiler;
    }

    public Code(String code, CompilerInterface compiler) {
        this.code = code.getBytes();
        this.compiler = compiler;
    }

    public Program compile() {
        byte[] precompiledCode = compiler.precompile(code);
        String path = compiler.compile(precompiledCode);
        path= compiler.postcompile(path);
        return new Program(path, compiler);
    }

    public CompilerInterface getCompiler() {
        return compiler;
    }

    public byte[] getCode() {
        return code;
    }

    public void setCompiler(CompilerInterface compiler) {
        this.compiler = compiler;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }
}
