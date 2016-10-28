package software.sigma.comparissonservice.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
		LOGGER.info("/configuration mapped");
		List<ConfigurationProtocol> listConfigsProtocol = configService.getAll();
		LOGGER.info("/configuration amount of retrieved configs --> " + listConfigsProtocol.size());
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
		LOGGER.info("/configuration/{id} get config by id -->" + id);
		return configService.getById(id);
	}

	/**
	 * Save configuration, received from user in XML format.
	 * 
	 * @param configurationProtocol
	 *            is config object from user
	 * @return result of operation
	 */
	@RequestMapping(path = "/configuration", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public Response testRequestBody(@RequestBody final ConfigurationProtocol configurationProtocol) {
		Response response = new Response();

		LOGGER.info("/configuration POST get object: " + configurationProtocol.getName() + ", config length: "
				+ configurationProtocol.getConfigContent().length());
		boolean successSave = configService.save(configurationProtocol);
		response.setSuccess(successSave);
		LOGGER.info("Object saved: " + successSave);
		return response;
	}

}
