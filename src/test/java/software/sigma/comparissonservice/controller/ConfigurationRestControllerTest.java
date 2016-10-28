package software.sigma.comparissonservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//TODO
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class ConfigurationRestControllerTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;
	private static final String URL_PREFIX = " http://localhost:8080/";

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

	}

	@Configuration
	@EnableWebMvc
	public static class TestConfig {
		@Bean
		public ConfigurationRestController getController() {
			return new ConfigurationRestController();
		}
	}

	@Test
	public void testControllerSimple() throws Exception {
		mockMvc.perform(get("/test").accept(MediaType.APPLICATION_JSON)).andDo(print());
	}

}
