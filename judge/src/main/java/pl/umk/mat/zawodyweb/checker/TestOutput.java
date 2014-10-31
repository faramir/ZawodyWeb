/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.checker;

import pl.umk.mat.zawodyweb.database.CheckerErrors;

/**
 *
 * @author lukash2k
 */
public class TestOutput {

    private String text;
    /* Possible results: MLE, TLE, CE, RE */
    private int result;
    private String resultDesc;
    private int runtime;
    private int memUsed;
    private int points;

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



    public TestOutput(String text) {
        this.text = text;
        this.result = CheckerErrors.UNDEF;
        this.resultDesc = null;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getText() {
        return text;
    }

    public int getResult() {
        return result;
    }
}
