package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.UsersRoles;
import pl.umk.mat.zawodyweb.database.UsersRolesDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for UsersRoless</p>
 * <p>Generated at Fri May 08 19:01:00 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class UsersRolesHibernateDAO extends
		AbstractHibernateDAO<UsersRoles, Integer> implements
		UsersRolesDAO {

	/**
	 * Find UsersRoles by usersid
	 */
	@SuppressWarnings("unchecked")
	public List<UsersRoles> findByUsersid(Integer usersid) {
		return findByCriteria(Restrictions.eq("users.id", usersid));
	}
	
	/**
	 * Find UsersRoles by rolesid
	 */
	@SuppressWarnings("unchecked")
	public List<UsersRoles> findByRolesid(Integer rolesid) {
		return findByCriteria(Restrictions.eq("roles.id", rolesid));
	}
	

}
