package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Roles;
import pl.umk.mat.zawodyweb.database.RolesDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Roless</p>
 * <p>Generated at Sun Mar 08 19:45:32 CET 2009</p>
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
	public List<Roles> findByAddcontest(Boolean addcontest) {
		return findByCriteria(Restrictions.eq("addcontest", addcontest));
	}
	
	/**
	 * Find Roles by editcontest
	 */
	public List<Roles> findByEditcontest(Boolean editcontest) {
		return findByCriteria(Restrictions.eq("editcontest", editcontest));
	}
	
	/**
	 * Find Roles by delcontest
	 */
	public List<Roles> findByDelcontest(Boolean delcontest) {
		return findByCriteria(Restrictions.eq("delcontest", delcontest));
	}
	
	/**
	 * Find Roles by addseries
	 */
	public List<Roles> findByAddseries(Boolean addseries) {
		return findByCriteria(Restrictions.eq("addseries", addseries));
	}
	
	/**
	 * Find Roles by editseries
	 */
	public List<Roles> findByEditseries(Boolean editseries) {
		return findByCriteria(Restrictions.eq("editseries", editseries));
	}
	
	/**
	 * Find Roles by delseries
	 */
	public List<Roles> findByDelseries(Boolean delseries) {
		return findByCriteria(Restrictions.eq("delseries", delseries));
	}
	
	/**
	 * Find Roles by addproblem
	 */
	public List<Roles> findByAddproblem(Boolean addproblem) {
		return findByCriteria(Restrictions.eq("addproblem", addproblem));
	}
	
	/**
	 * Find Roles by editproblem
	 */
	public List<Roles> findByEditproblem(Boolean editproblem) {
		return findByCriteria(Restrictions.eq("editproblem", editproblem));
	}
	
	/**
	 * Find Roles by delproblem
	 */
	public List<Roles> findByDelproblem(Boolean delproblem) {
		return findByCriteria(Restrictions.eq("delproblem", delproblem));
	}
	
	/**
	 * Find Roles by canrate
	 */
	public List<Roles> findByCanrate(Boolean canrate) {
		return findByCriteria(Restrictions.eq("canrate", canrate));
	}
	
	/**
	 * Find Roles by contestant
	 */
	public List<Roles> findByContestant(Boolean contestant) {
		return findByCriteria(Restrictions.eq("contestant", contestant));
	}
	

}
