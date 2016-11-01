package software.sigma.comparissonservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.protocol.EntityList;
import software.sigma.comparissonservice.protocol.Response;
import software.sigma.comparissonservice.service.ConfigurationService;

/**
 * Rest controller responsible for mapping requests related to configurations.
 * 
 * @author alexandr.efimov
 *
 */
@RestController
public final class ConfigurationRestController {

	@Autowired
	private ConfigurationService configService;

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(ConfigurationRestController.class);

	/**
	 * Set service object.
	 * 
	 * @param service
	 *            is object for work with configurations
	 */
	public void setService(final ConfigurationService service) {
		this.configService = service;
	}

	/**
	 * Get list with configurations.
	 * 
	 * @return list with objects of {@link Configuration} class
	 */
	@RequestMapping(path = "/configuration", produces = MediaType.APPLICATION_XML_VALUE)
	public EntityList<ConfigurationProtocol> getAllConfigs() {
		LOGGER.debug("/configuration mapped");
		List<ConfigurationProtocol> listConfigsProtocol = configService.getAll();
		LOGGER.debug("/configuration amount of retrieved configs --> " + listConfigsProtocol.size());
		return new EntityList<>(listConfigsProtocol);
	}

	/**
	 * Get configuration by it's id.
	 * 
	 * @param id
	 *            is id of configuration
	 * @return {@link Configuration} object with id like parameter
	 */
	@RequestMapping(path = "/configuration/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ConfigurationProtocol getConfiguration(@PathVariable("id") final Integer id) {
		LOGGER.debug("/configuration/{id} get config by id -->" + id);
		return configService.getById(id);
	}

	/**
	 * Save configuration, received from user in XML format.
	 * 
	 * @param configurationProtocol
	 *            is config object from user
	 * @return result of operation
	 * @throws ApplicationException
	 *             if can't save
	 */
	@RequestMapping(path = "/configuration", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public Response saveConfiguration(@Valid @RequestBody final ConfigurationProtocol configurationProtocol)
			throws ApplicationException {
		Response response = new Response();

		LOGGER.debug("/configuration POST get object for save: " + configurationProtocol);
		boolean successSave = configService.save(configurationProtocol);
		response.setSuccess(successSave);
		LOGGER.debug("Object saved success: " + successSave);
		return response;
	}

	/**
	 * Update configuration, received from user in XML format.
	 * 
	 * @param id
	 *            is id of config for updating
	 * @param configurationProtocol
	 *            is updated object
	 * @return result of operation
	 * @throws ApplicationException
	 *             if can't update
	 */
	@RequestMapping(path = "/configuration/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_XML_VALUE)
	public Response updateConfiguration(@PathVariable("id") final Integer id,
			@Valid @RequestBody final ConfigurationProtocol configurationProtocol) throws ApplicationException {
		Response response = new Response();

		LOGGER.debug("/configuration PUT, id for update: " + id + "\n get object for update: " + configurationProtocol);
		configurationProtocol.setId(id);

		boolean successSave = configService.update(configurationProtocol);
		response.setSuccess(successSave);

		LOGGER.debug("Object updated success: " + successSave);
		return response;
	}

	@RequestMapping(path = "/configuration/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_XML_VALUE)
	public Response deleteConfiguration(@PathVariable("id") final Integer id) throws ApplicationException {
		Response response = new Response();

		LOGGER.debug("/configuration DELETE, id for DELETE config: " + id);

		boolean successSave = configService.delete(id);
		response.setSuccess(successSave);

		LOGGER.debug("Object deleted success: " + successSave);
		return response;
	}
}
