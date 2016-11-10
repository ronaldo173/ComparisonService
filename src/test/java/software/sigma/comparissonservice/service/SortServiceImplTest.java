package software.sigma.comparissonservice.service;

import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import software.sigma.comparissonservice.TestUtils;
import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.protocol.InputData;
import software.sigma.comparissonservice.protocol.Response;
import software.sigma.comparissonservice.protocol.SortOrderField;
import software.sigma.comparissonservice.protocol.SortOrderFields;
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
	}

	@Test
	public void testValidateContentForSortShoudBeSuccess() throws ApplicationException {

		String xmlContentCorrectForCheck = getValidDataForSortWithMockDao();

		InputData inputData = new InputData();
		inputData.setDataForSort(xmlContentCorrectForCheck);
		Assert.assertTrue(sortService.validateXmlContentForSort(inputData, responseMock));
	}

	@Test
	public void testValidateContentForSortShoudBeNotSuccessForNotExistingConfig() throws ApplicationException {

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
	public void testValidateInputDataShouldBeNotSuccessPassNullOrEmptyArgs() {

		boolean isValidNullArg = sortService.validateInputData(null, null);
		boolean isValidEmptyInputData = sortService.validateInputData(new InputData(), responseMock);
		Assert.assertFalse(isValidNullArg);
		Assert.assertFalse(isValidEmptyInputData);
	}

	@Test
	public void testValidateInputDataShouldBeNotSuccessWrongSortOrder() throws ApplicationException {

		InputData inputData = new InputData();
		inputData.setDataForSort(getValidDataForSortWithMockDao());
		inputData.setSortOrder(new SortOrderFields());
		boolean validateInputData = sortService.validateInputData(inputData, responseMock);

		Assert.assertFalse(validateInputData);
	}

	@Test
	public void testValidateInputDataShouldBeNotSuccessWrongFieldNamesInSortOrder() throws ApplicationException {

		InputData inputData = new InputData();
		inputData.setDataForSort(getValidDataForSortWithMockDao());

		SortOrderFields sortOrder = new SortOrderFields();
		sortOrder.setFields(new ArrayList<SortOrderField>());
		for (int i = 0; i < 5; i++) {
			SortOrderField fieldOfOrder = new SortOrderField();
			fieldOfOrder.setName("name" + i);
			sortOrder.getFields().add(fieldOfOrder);
		}

		inputData.setSortOrder(sortOrder);

		boolean validateInputData = sortService.validateInputData(inputData, responseMock);

		Assert.assertFalse(validateInputData);
	}

	@Test
	public void testValidationShouldBeSuccess() throws ApplicationException {
		InputData inputData = new InputData();
		inputData.setDataForSort(getValidDataForSortWithMockDao());

		SortOrderFields sortOrder = new SortOrderFields();
		SortOrderField fieldOfOrder = new SortOrderField();
		fieldOfOrder.setName("diagonal");
		sortOrder.setFields(new ArrayList<SortOrderField>());
		sortOrder.getFields().add(fieldOfOrder);

		inputData.setSortOrder(sortOrder);
		boolean validateInputData = sortService.validateInputData(inputData, responseMock);

		Assert.assertTrue(validateInputData);
	}

	/**
	 * Get valid content for sort. Use mock dao for validation content according
	 * to schema.
	 * 
	 * @return string with xml content for sort
	 * @throws ApplicationException
	 */
	private String getValidDataForSortWithMockDao() throws ApplicationException {
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

		return xmlContentCorrectForCheck;

	}

	private SortOrderFields getValidSortOrder() {
		SortOrderFields sortOrder = new SortOrderFields();
		sortOrder.setFields(new ArrayList<SortOrderField>());

		SortOrderField fieldOfOrder = new SortOrderField();
		SortOrderField fieldOfOrder2 = new SortOrderField();
		SortOrderField fieldOfOrder3 = new SortOrderField();
		fieldOfOrder.setName("diagonal");
		fieldOfOrder2.setName("price");
		fieldOfOrder3.setName("resolution");

		sortOrder.getFields().add(fieldOfOrder);
		sortOrder.getFields().add(fieldOfOrder2);
		sortOrder.getFields().add(fieldOfOrder3);

		return sortOrder;
	}

	@Test
	public void testSortShouldBeSuccess() throws ApplicationException {

		InputData inputData = new InputData();
		inputData.setSortOrder(getValidSortOrder());
		inputData.setDataForSort(getValidDataForSortWithMockDao());

		String sortedData = sortService.sort(inputData);

		Assert.assertNotNull(sortedData);
		Assert.assertTrue(!sortedData.isEmpty());
	}

	@Test(expected = ApplicationException.class)
	public void testSortShouldBeExceptionWrongData() throws ApplicationException {

		InputData inputData = new InputData();
		inputData.setSortOrder(getValidSortOrder());
		inputData.setDataForSort("smth not valid");

		sortService.sort(inputData);
	}

	@Test(expected = ApplicationException.class)
	public void testSortShouldBeExceptionNullData() throws ApplicationException {
		sortService.sort(null);
	}

}
