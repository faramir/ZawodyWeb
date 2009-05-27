/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.checker;

/**
 *
 * @author lukash2k
 */
public class TestInput {

    private String text;
    private int maxPoints;
    private int timeLimit;
    private int memoryLimit;

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public TestInput(String text, int maxPoints, int timeLimit, int memoryLimit) {
        this.text = text;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.maxPoints = maxPoints;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
