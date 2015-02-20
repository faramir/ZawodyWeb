/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.compiler;

import pl.umk.mat.zawodyweb.commons.Program;
import pl.umk.mat.zawodyweb.commons.CompilerInterface;

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
