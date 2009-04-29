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
 * <p>Pojo mapping TABLE public.languages_problems</p>
 *
 * <p>Generated at Sun Mar 08 19:45:33 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "languages_problems", schema = "public")
@SuppressWarnings("serial")
public class LanguagesProblems implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute problems
	 */
	 private Problems problems;	

	/**
	 * Attribute languages
	 */
	 private Languages languages;	

	
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
	 * get problems
	 */
	@ManyToOne
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
	@ManyToOne
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



}