package software.sigma.comparissonservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.sigma.comparissonservice.dao.ConfigurationDao;
import software.sigma.comparissonservice.dao.GenericDao;
import software.sigma.comparissonservice.model.Configuration;

/**
 * Implementation of {@link GenericSerice} for work with {@link Configuration}
 * 
 * @author alexandr.efimov
 *
 */
@Service
public class ConfigurationService implements GenericSerice<Configuration> {

	@Autowired
	private GenericDao<Configuration> dao;

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

}
