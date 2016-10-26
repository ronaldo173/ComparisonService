package software.sigma.comparissonservice.dao;

import java.util.List;

public interface GenericDao<T> {
	/**
	 * Save object {@link t} in storage.
	 * 
	 * @param t
	 *            is object for saving
	 * @return true if saved successful, false if not
	 */
	boolean save(T t);

	T getById(int id);

	List<T> getAll();

	boolean update(T t);

	boolean delete(int id);

}
