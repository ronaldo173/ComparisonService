package software.sigma.comparissonservice.service;

import java.io.StringReader;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import software.sigma.comparissonservice.constants.ErrorConstants;
import software.sigma.comparissonservice.dao.ConfigurationDao;
import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.utils.ConfigurationsConverter;

/**
 * Implementation of {@link ConfigurationService} for work with
 * {@link Configuration}.
 * 
 * @author alexandr.efimov
 *
 */
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	private ConfigurationDao dao;

	public final void setDao(final ConfigurationDao dao) {
		this.dao = dao;
	}

	@Override
	public List<ConfigurationProtocol> getAll() {
		List<Configuration> configurations = dao.getAll();
		return ConfigurationsConverter.convertToProtocolList(configurations);
	}

	@Override
	public ConfigurationProtocol getById(final int id) {
		Configuration configById = dao.getById(id);
		return ConfigurationsConverter.convert(configById);
	}

	@Override
	public boolean save(final ConfigurationProtocol configuration) throws ApplicationException {
		boolean saveSuccess = false;

		if (validateConfigContent(configuration.getConfigContent())) {
			saveSuccess = dao.save(ConfigurationsConverter.convert(configuration));
		} else {
			throw new ApplicationException(ErrorConstants.ERR_NOT_VALID_CONFIG_CONTENT.getValue());
		}
		return saveSuccess;
	}

	@Override
	public boolean update(final ConfigurationProtocol configurationProtocol) throws ApplicationException {
		boolean updateSuccess = false;

		String configContent = configurationProtocol.getConfigContent();
		if (validateConfigContent(configContent)) {
			try {

				updateSuccess = dao.update(ConfigurationsConverter.convert(configurationProtocol));
			} catch (Throwable e) {
				throw new ApplicationException("Can't update configuration. Id: " + configurationProtocol.getId(), e);
			}
		} else {
			throw new ApplicationException(ErrorConstants.ERR_NOT_VALID_CONFIG_CONTENT.getValue());
		}

		return updateSuccess;

	}

	/**
	 * Validate xsd schema according to
	 * {@link http://www.w3.org/2001/XMLSchema}.
	 * 
	 * @param configContent
	 *            content of schema
	 * @return true if valid xsd schema
	 */
	private boolean validateConfigContent(final String configContent) {
		boolean isValid = true;

		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			factory.newSchema(new StreamSource(new StringReader(configContent.trim())));
		} catch (SAXException e) {
			isValid = false;
		}
		return isValid;
	}

	@Override
	public boolean delete(final Integer id) throws ApplicationException {
		boolean deleteSuccess = false;
		try {
			deleteSuccess = dao.delete(id);
		} catch (Throwable e) {
			throw new ApplicationException("Can't delete configuration. Id: " + id, e);
		}

		return deleteSuccess;
	}

}
