package software.sigma.comparissonservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.sigma.comparissonservice.dao.ConfigurationDao;
import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.protocol.ConfigurationProtocol;
import software.sigma.comparissonservice.utils.ConfigurationsConverter;

/**
 * Implementation of {@link ConfigurationService} for work with
 * {@link Configuration}.
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
	public List<ConfigurationProtocol> getAll() {
		List<Configuration> configurations = dao.getAll();
		return ConfigurationsConverter.convertToProtocolList(configurations);
	}

	@Override
	public ConfigurationProtocol getById(final int id) {
		Configuration configById = dao.getById(id);
		return ConfigurationsConverter.convert(configById);
	}

	@Override
	public boolean save(final ConfigurationProtocol configuration) {
		return dao.save(ConfigurationsConverter.convert(configuration));
	}

	@Override
	public boolean update(final ConfigurationProtocol configurationProtocol) throws ApplicationException {
		boolean updateSuccess = false;

		try {
			updateSuccess = dao.update(ConfigurationsConverter.convert(configurationProtocol));
		} catch (Throwable e) {
			throw new ApplicationException("Can't update configuration. Id: " + configurationProtocol.getId(), e);
		}
		return updateSuccess;
	}

	@Override
	public boolean delete(final Integer id) throws ApplicationException {
		boolean deleteSuccess = false;
		try {
			deleteSuccess = dao.delete(id);
		} catch (Throwable e) {
			throw new ApplicationException("Can't delete configuration. Id: " + id, e);
		}

		return deleteSuccess;
	}

}
