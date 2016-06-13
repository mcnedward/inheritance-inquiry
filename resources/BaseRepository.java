package com.eatingcinci.app.repository;

import java.util.List;

import com.eatingcinci.app.exception.DeleteException;
import com.eatingcinci.app.exception.EntityNotFoundException;
import com.eatingcinci.app.exception.SaveException;
import com.eatingcinci.app.exception.UpdateException;

/**
 * 
 * @author Edward - Sep 20, 2015
 *
 */
public interface BaseRepository<T> extends CrudRepository, Serializable, Cloneable {

	/**
	 * Save an entity in the database. This does not start or commit a transaction, so remember to do this inside
	 * a @Transactional!
	 * 
	 * @param entity
	 *            The entity to save in the database.
	 * @return True if the entity was persisted, false otherwise.
	 * @throws SaveException
	 */
	public boolean save(T entity) throws SaveException;

	/**
	 * Save an entity if it does not exist yet, or update an existing entity. This requires a transaction to be started
	 * and committed, it will not do it for you.
	 * 
	 * @param entity
	 *            The entity to save or update.
	 * @return The entity that has been saved or updated.
	 * @throws SaveException
	 */
	public T saveOrUpdate(T entity) throws SaveException;

	public T update(T entity) throws UpdateException;

	public boolean delete(Class<T> entityClass, int id) throws DeleteException;

	public T get(int id) throws EntityNotFoundException;

	/**
	 * Find all entities of a certain class in the database.
	 * 
	 * @param entityClass
	 *            The name of the entity class you want to find.
	 * @return The list of all entities of a certain class.
	 */
	public List<T> findAll(String entityClass);

}
