package org.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import org.zawodyweb.database.pojo.Tests;
import org.zawodyweb.database.TestsDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Testss</p>
 * <p>Generated at Thu Mar 05 04:19:38 CET 2009</p>
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
	

}
