package pl.umk.mat.zawodyweb.database;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Questions;
/**
 * <p>Generic DAO layer for Questionss</p>
 * <p>Generated at Sun Mar 08 19:45:32 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface QuestionsDAO extends GenericDAO<Questions,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildQuestionsDAO()
	 */
	  	 
	/**
	 * Find Questions by question
	 */
	public List<Questions> findByQuestion(String question);

	/**
	 * Find Questions by visibility
	 */
	public List<Questions> findByVisibility(Integer visibility);

	/**
	 * Find Questions by qtype
	 */
	public List<Questions> findByQtype(Integer qtype);

	/**
	 * Find Questions by usersid
	 */
	public List<Questions> findByUsersid(Integer usersid);

	/**
	 * Find Questions by contestsid
	 */
	public List<Questions> findByContestsid(Integer contestsid);

	/**
	 * Find Questions by tasksid
	 */
	public List<Questions> findByTasksid(Integer tasksid);

}