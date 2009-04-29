/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.checker;

/**
 *
 * @author lukash2k
 */
public class CheckerResult {

    private int result;
    private String decription;

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
