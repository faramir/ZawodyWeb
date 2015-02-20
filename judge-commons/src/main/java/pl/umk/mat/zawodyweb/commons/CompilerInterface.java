/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.commons;

import java.util.Properties;
import pl.umk.mat.zawodyweb.commons.TestInput;
import pl.umk.mat.zawodyweb.commons.TestOutput;

/**
 *
 * @author lukash2k
 */
public interface CompilerInterface {

    /**
     * Sets properties for using compiler like: <code>CODE_DIR</code>,
     * <code>CODE_FILENAME</code>, <code>CODEFILE_EXTENSION</code>,
     * <code>COMPILED_DIR</code>, <code>COMPILED_FILENAME</code>,
     * <code>COMPILE_TIMEOUT</code>, and other user properties
     *
     * @param properties
     */
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
    public String compile(byte[] code);

    /**
     * DO NOT USE THIS METHOD. Instead, use Code.compile()
     */
    public String postcompile(String path);

    /**
     * DO NOT USE THIS METHOD. Instead, use Program.closeProgram()
     */
    public void closeProgram(String path);
}
