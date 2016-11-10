package software.sigma.comparissonservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.net.URL;
import java.nio.charset.Charset;

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
import software.sigma.comparissonservice.protocol.InputData;
import software.sigma.comparissonservice.protocol.Response;
import software.sigma.comparissonservice.service.SortService;
import software.sigma.comparissonservice.utils.CommonUtils;

/**
 * Integration tests for sort feature.
 * 
 * @author alexandr.efimov
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { App.class, TestContext.class })
@WebAppConfiguration
public class SortRestControllerTest {

	/**
	 * Main entry point for server-side Spring MVC test support.
	 */
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private SortService sortService;

	/**
	 * Url prefix.
	 */
	private static final String URL_PREFIX = "http://localhost:8080/";

	/**
	 * Name of encoding.
	 */
	private static final String ENCODING = "UTF-8";

	@Before
	public void setup() {
		Mockito.reset(sortService);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testSortSuccesResult() throws Exception {
		String responseXpath = "response/isSuccess";
		InputData inputData = getValidInputObjectForSorting();

		String xmlValueOfInput = TestUtils.convertToXml(inputData).trim();
		byte[] contentInputObject = xmlValueOfInput.getBytes(Charset.forName(ENCODING));

		Mockito.when(sortService.validateInputData((InputData) Mockito.any(), (Response) Mockito.any()))
				.thenReturn(new Boolean(true));
		Mockito.when(sortService.sort((InputData) Mockito.any())).thenCallRealMethod();

		mockMvc.perform(post(URL_PREFIX + "sort/").contentType(MediaType.APPLICATION_XML).content(contentInputObject)
				.accept(MediaType.APPLICATION_XML)).andDo(MockMvcResultHandlers.print())
				.andExpect(xpath(responseXpath).booleanValue(true));
	}

	@Test
	public void testSortNotSuccessNotValidData() throws Exception {
		String responseXpath = "response/isSuccess";
		InputData inputData = getValidInputObjectForSorting();

		String xmlValueOfInput = TestUtils.convertToXml(inputData).trim();
		byte[] contentInputObject = xmlValueOfInput.getBytes(Charset.forName(ENCODING));

		Mockito.when(sortService.validateInputData((InputData) Mockito.any(), (Response) Mockito.any()))
				.thenReturn(new Boolean(false));
		Mockito.when(sortService.sort((InputData) Mockito.any())).thenCallRealMethod();

		mockMvc.perform(post(URL_PREFIX + "sort/").contentType(MediaType.APPLICATION_XML).content(contentInputObject)
				.accept(MediaType.APPLICATION_XML)).andDo(MockMvcResultHandlers.print())
				.andExpect(xpath(responseXpath).booleanValue(false));
	}

	private InputData getValidInputObjectForSorting() {
		InputData inputData = new InputData();

		URL urlFileXml = getClass()
				.getResource("/software/sigma/comparissonservice/resources/monitorsXmlForTest_VALID.xml");
		String xmlContentForSort = CommonUtils.readFileToString(urlFileXml.getFile());

		inputData.setDataForSort(xmlContentForSort);
		inputData.setSortOrder(TestUtils.getValidSortOrder());
		return inputData;
	}

}
