/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mat.umk.zawodyweb.checker.tests;

import org.junit.Test;
import pl.umk.mat.zawodyweb.checker.classes.NormalDiff;
import static org.junit.Assert.*;

/**
 *
 * @author Właściciel
 */
public class NormalDiffTest {

    public NormalDiffTest() {
    }

    @Test
    public void exactTest() {
        int expected = 0;
        String codeText = "15\n";
        String rightText = "15\n";
        NormalDiff nd = new NormalDiff();
        assertEquals(expected, nd.pubDiff(codeText, rightText));
    }

    @Test
    public void codeLongerTest() {
        int expected = 0;
        String codeText = "\n      15\n     \n";
        String rightText = "15\n";
        NormalDiff nd = new NormalDiff();
        assertEquals(expected, nd.pubDiff(codeText, rightText));
    }

    @Test
    public void rightLongerTest() {
        int expected = 0;
        String codeText = "15\n";
        String rightText = "\n 15\n\n";
        NormalDiff nd = new NormalDiff();
        assertEquals(expected, nd.pubDiff(codeText, rightText));
    }

    @Test
    public void whitespaceTest() {
        int expected = 0;
        String codeText = "15 13\n";
        String rightText = "\n 15\n13\n";
        NormalDiff nd = new NormalDiff();
        assertEquals(expected, nd.pubDiff(codeText, rightText));
    }

    @Test
    public void differentTest1() {
        int expected = 1;
        String codeText = "15 12\n";
        String rightText = "\n 15\n13\n";
        NormalDiff nd = new NormalDiff();
        assertEquals(expected, nd.pubDiff(codeText, rightText));
    }

    @Test
    public void differentTest2() {
        int expected = 1;
        String codeText = "1512\n";
        String rightText = "\n 15\n13\n";
        NormalDiff nd = new NormalDiff();
        assertEquals(expected, nd.pubDiff(codeText, rightText));
    }

    @Test
    public void differentTest3() {
        int expected = 1;
        String codeText = "151 2\n";
        String rightText = "\n 15\n13\n";
        NormalDiff nd = new NormalDiff();
        assertEquals(expected, nd.pubDiff(codeText, rightText));
    }

    @Test
    public void standardDifferentTest3() {
        int expected = 1;
        String codeText = "15 12\n";
        String rightText = "15 13\n";
        NormalDiff nd = new NormalDiff();
        assertEquals(expected, nd.pubDiff(codeText, rightText));
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}