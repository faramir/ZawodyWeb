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
import pl.umk.mat.zawodyweb.database.pojo.Contests;
import pl.umk.mat.zawodyweb.database.pojo.Languages;
import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.xml.ContestType;
import pl.umk.mat.zawodyweb.database.xml.SerieType;

/**
 *
 * @author faramir
 */
public class ZipContest {

    static ContestType addContest(ZipOutputStream out, ContestType xmlContest, Contests contest) throws IOException {
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
        xmlContest.setSeries(new ContestType.Series());
        
        for (Series serie : contest.getSeriess()) {
            SerieType xmlSerie = new SerieType();
            ZipSerie.addSerie(out, xmlSerie, serie);
            xmlContest.getSeries().getSerie().add(xmlSerie);
        }
        
        return xmlContest;
    }
    
    static Contests getContest(ZipInputStream in, ContestType xmlContest,
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
        contest.setVisibility(xmlContest.isVisible());
        
        if (xmlContest.getSeries() != null) {
            List<Series> series = new ArrayList<Series>();
            
            for (SerieType serie : xmlContest.getSeries().getSerie()) {
                series.add(ZipSerie.getSerie(in, serie, languages, diffClasses));
            }
            
            contest.setSeriess(series);
        }
        
        return contest;
    }
}
