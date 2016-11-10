package software.sigma.comparissonservice.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import software.sigma.comparissonservice.comparator.NodeComparatorByOrder;
import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.protocol.InputData;
import software.sigma.comparissonservice.protocol.Response;
import software.sigma.comparissonservice.protocol.SortOrderField;
import software.sigma.comparissonservice.protocol.SortOrderFields;

@Service
public class SortServiceImpl implements SortService {

	private static final Logger LOGGER = Logger.getLogger(SortServiceImpl.class);

	private static final String ERR_MESSAGE_CANT_GET_CONFIG_BY_NAME = "Can't get from service config by name, ";
	private static final String ERR_MESSAGE_VALIDATION_SORT_ORDER_TO_DATA = "Validation sort order to data for sort - not enough occurances of elements with names as in sort order";
	private static final String ERR_MESSAGE_CANT_FIND_CONFIG_FOR_DATA = "Can't find configuration according to your data for sort";
	private static final String ERR_MESSAGE_EMPTY_DATA_FOR_SORT = "Empty data for sort";
	private static final String ERR_MESSAGE_EMPTY_SORT_ORDER = "Empty sort order";
	private static final String ERR_MESSAGE_EMPTY_DATA = "Empty input data";
	private static final String ERR_MESSAGE_CONFIGURATION_NOT_VALID_NAME = "Configuration with your name not valid, name:";

	@Autowired
	ConfigurationService configService;

	/**
	 * Path to file with schema for sort order validation.
	 */

	public void setConfigService(ConfigurationService configService) {
		this.configService = configService;
	}

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

		String configName = inputData.getConfigName();
		if (configName != null) {
			LOGGER.debug("Try to find config by name, NAME --> " + configName);
			ConfigurationProtocol configByName;
			try {
				configByName = configService.getByName(configName);
				isValid = isValidToConfig(dataForValidation, configByName.getConfigContent());

				if (!isValid) {
					response.getErrors().add(ERR_MESSAGE_CONFIGURATION_NOT_VALID_NAME + configName);
					LOGGER.debug("Config by name not valid");
				}

			} catch (ApplicationException e) {
				LOGGER.debug("Can't get from service config by name, " + e.getMessage(), e);
				response.getErrors().add(ERR_MESSAGE_CANT_GET_CONFIG_BY_NAME + e.getMessage());
			}

		} else {
			List<ConfigurationProtocol> allConfigsIdentifiers = configService.getAll();
			for (ConfigurationProtocol configProtocol : allConfigsIdentifiers) {
				ConfigurationProtocol configById = null;
				try {
					configById = configService.getById(configProtocol.getId());
				} catch (ApplicationException e) {
					continue;
				}
				isValid = isValidToConfig(dataForValidation, configById.getConfigContent());

				if (isValid) {
					String nameOfConfig = configById.getName();
					LOGGER.debug("Found valid configuration, name: " + nameOfConfig);
					response.setInformationMessage("Configuration name: " + nameOfConfig);
					break;
				}
			}
			if (!isValid) {
				response.getErrors().add(ERR_MESSAGE_CANT_FIND_CONFIG_FOR_DATA);
				LOGGER.debug("Valid config not found");
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

	/**
	 * Parse content of XML file(with root element and list of nodes) to
	 * {@link List} with nodes.
	 * 
	 * @param xmlContent
	 *            for parsing
	 * @return list with parsed nodes
	 * @throws ApplicationException
	 *             if can't parse xml document
	 */
	private List<Node> getListNodesFromXmlContent(final String xmlContent) throws ApplicationException {

		List<Node> resultList = null;
		StringBuilder expression = new StringBuilder("/");

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(xmlContent)));
			XPath xPath = XPathFactory.newInstance().newXPath();
			Node nodeDoc = (Node) xPath.compile(expression.toString()).evaluate(document, XPathConstants.NODE);
			Node rootNode = nodeDoc.getFirstChild();
			expression.append(rootNode.getNodeName());
			Node childNode = rootNode.getFirstChild().getNextSibling();
			expression.append("/" + childNode.getNodeName());

			NodeList nodeList = (NodeList) xPath.compile(expression.toString()).evaluate(document,
					XPathConstants.NODESET);
			resultList = convertNodeListToList(nodeList);
		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
			LOGGER.debug("Can't parse xml to list with nodes", e);
			throw new ApplicationException(e.getMessage());
		}

		return resultList;
	}

	/**
	 * Convert {@link NodeList} to {@link List} with {@link Node}.
	 * 
	 * @param nodeList
	 * @return list with nodes
	 */
	private List<Node> convertNodeListToList(final NodeList nodeList) {
		List<Node> list = new ArrayList<Node>();

		for (int i = 0; i < nodeList.getLength(); i++) {
			list.add(nodeList.item(i));
		}
		return list;
	}

	@Override
	public boolean validateInputData(final InputData inputData, Response response) {
		String dataForSort = inputData.getDataForSort();
		if (dataForSort == null || dataForSort.trim().isEmpty()) {
			response.getErrors().add(ERR_MESSAGE_EMPTY_DATA_FOR_SORT);
			LOGGER.debug("empty data for sort");
			return false;
		}
		if (inputData.getSortOrder() == null || inputData.getSortOrder().getFields() == null
				|| inputData.getSortOrder().getFields().isEmpty()) {
			response.getErrors().add(ERR_MESSAGE_EMPTY_SORT_ORDER);
			LOGGER.debug("empty sort order");
			return false;
		}

		boolean isValidDataForSort = validateXmlContentForSort(inputData, response);
		LOGGER.debug("data for sort validation: " + isValidDataForSort);

		/**
		 * Check data for sort with sort order
		 */
		boolean isValidDataForSortToOrder = false;
		try {
			List<Node> listNodesForSort = getListNodesFromXmlContent(dataForSort);

			if (isValidDataForSort) {
				isValidDataForSortToOrder = validateXmlSortContentToSortOrder(listNodesForSort,
						getSortOrderMap(inputData.getSortOrder()), response);
			}
		} catch (ApplicationException e) {
			response.getErrors().add(e.getMessage());
		}
		LOGGER.debug("data for sort according to sort order validation: " + isValidDataForSortToOrder);

		return isValidDataForSort && isValidDataForSortToOrder;
	}

	@Override
	public Response sort(final InputData inputData) throws ApplicationException {
		if (inputData == null) {
			throw new ApplicationException(ERR_MESSAGE_EMPTY_DATA);
		}
		Response response = new Response();

		LinkedHashMap<String, String> mapOrderNamesOrdering = getSortOrderMap(inputData.getSortOrder());

		String dataForSort = inputData.getDataForSort().trim();
		List<Node> listNodesForSort = getListNodesFromXmlContent(dataForSort);

		Comparator<Node> comparator = getComparatorForNodes(mapOrderNamesOrdering);
		Collections.sort(listNodesForSort, comparator);

		String sortedXmlContent = convertListNodesToStringXml(listNodesForSort);
		response.setSortedData(sortedXmlContent);

		return response;
	}

	/**
	 * Convert sort order to map in format:
	 * 
	 * key - name of sort field, value - sort order type.
	 * 
	 * @param sortOrderFields
	 *            is object with fields for converting
	 * @return map with values
	 */
	private LinkedHashMap<String, String> getSortOrderMap(final SortOrderFields sortOrderFields) {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();

		for (SortOrderField field : sortOrderFields.getFields()) {
			if (field.getName() != null) {
				map.put(field.getName(), field.getOrdering());
			}
		}
		return map;
	}

	/**
	 * Validate data for sort according to sort order. Validation success if
	 * data for sort contains at least one element with name equals to field
	 * name from sort order. If not contain - there is no order to be sorted by.
	 * 
	 * @param listNodesForSort
	 *            is list with nodes for sorting
	 * @param mapOrderNamesOrdering
	 *            is map with sort order(field name, type of sorting)
	 * @param response
	 *            is object for adding information about success/not validation
	 * @return true if valid
	 */
	private boolean validateXmlSortContentToSortOrder(final List<Node> listNodesForSort,
			final LinkedHashMap<String, String> mapOrderNamesOrdering, final Response response) {
		if (listNodesForSort.isEmpty()) {
			response.getErrors().add("Not enough data for sorting");
			return false;
		}

		XPath xPath = XPathFactory.newInstance().newXPath();
		int counterFieldNamesInDataForSort = 0;
		try {
			for (Node node : listNodesForSort) {
				for (String fieldName : mapOrderNamesOrdering.keySet()) {

					Node nodeByFieldName = (Node) xPath.compile(fieldName).evaluate(node, XPathConstants.NODE);
					if (nodeByFieldName != null) {
						counterFieldNamesInDataForSort++;
					}
				}
			}
		} catch (XPathExpressionException e) {
			LOGGER.error("Validation error during parsing, can't parse", e);
		}

		boolean isValid = counterFieldNamesInDataForSort != 0;
		if (!isValid) {
			response.getErrors().add(ERR_MESSAGE_VALIDATION_SORT_ORDER_TO_DATA);
		}
		return isValid;
	}

	/**
	 * Convert {@code List<Node> listNodesForSort} to String with content of all
	 * nodes.
	 * 
	 * @param listNodesForSort
	 * @return
	 */
	private String convertListNodesToStringXml(List<Node> listNodesForSort) {
		String result = "";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();

			Element root = document.createElement("SortedData");
			document.appendChild(root);

			for (Node node : listNodesForSort) {
				Node copyNode = document.importNode(node, true);
				root.appendChild(copyNode);
			}

			/**
			 * Convert document to string.
			 */
			result = convertXmlDocumentToString(document);

		} catch (ParserConfigurationException | TransformerException e) {
			LOGGER.error("Can't convert nodes to string with xml content", e);
		}

		return result;
	}

	/**
	 * Convert {@link Document} to {@link String} with it's content.
	 * 
	 * @param document
	 *            is doc for converting
	 * @return string representation of document content
	 * @throws TransformerException
	 *             if can't transform
	 */
	private String convertXmlDocumentToString(final Document document) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		String omitXmlDeclarationValue = "yes";
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXmlDeclarationValue);
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(document), new StreamResult(writer));
		String output = writer.getBuffer().toString();
		return output;
	}

	/**
	 * Create {@literal NodeComparatorByOrder} object and return.
	 * 
	 * @param mapOrderNamesOrdering
	 * @return
	 */
	private Comparator<Node> getComparatorForNodes(final LinkedHashMap<String, String> mapOrderNamesOrdering) {
		return new NodeComparatorByOrder(mapOrderNamesOrdering);
	}
}
