/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www.zip;

import java.io.IOException;
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
}
