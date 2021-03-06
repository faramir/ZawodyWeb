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
import pl.umk.mat.zawodyweb.database.SeriesDAO;
import pl.umk.mat.zawodyweb.database.pojo.Series;

/**
 * <p>Hibernate DAO layer for Seriess</p>
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class SeriesHibernateDAO extends
		AbstractHibernateDAO<Series, Integer> implements
		SeriesDAO {

	/**
	 * Find Series by name
	 */
	public List<Series> findByName(String name) {
		return findByCriteria(Restrictions.eq("name", name));
	}
	
	/**
	 * Find Series by startdate
	 */
	public List<Series> findByStartdate(Timestamp startdate) {
		return findByCriteria(Restrictions.eq("startdate", startdate));
	}
	
	/**
	 * Find Series by enddate
	 */
	public List<Series> findByEnddate(Timestamp enddate) {
		return findByCriteria(Restrictions.eq("enddate", enddate));
	}
	
	/**
	 * Find Series by freezedate
	 */
	public List<Series> findByFreezedate(Timestamp freezedate) {
		return findByCriteria(Restrictions.eq("freezedate", freezedate));
	}
	
	/**
	 * Find Series by unfreezedate
	 */
	public List<Series> findByUnfreezedate(Timestamp unfreezedate) {
		return findByCriteria(Restrictions.eq("unfreezedate", unfreezedate));
	}
	
	/**
	 * Find Series by penaltytime
	 */
	public List<Series> findByPenaltytime(Integer penaltytime) {
		return findByCriteria(Restrictions.eq("penaltytime", penaltytime));
	}
	
	/**
	 * Find Series by contestsid
	 */
	@SuppressWarnings("unchecked")
	public List<Series> findByContestsid(Integer contestsid) {
		return findByCriteria(Restrictions.eq("contests.id", contestsid));
	}
	

}
