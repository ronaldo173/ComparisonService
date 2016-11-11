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
import software.sigma.comparissonservice.utils.CommonUtils;
import software.sigma.comparissonservice.vo.ConfigurationVO;
import software.sigma.comparissonservice.vo.InputDataVO;
import software.sigma.comparissonservice.vo.ResponseVO;
import software.sigma.comparissonservice.vo.SortOrderFieldVO;

/**
 * Test sort service.
 * 
 * @author alexandr.efimov
 *
 */
public class SortServiceImplTest {

	private ConfigurationService configService;
	private SortServiceImpl sortService;
	private ResponseVO responseMock;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		configService = Mockito.mock(ConfigurationService.class);
		sortService = new SortServiceImpl();
		sortService.setConfigService(configService);

		responseMock = Mockito.mock(ResponseVO.class);
	}

	@Test
	public void testValidateContentForSortShoudBeSuccess() throws ApplicationException {

		String xmlContentCorrectForCheck = getValidDataForSortWithMockDao();

		InputDataVO inputData = new InputDataVO();
		inputData.setDataForSort(xmlContentCorrectForCheck);
		Assert.assertTrue(sortService.validateXmlContentForSort(inputData, responseMock));
	}

	@Test
	public void testValidateContentForSortShoudBeNotSuccessForNotExistingConfig() throws ApplicationException {

		URL urlFileXml = getClass()
				.getResource("/software/sigma/comparissonservice/resources/monitorsXmlForTest_VALID.xml");
		String xmlContentCorrectForCheck = CommonUtils.readFileToString(urlFileXml.getFile());

		List<ConfigurationVO> configProtocolsList = new ArrayList<>(TestUtils.getConfigProtocolsList());

		when(configService.getAll()).thenReturn(configProtocolsList);
		for (ConfigurationVO config : configProtocolsList) {
			when(configService.getById(config.getId())).thenReturn(config);
		}

		InputDataVO inputData = new InputDataVO();
		inputData.setDataForSort(xmlContentCorrectForCheck);
		Assert.assertFalse(sortService.validateXmlContentForSort(inputData, responseMock));
	}

	@Test
	public void testValidateInputDataShouldBeNotSuccessPassNullOrEmptyArgs() {

		boolean isValidNullArg = sortService.validateInputData(null, null);
		boolean isValidEmptyInputData = sortService.validateInputData(new InputDataVO(), responseMock);
		Assert.assertFalse(isValidNullArg);
		Assert.assertFalse(isValidEmptyInputData);
	}

	@Test
	public void testValidateInputDataShouldBeNotSuccessWrongSortOrder() throws ApplicationException {

		InputDataVO inputData = new InputDataVO();
		inputData.setDataForSort(getValidDataForSortWithMockDao());
		inputData.setSortOrder(new ArrayList<SortOrderFieldVO>());
		boolean validateInputData = sortService.validateInputData(inputData, responseMock);

		Assert.assertFalse(validateInputData);
	}

	@Test
	public void testValidateInputDataShouldBeNotSuccessWrongFieldNamesInSortOrder() throws ApplicationException {

		InputDataVO inputData = new InputDataVO();
		inputData.setDataForSort(getValidDataForSortWithMockDao());

		List<SortOrderFieldVO> sortOrder = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			SortOrderFieldVO fieldOfOrder = new SortOrderFieldVO();
			fieldOfOrder.setName("name" + i);
			sortOrder.add(fieldOfOrder);
		}

		inputData.setSortOrder(sortOrder);

		boolean validateInputData = sortService.validateInputData(inputData, responseMock);

		Assert.assertFalse(validateInputData);
	}

	@Test
	public void testValidationShouldBeSuccess() throws ApplicationException {
		InputDataVO inputData = new InputDataVO();
		inputData.setDataForSort(getValidDataForSortWithMockDao());

		List<SortOrderFieldVO> sortOrder = new ArrayList<>();
		SortOrderFieldVO fieldOfOrder = new SortOrderFieldVO();
		fieldOfOrder.setName("diagonal");
		sortOrder.add(fieldOfOrder);

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

		List<ConfigurationVO> configProtocolsList = new ArrayList<>(TestUtils.getConfigProtocolsList());
		ConfigurationVO correctConfig = new ConfigurationVO();
		correctConfig.setId(configProtocolsList.size() + 1);
		correctConfig.setName("correctConfig");
		correctConfig.setConfigContent(xsdValidSchemaForXml);
		configProtocolsList.add(correctConfig);

		when(configService.getAll()).thenReturn(configProtocolsList);
		for (ConfigurationVO config : configProtocolsList) {
			when(configService.getById(config.getId())).thenReturn(config);
		}

		return xmlContentCorrectForCheck;

	}

	@Test
	public void testSortShouldBeSuccess() throws ApplicationException {

		InputDataVO inputData = new InputDataVO();
		inputData.setSortOrder(TestUtils.getValidSortOrder());
		inputData.setDataForSort(getValidDataForSortWithMockDao());

		String sortedData = sortService.sort(inputData);

		Assert.assertNotNull(sortedData);
		Assert.assertTrue(!sortedData.isEmpty());
	}

	@Test(expected = ApplicationException.class)
	public void testSortShouldBeExceptionWrongData() throws ApplicationException {

		InputDataVO inputData = new InputDataVO();
		inputData.setSortOrder(TestUtils.getValidSortOrder());
		inputData.setDataForSort("smth not valid");

		sortService.sort(inputData);
	}

	@Test(expected = ApplicationException.class)
	public void testSortShouldBeExceptionNullData() throws ApplicationException {
		sortService.sort(null);
	}

}
