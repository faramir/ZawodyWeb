package pl.umk.mat.zawodyweb.database.pojo;

import java.util.List;
import java.io.Serializable;
import java.sql.Timestamp;

import java.util.Collections;
import java.util.Comparator;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Embeddable;
import javax.persistence.GenerationType;
import org.hibernate.annotations.OrderBy;

/**
 * <p>Pojo mapping TABLE public.submits</p>
 *
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "submits", schema = "public")
@SuppressWarnings("serial")
public class Submits implements Serializable {

    /**
     * Attribute id.
     */
    private Integer id;
    /**
     * Attribute sdate.
     */
    private Timestamp sdate;
    /**
     * Attribute result.
     */
    private Integer result;
    /**
     * Attribute code.
     */
    private byte[] code;
    /**
     * Attribute filename.
     */
    private String filename;
    /**
     * Attribute notes.
     */
    private String notes;
    private String clientip;
    private Boolean visibleinranking = true;
    /**
     * Attribute problems
     */
    private Problems problems;
    /**
     * Attribute languages
     */
    private Languages languages;
    /**
     * Attribute languages
     */
    private Users users;
    /**
     * List of Results
     */
    private List<Results> resultss = null;

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
     * @return sdate
     */
    @Basic
    @Column(name = "sdate")
    public Timestamp getSdate() {
        return sdate;
    }

    /**
     * @param sdate new value for sdate
     */
    public void setSdate(Timestamp sdate) {
        this.sdate = sdate;
    }

    /**
     * @return result
     */
    @Basic
    @Column(name = "result")
    public Integer getResult() {
        return result;
    }

    /**
     * @param result new value for result
     */
    public void setResult(Integer result) {
        this.result = result;
    }

    /**
     * @return code
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "code")
    public byte[] getCode() {
        return code;
    }

    /**
     * @param code new value for code
     */
    public void setCode(byte[] code) {
        this.code = code;
    }

    /**
     * @return filename
     */
    @Basic
    @Column(name = "filename", length = 255)
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename new value for filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return notes
     */
    @Basic
    @Column(name = "notes", length = 2147483647)
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes new value for notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return clientIp
     */
    @Basic
    @Column(name = "clientip", length = 16)
    public String getClientip() {
        return clientip;
    }

    /**
     * @param notes new value for notes
     */
    public void setClientip(String clientip) {
        this.clientip = clientip;
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

    /**
     * get languages
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "languagesid")
    public Languages getLanguages() {
        return this.languages;
    }

    /**
     * set languages
     */
    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usersid")
    public Users getUsers() {
        return this.users;
    }

    /**
     * set languages
     */
    public void setUsers(Users users) {
        this.users = users;
    }

    /**
     * Get the list of Results
     */
    // resultsPK
    @OneToMany(mappedBy = "submits", cascade = CascadeType.REMOVE)
    @OrderBy(clause = "id asc")
    public List<Results> getResultss() {
        return this.resultss;
    }

    /**
     * Set the list of Results
     */
    public void setResultss(List<Results> resultss) {
        this.resultss = resultss;
    }
}
