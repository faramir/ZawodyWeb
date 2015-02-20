/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import pl.umk.mat.zawodyweb.database.pojo.Classes;

/**
 *
 * @author faramir
 */
public class ExternalLoadedClass {

    private final ExternalInterface external;
    private final Classes classes;

    public ExternalLoadedClass(ExternalInterface external, Classes classes) {
        this.external = external;
        this.classes = classes;
    }

    public ExternalInterface getExternal() {
        return external;
    }

    public Classes getClasses() {
        return classes;
    }
}
