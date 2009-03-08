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
 * <p>Pojo mapping TABLE public.tests</p>
 *
 * <p>Generated at Sun Mar 08 19:45:32 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "tests", schema = "public")
@SuppressWarnings("serial")
public class Tests implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute input.
	 */
	private String input;
	
	/**
	 * Attribute output.
	 */
	private String output;
	
	/**
	 * Attribute timelimit.
	 */
	private Integer timelimit;
	
	/**
	 * Attribute maxpoints.
	 */
	private Integer maxpoints;
	
	/**
	 * Attribute visibility.
	 */
	private Integer visibility;
	
	/**
	 * List of Results
	 */
	private List<Results> resultss = null;

	
	/* liste transiente */
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
	
	/* liste transiente */
	/**
	 * @return input
	 */
	@Basic
	@Column(name = "input", length = 2147483647)
		public String getInput() {
		return input;
	}

	/**
	 * @param input new value for input 
	 */
	public void setInput(String input) {
		this.input = input;
	}
	
	/* liste transiente */
	/**
	 * @return output
	 */
	@Basic
	@Column(name = "output", length = 2147483647)
		public String getOutput() {
		return output;
	}

	/**
	 * @param output new value for output 
	 */
	public void setOutput(String output) {
		this.output = output;
	}
	
	/* liste transiente */
	/**
	 * @return timelimit
	 */
	@Basic
	@Column(name = "timelimit")
		public Integer getTimelimit() {
		return timelimit;
	}

	/**
	 * @param timelimit new value for timelimit 
	 */
	public void setTimelimit(Integer timelimit) {
		this.timelimit = timelimit;
	}
	
	/* liste transiente */
	/**
	 * @return maxpoints
	 */
	@Basic
	@Column(name = "maxpoints")
		public Integer getMaxpoints() {
		return maxpoints;
	}

	/**
	 * @param maxpoints new value for maxpoints 
	 */
	public void setMaxpoints(Integer maxpoints) {
		this.maxpoints = maxpoints;
	}
	
	/* liste transiente */
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
	 * Get the list of Results
	 */
	 // resultsPK
	 @OneToMany(mappedBy="tests")
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