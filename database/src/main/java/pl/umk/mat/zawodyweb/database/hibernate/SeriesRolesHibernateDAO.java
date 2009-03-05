package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.SeriesRoles;
import pl.umk.mat.zawodyweb.database.SeriesRolesDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for SeriesRoless</p>
 * <p>Generated at Thu Mar 05 04:19:38 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class SeriesRolesHibernateDAO extends
		AbstractHibernateDAO<SeriesRoles, Integer> implements
		SeriesRolesDAO {

	/**
	 * Find SeriesRoles by seriesid
	 */
	@SuppressWarnings("unchecked")
	public List<SeriesRoles> findBySeriesid(Integer seriesid) {
		return findByCriteria(Restrictions.eq("series.seriesid", seriesid));
	}
	
	/**
	 * Find SeriesRoles by roleid
	 */
	@SuppressWarnings("unchecked")
	public List<SeriesRoles> findByRoleid(Integer roleid) {
		return findByCriteria(Restrictions.eq("roles.roleid", roleid));
	}
	

}
