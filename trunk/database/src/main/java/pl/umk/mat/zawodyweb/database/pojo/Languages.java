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

/**
 * <p>Pojo mapping TABLE public.languages</p>
 *
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "languages", schema = "public")
@SuppressWarnings("serial")
public class Languages implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute name.
	 */
	private String name;
	
	/**
	 * Attribute extension.
	 */
	private String extension;
	
	/**
	 * Attribute classes
	 */
	 private Classes classes;	

	/**
	 * List of LanguagesProblems
	 */
	private List<LanguagesProblems> languagesProblemss = null;

	/**
	 * List of Submits
	 */
	private List<Submits> submitss = null;

	
	/**
	 * @return id
	 */
	@Basic
	@Id
	@GeneratedValue
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
	 * @return extension
	 */
	@Basic
	@Column(name = "extension", length = 8)
		public String getExtension() {
		return extension;
	}

	/**
	 * @param extension new value for extension 
	 */
	public void setExtension(String extension) {
		this.extension = extension;
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
	 @OneToMany(mappedBy="languages")
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
	 * Get the list of Submits
	 */
	 // submitsPK
	 @OneToMany(mappedBy="languages")
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