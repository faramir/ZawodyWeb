package org.zawodyweb.database;

import java.util.List;
import java.sql.Timestamp;

import org.zawodyweb.database.pojo.SubmitsResultsTests;
/**
 * <p>Generic DAO layer for SubmitsResultsTestss</p>
 * <p>Generated at Thu Mar 05 04:19:38 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface SubmitsResultsTestsDAO extends GenericDAO<SubmitsResultsTests,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildSubmitsResultsTestsDAO()
	 */
	  	 
	/**
	 * Find SubmitsResultsTests by submitsid
	 */
	public List<SubmitsResultsTests> findBySubmitsid(Integer submitsid);

	/**
	 * Find SubmitsResultsTests by resultsid
	 */
	public List<SubmitsResultsTests> findByResultsid(Integer resultsid);

	/**
	 * Find SubmitsResultsTests by testsid
	 */
	public List<SubmitsResultsTests> findByTestsid(Integer testsid);

}