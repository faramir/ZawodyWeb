package pl.umk.mat.zawodyweb.www.ranking;

import java.util.Vector;
import org.apache.commons.collections.KeyValue;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingEntry {

    private int place;
    private String username;
    private Vector<String> table;

    RankingEntry() {
        place = -1;
        username = "";
        table = new Vector<String>();
    }

    public RankingEntry(int place, String username, Vector<String> table) {
        this.place = place;
        this.username = username;
        this.table = table;
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
    public String getUsername() {
        return username;
    }

    /**
     * @param name the name to set
     */
    public void setUsername(String username) {
        this.username = username;
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
