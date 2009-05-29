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
     * OK, this means program exit with exitStatus = 0
     */
    public static final int OK = 0;
    /**
     * Rule Violation. Code did not comply to contest rules.
     */
    public static final int RV = 1;
    /**
     * Compile Error. Code did not compile.
     */
    public static final int CE = 2;
    /**
     * Compile Time Limit Exceeded. Code had been compiling for too long.
     */
    public static final int CTLE = 3;
    /**
     * Time Limit Exceeded. Program did not return result in a specified time.
     */
    public static final int TLE = 4;
    /**
     * Memory Limit Exceeded. Process used to much memory.
     */
    public static final int MLE = 5;
    /**
     * Runtime Error. Error occured during the process runtime.
     */
    public static final int RE = 6;
    /**
     * What exactly happened is stated in result description.
     */
    public static final int UNKNOWN = 7;
}
