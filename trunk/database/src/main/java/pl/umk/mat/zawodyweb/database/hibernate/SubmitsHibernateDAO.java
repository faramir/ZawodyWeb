package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Submits;
import pl.umk.mat.zawodyweb.database.SubmitsDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Submitss</p>
 * <p>Generated at Sun Mar 08 19:45:33 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class SubmitsHibernateDAO extends
		AbstractHibernateDAO<Submits, Integer> implements
		SubmitsDAO {

	/**
	 * Find Submits by sdate
	 */
	public List<Submits> findBySdate(Timestamp sdate) {
		return findByCriteria(Restrictions.eq("sdate", sdate));
	}
	
	/**
	 * Find Submits by result
	 */
	public List<Submits> findByResult(Integer result) {
		return findByCriteria(Restrictions.eq("result", result));
	}
	
	/**
	 * Find Submits by code
	 */
	public List<Submits> findByCode(byte[] code) {
		return findByCriteria(Restrictions.eq("code", code));
	}
	
	/**
	 * Find Submits by filename
	 */
	public List<Submits> findByFilename(String filename) {
		return findByCriteria(Restrictions.eq("filename", filename));
	}
	
	/**
	 * Find Submits by notes
	 */
	public List<Submits> findByNotes(String notes) {
		return findByCriteria(Restrictions.eq("notes", notes));
	}
	
	/**
	 * Find Submits by problemsid
	 */
	@SuppressWarnings("unchecked")
	public List<Submits> findByProblemsid(Integer problemsid) {
		return findByCriteria(Restrictions.eq("problems.problemsid", problemsid));
	}
	
	/**
	 * Find Submits by languagesid
	 */
	@SuppressWarnings("unchecked")
	public List<Submits> findByLanguagesid(Integer languagesid) {
		return findByCriteria(Restrictions.eq("languages.languagesid", languagesid));
	}
	

}
