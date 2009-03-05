package org.zawodyweb.database.hibernate;

import java.util.List;
import java.sql.Timestamp;

import org.zawodyweb.database.pojo.Questions;
import org.zawodyweb.database.QuestionsDAO;

import org.hibernate.criterion.Restrictions;

/**
 * <p>Hibernate DAO layer for Questionss</p>
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
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
	 * Find Questions by answer
	 */
	public List<Questions> findByAnswer(String answer) {
		return findByCriteria(Restrictions.eq("answer", answer));
	}
	
	/**
	 * Find Questions by visibility
	 */
	public List<Questions> findByVisibility(Integer visibility) {
		return findByCriteria(Restrictions.eq("visibility", visibility));
	}
	
	/**
	 * Find Questions by userid
	 */
	@SuppressWarnings("unchecked")
	public List<Questions> findByUserid(Integer userid) {
		return findByCriteria(Restrictions.eq("users.userid", userid));
	}
	
	/**
	 * Find Questions by contestsid
	 */
	@SuppressWarnings("unchecked")
	public List<Questions> findByContestsid(Integer contestsid) {
		return findByCriteria(Restrictions.eq("contests.contestsid", contestsid));
	}
	
	/**
	 * Find Questions by taksid
	 */
	@SuppressWarnings("unchecked")
	public List<Questions> findByTaksid(Integer taksid) {
		return findByCriteria(Restrictions.eq("tasks.taksid", taksid));
	}
	

}
