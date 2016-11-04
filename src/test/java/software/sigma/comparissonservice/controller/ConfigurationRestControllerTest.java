package software.sigma.comparissonservice.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import software.sigma.comparissonservice.App;
import software.sigma.comparissonservice.TestContext;
import software.sigma.comparissonservice.TestUtils;
import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.service.ConfigurationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { App.class, TestContext.class })
@WebAppConfiguration
public class ConfigurationRestControllerTest {

	/**
	 * Main entry point for server-side Spring MVC test support.
	 */
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ConfigurationService configServiceMock;

	/**
	 * Url prefix.
	 */
	private static final String URL_PREFIX = "http://localhost:8080/";

	@Before
	public void setup() {
		/**
		 * Reset our mock between tests because the mock objects are managed by
		 * the Spring container. If we would not reset them, stubbing and
		 * verified behavior would "leak" from one test to another.
		 */
		Mockito.reset(configServiceMock);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

	}

	/**
	 * Test getAll by checking expectations on XML response content with XPath
	 * expressions.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetAllShouldReturnFoundEntities() throws Exception {
		String configNameXpath = "configurations/configuration[%s]/name";
		String configIdXpath = "configurations/configuration[%s]/id";

		List<ConfigurationProtocol> listConfigs = TestUtils.getConfigProtocolsList();
		when(configServiceMock.getAll()).thenReturn(listConfigs);

		mockMvc.perform(get(URL_PREFIX + "configuration/").accept(MediaType.APPLICATION_XML))
				.andExpect(xpath(configNameXpath, 1).string(listConfigs.get(0).getName()))
				.andExpect(xpath(configNameXpath, 2).string(listConfigs.get(1).getName()))
				.andExpect(xpath(configIdXpath, 1).number(Double.valueOf(listConfigs.get(0).getId())))
				.andExpect(xpath(configIdXpath, 2).number(Double.valueOf(listConfigs.get(1).getId())));

		verify(configServiceMock, times(1)).getAll();
		verifyNoMoreInteractions(configServiceMock);
	}

	@Test
	public void testGetConfigByIdShouldReturnEntity() throws Exception {
		String configNameXpath = "configuration[%s]/name";
		String configIdXpath = "configuration[%s]/id";
		int idOfConfig = 1;

		ConfigurationProtocol configuration = TestUtils.getConfigProtocolsList().get(0);
		when(configServiceMock.getById(idOfConfig)).thenReturn(configuration);

		mockMvc.perform(get(URL_PREFIX + "configuration/" + idOfConfig).accept(MediaType.APPLICATION_XML))

				.andExpect(xpath(configNameXpath, idOfConfig).string(configuration.getName()))
				.andExpect(xpath(configIdXpath, idOfConfig).number(Double.valueOf(configuration.getId())))
				.andExpect(xpath(configNameXpath, idOfConfig + 1).doesNotExist())
				.andExpect(xpath(configIdXpath, idOfConfig + 1).doesNotExist());

		verify(configServiceMock, times(1)).getById(idOfConfig);
		verifyNoMoreInteractions(configServiceMock);
	}

	/**
	 * Test not correct interaction with service - configuration/1s.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetConfigByIdWrongParamIdShouldReturnErrorResponse() throws Exception {
		String errorResponseXpath = "response/";
		mockMvc.perform(get(URL_PREFIX + "configuration/1s").accept(MediaType.APPLICATION_XML))
				.andExpect(xpath(errorResponseXpath + "isSuccess").booleanValue(false))
				.andExpect(xpath(errorResponseXpath + "errors").exists()).andDo(MockMvcResultHandlers.print());
	}

	/**
	 * Test response for not existing data - configuration/1, when config with
	 * id not exists.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetConfigByIdNotExistingIdShouldReturnErrorResponse() throws Exception {
		String errorSuccessPath = "response/isSuccess";

		when(configServiceMock.getById(1)).thenThrow(ApplicationException.class);
		mockMvc.perform(get(URL_PREFIX + "configuration/1").accept(MediaType.APPLICATION_XML))
				.andDo(MockMvcResultHandlers.print()).andExpect(xpath(errorSuccessPath).booleanValue(false));

		verify(configServiceMock, times(1)).getById(1);
		verifyNoMoreInteractions(configServiceMock);
	}

	@Test
	public void testGetAllShouldReturnEmptyResult() throws Exception {

		String configXpath = "configurations/configuration[%s]";
		when(configServiceMock.getAll()).thenReturn(new ArrayList<ConfigurationProtocol>());

		mockMvc.perform(get(URL_PREFIX + "configuration/").accept(MediaType.APPLICATION_XML))
				.andExpect(xpath(configXpath, 1).doesNotExist()).andDo(MockMvcResultHandlers.print());

		verify(configServiceMock, times(1)).getAll();
		verifyNoMoreInteractions(configServiceMock);
	}

	@Test
	public void testSaveEntityShouldReturnSuccessResponse() throws Exception {
		String responseXpath = "response/isSuccess";

		ConfigurationProtocol configurationProtocol = TestUtils.getConfigProtocolsList().get(0);
		byte[] contentForTest = TestUtils.convertToXml(configurationProtocol).getBytes(Charset.forName("UTF-8"));

		when(configServiceMock.save(configurationProtocol)).thenReturn(true);

		mockMvc.perform(post(URL_PREFIX + "configuration/").contentType(MediaType.APPLICATION_XML)
				.content(contentForTest).accept(MediaType.APPLICATION_XML))
				.andExpect(xpath(responseXpath).booleanValue(true)).andDo(MockMvcResultHandlers.print());
		verify(configServiceMock, times(1)).save(configurationProtocol);
		verifyNoMoreInteractions(configServiceMock);
	}

	@Test
	public void testUpdateEntityShouldReturnSuccessResponse() throws Exception {
		String responseXpath = "response/isSuccess";

		ConfigurationProtocol configurationProtocol = TestUtils.getConfigProtocolsList().get(0);
		configurationProtocol.setId(1);
		byte[] contentForTest = TestUtils.convertToXml(configurationProtocol).getBytes(Charset.forName("UTF-8"));

		when(configServiceMock.update(configurationProtocol)).thenReturn(true);

		mockMvc.perform(put(URL_PREFIX + "configuration/1").contentType(MediaType.APPLICATION_XML)
				.content(contentForTest).accept(MediaType.APPLICATION_XML))
				.andExpect(xpath(responseXpath).booleanValue(true)).andDo(MockMvcResultHandlers.print());
		verify(configServiceMock, times(1)).update(configurationProtocol);
		verifyNoMoreInteractions(configServiceMock);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testUpdateEntityShouldReturnErrorResponse() throws Exception {
		String responseXpath = "response/isSuccess";

		ConfigurationProtocol configurationProtocol = TestUtils.getConfigProtocolsList().get(0);
		configurationProtocol.setId(1);
		byte[] contentForTest = TestUtils.convertToXml(configurationProtocol).getBytes(Charset.forName("UTF-8"));

		when(configServiceMock.update(configurationProtocol)).thenThrow(ApplicationException.class);

		mockMvc.perform(put(URL_PREFIX + "configuration/1").contentType(MediaType.APPLICATION_XML)
				.content(contentForTest).accept(MediaType.APPLICATION_XML))
				.andExpect(xpath(responseXpath).booleanValue(false)).andDo(MockMvcResultHandlers.print());
		verify(configServiceMock, times(1)).update(configurationProtocol);
		verifyNoMoreInteractions(configServiceMock);

	}

	@Test
	public void testDeleteEntityShouldReturnSuccessResponse() throws Exception {
		String responseXpath = "response/isSuccess";

		int id = 1;
		when(configServiceMock.delete(id)).thenReturn(true);

		mockMvc.perform(delete(URL_PREFIX + "configuration/" + id).contentType(MediaType.APPLICATION_XML)
				.accept(MediaType.APPLICATION_XML)).andExpect(xpath(responseXpath).booleanValue(true))
				.andDo(MockMvcResultHandlers.print());

		verify(configServiceMock, times(1)).delete(id);
		verifyNoMoreInteractions(configServiceMock);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDeleteEntityShouldReturnErrorResponse() throws Exception {
		String responseXpath = "response/isSuccess";

		int id = 1;
		when(configServiceMock.delete(id)).thenThrow(ApplicationException.class);

		mockMvc.perform(delete(URL_PREFIX + "configuration/" + id).contentType(MediaType.APPLICATION_XML)
				.accept(MediaType.APPLICATION_XML)).andExpect(xpath(responseXpath).booleanValue(false))
				.andDo(MockMvcResultHandlers.print());

		verify(configServiceMock, times(1)).delete(id);
		verifyNoMoreInteractions(configServiceMock);

	}
}
