/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Problems;
import pl.umk.mat.zawodyweb.database.ProblemsDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Problemss</p>
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class ProblemsHibernateDAO extends
		AbstractHibernateDAO<Problems, Integer> implements
		ProblemsDAO {

	/**
	 * Find Problems by name
	 */
	public List<Problems> findByName(String name) {
		return findByCriteria(Restrictions.eq("name", name));
	}
	
	/**
	 * Find Problems by text
	 */
	public List<Problems> findByText(String text) {
		return findByCriteria(Restrictions.eq("text", text));
	}
	
	/**
	 * Find Problems by pdf
	 */
	public List<Problems> findByPdf(byte[] pdf) {
		return findByCriteria(Restrictions.eq("pdf", pdf));
	}
	
	/**
	 * Find Problems by abbrev
	 */
	public List<Problems> findByAbbrev(String abbrev) {
		return findByCriteria(Restrictions.eq("abbrev", abbrev));
	}
	
	/**
	 * Find Problems by memlimit
	 */
	public List<Problems> findByMemlimit(Integer memlimit) {
		return findByCriteria(Restrictions.eq("memlimit", memlimit));
	}
	
	/**
	 * Find Problems by seriesid
	 */
	@SuppressWarnings("unchecked")
	public List<Problems> findBySeriesid(Integer seriesid) {
		return findByCriteria(Restrictions.eq("series.id", seriesid));
	}
	
	/**
	 * Find Problems by classesid
	 */
	@SuppressWarnings("unchecked")
	public List<Problems> findByClassesid(Integer classesid) {
		return findByCriteria(Restrictions.eq("classes.id", classesid));
	}
	

}
