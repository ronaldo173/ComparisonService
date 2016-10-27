package software.sigma.comparissonservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import software.sigma.comparissonservice.dao.ConfigurationDao;
import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;

/**
 * Implementation of {@link ConfigurationService} for work with
 * {@link Configuration}
 * 
 * @author alexandr.efimov
 *
 */
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	private ConfigurationDao dao;
	@Autowired
	private Converter<Configuration, ConfigurationProtocol> converter;

	public final void setDao(ConfigurationDao dao) {
		this.dao = dao;
	}

	@Override
	public List<Configuration> getAll() {

		return dao.getAll();
	}

	@Override
	public Configuration getById(final int id) {

		return dao.getById(id);

	}

	@Override
	public ConfigurationProtocol convertToProtocol(Configuration configuration) {

		return converter.convert(configuration);
	}

	@Override
	public List<ConfigurationProtocol> convertToProtocolList(List<Configuration> configurations) {
		List<ConfigurationProtocol> listResults = null;
		if (configurations != null && !configurations.isEmpty()) {
			listResults = new ArrayList<>();
			for (Configuration configuration : configurations) {
				ConfigurationProtocol configProtocol = convertToProtocol(configuration);
				listResults.add(configProtocol);
			}
		}

		return listResults;
	}

}
