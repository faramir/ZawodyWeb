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
