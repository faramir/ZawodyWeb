/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.hibernate;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import pl.umk.mat.zawodyweb.database.ContestsDAO;
import pl.umk.mat.zawodyweb.database.pojo.Contests;

/**
 * <p>Hibernate DAO layer for Contestss</p>
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class ContestsHibernateDAO extends
		AbstractHibernateDAO<Contests, Integer> implements
		ContestsDAO {

	/**
	 * Find Contests by name
	 */
	public List<Contests> findByName(String name) {
		return findByCriteria(Restrictions.eq("name", name));
	}
	
	/**
	 * Find Contests by type
	 */
	public List<Contests> findByType(Integer type) {
		return findByCriteria(Restrictions.eq("type", type));
	}
	
	/**
	 * Find Contests by startdate
	 */
	public List<Contests> findByStartdate(Timestamp startdate) {
		return findByCriteria(Restrictions.eq("startdate", startdate));
	}
	
	/**
	 * Find Contests by about
	 */
	public List<Contests> findByAbout(String about) {
		return findByCriteria(Restrictions.eq("about", about));
	}
	
	/**
	 * Find Contests by rules
	 */
	public List<Contests> findByRules(String rules) {
		return findByCriteria(Restrictions.eq("rules", rules));
	}
	
	/**
	 * Find Contests by tech
	 */
	public List<Contests> findByTech(String tech) {
		return findByCriteria(Restrictions.eq("tech", tech));
	}
	
	/**
	 * Find Contests by visibility
	 */
	public List<Contests> findByVisibility(Integer visibility) {
		return findByCriteria(Restrictions.eq("visibility", visibility));
	}
	

}
