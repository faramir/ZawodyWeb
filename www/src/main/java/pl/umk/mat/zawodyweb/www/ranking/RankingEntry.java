package pl.umk.mat.zawodyweb.www.ranking;

import java.util.ArrayList;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$
 * Date: $Date$
 */
public class RankingEntry {

    private int place;
    private String username;
    private ArrayList<String> table;

    RankingEntry() {
        place = -1;
        username = "";
        table = new ArrayList<String>();
    }

    public RankingEntry(int place, String username, ArrayList<String> table) {
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
    public ArrayList<String> getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(ArrayList<String> table) {
        this.table = table;
    }
}
