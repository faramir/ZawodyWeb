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
public interface ExternalInterface {

    /**
     * @return string containing getPrefix from Submit description to distinguish
 external checkers
     */
    public String getPrefix();

    /**
     * Checks if external checker has finished processing file, and retrieve
     * output
     *
     * @param submit Submit to check
     * @return as a result returns null if checker has not finished yet, or
     * Submit with filled Results list to save into database
     */
    public Submits check(Submits submit);
}
