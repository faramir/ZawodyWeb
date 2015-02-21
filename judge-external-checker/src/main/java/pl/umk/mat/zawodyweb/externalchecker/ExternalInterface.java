/*
 * Copyright (c) 2015, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import java.util.Properties;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;

/**
 *
 * @author faramir
 */
public interface ExternalInterface {

    public void setProperties(Properties properties);

    /**
     * @return string containing getPrefix from Submit description to
     * distinguish external checkers
     */
    public String getPrefix();

    /**
     * Checks if external checker has finished processing file, and retrieve
     * output
     *
     * @return as a result returns null if checker has not finished yet, or
     * Submit with filled Results list to save into database
     */
    public TestOutput check(String externalId, TestInput testInput, TestOutput testOutput);
}
