/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import pl.umk.mat.zawodyweb.database.ClassesDAO;
import pl.umk.mat.zawodyweb.database.pojo.Classes;

/**
 * <p>Hibernate DAO layer for Classess</p>
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class ClassesHibernateDAO extends
		AbstractHibernateDAO<Classes, Integer> implements
		ClassesDAO {

	/**
	 * Find Classes by filename
	 */
	public List<Classes> findByFilename(String filename) {
		return findByCriteria(Restrictions.eq("filename", filename));
	}
	
	/**
	 * Find Classes by version
	 */
	public List<Classes> findByVersion(Integer version) {
		return findByCriteria(Restrictions.eq("version", version));
	}
	
	/**
	 * Find Classes by description
	 */
	public List<Classes> findByDescription(String description) {
		return findByCriteria(Restrictions.eq("description", description));
	}
	
	/**
	 * Find Classes by code
	 */
	public List<Classes> findByCode(byte[] code) {
		return findByCriteria(Restrictions.eq("code", code));
	}
	

}
