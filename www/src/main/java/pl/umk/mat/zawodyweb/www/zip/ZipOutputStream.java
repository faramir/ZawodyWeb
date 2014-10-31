/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www.zip;

import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 *
 * @author faramir
 */
public class ZipOutputStream extends java.util.zip.ZipOutputStream {

    private int seriesCount;
    private int problemsCount;
    private int testsCount;
    
    public ZipOutputStream(OutputStream out) {
        super(out);
    }

    public ZipOutputStream(OutputStream out, Charset charset) {
        super(out, charset);
    }

    public int getSerie() {
        return seriesCount;
    }

    public int nextSerie() {
        return ++seriesCount;
    }

    public int getProblem() {
        return problemsCount;
    }

    public int nextProblem() {
        return ++problemsCount;
    }

    public int getTest() {
        return testsCount;
    }

    public int nextTest() {
        return ++testsCount;
    }
}
