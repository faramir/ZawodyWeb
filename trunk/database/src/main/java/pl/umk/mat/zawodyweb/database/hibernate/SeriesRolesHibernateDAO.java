package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.SeriesRoles;
import pl.umk.mat.zawodyweb.database.SeriesRolesDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for SeriesRoless</p>
 * <p>Generated at Sun Mar 08 19:45:31 CET 2009</p>
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
	 * Find SeriesRoles by rolesid
	 */
	@SuppressWarnings("unchecked")
	public List<SeriesRoles> findByRolesid(Integer rolesid) {
		return findByCriteria(Restrictions.eq("roles.rolesid", rolesid));
	}
	

}
