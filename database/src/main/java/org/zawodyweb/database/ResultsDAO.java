package org.zawodyweb.database;

import java.util.List;
import java.sql.Timestamp;

import org.zawodyweb.database.pojo.Results;
/**
 * <p>Generic DAO layer for Resultss</p>
 * <p>Generated at Thu Mar 05 04:19:38 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface ResultsDAO extends GenericDAO<Results,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildResultsDAO()
	 */
	  	 
	/**
	 * Find Results by points
	 */
	public List<Results> findByPoints(Integer points);

	/**
	 * Find Results by runtime
	 */
	public List<Results> findByRuntime(Integer runtime);

	/**
	 * Find Results by memory
	 */
	public List<Results> findByMemory(Integer memory);

	/**
	 * Find Results by notes
	 */
	public List<Results> findByNotes(String notes);

	/**
	 * Find Results by submitid
	 */
	public List<Results> findBySubmitid(Integer submitid);

	/**
	 * Find Results by testsid
	 */
	public List<Results> findByTestsid(Integer testsid);

}