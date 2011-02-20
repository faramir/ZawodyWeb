/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
