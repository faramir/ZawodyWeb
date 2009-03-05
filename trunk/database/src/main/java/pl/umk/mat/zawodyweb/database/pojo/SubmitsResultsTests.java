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
 * <p>Pojo mapping TABLE public.submits_results_tests</p>
 *
 * <p>Generated at Thu Mar 05 04:19:38 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "submits_results_tests", schema = "public")
@SuppressWarnings("serial")
public class SubmitsResultsTests implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute submits
	 */
	 private Submits submits;	

	/**
	 * Attribute results
	 */
	 private Results results;	

	/**
	 * Attribute tests
	 */
	 private Tests tests;	

	
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
	 * get results
	 */
	@ManyToOne
	@JoinColumn(name = "resultsid")
	public Results getResults() {
		return this.results;
	}
	
	/**
	 * set results
	 */
	public void setResults(Results results) {
		this.results = results;
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