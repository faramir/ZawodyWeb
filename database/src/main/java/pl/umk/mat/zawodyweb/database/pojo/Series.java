/*
 * Copyright (c) 2009-2015, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.pojo;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.OrderBy;

/**
 * <p>Pojo mapping TABLE public.series</p>
 *
 * <p>Generated at Fri May 08 19:00:58 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3
 *
 */
@Entity
@Table(name = "series", schema = "public")
@SuppressWarnings("serial")
public class Series implements Serializable {

    /**
     * Attribute id.
     */
    private Integer id;
    /**
     * Attribute name.
     */
    private String name;
    /**
     * Attribute startdate.
     */
    private Date startdate;
    /**
     * Attribute enddate.
     */
    private Date enddate;
    /**
     * Attribute freezedate.
     */
    private Date freezedate;
    /**
     * Attribute unfreezedate.
     */
    private Date unfreezedate;
    /**
     * Attribute penaltytime.
     */
    private Integer penaltytime;
    /**
     * Attribute contests
     */
    private Contests contests;
    /**
     * Attribute visibleinranking
     */
    private Boolean visibleinranking = true;
    /**
     * Attribute visibleinranking
     */
    private String openips;
    /**
     * Attribute visibleinranking
     */
    private Boolean hiddenblocked = false;
    /**
     * List of Problems
     */
    private List<Problems> problemss = null;

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

    /**
     * @return name
     */
    @Basic
    @Column(name = "name", length = 40)
    public String getName() {
        return name;
    }

    /**
     * @param name new value for name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return startdate
     */
    @Basic
    @Column(name = "startdate")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getStartdate() {
        return startdate;
    }

    /**
     * @param startdate new value for startdate
     */
    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    /**
     * @return enddate
     */
    @Basic
    @Column(name = "enddate")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getEnddate() {
        return enddate;
    }

    /**
     * @param enddate new value for enddate
     */
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    /**
     * @return freezedate
     */
    @Basic
    @Column(name = "freezedate")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFreezedate() {
        return freezedate;
    }

    /**
     * @param freezedate new value for freezedate
     */
    public void setFreezedate(Date freezedate) {
        this.freezedate = freezedate;
    }

    /**
     * @return unfreezedate
     */
    @Basic
    @Column(name = "unfreezedate")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getUnfreezedate() {
        return unfreezedate;
    }

    /**
     * @param unfreezedate new value for unfreezedate
     */
    public void setUnfreezedate(Date unfreezedate) {
        this.unfreezedate = unfreezedate;
    }

    /**
     * @return penaltytime
     */
    @Basic
    @Column(name = "penaltytime")
    public Integer getPenaltytime() {
        return penaltytime;
    }

    /**
     * @param penaltytime new value for penaltytime
     */
    public void setPenaltytime(Integer penaltytime) {
        this.penaltytime = penaltytime;
    }

    /**
     * @return visibleInRanking
     */
    @Basic
    @Column(name = "visibleinranking")
    public Boolean getVisibleinranking() {
        return visibleinranking;
    }

    /**
     * @param name new value for name
     */
    public void setVisibleinranking(Boolean visibleinranking) {
        this.visibleinranking = visibleinranking;
    }

    /**
     * @return visibleInRanking
     */
    @Basic
    @Column(name = "openips", length = 2147483647)
    public String getOpenips() {
        return openips;
    }

    /**
     * @param name new value for name
     */
    public void setOpenips(String openips) {
        this.openips = openips;
    }

    /**
     * @return visibleInRanking
     */
    @Basic
    @Column(name = "hiddenblocked")
    public Boolean getHiddenblocked() {
        return hiddenblocked;
    }

    /**
     * @param name new value for name
     */
    public void setHiddenblocked(Boolean hiddenblocked) {
        this.hiddenblocked = hiddenblocked;
    }

    /**
     * get contests
     */
    @ManyToOne(targetEntity = Contests.class)
    @JoinColumn(name = "contestsid")
    public Contests getContests() {
        return this.contests;
    }

    /**
     * set contests
     */
    public void setContests(Contests contests) {
        this.contests = contests;
    }

    /**
     * Get the list of Problems
     */
    // problemsPK
    @OneToMany(mappedBy = "series", cascade = CascadeType.REMOVE)
    @OrderBy(clause = "abbrev")
    public List<Problems> getProblemss() {
        return this.problemss;
    }

    /**
     * Set the list of Problems
     */
    public void setProblemss(List<Problems> problemss) {
        this.problemss = problemss;
    }
}
