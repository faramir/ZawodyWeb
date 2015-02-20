/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.mat.umk.zawodyweb.judge.tests;

import java.util.Properties;
import pl.umk.mat.zawodyweb.commons.TestInput;
import pl.umk.mat.zawodyweb.commons.TestOutput;
import pl.umk.mat.zawodyweb.commons.CompilerInterface;
import pl.umk.mat.zawodyweb.compiler.classes.*;

/**
 *
 * @author faramir
 */
public class LMainTest {

    public static void main(String[] args) {
        CompilerInterface l = new LanguagePython();
        Properties properties = new Properties();
        properties.setProperty("uva.login", "spamz");
        properties.setProperty("uva.password", "spamz2");
        properties.setProperty("uva.max_time", "1000000");
        properties.setProperty("CODEFILE_EXTENSION", "java");
        properties.setProperty("COMPILED_DIR", "C:\\Users\\faramir\\Desktop");
        properties.setProperty("CODE_FILENAME", "MyFile.java");

        l.setProperties(properties);
        String script = l.compile(("#!/usr/bin/python\n"
                + "# -*- coding: utf-8 -*-\n"
                + "#\n"
                + "# end.\n"
                + "public class MyFile {\n"
                + "  public static void main(String[] args) {\n"
                + "#komentarz\n"
                + "    System.out.println(\"4\\n1 2 3 4\");\n"
                + "  }\n"
                + "}\n").getBytes());

        TestOutput o = l.runTest(script, new TestInput("11031", 1, 0, 0, null));

        System.out.println("text:" + o.getOutputText());
        System.out.println("desc:" + o.getNotes());
        System.out.println("pnts:" + o.getPoints());
        System.out.println("rtme:" + o.getRuntime());
        System.out.println("rslt:" + o.getStatus());
    }
}
