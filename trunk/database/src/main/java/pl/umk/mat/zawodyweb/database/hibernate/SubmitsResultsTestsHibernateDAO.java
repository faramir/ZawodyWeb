package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.SubmitsResultsTests;
import pl.umk.mat.zawodyweb.database.SubmitsResultsTestsDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for SubmitsResultsTestss</p>
 * <p>Generated at Thu Mar 05 04:19:38 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class SubmitsResultsTestsHibernateDAO extends
		AbstractHibernateDAO<SubmitsResultsTests, Integer> implements
		SubmitsResultsTestsDAO {

	/**
	 * Find SubmitsResultsTests by submitsid
	 */
	@SuppressWarnings("unchecked")
	public List<SubmitsResultsTests> findBySubmitsid(Integer submitsid) {
		return findByCriteria(Restrictions.eq("submits.submitsid", submitsid));
	}
	
	/**
	 * Find SubmitsResultsTests by resultsid
	 */
	@SuppressWarnings("unchecked")
	public List<SubmitsResultsTests> findByResultsid(Integer resultsid) {
		return findByCriteria(Restrictions.eq("results.resultsid", resultsid));
	}
	
	/**
	 * Find SubmitsResultsTests by testsid
	 */
	@SuppressWarnings("unchecked")
	public List<SubmitsResultsTests> findByTestsid(Integer testsid) {
		return findByCriteria(Restrictions.eq("tests.testsid", testsid));
	}
	

}
