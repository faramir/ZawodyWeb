/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.database;

/**
 *
 * @author faramir
 */
public enum SubmitsResultEnum {

    WAIT(0),
    PROCESS(1),
    DONE(2),
    MANUAL(3);
    private final int code;

    private SubmitsResultEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static SubmitsResultEnum getByCode(int code) {
        for (SubmitsResultEnum type : SubmitsResultEnum.values()) {
            if (code == type.getCode()) {
                return type;
            }
        }
        return null;
    }
}
