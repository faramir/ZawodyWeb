/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.judge;

import java.io.BufferedWriter;

/**
 *
 * @author faramir
 */
public class WriterFeeder implements Runnable {

    private String inputText;
    private BufferedWriter writer;
    private Exception exception;

    public WriterFeeder(BufferedWriter writer, String inputText) {
        this.writer = writer;
        this.inputText = inputText;
    }

    @Override
    public void run() {
        try {
            writer.write(inputText);
            writer.close();
        } catch (Exception ex) {
            this.exception = ex;
        }
    }

    /**
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }
}
