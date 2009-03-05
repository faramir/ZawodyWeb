package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Results;
import pl.umk.mat.zawodyweb.database.ResultsDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Resultss</p>
 * <p>Generated at Thu Mar 05 04:19:38 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class ResultsHibernateDAO extends
		AbstractHibernateDAO<Results, Integer> implements
		ResultsDAO {

	/**
	 * Find Results by points
	 */
	public List<Results> findByPoints(Integer points) {
		return findByCriteria(Restrictions.eq("points", points));
	}
	
	/**
	 * Find Results by runtime
	 */
	public List<Results> findByRuntime(Integer runtime) {
		return findByCriteria(Restrictions.eq("runtime", runtime));
	}
	
	/**
	 * Find Results by memory
	 */
	public List<Results> findByMemory(Integer memory) {
		return findByCriteria(Restrictions.eq("memory", memory));
	}
	
	/**
	 * Find Results by notes
	 */
	public List<Results> findByNotes(String notes) {
		return findByCriteria(Restrictions.eq("notes", notes));
	}
	
	/**
	 * Find Results by submitid
	 */
	@SuppressWarnings("unchecked")
	public List<Results> findBySubmitid(Integer submitid) {
		return findByCriteria(Restrictions.eq("submits.submitid", submitid));
	}
	
	/**
	 * Find Results by testsid
	 */
	@SuppressWarnings("unchecked")
	public List<Results> findByTestsid(Integer testsid) {
		return findByCriteria(Restrictions.eq("tests.testsid", testsid));
	}
	

}
