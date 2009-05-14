package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.LanguagesProblems;
import pl.umk.mat.zawodyweb.database.LanguagesProblemsDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for LanguagesProblemss</p>
 * <p>Generated at Fri May 08 19:01:00 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class LanguagesProblemsHibernateDAO extends
		AbstractHibernateDAO<LanguagesProblems, Integer> implements
		LanguagesProblemsDAO {

	/**
	 * Find LanguagesProblems by problemsid
	 */
	@SuppressWarnings("unchecked")
	public List<LanguagesProblems> findByProblemsid(Integer problemsid) {
		return findByCriteria(Restrictions.eq("problems.id", problemsid));
	}
	
	/**
	 * Find LanguagesProblems by languagesid
	 */
	@SuppressWarnings("unchecked")
	public List<LanguagesProblems> findByLanguagesid(Integer languagesid) {
		return findByCriteria(Restrictions.eq("languages.id", languagesid));
	}
	

}
