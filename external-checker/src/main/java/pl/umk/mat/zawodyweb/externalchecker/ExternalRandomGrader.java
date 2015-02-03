/*
 * Copyright (c) 2015, Marek Nowicki
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.externalchecker;

import java.util.Random;
import pl.umk.mat.zawodyweb.database.CheckerErrors;
import pl.umk.mat.zawodyweb.database.SubmitsResultEnum;
import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.pojo.Submits;

/**
 *
 * @author faramir
 */
public class ExternalRandomGrader implements ExternalInterface {

    final private static Random random = new Random();

    @Override
    public String getPrefix() {
        return "random:";
    }

    @Override
    public Submits check(Submits submit) {
        for (Results r : submit.getResultss()) {
            switch (random.nextInt(8)) {
                case 0:
                    r.setSubmitResult(CheckerErrors.RE);
                    r.setPoints(0);
                    break;
                case 1:
                    r.setSubmitResult(CheckerErrors.TLE);
                    r.setPoints(0);
                    break;
                case 2:
                case 3:
                case 4:
                    r.setSubmitResult(CheckerErrors.ACC);
                    r.setPoints(r.getTests().getMaxpoints());
                    break;
                case 5:
                case 6:
                case 7:
                    r.setSubmitResult(CheckerErrors.WA);
                    r.setPoints(random.nextInt(r.getTests().getMaxpoints()));
                    break;
            }

            r.setSubmitResult(CheckerErrors.ACC);
        }
        return submit;
    }

}
