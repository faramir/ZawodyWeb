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
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
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
	private Integer addcontest;
	
	/**
	 * Attribute editcontest.
	 */
	private Integer editcontest;
	
	/**
	 * Attribute delcontest.
	 */
	private Integer delcontest;
	
	/**
	 * Attribute addseries.
	 */
	private Integer addseries;
	
	/**
	 * Attribute editseries.
	 */
	private Integer editseries;
	
	/**
	 * Attribute delseries.
	 */
	private Integer delseries;
	
	/**
	 * Attribute contestant.
	 */
	private Integer contestant;
	
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
		public Integer getAddcontest() {
		return addcontest;
	}

	/**
	 * @param addcontest new value for addcontest 
	 */
	public void setAddcontest(Integer addcontest) {
		this.addcontest = addcontest;
	}
	
	/* liste transiente */
	/**
	 * @return editcontest
	 */
	@Basic
	@Column(name = "editcontest")
		public Integer getEditcontest() {
		return editcontest;
	}

	/**
	 * @param editcontest new value for editcontest 
	 */
	public void setEditcontest(Integer editcontest) {
		this.editcontest = editcontest;
	}
	
	/* liste transiente */
	/**
	 * @return delcontest
	 */
	@Basic
	@Column(name = "delcontest")
		public Integer getDelcontest() {
		return delcontest;
	}

	/**
	 * @param delcontest new value for delcontest 
	 */
	public void setDelcontest(Integer delcontest) {
		this.delcontest = delcontest;
	}
	
	/* liste transiente */
	/**
	 * @return addseries
	 */
	@Basic
	@Column(name = "addseries")
		public Integer getAddseries() {
		return addseries;
	}

	/**
	 * @param addseries new value for addseries 
	 */
	public void setAddseries(Integer addseries) {
		this.addseries = addseries;
	}
	
	/* liste transiente */
	/**
	 * @return editseries
	 */
	@Basic
	@Column(name = "editseries")
		public Integer getEditseries() {
		return editseries;
	}

	/**
	 * @param editseries new value for editseries 
	 */
	public void setEditseries(Integer editseries) {
		this.editseries = editseries;
	}
	
	/* liste transiente */
	/**
	 * @return delseries
	 */
	@Basic
	@Column(name = "delseries")
		public Integer getDelseries() {
		return delseries;
	}

	/**
	 * @param delseries new value for delseries 
	 */
	public void setDelseries(Integer delseries) {
		this.delseries = delseries;
	}
	
	/* liste transiente */
	/**
	 * @return contestant
	 */
	@Basic
	@Column(name = "contestant")
		public Integer getContestant() {
		return contestant;
	}

	/**
	 * @param contestant new value for contestant 
	 */
	public void setContestant(Integer contestant) {
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