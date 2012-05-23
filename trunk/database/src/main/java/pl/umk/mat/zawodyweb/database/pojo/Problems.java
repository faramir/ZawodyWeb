package pl.umk.mat.zawodyweb.database.pojo;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.*;

/**
 * <p>Pojo mapping TABLE public.problems</p>
 *
 * <p>Generated at Sun Mar 08 19:45:31 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3
 *
 */
@Entity
@Table(name = "problems", schema = "public")
@SuppressWarnings("serial")
public class Problems implements Serializable {

    /**
     * Attribute id.
     */
    private Integer id;
    /**
     * Attribute name.
     */
    private String name;
    /**
     * Attribute text.
     */
    private String text;
    /**
     * Attribute abbrev.
     */
    private String abbrev;
    /**
     * Attribute memlimit.
     */
    private Integer memlimit;
    /**
     * Attribute codesize.
     */
    private Integer codesize;
    /**
     * Attribute series
     */
    private Series series;
    /**
     * Attribute classes
     */
    private Classes classes;
    /**
     * List of LanguagesProblems
     */
    private List<LanguagesProblems> languagesProblemss = null;
    /**
     * List of Questions
     */
    private List<Questions> questionss = null;
    /**
     * List of Submits
     */
    private List<Submits> submitss = null;
    /**
     * List of Submits
     */
    private List<Tests> testss = null;
    private PDF pdfData = null;
    private String config;
    private Boolean visibleinranking = true;
    private Boolean viewpdf = false;

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
    @Column(name = "name", length = 80)
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
     * @return text
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "text", length = 2147483647)
    public String getText() {
        return text;
    }

    /**
     * @param text new value for text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @deprecated only for Hibernate. Instead, use loadProperties()
     * @return text
     */
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "config", length = 2147483647)
    public String getConfig() {
        return this.config;
    }

    /**
     * @deprecated only for Hibernate. Instead, use saveProperities()
     * @param config
     */
    public void setConfig(String config) {
        this.config = config;
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
            Logger.getLogger(Problems.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    /**
     * @param text new value for text
     */
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
            Logger.getLogger(Problems.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveProperties(String properties) {
        this.config = properties;
    }

    /**
     * @return pdf
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "pdfid")
    public PDF getPDF() {
        return pdfData;
    }

    /**
     * @param pdf new value for pdf
     */
    public void setPDF(PDF pdf) {
        this.pdfData = pdf;
    }

    /**
     * @return abbrev
     */
    @Basic
    @Column(name = "abbrev", length = 5)
    public String getAbbrev() {
        return abbrev;
    }

    /**
     * @param abbrev new value for abbrev
     */
    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    /**
     * @return memlimit
     */
    @Basic
    @Column(name = "memlimit")
    public Integer getMemlimit() {
        return memlimit;
    }

    /**
     * @param memlimit new value for memlimit
     */
    public void setMemlimit(Integer memlimit) {
        this.memlimit = memlimit;
    }

    /**
     * @return memlimit
     */
    @Basic
    @Column(name = "codesize")
    public Integer getCodesize() {
        return codesize;
    }

    /**
     * @param memlimit new value for memlimit
     */
    public void setCodesize(Integer codesize) {
        this.codesize = codesize;
    }

    /**
     * get series
     */
    @ManyToOne
    @JoinColumn(name = "seriesid")
    public Series getSeries() {
        return this.series;
    }

    /**
     * set series
     */
    public void setSeries(Series series) {
        this.series = series;
    }

    /**
     * get classes
     */
    @ManyToOne
    @JoinColumn(name = "classesid")
    public Classes getClasses() {
        return this.classes;
    }

    /**
     * set classes
     */
    public void setClasses(Classes classes) {
        this.classes = classes;
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
     * @param visibleinranking new value for visibleinranking
     */
    public void setVisibleinranking(Boolean visibleinranking) {
        this.visibleinranking = visibleinranking;
    }

    /**
     * @return viewPDF
     */
    @Basic
    @Column(name = "viewpdf")
    public Boolean getViewpdf() {
        return viewpdf;
    }

    /**
     * @param viewpdf new value for viewpdf
     */
    public void setViewpdf(Boolean viewpdf) {
        this.viewpdf = viewpdf;
    }

    /**
     * Get the list of LanguagesProblems
     */
    // languagesProblemsPK
    @OneToMany(mappedBy = "problems", cascade = CascadeType.REMOVE)
    public List<LanguagesProblems> getLanguagesProblemss() {
        return this.languagesProblemss;
    }

    /**
     * Set the list of LanguagesProblems
     */
    public void setLanguagesProblemss(List<LanguagesProblems> languagesProblemss) {
        this.languagesProblemss = languagesProblemss;
    }

    /**
     * Get the list of Questions
     */
    // questionsPK
    @OneToMany(mappedBy = "problems", cascade = CascadeType.REMOVE)
    public List<Questions> getQuestionss() {
        return this.questionss;
    }

    /**
     * Set the list of Questions
     */
    public void setQuestionss(List<Questions> questionss) {
        this.questionss = questionss;
    }

    /**
     * Get the list of Submits
     */
    // submitsPK
    @OneToMany(mappedBy = "problems", cascade = CascadeType.REMOVE)
    public List<Submits> getSubmitss() {
        return this.submitss;
    }

    /**
     * Set the list of Submits
     */
    public void setSubmitss(List<Submits> submitss) {
        this.submitss = submitss;
    }

    /**
     * Get the list of Submits
     */
    // submitsPK
    @OneToMany(mappedBy = "problems", cascade = CascadeType.REMOVE)
    @OrderBy(value = "testorder")
    public List<Tests> getTestss() {
        return this.testss;
    }

    /**
     * Set the list of Submits
     */
    public void setTestss(List<Tests> testss) {
        this.testss = testss;
    }
}
