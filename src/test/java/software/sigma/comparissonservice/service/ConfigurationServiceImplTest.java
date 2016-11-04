package software.sigma.comparissonservice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import software.sigma.comparissonservice.TestUtils;
import software.sigma.comparissonservice.dao.ConfigurationDao;
import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.utils.ConfigurationsConverter;

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
	public void testGetByIdShouldReturnFoundEntity() throws ApplicationException {
		Configuration configInDao = TestUtils.getConfigsList().get(0);
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
		List<Configuration> listConfigs = TestUtils.getConfigsList();
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

	@Test
	public void testDeleteConfig() throws ApplicationException {
		int idForDelete = 1;

		when(daoMocked.delete(idForDelete)).thenReturn(true);
		boolean deleteSuccess = configService.delete(idForDelete);

		verify(daoMocked, times(1)).delete(idForDelete);
		verifyNoMoreInteractions(daoMocked);

		Assert.assertTrue(deleteSuccess);
	}

	@Test
	public void testUpdateConfigValidContent() throws ApplicationException {
		String configContent = "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
				+ "<xs:element name=\"note\"></xs:element></xs:schema>";
		int idForUpdate = 1;
		Configuration configuration = new Configuration();
		configuration.setId(idForUpdate);
		configuration.setConfigContent(configContent);
		ConfigurationProtocol configProtocol = new ConfigurationProtocol();
		configProtocol.setId(idForUpdate);
		configProtocol.setConfigContent(configContent);

		when(daoMocked.update(configuration)).thenReturn(true);
		boolean updateSuccess = configService.update(configProtocol);

		verify(daoMocked, times(1)).update(configuration);
		verifyNoMoreInteractions(daoMocked);

		Assert.assertTrue(updateSuccess);
	}

	@Test(expected = ApplicationException.class)
	public void testUpdateConfigInvalidContentShouldThrowException() throws ApplicationException {
		String configContent = "NOT XSD SCHEMA";
		int idForUpdate = 1;
		ConfigurationProtocol configProtocol = new ConfigurationProtocol();
		configProtocol.setId(idForUpdate);
		configProtocol.setConfigContent(configContent);

		configService.update(configProtocol);
	}

	public void testAddConfigValidContent() throws ApplicationException {
		String configContent = "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">"
				+ "<xs:element name=\"note\"></xs:element></xs:schema>";
		ConfigurationProtocol configProtocol = new ConfigurationProtocol();
		configProtocol.setConfigContent(configContent);
		configProtocol.setName("test");
		Configuration configuration = ConfigurationsConverter.convert(configProtocol);

		when(daoMocked.save(configuration)).thenReturn(true);
		boolean saveSuccess = configService.save(configProtocol);
		verify(daoMocked, times(1)).save(configuration);
		verifyNoMoreInteractions(daoMocked);

		Assert.assertTrue(saveSuccess);
	}

	@Test(expected = ApplicationException.class)
	public void testAddConfigWithInvalidContentShouldThrowException() throws ApplicationException {
		String configContent = "NOT XSD SCHEMA";
		int idForUpdate = 1;
		ConfigurationProtocol configProtocol = new ConfigurationProtocol();
		configProtocol.setId(idForUpdate);
		configProtocol.setConfigContent(configContent);

		configService.save(configProtocol);
	}

	@Test
	public void testGetByNameShouldReturnFoundEntity() throws ApplicationException {
		Configuration config = TestUtils.getConfigsList().get(0);
		String nameForTest = config.getName();
		when(daoMocked.getByName(nameForTest)).thenReturn(config);

		ConfigurationProtocol configFromService = configService.getByName(nameForTest);
		verify(daoMocked, times(1)).getByName(nameForTest);
		verifyNoMoreInteractions(daoMocked);

		Assert.assertNotNull(configFromService);
		Assert.assertTrue(TestUtils.isConfigurationAndDtoConfigEquals(config, configFromService));

	}

	@SuppressWarnings("unchecked")
	@Test(expected = ApplicationException.class)
	public void testGetByNameShouldThrowErrorWhenNotFound() throws ApplicationException {
		Configuration config = TestUtils.getConfigsList().get(0);
		String nameForTest = config.getName();
		when(daoMocked.getByName(nameForTest)).thenThrow(Throwable.class);

		configService.getByName(nameForTest);
	}

}
