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
 * <p>Pojo mapping TABLE public.languages_tasks</p>
 *
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "languages_tasks", schema = "public")
@SuppressWarnings("serial")
public class LanguagesTasks implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute tasks
	 */
	 private Tasks tasks;	

	/**
	 * Attribute languages
	 */
	 private Languages languages;	

	
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
	 * get tasks
	 */
	@ManyToOne
	@JoinColumn(name = "tasksid")
	public Tasks getTasks() {
		return this.tasks;
	}
	
	/**
	 * set tasks
	 */
	public void setTasks(Tasks tasks) {
		this.tasks = tasks;
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