/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.commons;

/**
 *
 * @author lukash2k
 */
public class ClassInfo {

    private int version;
    private byte[] code;
    private int id;
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public void setCode(byte[] code) {
        this.code = code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public byte[] getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public ClassInfo(int id, String filename, byte[] code, int version) {
        this.version = version;
        this.code = code;
        this.id = id;
        this.filename = filename;
    }




}
