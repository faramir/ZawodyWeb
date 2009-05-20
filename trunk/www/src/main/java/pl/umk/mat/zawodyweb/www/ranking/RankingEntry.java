package pl.umk.mat.zawodyweb.www.ranking;

import java.util.Vector;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingEntry {

    private int place;
    private String name;
    private Vector<String> table;

    RankingEntry() {
        place = -1;
        name = "";
        table = new Vector<String>();
    }

    /**
     * @return the place
     */
    public int getPlace() {
        return place;
    }

    /**
     * @param place the place to set
     */
    public void setPlace(int place) {
        this.place = place;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the table
     */
    public Vector<String> getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(Vector<String> table) {
        this.table = table;
    }
}
