package software.sigma.comparissonservice;

import java.io.StringWriter;

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
}
