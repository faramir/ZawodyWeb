package pl.umk.mat.zawodyweb.www.ranking;

import java.util.ArrayList;

/**
 * @author <a href="mailto:faramir@mat.umk.pl">Marek Nowicki</a>
 * @version $Rev$ Date: $Date$
 */
public class RankingEntry {

    private int place;
    private String name;
    private String surname;
    private String login;
    private boolean loginOnly;
    private ArrayList<String> table;

    RankingEntry() {
        place = -1;
        name = "";
        surname = "";
        login = "";
        loginOnly = false;
        table = new ArrayList<String>();
    }

    public RankingEntry(int place, String name, String surname, String login, boolean loginOnly, ArrayList<String> table) {
        this.place = place;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.loginOnly = loginOnly;
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
    public String getUsername(boolean loggedIn) {
        if (loggedIn || !loginOnly) {
            return String.format("%s %s (%s)", name, surname, login);
        } else {
            return String.format("%s - (%s)", name, login);
        }
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
