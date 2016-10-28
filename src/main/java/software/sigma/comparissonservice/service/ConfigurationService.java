package software.sigma.comparissonservice.service;

import java.util.List;

import software.sigma.comparissonservice.model.Configuration;
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
	List<Configuration> getAll();

	/**
	 * Get configuration by id.
	 * 
	 * @param id
	 *            is id of configuration
	 * @return configuration
	 */
	Configuration getById(int id);

	/**
	 * Convert domain object {@link Configuration} to
	 * {@link ConfigurationProtocol}.
	 * 
	 * @param configuration
	 *            is object for converting
	 * @return result {@link ConfigurationProtocol} object
	 */
	ConfigurationProtocol convertToProtocol(Configuration configuration);

	/**
	 * Convert from {@link ConfigurationProtocol} to {@link Configuration}.
	 * 
	 * @param configurationProtocol
	 *            is object for converting
	 * @return result {@link Configuration} object
	 */
	Configuration convertFromFrotocol(ConfigurationProtocol configurationProtocol);

	/**
	 * Convert List with {@link Configuration} to {@link ConfigurationProtocol}.
	 * 
	 * @param configurations
	 *            is source list
	 * @return list with results
	 */
	List<ConfigurationProtocol> convertToProtocolList(List<Configuration> configurations);

	/**
	 * Save configuration.
	 * 
	 * @param configuration
	 *            is object for saving
	 * @return true if success
	 */
	boolean save(Configuration configuration);

}
