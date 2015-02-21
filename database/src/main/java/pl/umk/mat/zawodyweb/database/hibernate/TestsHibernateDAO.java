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
import pl.umk.mat.zawodyweb.database.TestsDAO;
import pl.umk.mat.zawodyweb.database.pojo.Tests;

/**
 * <p>Hibernate DAO layer for Testss</p>
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class TestsHibernateDAO extends
		AbstractHibernateDAO<Tests, Integer> implements
		TestsDAO {

	/**
	 * Find Tests by input
	 */
	public List<Tests> findByInput(String input) {
		return findByCriteria(Restrictions.eq("input", input));
	}
	
	/**
	 * Find Tests by output
	 */
	public List<Tests> findByOutput(String output) {
		return findByCriteria(Restrictions.eq("output", output));
	}
	
	/**
	 * Find Tests by timelimit
	 */
	public List<Tests> findByTimelimit(Integer timelimit) {
		return findByCriteria(Restrictions.eq("timelimit", timelimit));
	}
	
	/**
	 * Find Tests by maxpoints
	 */
	public List<Tests> findByMaxpoints(Integer maxpoints) {
		return findByCriteria(Restrictions.eq("maxpoints", maxpoints));
	}
	
	/**
	 * Find Tests by visibility
	 */
	public List<Tests> findByVisibility(Integer visibility) {
		return findByCriteria(Restrictions.eq("visibility", visibility));
	}
	
	/**
	 * Find Tests by problemsid
	 */
	@SuppressWarnings("unchecked")
	public List<Tests> findByProblemsid(Integer problemsid) {
		return findByCriteria(Restrictions.eq("problems.id", problemsid));
	}
	

}
