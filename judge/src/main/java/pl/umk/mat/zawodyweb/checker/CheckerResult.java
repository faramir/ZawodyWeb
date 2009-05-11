/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.checker;

import pl.umk.mat.zawodyweb.database.CheckerErrors;

/**
 *
 * @author lukash2k
 */
public class CheckerResult {

    private int result;
    private String decription;
    private int memUsed;
    private int runtime;

    public void setMemUsed(int memUsed) {
        this.memUsed = memUsed;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getMemUsed() {
        return memUsed;
    }

    public int getRuntime() {
        return runtime;
    }


    public CheckerResult() {
        result = CheckerErrors.UNDEF;
        decription = null;
    }

    public CheckerResult(int result, String decription) {
        this.result = result;
        this.decription = decription;
    }

    public String getDecription() {
        return decription;
    }

    public int getResult() {
        return result;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
