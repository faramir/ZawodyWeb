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
 * <p>Pojo mapping TABLE public.questions</p>
 *
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "questions", schema = "public")
@SuppressWarnings("serial")
public class Questions implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute question.
	 */
	private String question;
	
	/**
	 * Attribute answer.
	 */
	private String answer;
	
	/**
	 * Attribute visibility.
	 */
	private Integer visibility;
	
	/**
	 * Attribute users
	 */
	 private Users users;	

	/**
	 * Attribute contests
	 */
	 private Contests contests;	

	/**
	 * Attribute tasks
	 */
	 private Tasks tasks;	

	
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
	 * @return question
	 */
	@Basic
	@Column(name = "question", length = 2147483647)
		public String getQuestion() {
		return question;
	}

	/**
	 * @param question new value for question 
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	
	/**
	 * @return answer
	 */
	@Basic
	@Column(name = "answer", length = 2147483647)
		public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer new value for answer 
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
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
	 * get users
	 */
	@ManyToOne
	@JoinColumn(name = "userid")
	public Users getUsers() {
		return this.users;
	}
	
	/**
	 * set users
	 */
	public void setUsers(Users users) {
		this.users = users;
	}

	/**
	 * get contests
	 */
	@ManyToOne
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
	 * get tasks
	 */
	@ManyToOne
	@JoinColumn(name = "taksid")
	public Tasks getTasks() {
		return this.tasks;
	}
	
	/**
	 * set tasks
	 */
	public void setTasks(Tasks tasks) {
		this.tasks = tasks;
	}



}