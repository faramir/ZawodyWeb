package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Series;
import pl.umk.mat.zawodyweb.database.SeriesDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Seriess</p>
 * <p>Generated at Sun Mar 08 19:45:32 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class SeriesHibernateDAO extends
		AbstractHibernateDAO<Series, Integer> implements
		SeriesDAO {

	/**
	 * Find Series by name
	 */
	public List<Series> findByName(String name) {
		return findByCriteria(Restrictions.eq("name", name));
	}
	
	/**
	 * Find Series by startdate
	 */
	public List<Series> findByStartdate(Timestamp startdate) {
		return findByCriteria(Restrictions.eq("startdate", startdate));
	}
	
	/**
	 * Find Series by enddate
	 */
	public List<Series> findByEnddate(Timestamp enddate) {
		return findByCriteria(Restrictions.eq("enddate", enddate));
	}
	
	/**
	 * Find Series by freezedate
	 */
	public List<Series> findByFreezedate(Timestamp freezedate) {
		return findByCriteria(Restrictions.eq("freezedate", freezedate));
	}
	
	/**
	 * Find Series by unfreezedate
	 */
	public List<Series> findByUnfreezedate(Timestamp unfreezedate) {
		return findByCriteria(Restrictions.eq("unfreezedate", unfreezedate));
	}
	
	/**
	 * Find Series by penaltytime
	 */
	public List<Series> findByPenaltytime(Integer penaltytime) {
		return findByCriteria(Restrictions.eq("penaltytime", penaltytime));
	}
	
	/**
	 * Find Series by contestsid
	 */
	@SuppressWarnings("unchecked")
	public List<Series> findByContestsid(Integer contestsid) {
		return findByCriteria(Restrictions.eq("contests.contestsid", contestsid));
	}
	

}
