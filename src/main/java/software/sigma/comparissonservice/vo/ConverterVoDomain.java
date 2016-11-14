package software.sigma.comparissonservice.vo;

import java.util.ArrayList;
import java.util.List;

import software.sigma.comparissonservice.model.Configuration;

/**
 * Helper class for work with configuration objects.Convert
 * {@link Configuration} object to {@link ConfigurationVO} object and vice
 * versa. Not for creating, use static method.
 * 
 * @author alexandr.efimov
 *
 */
public final class ConverterVoDomain {

	/**
	 * Convert {@link Configuration} to {@link ConfigurationVO}.
	 * 
	 * @param source
	 *            is source object
	 * @return is result object
	 */
	public static ConfigurationVO convert(final Configuration source) {

		ConfigurationVO configurationVo = null;
		if (source != null) {
			configurationVo = new ConfigurationVO();
			configurationVo.setId(source.getId());
			configurationVo.setName(source.getName());
			configurationVo.setConfigContent(source.getConfigContent());
		}

		return configurationVo;
	}

	/**
	 * Convert {@link ConfigurationVO} to {@link Configuration}.
	 * 
	 * @param source
	 *            is source object
	 * @return is result object
	 */
	public static Configuration convert(final ConfigurationVO source) {

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
	 * Convert List with {@link Configuration} to list
	 * with{@link ConfigurationVO}.
	 * 
	 * @param configurations
	 *            is source list
	 * @return list with results
	 */
	public static List<ConfigurationVO> convertToProtocolList(final List<Configuration> configurations) {
		List<ConfigurationVO> listResults = new ArrayList<>();
		if (configurations != null) {

			for (Configuration configuration : configurations) {
				ConfigurationVO configVo = convert(configuration);
				listResults.add(configVo);
			}
		}

		return listResults;
	}

	/**
	 * Until class, not for creating - use static methods.
	 */
	private ConverterVoDomain() {
		throw new IllegalAccessError();
	}

}
