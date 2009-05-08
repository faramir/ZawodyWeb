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
 * <p>Pojo mapping TABLE public.results</p>
 *
 * <p>Generated at Sun Mar 08 19:45:31 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "results", schema = "public")
@SuppressWarnings("serial")
public class Results implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute points.
	 */
	private Integer points;
	
	/**
	 * Attribute runtime.
	 */
	private Integer runtime;
	
	/**
	 * Attribute memory.
	 */
	private Integer memory;
	
	/**
	 * Attribute notes.
	 */
	private String notes;
	
	/**
	 * Attribute submits
	 */
	 private Submits submits;	

	/**
	 * Attribute tests
	 */
	 private Tests tests;	

	
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
	 * @return points
	 */
	@Basic
	@Column(name = "points")
		public Integer getPoints() {
		return points;
	}

	/**
	 * @param points new value for points 
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}
	
	/**
	 * @return runtime
	 */
	@Basic
	@Column(name = "runtime")
		public Integer getRuntime() {
		return runtime;
	}

	/**
	 * @param runtime new value for runtime 
	 */
	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}
	
	/**
	 * @return memory
	 */
	@Basic
	@Column(name = "memory")
		public Integer getMemory() {
		return memory;
	}

	/**
	 * @param memory new value for memory 
	 */
	public void setMemory(Integer memory) {
		this.memory = memory;
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
	 * get submits
	 */
	@ManyToOne
	@JoinColumn(name = "submitsid")
	public Submits getSubmits() {
		return this.submits;
	}
	
	/**
	 * set submits
	 */
	public void setSubmits(Submits submits) {
		this.submits = submits;
	}

	/**
	 * get tests
	 */
	@ManyToOne
	@JoinColumn(name = "testsid")
	public Tests getTests() {
		return this.tests;
	}
	
	/**
	 * set tests
	 */
	public void setTests(Tests tests) {
		this.tests = tests;
	}



}