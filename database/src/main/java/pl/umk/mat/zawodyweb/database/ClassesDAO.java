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

import pl.umk.mat.zawodyweb.database.pojo.Classes;
/**
 * <p>Generic DAO layer for Classess</p>
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface ClassesDAO extends GenericDAO<Classes,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildClassesDAO()
	 */
	  	 
	/**
	 * Find Classes by filename
	 */
	public List<Classes> findByFilename(String filename);

	/**
	 * Find Classes by version
	 */
	public List<Classes> findByVersion(Integer version);

	/**
	 * Find Classes by description
	 */
	public List<Classes> findByDescription(String description);

	/**
	 * Find Classes by code
	 */
	public List<Classes> findByCode(byte[] code);

}
