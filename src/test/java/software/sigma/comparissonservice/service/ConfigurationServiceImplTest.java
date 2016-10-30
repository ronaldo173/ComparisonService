package software.sigma.comparissonservice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import software.sigma.comparissonservice.TestUtils;
import software.sigma.comparissonservice.dao.ConfigurationDao;
import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;

/**
 * Test configuration service.
 * 
 * @author alexandr.efimov
 *
 */
public class ConfigurationServiceImplTest {

	private ConfigurationDao daoMocked;

	private ConfigurationServiceImpl configService;

	@Before
	public void setUp() {
		daoMocked = mock(ConfigurationDao.class);
		configService = new ConfigurationServiceImpl();
		configService.setDao(daoMocked);
	}

	@Test
	public void testGetByIdShouldReturnFoundedEntity() {
		Configuration configInDao = getConfigsList().get(0);
		int idForTest = configInDao.getId();
		when(daoMocked.getById(idForTest)).thenReturn(configInDao);

		ConfigurationProtocol configFromService = configService.getById(idForTest);
		verify(daoMocked, times(1)).getById(idForTest);
		verifyNoMoreInteractions(daoMocked);

		Assert.assertNotNull(configFromService);
		Assert.assertTrue(TestUtils.isConfigurationAndDtoConfigEquals(configInDao, configFromService));

	}

	@Test
	public void testGetAllShouldReturnListWithEntities() {
		List<Configuration> listConfigs = getConfigsList();
		when(daoMocked.getAll()).thenReturn(listConfigs);

		List<ConfigurationProtocol> configsFromService = configService.getAll();

		verify(daoMocked, times(1)).getAll();
		verifyNoMoreInteractions(daoMocked);

		Assert.assertTrue(listConfigs.size() == configsFromService.size());
		for (int i = 0; i < configsFromService.size(); i++) {
			Assert.assertTrue(
					TestUtils.isConfigurationAndDtoConfigEquals(listConfigs.get(i), configsFromService.get(i)));
		}
	}

	/**
	 * Get list with configurations.
	 * 
	 * @return list with objects
	 */
	private List<Configuration> getConfigsList() {
		Configuration config1 = new Configuration();
		config1.setId(1);
		config1.setName("name1");
		config1.setConfigContent("content1");
		Configuration config2 = new Configuration();
		config2.setId(2);
		config2.setName("name2");
		config2.setConfigContent("content2");
		return Arrays.asList(config1, config2);
	}

}
