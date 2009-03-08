package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Users;
import pl.umk.mat.zawodyweb.database.UsersDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Userss</p>
 * <p>Generated at Sun Mar 08 19:45:31 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class UsersHibernateDAO extends
		AbstractHibernateDAO<Users, Integer> implements
		UsersDAO {

	/**
	 * Find Users by firstname
	 */
	public List<Users> findByFirstname(String firstname) {
		return findByCriteria(Restrictions.eq("firstname", firstname));
	}
	
	/**
	 * Find Users by lastname
	 */
	public List<Users> findByLastname(String lastname) {
		return findByCriteria(Restrictions.eq("lastname", lastname));
	}
	
	/**
	 * Find Users by email
	 */
	public List<Users> findByEmail(String email) {
		return findByCriteria(Restrictions.eq("email", email));
	}
	
	/**
	 * Find Users by birthdate
	 */
	public List<Users> findByBirthdate(Timestamp birthdate) {
		return findByCriteria(Restrictions.eq("birthdate", birthdate));
	}
	
	/**
	 * Find Users by login
	 */
	public List<Users> findByLogin(String login) {
		return findByCriteria(Restrictions.eq("login", login));
	}
	
	/**
	 * Find Users by pass
	 */
	public List<Users> findByPass(String pass) {
		return findByCriteria(Restrictions.eq("pass", pass));
	}
	
	/**
	 * Find Users by address
	 */
	public List<Users> findByAddress(String address) {
		return findByCriteria(Restrictions.eq("address", address));
	}
	
	/**
	 * Find Users by school
	 */
	public List<Users> findBySchool(String school) {
		return findByCriteria(Restrictions.eq("school", school));
	}
	
	/**
	 * Find Users by tutor
	 */
	public List<Users> findByTutor(String tutor) {
		return findByCriteria(Restrictions.eq("tutor", tutor));
	}
	
	/**
	 * Find Users by emailnotification
	 */
	public List<Users> findByEmailnotification(Integer emailnotification) {
		return findByCriteria(Restrictions.eq("emailnotification", emailnotification));
	}
	

}
