package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Contests;
import pl.umk.mat.zawodyweb.database.ContestsDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Contestss</p>
 * <p>Generated at Sun Mar 08 19:45:32 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class ContestsHibernateDAO extends
		AbstractHibernateDAO<Contests, Integer> implements
		ContestsDAO {

	/**
	 * Find Contests by name
	 */
	public List<Contests> findByName(String name) {
		return findByCriteria(Restrictions.eq("name", name));
	}
	
	/**
	 * Find Contests by type
	 */
	public List<Contests> findByType(Integer type) {
		return findByCriteria(Restrictions.eq("type", type));
	}
	
	/**
	 * Find Contests by startdate
	 */
	public List<Contests> findByStartdate(Timestamp startdate) {
		return findByCriteria(Restrictions.eq("startdate", startdate));
	}
	
	/**
	 * Find Contests by about
	 */
	public List<Contests> findByAbout(String about) {
		return findByCriteria(Restrictions.eq("about", about));
	}
	
	/**
	 * Find Contests by rules
	 */
	public List<Contests> findByRules(String rules) {
		return findByCriteria(Restrictions.eq("rules", rules));
	}
	
	/**
	 * Find Contests by tech
	 */
	public List<Contests> findByTech(String tech) {
		return findByCriteria(Restrictions.eq("tech", tech));
	}
	
	/**
	 * Find Contests by visibility
	 */
	public List<Contests> findByVisibility(Integer visibility) {
		return findByCriteria(Restrictions.eq("visibility", visibility));
	}
	

}
