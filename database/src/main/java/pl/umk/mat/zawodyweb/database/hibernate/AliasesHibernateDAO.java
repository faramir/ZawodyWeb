/*
 * Copyright (c) 2009-2014, ZawodyWeb Team
 * All rights reserved.
 *
 * This file is distributable under the Simplified BSD license. See the terms
 * of the Simplified BSD license in the documentation provided with this file.
 */
package pl.umk.mat.zawodyweb.database.hibernate;

import java.util.List;


import org.hibernate.criterion.Restrictions;
import pl.umk.mat.zawodyweb.database.AliasesDAO;
import pl.umk.mat.zawodyweb.database.pojo.Aliases;

/**
 * @author faramir
 * @see http://www.hibernate.org/328.html
 */
public class AliasesHibernateDAO extends
		AbstractHibernateDAO<Aliases, Integer> implements
		AliasesDAO {

	/**
	 * Find Classes by filename
	 */
	public List<Aliases> findByName(String name) {
		return findByCriteria(Restrictions.eq("name", name));
	}
}
