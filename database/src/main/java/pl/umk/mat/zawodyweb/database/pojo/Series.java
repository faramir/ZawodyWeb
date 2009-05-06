package pl.umk.mat.zawodyweb.database.pojo;

import java.util.List;
import java.io.Serializable;
import java.sql.Timestamp;

import java.util.Date;
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
 * <p>Pojo mapping TABLE public.series</p>
 *
 * <p>Generated at Sun Mar 08 19:45:32 CET 2009</p>
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
	private Date startdate;
	
	/**
	 * Attribute enddate.
	 */
	private Date enddate;
	
	/**
	 * Attribute freezedate.
	 */
	private Date freezedate;
	
	/**
	 * Attribute unfreezedate.
	 */
	private Date unfreezedate;
	
	/**
	 * Attribute penaltytime.
	 */
	private Integer penaltytime;
	
	/**
	 * Attribute contests
	 */
	 private Contests contests;	

	/**
	 * List of Problems
	 */
	private List<Problems> problemss = null;

	/**
	 * List of SeriesRoles
	 */
	private List<SeriesRoles> seriesRoless = null;

	
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
		public Date getStartdate() {
		return startdate;
	}

	/**
	 * @param startdate new value for startdate 
	 */
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	
	/**
	 * @return enddate
	 */
	@Basic
	@Column(name = "enddate")
		public Date getEnddate() {
		return enddate;
	}

	/**
	 * @param enddate new value for enddate 
	 */
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
	/**
	 * @return freezedate
	 */
	@Basic
	@Column(name = "freezedate")
		public Date getFreezedate() {
		return freezedate;
	}

	/**
	 * @param freezedate new value for freezedate 
	 */
	public void setFreezedate(Date freezedate) {
		this.freezedate = freezedate;
	}
	
	/**
	 * @return unfreezedate
	 */
	@Basic
	@Column(name = "unfreezedate")
		public Date getUnfreezedate() {
		return unfreezedate;
	}

	/**
	 * @param unfreezedate new value for unfreezedate 
	 */
	public void setUnfreezedate(Date unfreezedate) {
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
	 * Get the list of Problems
	 */
	 // problemsPK
	 @OneToMany(mappedBy="series")
	 public List<Problems> getProblemss() {
	 	return this.problemss;
	 }
	 
	/**
	 * Set the list of Problems
	 */
	 public void setProblemss(List<Problems> problemss) {
	 	this.problemss = problemss;
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


}