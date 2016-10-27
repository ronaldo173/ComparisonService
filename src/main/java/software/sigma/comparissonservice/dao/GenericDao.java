package software.sigma.comparissonservice.dao;

import java.util.List;

/**
 * 
 * Interface for a Data Access Object that can be used for a single specified
 * type domain object. A single instance implementing this interface can be used
 * only for the type of domain object specified in the type parameters.
 * 
 * @author alexandr.efimov
 *
 * @param <T>
 *            is any type
 */
public interface GenericDao<T> {
	/**
	 * Save object {@link t}.
	 * 
	 * @param t
	 *            is object for saving
	 * @return true if operation finished successful, false if not
	 */
	boolean save(T t);

	/**
	 * Get object of type {@link T} by it's id.
	 * 
	 * @param id
	 *            is id of object
	 * @return object of type {@link T}
	 */
	T getById(int id);

	/**
	 * Get all objects of type {@link T}.
	 * 
	 * @return {@link List} with objects
	 */
	List<T> getAll();

	/**
	 * Update object.
	 * 
	 * @param t
	 *            is updated object
	 * @return true if successful
	 */
	boolean update(T t);

	/**
	 * Delete object by id.
	 * 
	 * @param id
	 *            is id of object
	 * @return true if successful
	 */
	boolean delete(int id);

}
