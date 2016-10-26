package software.sigma.comparissonservice.dao;

import java.util.List;

public interface GenericDao<T> {
	boolean save(T t);

	T getById(int id);

	List<T> getAll();

	boolean update(T t);

	boolean delete(int id);

}
