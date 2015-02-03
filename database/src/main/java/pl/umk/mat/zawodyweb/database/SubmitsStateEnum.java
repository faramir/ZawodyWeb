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
 * @author faramir
 */
public enum SubmitsStateEnum {

    WAIT(0),
    PROCESS(1),
    DONE(2),
    MANUAL(3),
    EXTERNAL(4);
    private final int code;

    private SubmitsStateEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static SubmitsStateEnum getByCode(int code) {
        for (SubmitsStateEnum type : SubmitsStateEnum.values()) {
            if (code == type.getCode()) {
                return type;
            }
        }
        return null;
    }
}
