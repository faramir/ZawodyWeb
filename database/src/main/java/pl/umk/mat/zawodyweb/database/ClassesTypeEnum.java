/*
 * Copyright (c) 2015, Marek Nowicki
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
public enum ClassesTypeEnum {

    LANGUAGE(1),
    DIFF(2),
    EXTERNAL(3);
    private final int code;

    private ClassesTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ClassesTypeEnum getByCode(int code) {
        for (ClassesTypeEnum type : ClassesTypeEnum.values()) {
            if (code == type.getCode()) {
                return type;
            }
        }
        return null;
    }
}
