/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.checker;

import java.util.Properties;
import pl.umk.mat.zawodyweb.compiler.Program;

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

    public CheckerResult check(Program program, TestInput input, TestOutput output);
}
