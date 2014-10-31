/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www.ranking;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingTable {

    private ArrayList<String> columnsCaptions;
    private ArrayList<String> columnsCSS;
    private ArrayList<RankingEntry> table;
    private Boolean frozenRanking;
    private Date generationDate;
    private long generationTime;
    private int type;

    public RankingTable() {
    }

    public RankingTable(ArrayList<String> columnsCaptions, ArrayList<String> columnsCSS, ArrayList<RankingEntry> table, Boolean frozenRanking) {
        this.columnsCaptions = columnsCaptions;
        this.columnsCSS = columnsCSS;
        this.table = table;
        this.frozenRanking = frozenRanking;
    }

    /**
     * @return the columnsCaptions
     */
    public ArrayList<String> getColumnsCaptions() {
        return columnsCaptions;
    }

    /**
     * @param columnsCaptions the columnsCaptions to set
     */
    public void setColumnsCaptions(ArrayList<String> columnsCaptions) {
        this.columnsCaptions = columnsCaptions;
    }

    /**
     * @return the columnsCSS
     */
    public ArrayList<String> getColumnsCSS() {
        return columnsCSS;
    }

    /**
     * @param columnsCSS the columnsCSS to set
     */
    public void setColumnsCSS(ArrayList<String> columnsCSS) {
        this.columnsCSS = columnsCSS;
    }

    /**
     * @return the table
     */
    public ArrayList<RankingEntry> getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(ArrayList<RankingEntry> table) {
        this.table = table;
    }

    /**
     * @return the frozenRanking
     */
    public Boolean getFrozenRanking() {
        return frozenRanking;
    }

    /**
     * @return the generationDate
     */
    public Date getGenerationDate() {
        return generationDate;
    }

    /**
     * @param generationDate the generationDate to set
     */
    public void setGenerationDate(Date generationDate) {
        this.generationDate = generationDate;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the generationTime
     */
    public long getGenerationTime() {
        return generationTime;
    }

    /**
     * @param generationTime the generationTime to set
     */
    public void setGenerationTime(long generationTime) {
        this.generationTime = generationTime;
    }

    /**
     * @return the html
     */
    public String getHtml(boolean loggedIn) {
        return RankingUtils.generateHtml(this, loggedIn);
    }
}
