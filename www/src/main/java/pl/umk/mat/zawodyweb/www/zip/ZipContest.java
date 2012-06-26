/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.umk.mat.zawodyweb.www.zip;

import java.io.IOException;
import pl.umk.mat.zawodyweb.database.pojo.Contests;
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

        for (Series serie : contest.getSeriess()) {
            Serie xmlSerie = new Serie();
            ZipSerie.addSerie(out, xmlSerie, serie);
            xmlContest.getSeries().getSeries().add(xmlSerie);
        }

        return xmlContest;
    }
}
