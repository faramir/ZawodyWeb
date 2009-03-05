package org.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import org.zawodyweb.database.pojo.Submits;
import org.zawodyweb.database.SubmitsDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Submitss</p>
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
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
	 * Find Submits by tasksid
	 */
	@SuppressWarnings("unchecked")
	public List<Submits> findByTasksid(Integer tasksid) {
		return findByCriteria(Restrictions.eq("tasks.tasksid", tasksid));
	}
	

}
