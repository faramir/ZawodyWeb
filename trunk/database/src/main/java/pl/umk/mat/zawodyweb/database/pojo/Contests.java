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
 * <p>Pojo mapping TABLE public.contests</p>
 *
 * <p>Generated at Sun Mar 08 19:45:32 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "contests", schema = "public")
@SuppressWarnings("serial")
public class Contests implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute name.
	 */
	private String name;
	
	/**
	 * Attribute type.
	 */
	private Integer type;
	
	/**
	 * Attribute startdate.
	 */
	private Date startdate;
	
	/**
	 * Attribute about.
	 */
	private String about;
	
	/**
	 * Attribute rules.
	 */
	private String rules;
	
	/**
	 * Attribute tech.
	 */
	private String tech;
	
	/**
	 * Attribute visibility.
	 */
	private Integer visibility;
	
	/**
	 * List of ContestsRoles
	 */
	private List<ContestsRoles> contestsRoless = null;

	/**
	 * List of Questions
	 */
	private List<Questions> questionss = null;

	/**
	 * List of Series
	 */
	private List<Series> seriess = null;

	
	/* liste transiente */
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
	
	/* liste transiente */
	/**
	 * @return name
	 */
	@Basic
	@Column(name = "name", length = 120)
		public String getName() {
		return name;
	}

	/**
	 * @param name new value for name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/* liste transiente */
	/**
	 * @return type
	 */
	@Basic
	@Column(name = "type")
		public Integer getType() {
		return type;
	}

	/**
	 * @param type new value for type 
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	
	/* liste transiente */
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
	
	/* liste transiente */
	/**
	 * @return about
	 */
	@Basic
	@Column(name = "about", length = 2147483647)
		public String getAbout() {
		return about;
	}

	/**
	 * @param about new value for about 
	 */
	public void setAbout(String about) {
		this.about = about;
	}
	
	/* liste transiente */
	/**
	 * @return rules
	 */
	@Basic
	@Column(name = "rules", length = 2147483647)
		public String getRules() {
		return rules;
	}

	/**
	 * @param rules new value for rules 
	 */
	public void setRules(String rules) {
		this.rules = rules;
	}
	
	/* liste transiente */
	/**
	 * @return tech
	 */
	@Basic
	@Column(name = "tech", length = 2147483647)
		public String getTech() {
		return tech;
	}

	/**
	 * @param tech new value for tech 
	 */
	public void setTech(String tech) {
		this.tech = tech;
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
	 * Get the list of ContestsRoles
	 */
	 // contestsRolesPK
	 @OneToMany(mappedBy="contests")
	 public List<ContestsRoles> getContestsRoless() {
	 	return this.contestsRoless;
	 }
	 
	/**
	 * Set the list of ContestsRoles
	 */
	 public void setContestsRoless(List<ContestsRoles> contestsRoless) {
	 	this.contestsRoless = contestsRoless;
	 }
	/**
	 * Get the list of Questions
	 */
	 // questionsPK
	 @OneToMany(mappedBy="contests")
	 public List<Questions> getQuestionss() {
	 	return this.questionss;
	 }
	 
	/**
	 * Set the list of Questions
	 */
	 public void setQuestionss(List<Questions> questionss) {
	 	this.questionss = questionss;
	 }
	/**
	 * Get the list of Series
	 */
	 // seriesPK
	 @OneToMany(mappedBy="contests")
	 public List<Series> getSeriess() {
	 	return this.seriess;
	 }
	 
	/**
	 * Set the list of Series
	 */
	 public void setSeriess(List<Series> seriess) {
	 	this.seriess = seriess;
	 }


}