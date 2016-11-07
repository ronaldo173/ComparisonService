package software.sigma.comparissonservice.service;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import software.sigma.comparissonservice.TestUtils;
import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.protocol.InputData;
import software.sigma.comparissonservice.protocol.Response;
import software.sigma.comparissonservice.utils.CommonUtils;

/**
 * Test sort service.
 * 
 * @author alexandr.efimov
 *
 */
public class SortServiceImplTest {

	private ConfigurationService configService;
	private SortServiceImpl sortService;
	private Response responseMock;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		configService = Mockito.mock(ConfigurationService.class);
		sortService = new SortServiceImpl();
		sortService.setConfigService(configService);

		responseMock = Mockito.mock(Response.class);
		responseMock.setInformationMessages(Mockito.mock(ArrayList.class));
	}

	@Test
	public void testValidateContentForSortShoudBeSuccess() throws ApplicationException, IOException {

		URL urlFileXml = getClass()
				.getResource("/software/sigma/comparissonservice/resources/monitorsXmlForTest_VALID.xml");
		URL urlFileXsd = getClass()
				.getResource("/software/sigma/comparissonservice/resources/monitorsXsdForTest_VALID.xsd");
		String xmlContentCorrectForCheck = CommonUtils.readFileToString(urlFileXml.getFile());
		String xsdValidSchemaForXml = CommonUtils.readFileToString(urlFileXsd.getFile());

		List<ConfigurationProtocol> configProtocolsList = new ArrayList<>(TestUtils.getConfigProtocolsList());
		ConfigurationProtocol correctConfig = new ConfigurationProtocol();
		correctConfig.setId(configProtocolsList.size() + 1);
		correctConfig.setName("correctConfig");
		correctConfig.setConfigContent(xsdValidSchemaForXml);
		configProtocolsList.add(correctConfig);

		when(configService.getAll()).thenReturn(configProtocolsList);
		for (ConfigurationProtocol config : configProtocolsList) {
			when(configService.getById(config.getId())).thenReturn(config);
		}

		InputData inputData = new InputData();
		inputData.setDataForSort(xmlContentCorrectForCheck);
		Assert.assertTrue(sortService.validateXmlContentForSort(inputData, responseMock));
	}

	@Test
	public void testValidateContentForSortShoudBeNotSuccessNotExistsConfig() throws ApplicationException, IOException {

		URL urlFileXml = getClass()
				.getResource("/software/sigma/comparissonservice/resources/monitorsXmlForTest_VALID.xml");
		String xmlContentCorrectForCheck = CommonUtils.readFileToString(urlFileXml.getFile());

		List<ConfigurationProtocol> configProtocolsList = new ArrayList<>(TestUtils.getConfigProtocolsList());

		when(configService.getAll()).thenReturn(configProtocolsList);
		for (ConfigurationProtocol config : configProtocolsList) {
			when(configService.getById(config.getId())).thenReturn(config);
		}

		InputData inputData = new InputData();
		inputData.setDataForSort(xmlContentCorrectForCheck);
		Assert.assertFalse(sortService.validateXmlContentForSort(inputData, responseMock));
	}

	@Test
	public void testValidateSortOrderShouldBeSuccess() {

		URL urlFileXmlOrder = getClass()
				.getResource("/software/sigma/comparissonservice/resources/sortOrderMonitors_VALID.xml");
		String xmlContentSortOrder = CommonUtils.readFileToString(urlFileXmlOrder.getFile());

		boolean isValid = sortService.validateXmlSortOrderToSchema(xmlContentSortOrder, responseMock);
		Assert.assertTrue(isValid);
	}

	@Test
	public void testValidateSortOrderShouldBeNotSuccessIfPassNullOrEmpty() {

		boolean isValidNullArg = sortService.validateXmlSortOrderToSchema(null, responseMock);
		boolean isValidEmptyArg = sortService.validateXmlSortOrderToSchema("", responseMock);
		Assert.assertFalse(isValidNullArg);
		Assert.assertFalse(isValidEmptyArg);
	}

	@Test
	public void testValidateSortOrderShouldBeNotSuccessNotValidData() {

		URL urlFileXmlOrder = getClass()
				.getResource("/software/sigma/comparissonservice/resources/sortOrder_NOTVALID.xml");
		String xmlContentSortOrder = CommonUtils.readFileToString(urlFileXmlOrder.getFile());

		boolean isValid = sortService.validateXmlSortOrderToSchema(xmlContentSortOrder, responseMock);
		Assert.assertFalse(isValid);
	}

	@Test
	public void testValidateInputDataShouldBeNotSuccess() throws ApplicationException {

		InputData inputData = new InputData();
		inputData.setDataForSort("wrong");
		inputData.setSortOrder("wrong");
		boolean validateInputData = sortService.validateInputData(inputData, responseMock);

		Assert.assertFalse(validateInputData);
	}

	@Test
	public void testGetOrderingsFromXml() throws ApplicationException {
		URL urlFileXmlOrder = getClass()
				.getResource("/software/sigma/comparissonservice/resources/sortOrder_NOTVALID.xml");
		String xmlContentSortOrder = CommonUtils.readFileToString(urlFileXmlOrder.getFile());

		Map<String, String> mapOrder = sortService.getOrderingsFromXml(xmlContentSortOrder);
		System.out.println(mapOrder.entrySet());

		Assert.assertNotNull(mapOrder);
		Assert.assertTrue(!mapOrder.isEmpty());

		for (Entry<String, String> entry : mapOrder.entrySet()) {
			Assert.assertNotNull(entry.getKey());
		}
	}

	@Test(expected = ApplicationException.class)
	public void testGetOrderingsFromXmlShouldBeExceptionWrongData() throws ApplicationException {
		sortService.getOrderingsFromXml("123456");
	}

}
