package software.sigma.comparissonservice.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import software.sigma.comparissonservice.model.Configuration;

@Component
public class ConfigurationDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Configuration> getAll() {
		List<Configuration> configurations = new ArrayList<>();

		String sql = "SELECT * FROM configuration";

		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> row : queryForList) {
			Configuration configuration = new Configuration();
			configuration.setId(Integer.parseInt(String.valueOf(row.get("id"))));
			configuration.setName((String) row.get("name"));
			configuration.setConfiguration((byte[]) row.get("configuration_schema"));

			configurations.add(configuration);
		}

		return configurations;
	}

}
