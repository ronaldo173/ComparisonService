package software.sigma.comparissonservice.service;

import java.util.List;

/**
 * Generic service with business operations.
 * 
 * @author alexandr.efimov
 *
 ** @param <T>
 *            is any type
 */
public interface GenericSerice<T> {

	/**
	 * Get all objects of type {@link T} from storage.
	 * 
	 * @return list with objects
	 */
	List<T> getAll();

	/**
	 * Get object by it's id.
	 * 
	 * @param id
	 *            is id of object
	 * @return object
	 */
	T getById(int id);
}
