package org.zawodyweb.database;

import java.util.List;
import java.sql.Timestamp;

import org.zawodyweb.database.pojo.Roles;
/**
 * <p>Generic DAO layer for Roless</p>
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface RolesDAO extends GenericDAO<Roles,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildRolesDAO()
	 */
	  	 
	/**
	 * Find Roles by name
	 */
	public List<Roles> findByName(String name);

	/**
	 * Find Roles by addcontest
	 */
	public List<Roles> findByAddcontest(Integer addcontest);

	/**
	 * Find Roles by editcontest
	 */
	public List<Roles> findByEditcontest(Integer editcontest);

	/**
	 * Find Roles by delcontest
	 */
	public List<Roles> findByDelcontest(Integer delcontest);

	/**
	 * Find Roles by addseries
	 */
	public List<Roles> findByAddseries(Integer addseries);

	/**
	 * Find Roles by editseries
	 */
	public List<Roles> findByEditseries(Integer editseries);

	/**
	 * Find Roles by delseries
	 */
	public List<Roles> findByDelseries(Integer delseries);

	/**
	 * Find Roles by contestant
	 */
	public List<Roles> findByContestant(Integer contestant);

}