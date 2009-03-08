package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Questions;
import pl.umk.mat.zawodyweb.database.QuestionsDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Questionss</p>
 * <p>Generated at Sun Mar 08 19:45:32 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public class QuestionsHibernateDAO extends
		AbstractHibernateDAO<Questions, Integer> implements
		QuestionsDAO {

	/**
	 * Find Questions by question
	 */
	public List<Questions> findByQuestion(String question) {
		return findByCriteria(Restrictions.eq("question", question));
	}
	
	/**
	 * Find Questions by visibility
	 */
	public List<Questions> findByVisibility(Integer visibility) {
		return findByCriteria(Restrictions.eq("visibility", visibility));
	}
	
	/**
	 * Find Questions by qtype
	 */
	public List<Questions> findByQtype(Integer qtype) {
		return findByCriteria(Restrictions.eq("qtype", qtype));
	}
	
	/**
	 * Find Questions by usersid
	 */
	@SuppressWarnings("unchecked")
	public List<Questions> findByUsersid(Integer usersid) {
		return findByCriteria(Restrictions.eq("users.usersid", usersid));
	}
	
	/**
	 * Find Questions by contestsid
	 */
	@SuppressWarnings("unchecked")
	public List<Questions> findByContestsid(Integer contestsid) {
		return findByCriteria(Restrictions.eq("contests.contestsid", contestsid));
	}
	
	/**
	 * Find Questions by tasksid
	 */
	@SuppressWarnings("unchecked")
	public List<Questions> findByTasksid(Integer tasksid) {
		return findByCriteria(Restrictions.eq("problems.tasksid", tasksid));
	}
	

}
