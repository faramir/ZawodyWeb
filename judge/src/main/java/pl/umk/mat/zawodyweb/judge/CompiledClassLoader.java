/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.judge;

/**
 *
 * @author lukash2k
 */
public class CompiledClassLoader extends ClassLoader {

    public Class<?> loadCompiledClass(String name, byte[] code) {
        Class<?> result = this.defineClass(name, code, 0, code.length);
        this.resolveClass(result);
        return result;
    }

}
