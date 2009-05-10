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
	@Basic
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

    	@ManyToOne
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
	 @OneToMany(mappedBy="submits")
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