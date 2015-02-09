/*
 * Copyright (c) 2015, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.externalchecker.classes;

import java.util.Random;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.externalchecker.ExternalInterface;

/**
 *
 * @author faramir
 */
public class ExternalRandomGrader implements ExternalInterface {

    final private static Random random = new Random();

    @Override
    public String getPrefix() {
        return "random";
    }

    @Override
    public Submits check(Submits submit) {
        System.out.println("ExternalRandomGrader: " + submit.getId());
        for (Results r : submit.getResultss()) {
            System.out.println("\tr: " + r.getId());
            switch (random.nextInt(10)) {
                case 0:
                    r.setNotes(null);
                    r.setStatus(ResultsStatusEnum.RE.getCode());
                    r.setPoints(0);
                    break;
                case 1:
                    r.setNotes(null);
                    r.setStatus(ResultsStatusEnum.TLE.getCode());
                    r.setPoints(0);
                    break;
                case 2:
                    r.setNotes(null);
                    r.setStatus(ResultsStatusEnum.ACC.getCode());
                    r.setPoints(r.getTests().getMaxpoints());
                    break;
                case 3:
                    r.setNotes(null);
                    r.setStatus(ResultsStatusEnum.WA.getCode());
                    r.setPoints(random.nextInt(r.getTests().getMaxpoints()));
                    break;
                default:
                    return null;
            }
        }
        return submit;
    }

}
