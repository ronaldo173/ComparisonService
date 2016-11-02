package software.sigma.comparissonservice.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.protocol.InputData;

@Service
public class SortServiceImpl implements SortService {

	@Autowired
	ConfigurationService configService;

	public void setConfigService(ConfigurationService configService) {
		this.configService = configService;
	}

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(SortServiceImpl.class);
	/**
	 * Message than there is no schema for validating of current data.
	 */
	private static final String ERR_MESSAGE_NO_VALID_SCHEMA_FOR_DATA = "Valid XSD schema not found for input data";

	/**
	 * Validate content of XML, trying to find according to XSD configuration.
	 * 
	 * @param xmlForCheck
	 *            is string with content of XML file for validation
	 * @return true if success
	 */
	private boolean validateXmlContentForSort(final String xmlForCheck) {
		LOGGER.debug("Validate input data to xsd.");
		boolean isValid = false;

		List<ConfigurationProtocol> allConfigsIdentifiers = configService.getAll();
		for (ConfigurationProtocol configurationProtocol : allConfigsIdentifiers) {
			ConfigurationProtocol configById = configService.getById(configurationProtocol.getId());
			isValid = isValidToConfig(xmlForCheck, configById.getConfigContent());

			if (isValid) {
				LOGGER.debug(
						"Validation xml to xsd success, config: " + configById.getId() + ", " + configById.getName());
				break;
			}
		}
		return isValid;
	}

	/**
	 * Check if {@link String xmlContent} valid according to
	 * {@link ConfigurationProtocol xsd schema}.
	 * 
	 * @param xmlContent
	 *            is string with content of xml for validation
	 * @param xsdSchema
	 *            is xsd schema
	 * @return true if valid
	 */
	private boolean isValidToConfig(final String xmlContent, final String xsdSchema) {
		if (xmlContent == null || xsdSchema == null) {
			return false;
		}

		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = factory.newSchema(new StreamSource(new StringReader(xsdSchema)));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new StringReader(xmlContent)));

		} catch (SAXException | IOException e) {
			return false;
		}

		return true;
	}

	@Override
	public void sort(final InputData inputData) throws ApplicationException {

		boolean isValidDataForSort = validateInputData(inputData);

		if (!isValidDataForSort) {
			throw new ApplicationException(ERR_MESSAGE_NO_VALID_SCHEMA_FOR_DATA);
		}
	}

	@Override
	public boolean validateInputData(final InputData inputData) {
		boolean isValidDataForSort = validateXmlContentForSort(inputData.getDataForSort().trim());

		return isValidDataForSort;
	}

}
