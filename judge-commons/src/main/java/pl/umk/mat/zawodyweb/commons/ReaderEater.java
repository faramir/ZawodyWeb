/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.commons;

import java.io.BufferedReader;

/**
 *
 * @author faramir
 */
public class ReaderEater implements Runnable {

    private StringBuilder outputText;
    private BufferedReader reader;
    private Exception exception;

    public ReaderEater(BufferedReader reader) {
        this.reader = reader;
        outputText = new StringBuilder();
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                outputText.append(line).append("\n");
            }
            reader.close();
        } catch (Exception ex) {
            this.exception = ex;
        }
    }

    /**
     * @return the outputText
     */
    public String getOutputText() {
        return outputText.toString();
    }

    /**
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }
}
