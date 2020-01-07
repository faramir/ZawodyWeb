/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.checker.classes;

import org.apache.log4j.Logger;
import pl.umk.mat.zawodyweb.database.ResultsStatusEnum;
import pl.umk.mat.zawodyweb.judge.commons.CheckerInterface;
import pl.umk.mat.zawodyweb.judge.commons.Program;
import pl.umk.mat.zawodyweb.judge.commons.TestInput;
import pl.umk.mat.zawodyweb.judge.commons.TestOutput;

import java.util.*;

/**
 * @author faramir
 */
public class Wwp3Diff implements CheckerInterface {
    public static final org.apache.log4j.Logger logger = Logger.getLogger(Wwp3Diff.class);
    private Properties properties;

    private static class Tuple implements Comparable<Tuple> {
        private int begin;
        private int end;
        private int value;

        public Tuple(int begin, int end, int value) {
            this.begin = begin;
            this.end = end;
            this.value = value;
        }

        public boolean timeIn(int time) {
            return begin <= time && time < end;
        }

        public int getValue() {
            return value;
        }

        @Override
        public int compareTo(Tuple o) {
            if (begin != o.begin) return begin - o.begin;
            if (end != o.end) return end - o.end;
            return value - o.value;
        }
    }

    @Override
    public TestOutput check(Program program, TestInput input, TestOutput output) {
        final int N = new Scanner(input.getInputText()).nextInt();

        TestOutput codeOutput = program.runTest(input);
        try {
            List<Tuple> theirTuples = readTuples(codeOutput.getOutputText());
            List<Tuple> ourTuples = readTuples(output.getOutputText());

            long sumOurV = 0;
            double s = 0.0;
            for (int time = 0; time < N; ++time) {
                long theirV = getSumV(time, theirTuples);
                long ourV = getSumV(time, ourTuples);

                s += Math.pow(ourV - theirV, 2);
                sumOurV += ourV;
            }
            double averageOurV = (double) sumOurV / N;

            int maxPoints = input.getMaxPoints();

            int theirM = theirTuples.size();
            int ourM = ourTuples.size();
            double varM = 1 - Math.abs(ourM - theirM) / (double) ourM;

            double points = maxPoints * varM * (averageOurV - Math.sqrt(s / N)) / averageOurV;

            if (!Double.isFinite(points) || points < 0.0) points = 0.0;


            codeOutput.setPoints((int) Math.floor(points));
            codeOutput.setStatus(ResultsStatusEnum.ACC.getCode());
            logger.trace("Processing output: s = " + s);
        } catch (Exception e) {
            codeOutput.setPoints(0);
            codeOutput.setStatus(ResultsStatusEnum.RV.getCode());
            logger.warn("Processing failed", e);
        }

        codeOutput.setOutputText(null);
        return codeOutput;
    }

    private long getSumV(int time, List<Tuple> tupleList) {
        return tupleList.stream().filter(tuple -> tuple.timeIn(time)).mapToLong(Tuple::getValue).sum();
    }

    private List<Tuple> readTuples(String output) {
        Scanner scanner = new Scanner(output);

        List<Tuple> tuples = new ArrayList<>();
        int m = scanner.nextInt();
        int mMin = getProperty("m.min", 1);
        int mMax = getProperty("m.max", 100);
        int tMax = getProperty("t.max", 100_000_000);

        if (m < mMin || m > mMax) {
            throw new IllegalArgumentException("`m` out of bounds: " + m);
        }

        for (int i = 0; i < m; ++i) {
            int a = scanner.nextInt();
            if (a < 0 || a > tMax) {
                throw new IllegalArgumentException("Incorrect value of `begin` (negative or too large): " + a);
            }

            int b = scanner.nextInt();
            if (b < a || b > tMax) {
                throw new IllegalArgumentException("Incorrect value of `end` (lower than `begin` or too large): " + b);
            }

            int v = scanner.nextInt();
            if (v <= 0) {
                throw new IllegalArgumentException("`value` is not positive: " + v);
            }

            tuples.add(new Tuple(a, b, v));
        }

        Collections.sort(tuples);

        return tuples;
    }

    private int getProperty(String propertyName, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(propertyName, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            logger.warn("Incorrect value of integer `" + propertyName + "`", e);
            return defaultValue;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
