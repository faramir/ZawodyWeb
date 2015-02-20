/*
 * Copyright (c) 2009-2015, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.commons;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 *
 * @author lukash2k
 * @author faramir
 */
public class BinaryClassLoader extends ClassLoader {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BinaryClassLoader.class);

    private final Map<String, byte[]> entries = new HashMap<String, byte[]>();

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
            logger.trace("loading: " + name);
            if (classBytes != null) {
                c = defineClass(name, classBytes, 0, classBytes.length);
                if (c != null) {
                    return c;
                }
            }
        } catch (ClassFormatError ex) {
            ex.printStackTrace(System.err);
        }

        loadByParent(name);

        throw new ClassNotFoundException("Class not found: " + name);
    }

    public Class<?> loadCompiledClass(String name, byte[] code) {
        Class<?> c = findLoadedClass(name);
        if (c != null) {
            return c;
        }

        logger.trace("loading compiled: " + name);

        Class<?> result = this.defineClass(name, code, 0, code.length);
        this.resolveClass(result);

        return result;
    }

//    /**
//     * @throws ClassFormatError is thrown when there is problem with loading JAR
//     * file
//     */
//    public Manifest loadManifestFromBinaryJar(byte[] jarBytes) {
//        try {
//            JarInputStream jis = null;
//            try {
//                jis = new JarInputStream(new ByteArrayInputStream(jarBytes));
//                
//                return jis.getManifest();
//            } finally {
//                if (jis != null) {
//                    jis.close();
//                }
//            }
//        } catch (IOException ex) {
//            throw new ClassFormatError(ex.getLocalizedMessage());
//        }
//    }

    /**
     * @throws ClassFormatError is thrown when there is problem with loading JAR
     * file
     */
    public Class<?> loadClassFromBinaryJar(String name, byte[] jarBytes) {
        try {
            JarInputStream jis = null;
            try {
                jis = new JarInputStream(new ByteArrayInputStream(jarBytes));

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
            } finally {
                if (jis != null) {
                    jis.close();
                }
            }
        } catch (IOException ex) {
            throw new ClassFormatError(ex.getLocalizedMessage());
        }
    }
}
