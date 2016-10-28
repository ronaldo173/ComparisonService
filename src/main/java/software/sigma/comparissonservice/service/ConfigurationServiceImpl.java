package software.sigma.comparissonservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.sigma.comparissonservice.dao.ConfigurationDao;
import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.utils.ConfigurationsConverter;

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

	public final void setDao(final ConfigurationDao dao) {
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
	public ConfigurationProtocol convertToProtocol(final Configuration configuration) {

		return ConfigurationsConverter.convert(configuration);
	}

	@Override
	public List<ConfigurationProtocol> convertToProtocolList(final List<Configuration> configurations) {
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

	@Override
	public boolean save(final Configuration configuration) {
		return dao.save(configuration);
	}

	@Override
	public Configuration convertFromFrotocol(ConfigurationProtocol configurationProtocol) {
		return ConfigurationsConverter.convert(configurationProtocol);
	}

}
