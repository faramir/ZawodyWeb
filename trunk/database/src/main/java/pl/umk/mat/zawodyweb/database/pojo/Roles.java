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
 * <p>Pojo mapping TABLE public.roles</p>
 *
 * <p>Generated at Sun Mar 08 19:45:32 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "roles", schema = "public")
@SuppressWarnings("serial")
public class Roles implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute name.
	 */
	private String name;
	
	/**
	 * Attribute addcontest.
	 */
	private Boolean addcontest;
	
	/**
	 * Attribute editcontest.
	 */
	private Boolean editcontest;
	
	/**
	 * Attribute delcontest.
	 */
	private Boolean delcontest;
	
	/**
	 * Attribute addseries.
	 */
	private Boolean addseries;
	
	/**
	 * Attribute editseries.
	 */
	private Boolean editseries;
	
	/**
	 * Attribute delseries.
	 */
	private Boolean delseries;
	
	/**
	 * Attribute addproblem.
	 */
	private Boolean addproblem;
	
	/**
	 * Attribute editproblem.
	 */
	private Boolean editproblem;
	
	/**
	 * Attribute delproblem.
	 */
	private Boolean delproblem;
	
	/**
	 * Attribute canrate.
	 */
	private Boolean canrate;
	
	/**
	 * Attribute contestant.
	 */
	private Boolean contestant;
	
	/**
	 * List of ContestsRoles
	 */
	private List<ContestsRoles> contestsRoless = null;

	/**
	 * List of SeriesRoles
	 */
	private List<SeriesRoles> seriesRoless = null;

	/**
	 * List of UsersRoles
	 */
	private List<UsersRoles> usersRoless = null;

	
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
	
	/* liste transiente */
	/**
	 * @return addcontest
	 */
	@Basic
	@Column(name = "addcontest")
		public Boolean getAddcontest() {
		return addcontest;
	}

	/**
	 * @param addcontest new value for addcontest 
	 */
	public void setAddcontest(Boolean addcontest) {
		this.addcontest = addcontest;
	}
	
	/* liste transiente */
	/**
	 * @return editcontest
	 */
	@Basic
	@Column(name = "editcontest")
		public Boolean getEditcontest() {
		return editcontest;
	}

	/**
	 * @param editcontest new value for editcontest 
	 */
	public void setEditcontest(Boolean editcontest) {
		this.editcontest = editcontest;
	}
	
	/* liste transiente */
	/**
	 * @return delcontest
	 */
	@Basic
	@Column(name = "delcontest")
		public Boolean getDelcontest() {
		return delcontest;
	}

	/**
	 * @param delcontest new value for delcontest 
	 */
	public void setDelcontest(Boolean delcontest) {
		this.delcontest = delcontest;
	}
	
	/* liste transiente */
	/**
	 * @return addseries
	 */
	@Basic
	@Column(name = "addseries")
		public Boolean getAddseries() {
		return addseries;
	}

	/**
	 * @param addseries new value for addseries 
	 */
	public void setAddseries(Boolean addseries) {
		this.addseries = addseries;
	}
	
	/* liste transiente */
	/**
	 * @return editseries
	 */
	@Basic
	@Column(name = "editseries")
		public Boolean getEditseries() {
		return editseries;
	}

	/**
	 * @param editseries new value for editseries 
	 */
	public void setEditseries(Boolean editseries) {
		this.editseries = editseries;
	}
	
	/* liste transiente */
	/**
	 * @return delseries
	 */
	@Basic
	@Column(name = "delseries")
		public Boolean getDelseries() {
		return delseries;
	}

	/**
	 * @param delseries new value for delseries 
	 */
	public void setDelseries(Boolean delseries) {
		this.delseries = delseries;
	}
	
	/* liste transiente */
	/**
	 * @return addproblem
	 */
	@Basic
	@Column(name = "addproblem")
		public Boolean getAddproblem() {
		return addproblem;
	}

	/**
	 * @param addproblem new value for addproblem 
	 */
	public void setAddproblem(Boolean addproblem) {
		this.addproblem = addproblem;
	}
	
	/* liste transiente */
	/**
	 * @return editproblem
	 */
	@Basic
	@Column(name = "editproblem")
		public Boolean getEditproblem() {
		return editproblem;
	}

	/**
	 * @param editproblem new value for editproblem 
	 */
	public void setEditproblem(Boolean editproblem) {
		this.editproblem = editproblem;
	}
	
	/* liste transiente */
	/**
	 * @return delproblem
	 */
	@Basic
	@Column(name = "delproblem")
		public Boolean getDelproblem() {
		return delproblem;
	}

	/**
	 * @param delproblem new value for delproblem 
	 */
	public void setDelproblem(Boolean delproblem) {
		this.delproblem = delproblem;
	}
	
	/* liste transiente */
	/**
	 * @return canrate
	 */
	@Basic
	@Column(name = "canrate")
		public Boolean getCanrate() {
		return canrate;
	}

	/**
	 * @param canrate new value for canrate 
	 */
	public void setCanrate(Boolean canrate) {
		this.canrate = canrate;
	}
	
	/* liste transiente */
	/**
	 * @return contestant
	 */
	@Basic
	@Column(name = "contestant")
		public Boolean getContestant() {
		return contestant;
	}

	/**
	 * @param contestant new value for contestant 
	 */
	public void setContestant(Boolean contestant) {
		this.contestant = contestant;
	}
	
	/**
	 * Get the list of ContestsRoles
	 */
	 // contestsRolesPK
	 @OneToMany(mappedBy="roles")
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
	 * Get the list of SeriesRoles
	 */
	 // seriesRolesPK
	 @OneToMany(mappedBy="roles")
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
	 * Get the list of UsersRoles
	 */
	 // usersRolesPK
	 @OneToMany(mappedBy="roles")
	 public List<UsersRoles> getUsersRoless() {
	 	return this.usersRoless;
	 }
	 
	/**
	 * Set the list of UsersRoles
	 */
	 public void setUsersRoless(List<UsersRoles> usersRoless) {
	 	this.usersRoless = usersRoless;
	 }


}