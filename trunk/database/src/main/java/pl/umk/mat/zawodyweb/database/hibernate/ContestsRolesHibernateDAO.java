package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.ContestsRoles;
import pl.umk.mat.zawodyweb.database.ContestsRolesDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for ContestsRoless</p>
 * <p>Generated at Sun Mar 08 19:45:31 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class ContestsRolesHibernateDAO extends
		AbstractHibernateDAO<ContestsRoles, Integer> implements
		ContestsRolesDAO {

	/**
	 * Find ContestsRoles by contestsid
	 */
	@SuppressWarnings("unchecked")
	public List<ContestsRoles> findByContestsid(Integer contestsid) {
		return findByCriteria(Restrictions.eq("contests.contestsid", contestsid));
	}
	
	/**
	 * Find ContestsRoles by rolesid
	 */
	@SuppressWarnings("unchecked")
	public List<ContestsRoles> findByRolesid(Integer rolesid) {
		return findByCriteria(Restrictions.eq("roles.rolesid", rolesid));
	}
	

}
