/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.commons;

import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;

/**
 *
 * @author lukash2k
 */
public class TestOutput {

    /* Possible statuses: MLE, TLE, CE, RE */
    private int status;
    private String outputText;
    private String notes;
    private int runtime;
    private int memUsed;
    private int points;

    public TestOutput() {
        this(null);
    }

    public TestOutput(String text) {
        this.outputText = text;
        this.status = ResultsStatusEnum.UNDEF.getCode();
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getMemUsed() {
        return memUsed;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setMemUsed(int memUsed) {
        this.memUsed = memUsed;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setOutputText(String outputText) {
        this.outputText = outputText;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOutputText() {
        return outputText;
    }

    public int getStatus() {
        return status;
    }
}
