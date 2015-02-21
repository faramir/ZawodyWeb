/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.judge.commons;

import pl.umk.mat.zawodyweb.judge.commons.CompilerInterface;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;

/**
 *
 * @author lukash2k
 */
public class Program {

    private String path;
    private CompilerInterface compiler;

    public void closeProgram() {
        compiler.closeProgram(path);
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

    public Program(String path, CompilerInterface compiler) {
        this.path = path;
        this.compiler = compiler;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
