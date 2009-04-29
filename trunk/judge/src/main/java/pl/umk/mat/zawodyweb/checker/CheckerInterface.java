/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.checker;

import pl.umk.mat.zawodyweb.compiler.Code;

/**
 *
 * @author lukash2k
 */
public interface CheckerInterface {

    public CheckerResult check(Code code, TestInput input, TestOutput output);
}
