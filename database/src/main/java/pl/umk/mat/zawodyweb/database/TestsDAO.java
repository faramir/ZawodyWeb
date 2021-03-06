/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database;

import java.util.List;
import pl.umk.mat.zawodyweb.database.pojo.Tests;
/**
 * <p>Generic DAO layer for Testss</p>
 * <p>Generated at Fri May 08 19:00:59 CEST 2009</p>
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface TestsDAO extends GenericDAO<Tests,Integer> {

	/*
	 * TODO : Add specific businesses daos here.
	 * These methods will be overwrited if you re-generate this interface.
	 * You might want to extend this interface and to change the dao factory to return 
	 * an instance of the new implemenation in buildTestsDAO()
	 */
	  	 
	/**
	 * Find Tests by input
	 */
	public List<Tests> findByInput(String input);

	/**
	 * Find Tests by output
	 */
	public List<Tests> findByOutput(String output);

	/**
	 * Find Tests by timelimit
	 */
	public List<Tests> findByTimelimit(Integer timelimit);

	/**
	 * Find Tests by maxpoints
	 */
	public List<Tests> findByMaxpoints(Integer maxpoints);

	/**
	 * Find Tests by visibility
	 */
	public List<Tests> findByVisibility(Integer visibility);

	/**
	 * Find Tests by problemsid
	 */
	public List<Tests> findByProblemsid(Integer problemsid);

}
