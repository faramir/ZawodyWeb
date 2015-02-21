/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import java.util.logging.Level;
import java.util.logging.Logger;
import pl.umk.mat.zawodyweb.database.pojo.Classes;

/**
 *
 * @author faramir
 */
public class ExternalLoadedClass {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ExternalLoadedClass.class);
    private static final BinaryClassLoader classLoader = new BinaryClassLoader();
    private final String externalPrefix;
    private final Classes classes;

    public ExternalLoadedClass(Classes classes) {
        this.classes = classes;
        try {
            ExternalInterface external = newExternal0();
            externalPrefix = external.getPrefix();
        } catch (ClassCastException | InstantiationException | IllegalAccessException ex) {
            logger.fatal("Unable to create new external instance", ex);
            throw new ClassCastException("Unable to create new external instance");
        }
    }

    public String getExternalPrefix() {
        return externalPrefix;
    }

    public Integer getVersion() {
        return classes.getVersion();
    }

    public ExternalInterface newExternal() {
        try {
            return newExternal0();
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.fatal("Unable to create new external instance", ex);
        }
        return null;
    }

    private ExternalInterface newExternal0() throws InstantiationException, IllegalAccessException {
        Class<ExternalInterface> externalClass = (Class<ExternalInterface>) classLoader.loadCompiledClass(classes.getFilename(), classes.getCode());
        return externalClass.newInstance();
    }
}
