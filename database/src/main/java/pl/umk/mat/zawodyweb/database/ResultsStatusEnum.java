/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database;

/**
 *
 * @author lukash2k
 */
public enum ResultsStatusEnum {

    /**
     * Undefined result. This usually should not happend.
     */
    UNDEF(-1),
    /**
     * Accepted, this means (here is what Marek thinks about it)
     */
    ACC(0),
    /**
     * Wrong answer, this means (here is what Marek thinks about it)
     */
    WA(1),
    /**
     * Time Limit Exceeded. Program did not return result in a specified time.
     */
    TLE(2),
    /**
     * Compile Error. Code did not compile.
     */
    CE(3),
    /**
     * Runtime Error. Error occured during the process runtime.
     */
    RE(4),
    /**
     * Memory Limit Exceeded. Process used to much memory.
     */
    MLE(5),
    /**
     * Rule Violation. Code did not comply to contest rules.
     */
    RV(6),
    /**
     * Compile Time Limit Exceeded. Code had been compiling for too long.
     */
    CTLE(7),
    /**
     * What exactly happened is stated in result description.
     */
    UNKNOWN(8),
    /**
     * Language is not checking solution, set only manual mode.
     */
    MANUAL(9),
    /**
     * Test will be checked externally.
     */
    EXTERNAL(10);

    private final int code;

    private ResultsStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ResultsStatusEnum getByCode(int code) {
        for (ResultsStatusEnum type : ResultsStatusEnum.values()) {
            if (code == type.getCode()) {
                return type;
            }
        }
        return null;
    }
}
