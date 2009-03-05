package org.zawodyweb.database;

import java.util.List;
import java.sql.Timestamp;

import org.zawodyweb.database.pojo.ContestsRoles;
/**
 * <p>Generic DAO layer for ContestsRoless</p>
 * <p>Generated at Thu Mar 05 04:19:38 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface ContestsRolesDAO extends GenericDAO<ContestsRoles,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildContestsRolesDAO()
	 */
	  	 
	/**
	 * Find ContestsRoles by contestsid
	 */
	public List<ContestsRoles> findByContestsid(Integer contestsid);

	/**
	 * Find ContestsRoles by roleid
	 */
	public List<ContestsRoles> findByRoleid(Integer roleid);

}