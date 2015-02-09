/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import java.util.Properties;

/**
 *
 * @author faramir
 */
public class ExternalInput {

    private final String inputText;
    private final int maxPoints;
    private final int timeLimit;
    private final int memoryLimit;
    private final Properties property;

    public ExternalInput(String inputText, int maxPoints, int timeLimit, int memoryLimit, Properties property) {
        this.inputText = inputText;
        this.maxPoints = maxPoints;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.property = property;
    }

    public String getInputText() {
        return inputText;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public Properties getProperty() {
        return property;
    }

}
