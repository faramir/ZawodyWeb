/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.judge.commons;

import java.util.Properties;
import pl.umk.mat.zawodyweb.judge.commons.Program;

/**
 *
 * @author lukash2k
 */
public interface CheckerInterface {
    /**
     * Sets properties for using compiler like:
     * <code>CODE_DIR</code>,
     * <code>CODE_FILENAME</code>,
     * <code>CODEFILE_EXTENSION</code>,
     * <code>COMPILED_DIR</code>,
     * <code>COMPILED_FILENAME</code>,
     * <code>COMPILE_TIMEOUT</code>, and other user properties
     *
     * @param properties
     */
    public void setProperties(Properties properties);

    public TestOutput check(Program program, TestInput input, TestOutput output);
}
