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

import pl.umk.mat.zawodyweb.database.pojo.Users;
/**
 * <p>Generic DAO layer for Userss</p>
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface UsersDAO extends GenericDAO<Users,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildUsersDAO()
	 */
	  	 
	/**
	 * Find Users by firstname
	 */
	public List<Users> findByFirstname(String firstname);

	/**
	 * Find Users by lastname
	 */
	public List<Users> findByLastname(String lastname);

	/**
	 * Find Users by email
	 */
	public List<Users> findByEmail(String email);

	/**
	 * Find Users by birthdate
	 */
	public List<Users> findByBirthdate(Timestamp birthdate);

	/**
	 * Find Users by login
	 */
	public List<Users> findByLogin(String login);

	/**
	 * Find Users by pass
	 */
	public List<Users> findByPass(String pass);

	/**
	 * Find Users by address
	 */
	public List<Users> findByAddress(String address);

	/**
	 * Find Users by school
	 */
	public List<Users> findBySchool(String school);

	/**
	 * Find Users by tutor
	 */
	public List<Users> findByTutor(String tutor);

	/**
	 * Find Users by emailnotification
	 */
	public List<Users> findByEmailnotification(Integer emailnotification);

}
