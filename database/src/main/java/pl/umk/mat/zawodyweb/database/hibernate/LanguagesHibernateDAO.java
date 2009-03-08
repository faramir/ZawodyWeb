package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Languages;
import pl.umk.mat.zawodyweb.database.LanguagesDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Languagess</p>
 * <p>Generated at Sun Mar 08 19:45:31 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class LanguagesHibernateDAO extends
		AbstractHibernateDAO<Languages, Integer> implements
		LanguagesDAO {

	/**
	 * Find Languages by name
	 */
	public List<Languages> findByName(String name) {
		return findByCriteria(Restrictions.eq("name", name));
	}
	
	/**
	 * Find Languages by extension
	 */
	public List<Languages> findByExtension(String extension) {
		return findByCriteria(Restrictions.eq("extension", extension));
	}
	
	/**
	 * Find Languages by classesid
	 */
	@SuppressWarnings("unchecked")
	public List<Languages> findByClassesid(Integer classesid) {
		return findByCriteria(Restrictions.eq("classes.classesid", classesid));
	}
	

}
