package software.sigma.comparissonservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import java.net.URL;
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
import software.sigma.comparissonservice.protocol.InputDataDTO;
import software.sigma.comparissonservice.protocol.SortOrderFieldDTO;
import software.sigma.comparissonservice.protocol.SortOrderFieldsDTO;
import software.sigma.comparissonservice.service.SortService;
import software.sigma.comparissonservice.utils.CommonUtils;
import software.sigma.comparissonservice.vo.InputDataVO;
import software.sigma.comparissonservice.vo.ResponseVO;
import software.sigma.comparissonservice.vo.SortOrderFieldVO;

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
		InputDataDTO inputData = getValidInputObjectForSorting();

		String xmlValueOfInput = TestUtils.convertToXml(inputData).trim();
		byte[] contentInputObject = xmlValueOfInput.getBytes(Charset.forName(ENCODING));

		Mockito.when(sortService.validateInputData((InputDataVO) Mockito.any(), (ResponseVO) Mockito.any()))
				.thenReturn(Boolean.valueOf(true));
		Mockito.when(sortService.sort((InputDataVO) Mockito.any())).thenCallRealMethod();

		mockMvc.perform(post(URL_PREFIX + "sort/").contentType(MediaType.APPLICATION_XML).content(contentInputObject)
				.accept(MediaType.APPLICATION_XML)).andDo(MockMvcResultHandlers.print())
				.andExpect(xpath(responseXpath).booleanValue(true));
	}

	@Test
	public void testSortNotSuccessNotValidData() throws Exception {
		String responseXpath = "response/isSuccess";
		InputDataDTO inputData = getValidInputObjectForSorting();

		String xmlValueOfInput = TestUtils.convertToXml(inputData).trim();
		byte[] contentInputObject = xmlValueOfInput.getBytes(Charset.forName(ENCODING));

		Mockito.when(sortService.validateInputData((InputDataVO) Mockito.any(), (ResponseVO) Mockito.any()))
				.thenReturn(Boolean.FALSE);
		Mockito.when(sortService.sort((InputDataVO) Mockito.any())).thenCallRealMethod();

		mockMvc.perform(post(URL_PREFIX + "sort/").contentType(MediaType.APPLICATION_XML).content(contentInputObject)
				.accept(MediaType.APPLICATION_XML)).andDo(MockMvcResultHandlers.print())
				.andExpect(xpath(responseXpath).booleanValue(false));
	}

	private InputDataDTO getValidInputObjectForSorting() {
		InputDataDTO inputData = new InputDataDTO();

		URL urlFileXml = getClass()
				.getResource("/software/sigma/comparissonservice/resources/monitorsXmlForTest_VALID.xml");
		String xmlContentForSort = CommonUtils.readFileToString(urlFileXml.getFile());

		inputData.setDataForSort(xmlContentForSort);
		List<SortOrderFieldVO> validSortOrderVO = TestUtils.getValidSortOrder();
		SortOrderFieldsDTO sortOrder = new SortOrderFieldsDTO();
		sortOrder.setFields(new ArrayList<SortOrderFieldDTO>());

		for (SortOrderFieldVO sortOrderFieldVO : validSortOrderVO) {
			sortOrder.getFields().add(convert(sortOrderFieldVO));
		}

		inputData.setSortOrder(sortOrder);
		return inputData;
	}

	private SortOrderFieldDTO convert(SortOrderFieldVO sortOrderFieldVO) {
		SortOrderFieldDTO sortOrderFieldDTO = new SortOrderFieldDTO();

		sortOrderFieldDTO.setName(sortOrderFieldVO.getName());
		sortOrderFieldDTO.setOrdering(sortOrderFieldDTO.getOrdering());
		return sortOrderFieldDTO;
	}

}
