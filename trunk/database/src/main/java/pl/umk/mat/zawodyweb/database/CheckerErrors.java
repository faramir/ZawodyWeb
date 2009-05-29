package pl.umk.mat.zawodyweb.database;

/**
 *
 * @author lukash2k
 */
public class CheckerErrors {
    /**
     * Undefined result. This usually should not happend.
     */
    public static final int UNDEF = -1;
    /**
     * Accepted, this means (here is what Marek thinks about it)
     */
    public static final int ACC = 0;
    /**
     * Wrong answer, this means (here is what Marek thinks about it)
     */
    public static final int WA = 1;
    /**
     * Time Limit Exceeded. Program did not return result in a specified time.
     */
    public static final int TLE = 2;
    /**
     * Compile Error. Code did not compile.
     */
    public static final int CE = 3;
    /**
     * Runtime Error. Error occured during the process runtime.
     */
    public static final int RE = 4;
    /**
     * Memory Limit Exceeded. Process used to much memory.
     */
    public static final int MLE = 5;
    /**
     * Rule Violation. Code did not comply to contest rules.
     */
    public static final int RV = 6;
    /**
     * Compile Time Limit Exceeded. Code had been compiling for too long.
     */
    public static final int CTLE = 7;
    /**
     * What exactly happened is stated in result description.
     */
    public static final int UNKNOWN = 8;
}
