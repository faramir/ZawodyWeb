/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.checker;

import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;

/**
 *
 * @author lukash2k
 */
public class CheckerResult {

    private int status;
    private String description;
    private int memUsed;
    private int runtime;
    private int points;

    public CheckerResult() {
        status = ResultsStatusEnum.UNDEF.getCode();
        description = null;
    }

    public CheckerResult(int result, String description) {
        this.status = result;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(int memUsed) {
        this.memUsed = memUsed;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
