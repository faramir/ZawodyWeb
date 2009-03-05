package org.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import org.zawodyweb.database.pojo.ContestsRoles;
import org.zawodyweb.database.ContestsRolesDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for ContestsRoless</p>
 * <p>Generated at Thu Mar 05 04:19:38 CET 2009</p>
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
	 * Find ContestsRoles by roleid
	 */
	@SuppressWarnings("unchecked")
	public List<ContestsRoles> findByRoleid(Integer roleid) {
		return findByCriteria(Restrictions.eq("roles.roleid", roleid));
	}
	

}
