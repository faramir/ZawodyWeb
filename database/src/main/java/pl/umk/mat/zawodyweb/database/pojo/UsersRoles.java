/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
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
 * <p>Pojo mapping TABLE public.users_roles</p>
 *
 * <p>Generated at Fri May 08 19:01:00 CEST 2009</p>
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
	 * get users
	 */
	@ManyToOne
	@JoinColumn(name = "usersid")
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
	@JoinColumn(name = "rolesid")
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
