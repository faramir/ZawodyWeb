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
import pl.umk.mat.zawodyweb.database.pojo.Contests;
import pl.umk.mat.zawodyweb.database.pojo.Languages;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.xml.Contest;
import pl.umk.mat.zawodyweb.database.xml.Serie;

/**
 *
 * @author faramir
 */
public class ZipContest {
    
    static Contest addContest(ZipOutputStream out, Contest xmlContest, Contests contest) throws IOException {
        xmlContest.setName(contest.getName());
        xmlContest.setType(contest.getType());
        xmlContest.setStartdate(ZipFile.convert(contest.getStartdate()));
        xmlContest.setAbout(contest.getAbout());
        xmlContest.setRules(contest.getRules());
        xmlContest.setTech(contest.getTech());
        xmlContest.setEmail(contest.getEmail());
        xmlContest.setRefreshrate(contest.getRankingrefreshrate());
        xmlContest.setSubtype(contest.getSubtype());
        xmlContest.setSubtypename(contest.getSubtypename());
        xmlContest.setVisible(contest.getVisibility());
        xmlContest.setSeries(new Contest.Series());
        
        for (Series serie : contest.getSeriess()) {
            Serie xmlSerie = new Serie();
            ZipSerie.addSerie(out, xmlSerie, serie);
            xmlContest.getSeries().getSeries().add(xmlSerie);
        }
        
        return xmlContest;
    }
    
    static Contests getContest(ZipInputStream in, Contest xmlContest,
            List<Languages> languages, List<Classes> diffClasses)
            throws UnsupportedEncodingException, FileNotFoundException {
        Contests contest = new Contests();
        
        contest.setName(xmlContest.getName());
        contest.setType(xmlContest.getType());
        contest.setStartdate(ZipFile.convert(xmlContest.getStartdate()));
        contest.setAbout(xmlContest.getAbout());
        contest.setRules(xmlContest.getRules());
        contest.setTech(xmlContest.getTech());
        contest.setEmail(xmlContest.getEmail());
        contest.setRankingrefreshrate(xmlContest.getRefreshrate());
        contest.setSubtype(xmlContest.getSubtype());
        contest.setSubtypename(xmlContest.getSubtypename());
        contest.setVisibility(xmlContest.getVisible());
        
        if (xmlContest.getSeries().getSeries() != null) {
            List<Series> series = new ArrayList<Series>();
            
            for (Serie serie : xmlContest.getSeries().getSeries()) {
                series.add(ZipSerie.getSerie(in, serie, languages, diffClasses));
            }
            
            contest.setSeriess(series);
        }
        
        return contest;
    }
}
