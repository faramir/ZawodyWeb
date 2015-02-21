/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database;

import java.util.List;
import pl.umk.mat.zawodyweb.database.pojo.Problems;
/**
 * <p>Generic DAO layer for Problemss</p>
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface ProblemsDAO extends GenericDAO<Problems,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildProblemsDAO()
	 */
	  	 
	/**
	 * Find Problems by name
	 */
	public List<Problems> findByName(String name);

	/**
	 * Find Problems by text
	 */
	public List<Problems> findByText(String text);

	/**
	 * Find Problems by pdf
	 */
	public List<Problems> findByPdf(byte[] pdf);

	/**
	 * Find Problems by abbrev
	 */
	public List<Problems> findByAbbrev(String abbrev);

	/**
	 * Find Problems by memlimit
	 */
	public List<Problems> findByMemlimit(Integer memlimit);

	/**
	 * Find Problems by seriesid
	 */
	public List<Problems> findBySeriesid(Integer seriesid);

	/**
	 * Find Problems by classesid
	 */
	public List<Problems> findByClassesid(Integer classesid);

}
