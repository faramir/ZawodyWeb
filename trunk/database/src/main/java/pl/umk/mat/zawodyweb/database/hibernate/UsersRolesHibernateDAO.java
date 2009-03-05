package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.UsersRoles;
import pl.umk.mat.zawodyweb.database.UsersRolesDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for UsersRoless</p>
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class UsersRolesHibernateDAO extends
		AbstractHibernateDAO<UsersRoles, Integer> implements
		UsersRolesDAO {

	/**
	 * Find UsersRoles by userid
	 */
	@SuppressWarnings("unchecked")
	public List<UsersRoles> findByUserid(Integer userid) {
		return findByCriteria(Restrictions.eq("users.userid", userid));
	}
	
	/**
	 * Find UsersRoles by roleid
	 */
	@SuppressWarnings("unchecked")
	public List<UsersRoles> findByRoleid(Integer roleid) {
		return findByCriteria(Restrictions.eq("roles.roleid", roleid));
	}
	

}
