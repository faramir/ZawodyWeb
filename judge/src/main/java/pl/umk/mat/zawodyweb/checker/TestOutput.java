/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.checker;

/**
 *
 * @author lukash2k
 */
public class TestOutput {

    private String text;
    /* Possible results: MLE, TLE, CE, RE */
    private int result;
    private String resultDesc;

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
