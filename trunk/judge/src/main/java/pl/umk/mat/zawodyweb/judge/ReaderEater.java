/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.judge;

import java.io.BufferedReader;
import java.io.IOException;

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
