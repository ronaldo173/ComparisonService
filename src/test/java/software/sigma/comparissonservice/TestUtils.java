package software.sigma.comparissonservice;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.utils.ConfigurationsConverter;

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
	public static String convertToXml(final ConfigurationProtocol source) throws Exception {
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
			final ConfigurationProtocol configurationProtocol) {
		ConfigurationProtocol configConvertedToProtocol = ConfigurationsConverter.convert(configuration);

		return configConvertedToProtocol.equals(configurationProtocol);
	}

	/**
	 * Get list with {@link ConfigurationProtocol} objects.
	 * 
	 * @return list with objects
	 */
	public static List<ConfigurationProtocol> getConfigProtocolsList() {
		ConfigurationProtocol configFirst = new ConfigurationProtocol();
		configFirst.setId(1);
		configFirst.setName("Mock config 1");
		configFirst.setConfigContent("<?xml version=\"1.0\"?><xs:schema></xs:schema>");

		ConfigurationProtocol configSecond = new ConfigurationProtocol();
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

}
