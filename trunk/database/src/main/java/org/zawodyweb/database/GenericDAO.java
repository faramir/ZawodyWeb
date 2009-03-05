package org.zawodyweb.database;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Generated at Thu Mar 05 04:19:39 CET 2009
 *
 * @author Salto-db Generator v1.1 / EJB3 + Hibernate DAO
 * @see http://www.hibernate.org/328.html
 */
public interface GenericDAO<T, ID extends Serializable> {

	T getById(ID id, boolean lock);

	T getById(ID id);
	
	T loadById(ID id);

	List<T> findAll();
	
	List<T> findByCriteria(Map criterias);
	
	public List<T> findByExample(T exampleInstance, String[] excludeProperty);

	void save(T entity);

	void update(T entity);

	void saveOrUpdate(T entity);

	void delete(T entity);
	
	void deleteById(ID id);
	
}