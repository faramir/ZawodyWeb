/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www.zip;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import pl.umk.mat.zawodyweb.database.pojo.Classes;
import pl.umk.mat.zawodyweb.database.pojo.Languages;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.xml.ProblemType;
import pl.umk.mat.zawodyweb.database.xml.SerieType;

/**
 *
 * @author faramir
 */
public class ZipSerie {

    static SerieType addSerie(ZipOutputStream out, SerieType xmlSerie, Series serie) throws IOException {
        xmlSerie.setName(serie.getName());
        xmlSerie.setStartdate(ZipFile.convert(serie.getStartdate()));
        xmlSerie.setEnddate(ZipFile.convert(serie.getEnddate()));
        xmlSerie.setFreezedate(ZipFile.convert(serie.getFreezedate()));
        xmlSerie.setUnfreezedate(ZipFile.convert(serie.getUnfreezedate()));
        xmlSerie.setPenaltytime(serie.getPenaltytime());
        xmlSerie.setHiddenblocked(serie.getHiddenblocked());
        xmlSerie.setOpenips(serie.getOpenips());
        xmlSerie.setVisible(serie.getVisibleinranking());
        xmlSerie.setProblems(new SerieType.Problems());
        
        for (Problems problem : serie.getProblemss()) {
            ProblemType xmlProblem = new ProblemType();
            ZipProblem.addProblem(out, xmlProblem, problem);
            xmlSerie.getProblems().getProblem().add(xmlProblem);
        }
        
        return xmlSerie;
    }
    
    static Series getSerie(ZipInputStream in, SerieType xmlSerie,
            List<Languages> languages, List<Classes> diffClasses)
            throws UnsupportedEncodingException, FileNotFoundException {
        Series serie = new Series();
        serie.setName(xmlSerie.getName());
        serie.setStartdate(ZipFile.convert(xmlSerie.getStartdate()));
        serie.setEnddate(ZipFile.convert(xmlSerie.getEnddate()));
        serie.setFreezedate(ZipFile.convert(xmlSerie.getFreezedate()));
        serie.setUnfreezedate(ZipFile.convert(xmlSerie.getUnfreezedate()));
        serie.setPenaltytime(xmlSerie.getPenaltytime());
        serie.setHiddenblocked(xmlSerie.isHiddenblocked());
        serie.setOpenips(xmlSerie.getOpenips());
        serie.setVisibleinranking(xmlSerie.isVisible());
        
        if (xmlSerie.getProblems().getProblem() != null) {
            List<Problems> problems = new ArrayList<Problems>();
            
            for (ProblemType problem : xmlSerie.getProblems().getProblem()) {
                problems.add(ZipProblem.getProblem(in, problem, languages, diffClasses));
            }
            
            serie.setProblemss(problems);
        }
        
        return serie;
    }
}
