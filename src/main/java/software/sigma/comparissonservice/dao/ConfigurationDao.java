package software.sigma.comparissonservice.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import software.sigma.comparissonservice.model.Configuration;

@Component
public class ConfigurationDao implements GenericDao<Configuration> {
	private static final String SQL_ALL_CONFIGS = "SELECT * FROM configuration";
	private static final String SQL_FIND_CONFIG = "SELECT * FROM comparisson_service.configuration_schema JOIN configuration ON configuration_schema.id=configuration.id WHERE configuration.id=?;";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Configuration> getAll() {
		List<Configuration> configurations = new ArrayList<>();

		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(SQL_ALL_CONFIGS);
		for (Map<String, Object> row : queryForList) {
			Configuration configuration = new Configuration();
			configuration.setId(Integer.parseInt(String.valueOf(row.get("id"))));
			configuration.setName((String) row.get("name"));
			configuration.setConfiguration((byte[]) row.get("configuration_schema"));

			configurations.add(configuration);
		}

		return configurations;
	}

	@Override
	public Configuration getById(int id) {

		Configuration configuration = jdbcTemplate.queryForObject(SQL_FIND_CONFIG, new Object[] { id },
				new RowMapper<Configuration>() {

					@Override
					public Configuration mapRow(ResultSet rs, int rowNum) throws SQLException {
						Configuration configuration = new Configuration();
						configuration.setId(rs.getInt("id"));
						configuration.setName(rs.getString("name"));
						configuration.setConfiguration(rs.getBytes("configuration_schema"));
						return configuration;
					}

				});

		return configuration;
	}

	@Override
	public boolean update(Configuration t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean save(Configuration t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

}
