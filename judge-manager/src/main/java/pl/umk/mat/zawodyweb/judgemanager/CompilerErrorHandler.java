/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.judgemanager;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author faramir
 */
public class CompilerErrorHandler {

    private final int compilerErrorTime;
    private final int compilerErrorCount;
    private final ConcurrentHashMap<Long, Integer> compilerErrorsMap;

    public CompilerErrorHandler(int compilerErrorTime, int compilerErrorCount) {
        this.compilerErrorTime = compilerErrorTime;
        this.compilerErrorCount = compilerErrorCount;
        compilerErrorsMap = new ConcurrentHashMap<Long, Integer>();
    }

    synchronized private void clearOldEntries() {
        long now = System.currentTimeMillis();
        for (Long then : compilerErrorsMap.keySet()) {
            if (now - then > compilerErrorTime) {
                compilerErrorsMap.remove(then);
            }
        }
    }

    synchronized public boolean canUseCompiler(int compiler_id) {
        clearOldEntries();

        int count = 0;
        for (Long time : compilerErrorsMap.keySet()) {
            if (compilerErrorsMap.get(time) == compiler_id) {
                ++count;
                if (count == compilerErrorCount) {
                    return false;
                }
            }
        }

        return true;
    }

    synchronized public void addCompilerError(int compiler_id) {
        clearOldEntries();

        long now;

        do {
            now = System.currentTimeMillis();
        } while (compilerErrorsMap.contains(now));

        compilerErrorsMap.put(now, compiler_id);
    }
}
