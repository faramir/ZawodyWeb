package org.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import org.zawodyweb.database.pojo.Tasks;
import org.zawodyweb.database.TasksDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Taskss</p>
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class TasksHibernateDAO extends
		AbstractHibernateDAO<Tasks, Integer> implements
		TasksDAO {

	/**
	 * Find Tasks by name
	 */
	public List<Tasks> findByName(String name) {
		return findByCriteria(Restrictions.eq("name", name));
	}
	
	/**
	 * Find Tasks by text
	 */
	public List<Tasks> findByText(String text) {
		return findByCriteria(Restrictions.eq("text", text));
	}
	
	/**
	 * Find Tasks by pdf
	 */
	public List<Tasks> findByPdf(byte[] pdf) {
		return findByCriteria(Restrictions.eq("pdf", pdf));
	}
	
	/**
	 * Find Tasks by abbrev
	 */
	public List<Tasks> findByAbbrev(String abbrev) {
		return findByCriteria(Restrictions.eq("abbrev", abbrev));
	}
	
	/**
	 * Find Tasks by memlimit
	 */
	public List<Tasks> findByMemlimit(Integer memlimit) {
		return findByCriteria(Restrictions.eq("memlimit", memlimit));
	}
	
	/**
	 * Find Tasks by seriesid
	 */
	@SuppressWarnings("unchecked")
	public List<Tasks> findBySeriesid(Integer seriesid) {
		return findByCriteria(Restrictions.eq("series.seriesid", seriesid));
	}
	
	/**
	 * Find Tasks by classesid
	 */
	@SuppressWarnings("unchecked")
	public List<Tasks> findByClassesid(Integer classesid) {
		return findByCriteria(Restrictions.eq("classes.classesid", classesid));
	}
	

}
