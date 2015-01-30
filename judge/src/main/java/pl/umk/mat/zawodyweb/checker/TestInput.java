/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.checker;

import java.util.Properties;

/**
 *
 * @author lukash2k
 */
public class TestInput {

    private final String text;
    private final int maxPoints;
    private final int timeLimit;
    private final int memoryLimit;
    private final Properties property;

    public TestInput(String text, int maxPoints, int timeLimit, int memoryLimit, Properties property) {
        this.text = text;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.maxPoints = maxPoints;
        this.property = property;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public String getText() {
        return text;
    }

    public Properties getProperty() {
        return property;
    }
}
