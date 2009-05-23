package pl.umk.mat.zawodyweb.database.pojo;

import java.util.List;
import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Basic;
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
import javax.persistence.Lob;

/**
 * <p>Pojo mapping TABLE public.classes</p>
 *
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "classes", schema = "public")
@SuppressWarnings("serial")
public class Classes implements Serializable {

    /**
     * Attribute id.
     */
    private Integer id;
    /**
     * Attribute filename.
     */
    private String filename;
    /**
     * Attribute version.
     */
    private Integer version;
    /**
     * Attribute description.
     */
    private String description;
    /**
     * Attribute code.
     */
    private byte[] code;
    /**
     * List of Languages
     */
    private List<Languages> languagess = null;
    /**
     * List of Problems
     */
    private List<Problems> problemss = null;

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

    /* liste transiente */
    /**
     * @return version
     */
    @Basic
    @Column(name = "version")
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version new value for version
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /* liste transiente */
    /**
     * @return description
     */
    @Basic
    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    /**
     * @param description new value for description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /* liste transiente */
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
     * Get the list of Languages
     */
    // languagesPK
    @OneToMany(mappedBy = "classes")
    public List<Languages> getLanguagess() {
        return this.languagess;
    }

    /**
     * Set the list of Languages
     */
    public void setLanguagess(List<Languages> languagess) {
        this.languagess = languagess;
    }

    /**
     * Get the list of Problems
     */
    // problemsPK
    @OneToMany(mappedBy = "classes")
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