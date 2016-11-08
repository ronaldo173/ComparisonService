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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import software.sigma.comparissonservice.NodeComparatorByOrder;
import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.protocol.InputData;
import software.sigma.comparissonservice.protocol.Response;
import software.sigma.comparissonservice.utils.CommonUtils;

@Service
public class SortServiceImpl implements SortService {

	private static final Logger LOGGER = Logger.getLogger(SortServiceImpl.class);

	private static final String SORT_ORDER_ATTR_NAME = "name";
	private static final String SORT_ORDER_ATTR_ORDERING = "ordering";
	private static final String MESSAGE_FOUND_VALID_CONFIG = "Found valid configuration, name: ";
	private static final String MESSAGE_VALIDATION_DATA_FOR_SORT_SUCCESS = "Validation data for sort success: ";
	private static final String MESSAGE_VALIDATION_SORT_ORDER_SUCCESS = "Validation sort order success: ";;
	private static final String ERR_MESSAGE_CANT_GET_CONFIG_BY_NAME = "Can't get from service config by name, ";
	private static final String PATH_TO_SORT_ORDER_VALIDATON_SCHEMA_FILE = "SortOrderValidatonSchema.xsd";

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

	/**
	 * Parse content of XML file(with root element and list of nodes) to
	 * {@link List} with nodes.
	 * 
	 * @param xmlContent
	 *            for parsing
	 * @return list with parsed nodes
	 */
	private List<Node> getListNodesFromXmlContent(final String xmlContent) {

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

	/**
	 * Parse sort order from xml to map in format:
	 * 
	 * key - name of field value - ordering(asc or desc).
	 * 
	 * @param sortOrder
	 *            is content of XML file with sort order
	 * @return ordered map with sort order
	 * @throws ApplicationException
	 *             if can't parse
	 */
	final LinkedHashMap<String, String> getOrderingsFromXml(String sortOrder) throws ApplicationException {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
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
		LOGGER.debug("Sort order parsed to: " + map.entrySet());
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

	@Override
	public Response sort(final InputData inputData) throws ApplicationException {

		Response response = new Response();
		response.setInformationMessages(new ArrayList<String>());
		boolean isValid = validateInputData(inputData, response);

		if (isValid) {
			LinkedHashMap<String, String> mapOrderNamesOrdering = getOrderingsFromXml(inputData.getSortOrder());
			response.getInformationMessages().add("Sort order: " + mapOrderNamesOrdering.entrySet());

			String dataForSort = inputData.getDataForSort().trim();
			List<Node> listNodesForSort = getListNodesFromXmlContent(dataForSort);
			response.getInformationMessages().add("Objects for sort amount: " + listNodesForSort.size());

			Comparator<Node> comparator = getComparatorForNodes(mapOrderNamesOrdering);
			Collections.sort(listNodesForSort, comparator);

			String sortedXmlContent = convertListNodesToStringXml(listNodesForSort);
			response.setSortedData(sortedXmlContent);
		}

		response.setSuccess(isValid);
		return response;
	}

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
	 * @return
	 * @throws TransformerException
	 */
	private String convertXmlDocumentToString(final Document document) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
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
