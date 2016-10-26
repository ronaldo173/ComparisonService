package software.sigma.comparissonservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.sigma.comparissonservice.dao.ConfigurationDao;
import software.sigma.comparissonservice.model.Configuration;

@Service
public class ConfigurationService {

	@Autowired
	private ConfigurationDao dao;

	public void setDao(ConfigurationDao dao) {
		this.dao = dao;
	}

	public List<Configuration> getAllConfigs() {

		return dao.getAll();
	}

	public Configuration getConfig(int id) {

		return dao.getById(id);

	}

}
