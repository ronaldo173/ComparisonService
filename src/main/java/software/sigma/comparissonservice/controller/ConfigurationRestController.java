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
import software.sigma.comparissonservice.protocol.ConfigurationDTO;
import software.sigma.comparissonservice.protocol.ConfigurationsDTO;
import software.sigma.comparissonservice.protocol.ConverterDtoVo;
import software.sigma.comparissonservice.protocol.ResponseDTO;
import software.sigma.comparissonservice.service.ConfigurationService;
import software.sigma.comparissonservice.vo.ConfigurationVO;

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
	public ConfigurationsDTO getAllConfigs() {
		LOGGER.debug("/configuration mapped");
		List<ConfigurationVO> listConfigs = configService.getAll();
		LOGGER.debug("/configuration amount of retrieved configs --> " + listConfigs.size());
		return new ConfigurationsDTO(ConverterDtoVo.convert(listConfigs));
	}

	/**
	 * Get configuration by it's id.
	 * 
	 * @param id
	 *            is id of configuration
	 * @return {@link Configuration} object with id like parameter
	 * @throws ApplicationException
	 *             if can't get config
	 */
	@RequestMapping(path = "/configuration/{id}", produces = MediaType.APPLICATION_XML_VALUE)
	public ConfigurationDTO getConfiguration(@PathVariable("id") final Integer id) throws ApplicationException {
		LOGGER.debug("/configuration/{id} get config by id -->" + id);
		return ConverterDtoVo.convert(configService.getById(id));
	}

	/**
	 * Save configuration, received from user in XML format.
	 * 
	 * @param configuration
	 *            is config object from user
	 * @return result of operation
	 * @throws ApplicationException
	 *             if can't save
	 */
	@RequestMapping(path = "/configuration", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseDTO saveConfiguration(@Valid @RequestBody final ConfigurationDTO configuration)
			throws ApplicationException {
		ResponseDTO response = new ResponseDTO();

		LOGGER.debug("/configuration POST get object for save: " + configuration);
		boolean successSave = configService.save(ConverterDtoVo.convert(configuration));
		response.setSuccess(successSave);
		LOGGER.debug("Object saved success: " + successSave);
		return response;
	}

	/**
	 * Update configuration, received from user in XML format.
	 * 
	 * @param id
	 *            is id of config for updating
	 * @param configuration
	 *            is updated object
	 * @return result of operation
	 * @throws ApplicationException
	 *             if can't update
	 */
	@RequestMapping(path = "/configuration/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseDTO updateConfiguration(@PathVariable("id") final Integer id,
			@Valid @RequestBody final ConfigurationDTO configuration) throws ApplicationException {
		ResponseDTO response = new ResponseDTO();

		LOGGER.debug(
				"/configuration PUT, id for update: " + id + "\n get object for update: " + configuration.getName());
		configuration.setId(id);

		boolean successSave = configService.update(ConverterDtoVo.convert(configuration));
		response.setSuccess(successSave);

		LOGGER.debug("Object updated success: " + successSave);
		return response;
	}

	/**
	 * Delete configuration by it id.
	 * 
	 * @param id
	 *            is id of configuration
	 * @return response
	 * @throws ApplicationException
	 *             if smth happened
	 */
	@RequestMapping(path = "/configuration/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseDTO deleteConfiguration(@PathVariable("id") final Integer id) throws ApplicationException {
		ResponseDTO response = new ResponseDTO();

		LOGGER.debug("/configuration DELETE, id for DELETE config: " + id);

		boolean successSave = configService.delete(id);
		response.setSuccess(successSave);

		LOGGER.debug("Object deleted success: " + successSave);
		return response;
	}

}
