package software.sigma.comparissonservice.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.protocol.InputData;
import software.sigma.comparissonservice.protocol.Response;
import software.sigma.comparissonservice.utils.CommonUtils;

@Service
public class SortServiceImpl implements SortService {

	private static final String SORT_ORDER_ATTR_NAME = "name";
	private static final String SORT_ORDER_ATTR_ORDERING = "ordering";
	private static final String MESSAGE_FOUND_VALID_CONFIG = "Found valid configuration, name: ";
	private static final String MESSAGE_VALIDATION_DATA_FOR_SORT_SUCCESS = "Validation data for sort success: ";
	private static final String MESSAGE_VALIDATION_SORT_ORDER_SUCCESS = "Validation sort order success: ";;
	private static final String ERR_MESSAGE_CANT_GET_CONFIG_BY_NAME = "Can't get from service config by name, ";

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
	 * Validate content of XML, trying to find according to XSD configuration if
	 * {@link inputData} param not contain name of configuration with schema,
	 * else validate according to schema by schema name.
	 * 
	 * @param inputData
	 *            is object with input data
	 * @param response
	 *            is object for adding information about validation
	 * @return true if success
	 */
	final boolean validateXmlContentForSort(final InputData inputData, final Response response) {
		LOGGER.debug("Validate input data with objects to xsd.");

		boolean isValid = false;
		String dataForValidation = inputData.getDataForSort().trim();

		LOGGER.debug("Try to find config by name, NAME --> " + inputData.getConfigName());
		if (inputData.getConfigName() != null) {
			ConfigurationProtocol configByName;
			try {
				configByName = configService.getByName(inputData.getConfigName());
				isValid = isValidToConfig(dataForValidation, configByName.getConfigContent());
			} catch (ApplicationException e) {
				LOGGER.debug(ERR_MESSAGE_CANT_GET_CONFIG_BY_NAME + e.getMessage(), e);
				response.getErrors().add(ERR_MESSAGE_CANT_GET_CONFIG_BY_NAME + e.getMessage());
			}

		} else {
			List<ConfigurationProtocol> allConfigsIdentifiers = configService.getAll();
			for (ConfigurationProtocol configurationProtocol : allConfigsIdentifiers) {
				ConfigurationProtocol configById = null;
				try {
					configById = configService.getById(configurationProtocol.getId());
				} catch (ApplicationException e) {
					continue;
				}
				isValid = isValidToConfig(dataForValidation, configById.getConfigContent());

				if (isValid) {
					LOGGER.debug(MESSAGE_FOUND_VALID_CONFIG + configById.getName());
					response.getInformationMessages().add(MESSAGE_FOUND_VALID_CONFIG + configById.getName());
					break;
				}
			}
		}
		response.getInformationMessages().add(MESSAGE_VALIDATION_DATA_FOR_SORT_SUCCESS + isValid);
		return isValid;
	}

	/**
	 * Validate sort order according to scheme for sort order.
	 * 
	 * @param xmlSortOrder
	 *            is content of sort order
	 * @param response
	 *            is object for adding info messages
	 * @return true if valid
	 */
	final boolean validateXmlSortOrderToSchema(final String xmlSortOrder, Response response) {

		LOGGER.debug("Validate sort order to schema");
		Resource resourceFileSchema = new ClassPathResource(PATH_TO_SORT_ORDER_VALIDATON_SCHEMA_FILE);

		String schemaContentForOrder;
		try {
			schemaContentForOrder = CommonUtils.readFileToString(resourceFileSchema.getFile().getPath());
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}

		boolean isValidToConfig = isValidToConfig(xmlSortOrder, schemaContentForOrder);
		response.getInformationMessages().add(MESSAGE_VALIDATION_SORT_ORDER_SUCCESS + isValidToConfig);
		return isValidToConfig;
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
	public Response sort(final InputData inputData) throws ApplicationException {

		Response response = new Response();
		response.setInformationMessages(new ArrayList<String>());
		boolean isValid = validateInputData(inputData, response);

		// TODO implement converting to objects, sorting
		if (isValid) {
			Map<String, String> mapOrderNamesOrdering = getOrderingsFromXml(inputData.getSortOrder());
			LOGGER.debug("Sort order parsed to: " + mapOrderNamesOrdering.entrySet());

		}

		response.setSuccess(isValid);
		return response;
	}

	/**
	 * Parse sort order from xml to map in format:
	 * 
	 * key - name of field value - ordering(asc or desc).
	 * 
	 * @param sortOrder
	 *            is content of XML file with sort order
	 * @return map with sort order
	 * @throws ApplicationException
	 *             if can't parse
	 */
	final Map<String, String> getOrderingsFromXml(String sortOrder) throws ApplicationException {
		Map<String, String> map = new LinkedHashMap<>();
		sortOrder = sortOrder.trim();

		String expressionOrderField = "/order/field";

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(sortOrder)));
			XPath xPath = XPathFactory.newInstance().newXPath();

			NodeList nodeList = (NodeList) xPath.compile(expressionOrderField).evaluate(document,
					XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				NamedNodeMap attributes = nodeList.item(i).getAttributes();
				Node nameAttrNode = attributes.getNamedItem(SORT_ORDER_ATTR_NAME);
				Node orderingAttrNode = attributes.getNamedItem(SORT_ORDER_ATTR_ORDERING);
				String nameAttr = null;
				String orderingAttr = null;
				if (nameAttrNode != null) {
					nameAttr = nameAttrNode.getNodeValue();
				}
				if (orderingAttrNode != null) {
					orderingAttr = orderingAttrNode.getNodeValue();
				}
				map.put(nameAttr, orderingAttr);
			}

		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
			String errorMessage = "Can't parse sort order";
			throw new ApplicationException(errorMessage + ", " + e.getMessage(), e);
		}

		return map;
	}

	@Override
	public boolean validateInputData(final InputData inputData, Response response) throws ApplicationException {
		if (response == null) {
			response = new Response();
		}
		if (response.getInformationMessages() == null) {
			response.setInformationMessages(new ArrayList<String>());
		}
		boolean isValidDataForSort = validateXmlContentForSort(inputData, response);
		boolean isValidSortOrderToSchema = validateXmlSortOrderToSchema(inputData.getSortOrder().trim(), response);

		return isValidDataForSort && isValidSortOrderToSchema;
	}

}
