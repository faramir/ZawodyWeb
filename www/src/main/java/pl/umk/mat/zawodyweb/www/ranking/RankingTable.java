package pl.umk.mat.zawodyweb.www.ranking;

import java.util.Date;
import java.util.Vector;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingTable {

    private Vector<String> columnsCaptions;
    private Vector<String> columnsCSS;
    private Vector<RankingEntry> table;
    private Boolean frozenRanking;
    private Date generationDate;
    private long generationTime;
    private int type;

    public RankingTable() {
    }

    public RankingTable(Vector<String> columnsCaptions, Vector<String> columnsCSS, Vector<RankingEntry> table, Boolean frozenRanking) {
        this.columnsCaptions = columnsCaptions;
        this.columnsCSS = columnsCSS;
        this.table = table;
        this.frozenRanking = frozenRanking;
    }

    /**
     * @return the columnsCaptions
     */
    public Vector<String> getColumnsCaptions() {
        return columnsCaptions;
    }

    /**
     * @param columnsCaptions the columnsCaptions to set
     */
    public void setColumnsCaptions(Vector<String> columnsCaptions) {
        this.columnsCaptions = columnsCaptions;
    }

    /**
     * @return the columnsCSS
     */
    public Vector<String> getColumnsCSS() {
        return columnsCSS;
    }

    /**
     * @param columnsCSS the columnsCSS to set
     */
    public void setColumnsCSS(Vector<String> columnsCSS) {
        this.columnsCSS = columnsCSS;
    }

    /**
     * @return the table
     */
    public Vector<RankingEntry> getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(Vector<RankingEntry> table) {
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
}
