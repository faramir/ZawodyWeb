package org.zawodyweb.database;

import java.util.List;
import java.sql.Timestamp;

import org.zawodyweb.database.pojo.Tasks;
/**
 * <p>Generic DAO layer for Taskss</p>
 * <p>Generated at Thu Mar 05 04:19:39 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface TasksDAO extends GenericDAO<Tasks,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildTasksDAO()
	 */
	  	 
	/**
	 * Find Tasks by name
	 */
	public List<Tasks> findByName(String name);

	/**
	 * Find Tasks by text
	 */
	public List<Tasks> findByText(String text);

	/**
	 * Find Tasks by pdf
	 */
	public List<Tasks> findByPdf(byte[] pdf);

	/**
	 * Find Tasks by abbrev
	 */
	public List<Tasks> findByAbbrev(String abbrev);

	/**
	 * Find Tasks by memlimit
	 */
	public List<Tasks> findByMemlimit(Integer memlimit);

	/**
	 * Find Tasks by seriesid
	 */
	public List<Tasks> findBySeriesid(Integer seriesid);

	/**
	 * Find Tasks by classesid
	 */
	public List<Tasks> findByClassesid(Integer classesid);

}