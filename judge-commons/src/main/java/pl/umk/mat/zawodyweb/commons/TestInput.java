/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.commons;

import java.util.Properties;

/**
 *
 * @author lukash2k
 */
public class TestInput {

    private final String inputText;
    private final int maxPoints;
    private final int timeLimit;
    private final int memoryLimit;
    private final Properties property;

    public TestInput(String inputText, int maxPoints, int timeLimit, int memoryLimit, Properties property) {
        this.inputText = inputText;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.maxPoints = maxPoints;
        this.property = property;
    }

    /**
     * Time limit in miliseconds (N/1000 seconds)
     * @return 
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * Memory limit in MB (N * 1024 KB)
     * @return 
     */
    public int getMemoryLimit() {
        return memoryLimit;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public String getInputText() {
        return inputText;
    }

    public Properties getProperty() {
        return property;
    }
}
