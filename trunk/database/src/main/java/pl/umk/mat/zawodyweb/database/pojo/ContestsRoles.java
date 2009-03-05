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
 * <p>Pojo mapping TABLE public.contests_roles</p>
 *
 * <p>Generated at Thu Mar 05 04:19:38 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "contests_roles", schema = "public")
@SuppressWarnings("serial")
public class ContestsRoles implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute contests
	 */
	 private Contests contests;	

	/**
	 * Attribute roles
	 */
	 private Roles roles;	

	
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
	 * get roles
	 */
	@ManyToOne
	@JoinColumn(name = "roleid")
	public Roles getRoles() {
		return this.roles;
	}
	
	/**
	 * set roles
	 */
	public void setRoles(Roles roles) {
		this.roles = roles;
	}



}