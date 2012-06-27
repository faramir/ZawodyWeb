/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
