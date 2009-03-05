package org.zawodyweb.database.pojo;

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

/**
 * <p>Pojo mapping TABLE public.tasks</p>
 *
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "tasks", schema = "public")
@SuppressWarnings("serial")
public class Tasks implements Serializable {

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
	 * List of LanguagesTasks
	 */
	private List<LanguagesTasks> languagesTaskss = null;

	/**
	 * List of Questions
	 */
	private List<Questions> questionss = null;

	/**
	 * List of Submits
	 */
	private List<Submits> submitss = null;

	
	/**
	 * @return id
	 */
	@Basic
	@Id
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
	 * Get the list of LanguagesTasks
	 */
	 // languagesTasksPK
	 @OneToMany(mappedBy="tasks")
	 public List<LanguagesTasks> getLanguagesTaskss() {
	 	return this.languagesTaskss;
	 }
	 
	/**
	 * Set the list of LanguagesTasks
	 */
	 public void setLanguagesTaskss(List<LanguagesTasks> languagesTaskss) {
	 	this.languagesTaskss = languagesTaskss;
	 }
	/**
	 * Get the list of Questions
	 */
	 // questionsPK
	 @OneToMany(mappedBy="tasks")
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
	 @OneToMany(mappedBy="tasks")
	 public List<Submits> getSubmitss() {
	 	return this.submitss;
	 }
	 
	/**
	 * Set the list of Submits
	 */
	 public void setSubmitss(List<Submits> submitss) {
	 	this.submitss = submitss;
	 }


}