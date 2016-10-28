package software.sigma.comparissonservice.controller;

import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import software.sigma.comparissonservice.TestContext;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.service.ConfigurationService;

//TODO in progress
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContext.class })
@WebAppConfiguration
public class ConfigurationRestControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	ConfigurationService configServiceMock;

	/**
	 * Url prefix.
	 */
	private static final String URL_PREFIX = " http://localhost:8080/";

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

	@Test
	public void getAll_ShouldReturnFoundedEntities() throws Exception {

		ConfigurationProtocol configFirst = new ConfigurationProtocol();
		configFirst.setId(1);
		configFirst.setName("Mock config 1");
		configFirst.setConfigContent("<?xml version=\"1.0\"?><xs:schema></xs:schema>");

		ConfigurationProtocol configSecond = new ConfigurationProtocol();
		configSecond.setId(2);
		configSecond.setName("Mock config 2");
		configSecond.setConfigContent("<?xml version=\"1.0\"?><xs:schema>SCHEMA2</xs:schema>");

		when(configServiceMock.getAll()).thenReturn(Arrays.asList(configFirst, configSecond));

		// mockMvc.perform(get("configuration/").accept(MediaType.APPLICATION_XML)).andExpect()
		//
		// .andDo(print());
	}

}
