package software.sigma.comparissonservice.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.model.EntityList;
import software.sigma.comparissonservice.service.ConfigurationService;
import software.sigma.comparissonservice.service.GenericSerice;

/**
 * Rest controller responsible for mapping requests connected to configurations.
 * 
 * @author alexandr.efimov
 *
 */
@RestController
public final class ConfigurationRestController {

	@Autowired
	private GenericSerice<Configuration> service;

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
		this.service = service;
	}

	/**
	 * Get list with configurations.
	 * 
	 * @return list with objects of {@link Configuration} class
	 */
	@RequestMapping(path = "/configuration", produces = MediaType.APPLICATION_XML_VALUE)
	public EntityList<Configuration> getAllConfigs() {
		List<Configuration> allConfigs = service.getAll();
		LOGGER.info("/configuration amount of retrieved configs --> " + allConfigs.size());
		return new EntityList<Configuration>(allConfigs);
	}

	/**
	 * Get configuration by it's id.
	 * 
	 * @param id
	 *            is id of configuration
	 * @return {@link Configuration} object with id like parameter
	 */
	@RequestMapping(path = "/configuration/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public Configuration getConfiguration(@PathVariable("id") final Integer id) {
		LOGGER.info("/configuration/{id} get config by id -->" + id);
		return service.getById(id);
	}

}
