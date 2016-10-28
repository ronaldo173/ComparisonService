package software.sigma.comparissonservice.service;

import java.util.List;

import software.sigma.comparissonservice.protocol.ConfigurationProtocol;

/**
 * Configuration service with business operations.
 * 
 * @author alexandr.efimov
 *
 */
public interface ConfigurationService {

	/**
	 * Get all configurations from storage.
	 * 
	 * @return list with objects
	 */
	List<ConfigurationProtocol> getAll();

	/**
	 * Get configuration by id.
	 * 
	 * @param id
	 *            is id of configuration
	 * @return configuration
	 */
	ConfigurationProtocol getById(int id);

	/**
	 * Save configuration to storage.
	 * 
	 * @param configuration
	 *            is object for saving
	 * @return true if success
	 */
	boolean save(ConfigurationProtocol configuration);

}
