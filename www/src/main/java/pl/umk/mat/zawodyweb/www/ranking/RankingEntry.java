/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.www.ranking;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> table;

    RankingEntry() {
        this(-1, "", "", "", false, new ArrayList<>());
    }

    public RankingEntry(int place, String name, String surname, String login, boolean loginOnly, List<String> table) {
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
    public List<String> getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(List<String> table) {
        this.table = table;
    }
}
