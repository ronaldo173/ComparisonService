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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.protocol.InputData;
import software.sigma.comparissonservice.utils.CommonUtils;

@Service
public class SortServiceImpl implements SortService {

	@Autowired
	ConfigurationService configService;

	/**
	 * Path to file with schema for sort order validation.
	 */
	private static final String PATH_TO_SORT_ORDER_VALIDATON_SCHEMA_FILE = "SortOrderValidatonSchema.xsd";

	public void setConfigService(ConfigurationService configService) {
		this.configService = configService;
	}

	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(SortServiceImpl.class);
	/**
	 * Message than there is no schema for validating of current data for sort.
	 */
	private static final String ERR_MESSAGE_NO_VALID_SCHEMA_FOR_DATA = "Valid XSD schema not found for input data";

	/**
	 * Message than sort order is not valid.
	 */
	private static final String ERR_MESSAGE_NOT_VALID_SORT_ORDER = "Sort order isn't valid!";

	/**
	 * Validate content of XML, trying to find according to XSD configuration.
	 * 
	 * @param xmlForCheck
	 *            is string with content of XML file for validation
	 * @return true if success
	 */
	final boolean validateXmlContentForSort(final String xmlForCheck) {
		LOGGER.debug("Validate input data with objects to xsd.");
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
	 * Validate sort order according to scheme for sort order.
	 * 
	 * @param xmlSortOrder
	 *            is content of sort order
	 * @return true if valid
	 */
	final boolean validateXmlSortOrderToSchema(final String xmlSortOrder) {

		LOGGER.debug("Validate sort order to schema");
		Resource resourceFileSchema = new ClassPathResource(PATH_TO_SORT_ORDER_VALIDATON_SCHEMA_FILE);

		String schemaContentForOrder;
		try {
			schemaContentForOrder = CommonUtils.readFileToString(resourceFileSchema.getFile().getPath());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}

		return isValidToConfig(xmlSortOrder, schemaContentForOrder);
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

		validateInputData(inputData);
		// TODO implement converting to objects, sorting

	}

	@Override
	public boolean validateInputData(final InputData inputData) throws ApplicationException {
		boolean isValidDataForSort = validateXmlContentForSort(inputData.getDataForSort().trim());
		boolean isValidSortOrderToSchema = validateXmlSortOrderToSchema(inputData.getSortOrder().trim());

		if (!isValidDataForSort) {
			throw new ApplicationException(ERR_MESSAGE_NO_VALID_SCHEMA_FOR_DATA);
		}
		if (!isValidSortOrderToSchema) {
			throw new ApplicationException(ERR_MESSAGE_NOT_VALID_SORT_ORDER);
		}
		return isValidDataForSort && isValidSortOrderToSchema;
	}

}
