/*
 * Copyright (c) 2009-2015, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author Marek Nowicki /faramir/
 */
public class BinaryClassLoader extends ClassLoader {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BinaryClassLoader.class);

    private final Map<String, byte[]> entries = new HashMap<>();

    private Class<?> loadByParent(String name) throws ClassNotFoundException {
        if (getParent() != null) {
            return getParent().loadClass(name);
        } else {
            return getSystemClassLoader().loadClass(name);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name == null) {
            return null;
        }

        Class<?> c = findLoadedClass(name);
        if (c != null) {
            return c;
        }

        try {
            byte[] classBytes = entries.get(name);
            if (classBytes != null) {
                logger.trace("Loading class: " + name);
                c = defineClass(name, classBytes, 0, classBytes.length);
                if (c != null) {
                    return c;
                }
            }
        } catch (ClassFormatError ex) {
            ex.printStackTrace(System.err);
        }

        c = loadByParent(name);

        if (c != null) {
            return c;
        }
        
        throw new ClassNotFoundException("Class not found: " + name);
    }

    /* @author lukash2k */
    public Class<?> loadCompiledClass(String name, byte[] code) {
        Class<?> c = findLoadedClass(name);
        if (c != null) {
            logger.trace("Class already loaded: " + name);
            return c;
        }

        logger.trace("Loading class: " + name);
//        Arrays.stream(Thread.currentThread().getStackTrace()).forEach(
//                st -> logger.trace(st.toString())
//        );

        Class<?> result = this.defineClass(name, code, 0, code.length);
        this.resolveClass(result);

        return result;
    }

    /**
     * @throws ClassFormatError is thrown when there is problem with loading JAR
     * file
     */
    public Class<?> loadClassFromBinaryJar(String name, byte[] jarBytes) {
        try {
            try (JarInputStream jis = new JarInputStream(new ByteArrayInputStream(jarBytes))) {
                JarEntry entry;

                while ((entry = jis.getNextJarEntry()) != null) {
                    if (entry.isDirectory()) {
                        continue;
                    }

                    String classFileName = entry.getName();
                    if (!classFileName.endsWith(".class")) {
                        continue;
                    }

                    String className = classFileName.substring(0, classFileName.length() - 6/*.class*/);
                    className = className.replace('/', '.');

                    int classSize = (int) entry.getSize();
                    byte[] classBytes = new byte[classSize];

                    jis.read(classBytes, 0, classSize);

                    entries.put(className, classBytes);
                }

                return loadCompiledClass(name, entries.get(name));
            }
        } catch (IOException ex) {
            throw new ClassFormatError(ex.getLocalizedMessage());
        }
    }

    public Class<?> loadBinary(String name, byte[] bytes) {
        try {
            return loadCompiledClass(name, bytes);
        } catch (Throwable t) {
            return loadClassFromBinaryJar(name, bytes);
        }
    }
}
