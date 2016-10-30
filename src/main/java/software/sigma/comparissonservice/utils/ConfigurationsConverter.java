package software.sigma.comparissonservice.utils;

import java.util.ArrayList;
import java.util.List;

import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;

/**
 * Helper class for work with configuration objects.Convert
 * {@link Configuration} object to {@link ConfigurationProtocol} object and vice
 * versa.
 * 
 * @author alexandr.efimov
 *
 */
public final class ConfigurationsConverter {

	/**
	 * Convert {@link Configuration} to {@link ConfigurationProtocol}.
	 * 
	 * @param source
	 *            is source object
	 * @return is result object
	 */
	public static ConfigurationProtocol convert(final Configuration source) {

		ConfigurationProtocol configurationProtocol = null;
		if (source != null) {
			configurationProtocol = new ConfigurationProtocol();
			configurationProtocol.setId(source.getId());
			configurationProtocol.setName(source.getName());
			configurationProtocol.setConfigContent(source.getConfigContent());
		}

		return configurationProtocol;
	}

	/**
	 * Convert {@link ConfigurationProtocol} to {@link Configuration}.
	 * 
	 * @param source
	 *            is source object
	 * @return is result object
	 */
	public static Configuration convert(final ConfigurationProtocol source) {

		Configuration configuration = null;
		if (source != null) {
			configuration = new Configuration();
			configuration.setId(source.getId());
			configuration.setName(source.getName());
			configuration.setConfigContent(source.getConfigContent());
		}

		return configuration;
	}

	/**
	 * Convert List with {@link Configuration} to {@link ConfigurationProtocol}.
	 * 
	 * @param configurations
	 *            is source list
	 * @return list with results
	 */
	public static List<ConfigurationProtocol> convertToProtocolList(final List<Configuration> configurations) {
		List<ConfigurationProtocol> listResults = new ArrayList<>();
		if (configurations != null) {

			for (Configuration configuration : configurations) {
				ConfigurationProtocol configProtocol = convert(configuration);
				listResults.add(configProtocol);
			}
		}

		return listResults;
	}

	/**
	 * Until class, not for creating - use static methods.
	 */
	private ConfigurationsConverter() {
		throw new IllegalAccessError();
	}

}
