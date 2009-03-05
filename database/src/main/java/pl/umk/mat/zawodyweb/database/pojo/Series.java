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
 * <p>Pojo mapping TABLE public.series</p>
 *
 * <p>Generated at Thu Mar 05 04:19:37 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "series", schema = "public")
@SuppressWarnings("serial")
public class Series implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute name.
	 */
	private String name;
	
	/**
	 * Attribute startdate.
	 */
	private Timestamp startdate;
	
	/**
	 * Attribute enddate.
	 */
	private Timestamp enddate;
	
	/**
	 * Attribute freezedate.
	 */
	private Timestamp freezedate;
	
	/**
	 * Attribute unfreezedate.
	 */
	private Timestamp unfreezedate;
	
	/**
	 * Attribute penaltytime.
	 */
	private Integer penaltytime;
	
	/**
	 * Attribute contests
	 */
	 private Contests contests;	

	/**
	 * List of SeriesRoles
	 */
	private List<SeriesRoles> seriesRoless = null;

	/**
	 * List of Tasks
	 */
	private List<Tasks> taskss = null;

	
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
	 * @return startdate
	 */
	@Basic
	@Column(name = "startdate")
		public Timestamp getStartdate() {
		return startdate;
	}

	/**
	 * @param startdate new value for startdate 
	 */
	public void setStartdate(Timestamp startdate) {
		this.startdate = startdate;
	}
	
	/**
	 * @return enddate
	 */
	@Basic
	@Column(name = "enddate")
		public Timestamp getEnddate() {
		return enddate;
	}

	/**
	 * @param enddate new value for enddate 
	 */
	public void setEnddate(Timestamp enddate) {
		this.enddate = enddate;
	}
	
	/**
	 * @return freezedate
	 */
	@Basic
	@Column(name = "freezedate")
		public Timestamp getFreezedate() {
		return freezedate;
	}

	/**
	 * @param freezedate new value for freezedate 
	 */
	public void setFreezedate(Timestamp freezedate) {
		this.freezedate = freezedate;
	}
	
	/**
	 * @return unfreezedate
	 */
	@Basic
	@Column(name = "unfreezedate")
		public Timestamp getUnfreezedate() {
		return unfreezedate;
	}

	/**
	 * @param unfreezedate new value for unfreezedate 
	 */
	public void setUnfreezedate(Timestamp unfreezedate) {
		this.unfreezedate = unfreezedate;
	}
	
	/**
	 * @return penaltytime
	 */
	@Basic
	@Column(name = "penaltytime")
		public Integer getPenaltytime() {
		return penaltytime;
	}

	/**
	 * @param penaltytime new value for penaltytime 
	 */
	public void setPenaltytime(Integer penaltytime) {
		this.penaltytime = penaltytime;
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
	 * Get the list of SeriesRoles
	 */
	 // seriesRolesPK
	 @OneToMany(mappedBy="series")
	 public List<SeriesRoles> getSeriesRoless() {
	 	return this.seriesRoless;
	 }
	 
	/**
	 * Set the list of SeriesRoles
	 */
	 public void setSeriesRoless(List<SeriesRoles> seriesRoless) {
	 	this.seriesRoless = seriesRoless;
	 }
	/**
	 * Get the list of Tasks
	 */
	 // tasksPK
	 @OneToMany(mappedBy="series")
	 public List<Tasks> getTaskss() {
	 	return this.taskss;
	 }
	 
	/**
	 * Set the list of Tasks
	 */
	 public void setTaskss(List<Tasks> taskss) {
	 	this.taskss = taskss;
	 }


}