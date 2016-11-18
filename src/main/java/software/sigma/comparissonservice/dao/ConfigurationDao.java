package software.sigma.comparissonservice.dao;

import java.util.List;

import software.sigma.comparissonservice.model.Configuration;

/**
 * 
 * Interface for a Data Access Object that can be used for a
 * {@link Configuration} objects.
 * 
 * @author alexandr.efimov
 *
 */
public interface ConfigurationDao {
	/**
	 * Save configuration in storage.
	 * 
	 * @param configuration
	 *            is object for saving
	 * @return true if operation finished successful, false if not
	 */
	boolean save(Configuration configuration);

	/**
	 * Get configuration by it's id.
	 * 
	 * @param id
	 *            is id of object
	 * @return found object by id or null if not exist with id
	 */
	Configuration getById(int id);

	/**
	 * Get all configurations.
	 * 
	 * @return {@link List} with configurations
	 */
	List<Configuration> getAll();

	/**
	 * Update configuration.
	 * 
	 * @param configuration
	 *            is updated object
	 * @return true if successful
	 */
	boolean update(Configuration configuration);

	/**
	 * Delete configuration by id.
	 * 
	 * @param id
	 *            is id of configuration
	 * @return true if successful
	 */
	boolean delete(int id);

	/**
	 * Get configuration by it's name.
	 * 
	 * @param name
	 *            is name of config
	 * @return found object by name or null if not exist with id
	 */
	Configuration getByName(String name);

}
