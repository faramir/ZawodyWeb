/*
 * Copyright (c) 2015, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import pl.umk.mat.zawodyweb.database.pojo.Classes;

/**
 * @author Marek Nowicki /faramir/
 */
public class ExternalLoadedClass {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ExternalLoadedClass.class);
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
        Class<ExternalInterface> externalClass = (Class<ExternalInterface>) new BinaryClassLoader().loadCompiledClass(classes.getFilename(), classes.getCode());
        return externalClass.newInstance();
    }
}
