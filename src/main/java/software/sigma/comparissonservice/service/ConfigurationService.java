package software.sigma.comparissonservice.service;

import java.util.List;

import software.sigma.comparissonservice.exception.ApplicationException;
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
	 * @throws ApplicationException
	 *             if can't get with reason in message
	 */
	ConfigurationProtocol getById(int id) throws ApplicationException;

	/**
	 * Get configuration by it's name.
	 * 
	 * @param name
	 *            is name of configuration
	 * @return configuration
	 * @throws ApplicationException
	 *             if can't get with reason in message
	 */
	ConfigurationProtocol getByName(String name) throws ApplicationException;

	/**
	 * Save configuration to storage.
	 * 
	 * @param configuration
	 *            is object for saving
	 * @return true if success
	 * @throws ApplicationException
	 *             if can't save object
	 */
	boolean save(ConfigurationProtocol configuration) throws ApplicationException;

	/**
	 * Update configuration in storage, criteria for update - id of
	 * configuration.
	 * 
	 * @param configurationProtocol
	 *            is updated object
	 * @return true if success, else - false(if id is null also)
	 * @throws ApplicationException
	 *             if can't update object
	 */
	boolean update(ConfigurationProtocol configurationProtocol) throws ApplicationException;

	/**
	 * Delete configuration by id.
	 * 
	 * @param id
	 *            of config for delete
	 * @return true if success
	 * @throws ApplicationException
	 *             if can't delete object
	 */
	boolean delete(Integer id) throws ApplicationException;

}
