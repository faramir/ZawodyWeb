package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Classes;
import pl.umk.mat.zawodyweb.database.ClassesDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Classess</p>
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class ClassesHibernateDAO extends
		AbstractHibernateDAO<Classes, Integer> implements
		ClassesDAO {

	/**
	 * Find Classes by filename
	 */
	public List<Classes> findByFilename(String filename) {
		return findByCriteria(Restrictions.eq("filename", filename));
	}
	
	/**
	 * Find Classes by version
	 */
	public List<Classes> findByVersion(Integer version) {
		return findByCriteria(Restrictions.eq("version", version));
	}
	
	/**
	 * Find Classes by description
	 */
	public List<Classes> findByDescription(String description) {
		return findByCriteria(Restrictions.eq("description", description));
	}
	

}
