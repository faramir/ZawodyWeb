package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Roles;
import pl.umk.mat.zawodyweb.database.RolesDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Roless</p>
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class RolesHibernateDAO extends
		AbstractHibernateDAO<Roles, Integer> implements
		RolesDAO {

	/**
	 * Find Roles by name
	 */
	public List<Roles> findByName(String name) {
		return findByCriteria(Restrictions.eq("name", name));
	}
	
	/**
	 * Find Roles by addcontest
	 */
	public List<Roles> findByAddcontest(Integer addcontest) {
		return findByCriteria(Restrictions.eq("addcontest", addcontest));
	}
	
	/**
	 * Find Roles by editcontest
	 */
	public List<Roles> findByEditcontest(Integer editcontest) {
		return findByCriteria(Restrictions.eq("editcontest", editcontest));
	}
	
	/**
	 * Find Roles by delcontest
	 */
	public List<Roles> findByDelcontest(Integer delcontest) {
		return findByCriteria(Restrictions.eq("delcontest", delcontest));
	}
	
	/**
	 * Find Roles by addseries
	 */
	public List<Roles> findByAddseries(Integer addseries) {
		return findByCriteria(Restrictions.eq("addseries", addseries));
	}
	
	/**
	 * Find Roles by editseries
	 */
	public List<Roles> findByEditseries(Integer editseries) {
		return findByCriteria(Restrictions.eq("editseries", editseries));
	}
	
	/**
	 * Find Roles by delseries
	 */
	public List<Roles> findByDelseries(Integer delseries) {
		return findByCriteria(Restrictions.eq("delseries", delseries));
	}
	
	/**
	 * Find Roles by contestant
	 */
	public List<Roles> findByContestant(Integer contestant) {
		return findByCriteria(Restrictions.eq("contestant", contestant));
	}
	

}
