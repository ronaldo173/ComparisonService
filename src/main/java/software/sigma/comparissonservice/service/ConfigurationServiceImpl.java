package software.sigma.comparissonservice.service;

import java.io.StringReader;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import software.sigma.comparissonservice.dao.ConfigurationDao;
import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.vo.ConfigurationVO;
import software.sigma.comparissonservice.vo.ConverterVoDomain;

/**
 * Implementation of {@link ConfigurationService} for work with
 * {@link Configuration}.
 * 
 * @author alexandr.efimov
 *
 */
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	private static final String ERR_MESSAGE_NOT_VALID_CONFIG_CONTENT = "Configuration content(xsd schema) is not valid";

	private static final String ERR_MESSAGE_NOT_FOUND_CONFIG_BY_ID = "Configuration with your id not exists! Id: ";

	private static final String ERR_MESSAGE_NOT_FOUND_CONFIG_BY_NAME = "Configuration with your name not exists! Name: ";
	private static final String ERR_MESSAGE_CANT_UPDATE_CONFIG = "Can't update configuration. Id: ";

	@Autowired
	private ConfigurationDao dao;

	public final void setDao(final ConfigurationDao dao) {
		this.dao = dao;
	}

	@Override
	public List<ConfigurationVO> getAll() {
		List<Configuration> configurations = dao.getAll();
		return ConverterVoDomain.convertToProtocolList(configurations);
	}

	@Override
	public ConfigurationVO getById(final int id) throws ApplicationException {
		Configuration configById = dao.getById(id);
		if (configById == null) {
			throw new ApplicationException(ERR_MESSAGE_NOT_FOUND_CONFIG_BY_ID + id);
		}
		return ConverterVoDomain.convert(configById);
	}

	@Override
	public boolean save(final ConfigurationVO configuration) throws ApplicationException {
		boolean saveSuccess = false;

		if (validateConfigContent(configuration.getConfigContent())) {
			saveSuccess = dao.save(ConverterVoDomain.convert(configuration));
		} else {
			throw new ApplicationException(ERR_MESSAGE_NOT_VALID_CONFIG_CONTENT);
		}
		return saveSuccess;
	}

	@Override
	public boolean update(final ConfigurationVO configurationProtocol) throws ApplicationException {
		boolean updateSuccess = false;

		String configContent = configurationProtocol.getConfigContent();
		if (validateConfigContent(configContent)) {
			try {

				updateSuccess = dao.update(ConverterVoDomain.convert(configurationProtocol));
			} catch (Throwable e) {
				String errMessage = ERR_MESSAGE_CANT_UPDATE_CONFIG + configurationProtocol.getId();
				if (e.getCause() != null) {
					errMessage += ", " + e.getCause().getMessage();
				}
				throw new ApplicationException(errMessage, e);
			}
		} else {
			throw new ApplicationException(ERR_MESSAGE_NOT_VALID_CONFIG_CONTENT);
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

	@Override
	public ConfigurationVO getByName(final String name) throws ApplicationException {

		Configuration configByName;
		try {
			configByName = dao.getByName(name);
		} catch (Throwable e) {
			throw new ApplicationException(ERR_MESSAGE_NOT_FOUND_CONFIG_BY_NAME + name, e);
		}

		return ConverterVoDomain.convert(configByName);
	}

}
