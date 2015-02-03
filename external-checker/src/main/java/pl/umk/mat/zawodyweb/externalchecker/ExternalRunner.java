/*
 * Copyright (c) 2015, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 *
 * @author faramir
 */
public class ExternalRunner implements Runnable {

    private final Submits submit;

    public ExternalRunner(Submits submit) {
        this.submit = submit;
    }
    
    @Override
    public void run() {
    }
    
}
