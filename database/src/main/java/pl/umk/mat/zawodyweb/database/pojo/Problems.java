package pl.umk.mat.zawodyweb.database.pojo;

import java.util.List;
import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.GenerationType;
import javax.persistence.OneToOne;

/**
 * <p>Pojo mapping TABLE public.problems</p>
 *
 * <p>Generated at Sun Mar 08 19:45:31 CET 2009</p>
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
	 * Attribute pdf.
	 */
	private byte[] pdf;
	
	/**
	 * Attribute abbrev.
	 */
	private String abbrev;
	
	/**
	 * Attribute memlimit.
	 */
	private Integer memlimit;
	
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

	/**
	 * @return id
	 */
	@Basic
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	@Basic
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
	 * @return pdf
	 */
    @Basic
    @Column(name = "pdf")
		public byte[] getPdf() {
		return pdf;
	}

	/**
	 * @param pdf new value for pdf 
	 */
	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}

    	/**
	 * @return pdf
	 */
    @ManyToOne(fetch=FetchType.LAZY)
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
	 * Get the list of LanguagesProblems
	 */
	 // languagesProblemsPK
	 @OneToMany(mappedBy="problems")
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
	 @OneToMany(mappedBy="problems")
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
	 @OneToMany(mappedBy="problems")
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
	 @OneToMany(mappedBy="problems")
	 public List<Tests> getTestss() {
	 	return  this.testss;
	 }

	/**
	 * Set the list of Submits
	 */
	 public void setTestss(List<Tests> testss) {
	 	this.testss = testss;
	 }

}