/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import pl.umk.mat.zawodyweb.database.xml.Problem;
import pl.umk.mat.zawodyweb.database.xml.Serie;

/**
 *
 * @author faramir
 */
public class ZipSerie {
    
    static Serie addSerie(ZipOutputStream out, Serie xmlSerie, Series serie) throws IOException {
        xmlSerie.setName(serie.getName());
        xmlSerie.setStartdate(ZipFile.convert(serie.getStartdate()));
        xmlSerie.setEnddate(ZipFile.convert(serie.getEnddate()));
        xmlSerie.setFreezedate(ZipFile.convert(serie.getFreezedate()));
        xmlSerie.setUnfreezedate(ZipFile.convert(serie.getUnfreezedate()));
        xmlSerie.setPenaltytime(serie.getPenaltytime());
        xmlSerie.setHiddenblocked(serie.getHiddenblocked());
        xmlSerie.setOpenips(serie.getOpenips());
        xmlSerie.setVisible(serie.getVisibleinranking());
        
        for (Problems problem : serie.getProblemss()) {
            Problem xmlProblem = new Problem();
            ZipProblem.addProblem(out, xmlProblem, problem);
            xmlSerie.getProblems().getProblems().add(xmlProblem);
        }
        
        return xmlSerie;
    }
    
    static Series getSerie(ZipInputStream in, Serie xmlSerie,
            List<Languages> languages, List<Classes> diffClasses)
            throws UnsupportedEncodingException, FileNotFoundException {
        Series serie = new Series();
        serie.setName(xmlSerie.getName());
        serie.setStartdate(ZipFile.convert(xmlSerie.getStartdate()));
        serie.setEnddate(ZipFile.convert(xmlSerie.getEnddate()));
        serie.setFreezedate(ZipFile.convert(xmlSerie.getFreezedate()));
        serie.setUnfreezedate(ZipFile.convert(xmlSerie.getUnfreezedate()));
        serie.setPenaltytime(xmlSerie.getPenaltytime());
        serie.setHiddenblocked(xmlSerie.getHiddenblocked());
        serie.setOpenips(xmlSerie.getOpenips());
        serie.setVisibleinranking(xmlSerie.getVisible());
        
        if (xmlSerie.getProblems().getProblems() != null) {
            List<Problems> problems = new ArrayList<Problems>();
            
            for (Problem problem : xmlSerie.getProblems().getProblems()) {
                problems.add(ZipProblem.getProblem(in, problem, languages, diffClasses));
            }
            
            serie.setProblemss(problems);
        }
        
        return serie;
    }
}
