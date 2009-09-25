package pl.umk.mat.zawodyweb.database;

import java.util.List;
import java.sql.Timestamp;

import pl.umk.mat.zawodyweb.database.pojo.Roles;
/**
 * <p>Generic DAO layer for Roless</p>
 * <p>Generated at Fri May 08 19:01:00 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface RolesDAO extends GenericDAO<Roles,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildRolesDAO()
	 */
	  	 
	/**
	 * Find Roles by name
	 */
	public List<Roles> findByName(String name);

	/**
	 * Find Roles by edituser
	 */
	public List<Roles> findByEdituser(Boolean edituser);

        /**
	 * Find Roles by addcontest
	 */
	public List<Roles> findByAddcontest(Boolean addcontest);

	/**
	 * Find Roles by editcontest
	 */
	public List<Roles> findByEditcontest(Boolean editcontest);

	/**
	 * Find Roles by delcontest
	 */
	public List<Roles> findByDelcontest(Boolean delcontest);

	/**
	 * Find Roles by addseries
	 */
	public List<Roles> findByAddseries(Boolean addseries);

	/**
	 * Find Roles by editseries
	 */
	public List<Roles> findByEditseries(Boolean editseries);

	/**
	 * Find Roles by delseries
	 */
	public List<Roles> findByDelseries(Boolean delseries);

	/**
	 * Find Roles by addproblem
	 */
	public List<Roles> findByAddproblem(Boolean addproblem);

	/**
	 * Find Roles by editproblem
	 */
	public List<Roles> findByEditproblem(Boolean editproblem);

	/**
	 * Find Roles by delproblem
	 */
	public List<Roles> findByDelproblem(Boolean delproblem);

	/**
	 * Find Roles by canrate
	 */
	public List<Roles> findByCanrate(Boolean canrate);

	/**
	 * Find Roles by contestant
	 */
	public List<Roles> findByContestant(Boolean contestant);

	/**
	 * Find Roles by contestsid
	 */
	public List<Roles> findByContestsid(Integer contestsid);

	/**
	 * Find Roles by seriesid
	 */
	public List<Roles> findBySeriesid(Integer seriesid);

}