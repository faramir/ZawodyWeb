/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.pojo;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * <p>
 * Pojo mapping TABLE public.tests</p>
 *
 * <p>
 * Generated at Sun Mar 08 19:45:32 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3
 *
 */
@Entity
@Table(name = "tests", schema = "public")
@SuppressWarnings("serial")
public class Tests implements Serializable, Comparable<Tests> {

    private static final Pattern pattern = Pattern.compile("(\\D*)(\\d*)");

    @Override
    public int compareTo(Tests o) {
        String t1 = this.getTestorder();
        String t2 = o.getTestorder();

        if (t1 == null || t2 == null || t1.compareTo(t2) == 0) {
            return this.getId() - o.getId();
        }
        Matcher m1 = pattern.matcher(t1);
        Matcher m2 = pattern.matcher(t2);

        // The only way find() could fail is at the end of a string
        while (m1.find() && m2.find()) {
            // matcher.group(1) fetches any non-digits captured by the
            // first parentheses in PATTERN.
            int nonDigitCompare = m1.group(1).compareTo(m2.group(1));
            if (nonDigitCompare!=0) {
                return nonDigitCompare;
            }

            // matcher.group(2) fetches any digits captured by the
            // second parentheses in PATTERN.
            if (m1.group(2).isEmpty()) {
                return m2.group(2).isEmpty() ? 0 : -1;
            } else if (m2.group(2).isEmpty()) {
                return 1;
            }

            int n1 = Integer.parseInt(m1.group(2)); // Integer a nie BigInt, ze wzgledu na limit 5 znakow
            int n2 = Integer.parseInt(m2.group(2));

            if (n1 != n2) {
                return n1 < n2 ? -1 : 1;
            }
        }

        // Handle if one string is a prefix of the other.
        // Nothing comes before something.
        return m1.hitEnd() && m2.hitEnd() ? 0
                : m1.hitEnd() ? -1 : 1;
    }

    /**
     * Attribute id.
     */
    private Integer id;
    /**
     * Attribute input.
     */
    private String input;
    /**
     * Attribute output.
     */
    private String output;
    /**
     * Attribute timelimit.
     */
    private Integer timelimit;
    /**
     * Attribute maxpoints.
     */
    private Integer maxpoints;
    /**
     * Attribute visibility.
     */
    private Integer visibility;
    /**
     * Attribute visibility.
     */
    private String config;
    /**
     * Attribute problems
     */
    private Problems problems;
    /**
     * List of Results
     */
    private List<Results> resultss = null;
    private String testorder;

    /* liste transiente */
    /**
     * @return id
     */
    @Basic
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id new value for id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /* liste transiente */
    /**
     * @return input
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "input", length = 2147483647)
    public String getInput() {
        return input;
    }

    /**
     * @param input new value for input
     */
    public void setInput(String input) {
        this.input = input;
    }

    /* liste transiente */
    /**
     * @return output
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "output", length = 2147483647)
    public String getOutput() {
        return output;
    }

    /**
     * @param output new value for output
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /* liste transiente */
    /**
     * @return timelimit
     */
    @Basic
    @Column(name = "timelimit")
    public Integer getTimelimit() {
        return timelimit;
    }

    /**
     * @param timelimit new value for timelimit
     */
    public void setTimelimit(Integer timelimit) {
        this.timelimit = timelimit;
    }

    /* liste transiente */
    /**
     * @return maxpoints
     */
    @Basic
    @Column(name = "maxpoints")
    public Integer getMaxpoints() {
        return maxpoints;
    }

    /**
     * @param maxpoints new value for maxpoints
     */
    public void setMaxpoints(Integer maxpoints) {
        this.maxpoints = maxpoints;
    }

    /**
     * @return visibility
     */
    @Basic
    @Column(name = "visibility")
    public Integer getVisibility() {
        return visibility;
    }

    /**
     * @param visibility new value for visibility
     */
    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    /**
     * @deprecated only for Hibernate. Instead, use
     * loadProperties()
     * @return config
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "config", length = 2147483647)
    public String getConfig() {
        return config;
    }

    public Properties loadProperties() {
        Properties result = new Properties();
        if (config == null) {
            return new Properties();
        }
        try {
            result.load(new StringReader(config));
        } catch (IOException ex) {
            //This shouldn't happend. Not ever!
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * @deprecated only for Hibernate. Instead, use
     * saveProperities()
     * @param config new value for visibility
     */
    public void setConfig(String config) {
        this.config = config;
    }

    public void saveProperties(Properties properties) {
        if (properties == null) {
            return;
        }
        try {
            StringWriter sw = new StringWriter();
            properties.store(sw, new String());
            this.config = sw.toString();

        } catch (IOException ex) {
            //This also shouldn't ever happend.
            Logger.getLogger(Tests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* liste transiente */
    /**
     * Get the list of Results
     */
    // resultsPK
    @OneToMany(mappedBy = "tests", cascade = javax.persistence.CascadeType.ALL)
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN})
    public List<Results> getResultss() {
        return this.resultss;
    }

    /**
     * Set the list of Results
     */
    public void setResultss(List<Results> resultss) {
        this.resultss = resultss;
    }

    /**
     * get problems
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problemsid")
    public Problems getProblems() {
        return this.problems;
    }

    /**
     * set problems
     */
    public void setProblems(Problems problems) {
        this.problems = problems;
    }

    @Basic
    @Column(name = "testorder", length = 15)
    public String getTestorder() {
        return testorder;
    }

    public void setTestorder(String testorder) {
        this.testorder = testorder;
    }
}
