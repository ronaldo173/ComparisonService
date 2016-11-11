package software.sigma.comparissonservice;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.vo.ConfigurationVO;
import software.sigma.comparissonservice.vo.ConverterVoDomain;
import software.sigma.comparissonservice.vo.SortOrderFieldVO;

public final class TestUtils {

	/**
	 * Convert POJO object to XML in string.
	 * 
	 * @param source
	 *            is source pojo
	 * @return result
	 * @throws Exception
	 *             if smth happened
	 */
	public static <T> String convertToXml(final T source) throws Exception {
		String result;
		StringWriter sw = new StringWriter();
		JAXBContext contextConfig = JAXBContext.newInstance(source.getClass());
		Marshaller marshaller = contextConfig.createMarshaller();
		marshaller.marshal(source, sw);
		result = sw.toString();

		return result;
	}

	/**
	 * Check if content of objects equal.
	 * 
	 * @param configuration
	 * @param configurationProtocol
	 * @return result of check, true - equals
	 */
	public static boolean isConfigurationAndDtoConfigEquals(final Configuration configuration,
			final ConfigurationVO configurationProtocol) {
		ConfigurationVO configConvertedToProtocol = ConverterVoDomain.convert(configuration);

		return configConvertedToProtocol.equals(configurationProtocol);
	}

	/**
	 * Get list with {@link ConfigurationVO} objects.
	 * 
	 * @return list with objects
	 */
	public static List<ConfigurationVO> getConfigProtocolsList() {
		ConfigurationVO configFirst = new ConfigurationVO();
		configFirst.setId(1);
		configFirst.setName("Mock config 1");
		configFirst.setConfigContent("<?xml version=\"1.0\"?><xs:schema></xs:schema>");

		ConfigurationVO configSecond = new ConfigurationVO();
		configSecond.setId(2);
		configSecond.setName("Mock config 2");
		configSecond.setConfigContent("<?xml version=\"1.0\"?><xs:schema>SCHEMA2</xs:schema>");
		return Arrays.asList(configFirst, configSecond);
	}

	/**
	 * Get list with configurations.
	 * 
	 * @return list with objects
	 */
	public static List<Configuration> getConfigsList() {
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

	/**
	 * Return valid sort order for testing sort with content from file with
	 * monitors.
	 * 
	 * @return sort order for sorting
	 */
	public static List<SortOrderFieldVO> getValidSortOrder() {
		List<SortOrderFieldVO> sortOrder = new ArrayList<>();

		SortOrderFieldVO fieldOfOrder = new SortOrderFieldVO();
		SortOrderFieldVO fieldOfOrder2 = new SortOrderFieldVO();
		SortOrderFieldVO fieldOfOrder3 = new SortOrderFieldVO();
		fieldOfOrder.setName("diagonal");
		fieldOfOrder2.setName("price");
		fieldOfOrder3.setName("resolution");

		sortOrder.add(fieldOfOrder);
		sortOrder.add(fieldOfOrder2);
		sortOrder.add(fieldOfOrder3);

		return sortOrder;
	}

}
