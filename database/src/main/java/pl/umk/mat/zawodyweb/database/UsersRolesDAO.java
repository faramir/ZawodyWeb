/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.UsersRoles;
/**
 * <p>Generic DAO layer for UsersRoless</p>
 * <p>Generated at Fri May 08 19:01:00 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface UsersRolesDAO extends GenericDAO<UsersRoles,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildUsersRolesDAO()
	 */
	  	 
	/**
	 * Find UsersRoles by usersid
	 */
	public List<UsersRoles> findByUsersid(Integer usersid);

	/**
	 * Find UsersRoles by rolesid
	 */
	public List<UsersRoles> findByRolesid(Integer rolesid);

}
