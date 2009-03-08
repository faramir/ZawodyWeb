package pl.umk.mat.zawodyweb.database;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.SeriesRoles;
/**
 * <p>Generic DAO layer for SeriesRoless</p>
 * <p>Generated at Sun Mar 08 19:45:31 CET 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface SeriesRolesDAO extends GenericDAO<SeriesRoles,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildSeriesRolesDAO()
	 */
	  	 
	/**
	 * Find SeriesRoles by seriesid
	 */
	public List<SeriesRoles> findBySeriesid(Integer seriesid);

	/**
	 * Find SeriesRoles by rolesid
	 */
	public List<SeriesRoles> findByRolesid(Integer rolesid);

}