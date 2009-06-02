package pl.umk.mat.zawodyweb.www.ranking;

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

    public RankingTable() {
    }

    public RankingTable(Vector<String> columnsCaptions, Vector<String> columnsCSS, Vector<RankingEntry> table) {
        this.columnsCaptions = columnsCaptions;
        this.columnsCSS = columnsCSS;
        this.table = table;
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
}
