package org.zawodyweb.database.pojo;

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
 * <p>Pojo mapping TABLE public.users_roles</p>
 *
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 * @author Salto-db Generator v1.1 / EJB3
 * 
 */
@Entity
@Table(name = "users_roles", schema = "public")
@SuppressWarnings("serial")
public class UsersRoles implements Serializable {

	/**
	 * Attribute id.
	 */
	private Integer id;
	
	/**
	 * Attribute users
	 */
	 private Users users;	

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
	 * get users
	 */
	@ManyToOne
	@JoinColumn(name = "userid")
	public Users getUsers() {
		return this.users;
	}
	
	/**
	 * set users
	 */
	public void setUsers(Users users) {
		this.users = users;
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