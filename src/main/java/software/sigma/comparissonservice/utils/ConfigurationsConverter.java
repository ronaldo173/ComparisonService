package software.sigma.comparissonservice.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;

/**
 * Convert {@link Configuration} object to {@link ConfigurationProtocol} object.
 * 
 * @author alexandr.efimov
 *
 */
@Component
public class ConfigurationsConverter implements Converter<Configuration, ConfigurationProtocol> {

	@Override
	public ConfigurationProtocol convert(final Configuration source) {

		ConfigurationProtocol configurationProtocol = null;
		if (source != null) {
			configurationProtocol = new ConfigurationProtocol();
			configurationProtocol.setId(source.getId());
			configurationProtocol.setName(source.getName());
			configurationProtocol.setConfigContent(source.getConfigContent());
		}

		return configurationProtocol;
	}

}
