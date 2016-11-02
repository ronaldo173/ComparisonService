package software.sigma.comparissonservice.service;

import static org.mockito.Mockito.when;

import java.io.IOException;
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

/**
 * Test sort service.
 * 
 * @author alexandr.efimov
 *
 */
public class SortServiceImplTest {

	private ConfigurationService configService;
	private SortServiceImpl sortService;

	@Before
	public void setUp() {
		configService = Mockito.mock(ConfigurationService.class);
		sortService = new SortServiceImpl();
		sortService.setConfigService(configService);
	}

	@Test
	public void testValidateInputDataShoudBeSuccess() throws ApplicationException, IOException {

		URL urlFileXml = getClass()
				.getResource("/software/sigma/comparissonservice/resources/monitorsXmlForTest_VALID.xml");
		URL urlFileXsd = getClass()
				.getResource("/software/sigma/comparissonservice/resources/monitorsXsdForTest_VALID.xsd");
		String xmlContentCorrectForCheck = TestUtils.readFile(urlFileXml.getFile());
		String xsdValidSchemaForXml = TestUtils.readFile(urlFileXsd.getFile());

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

		Assert.assertTrue(sortService.validateInputData(inputData));
	}

	@Test
	public void testValidateInputDataShoudBeNotSuccessNotExistsConfig() throws ApplicationException, IOException {

		URL urlFileXml = getClass()
				.getResource("/software/sigma/comparissonservice/resources/monitorsXmlForTest_VALID.xml");
		String xmlContentCorrectForCheck = TestUtils.readFile(urlFileXml.getFile());

		List<ConfigurationProtocol> configProtocolsList = new ArrayList<>(TestUtils.getConfigProtocolsList());

		when(configService.getAll()).thenReturn(configProtocolsList);
		for (ConfigurationProtocol config : configProtocolsList) {
			when(configService.getById(config.getId())).thenReturn(config);
		}

		InputData inputData = new InputData();
		inputData.setDataForSort(xmlContentCorrectForCheck);

		Assert.assertFalse(sortService.validateInputData(inputData));
	}

}
