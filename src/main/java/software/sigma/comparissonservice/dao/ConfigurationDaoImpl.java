package software.sigma.comparissonservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import software.sigma.comparissonservice.model.Configuration;

/**
 * Implementation of {@link GenericDao} for {@link Configuration} entities.
 * 
 * @author alexandr.efimov
 *
 */
@Component
public class ConfigurationDaoImpl implements ConfigurationDao {
	/**
	 * Name of column with config_schema in table.
	 */
	private static final String COL_CONFIG_SCHEMA = "config_content";
	/**
	 * Name of column with configuration name in table.
	 */
	private static final String COL_NAME = "name";
	/**
	 * Name of column with config_schema in table.
	 */
	private static final String COL_ID = "id";

	/**
	 * SQL query for retrieving all configurations from db.
	 */
	private static final String SQL_ALL_CONFIGS = "SELECT * FROM configuration";
	/**
	 * SQL query for retrieving configuration by id from db.
	 */
	private static final String SQL_FIND_CONFIG = "SELECT * FROM configuration_schema JOIN configuration ON configuration_schema.id=configuration.id_schema WHERE configuration.id=?;";
	/**
	 * SQL query for insert configuration content to storage.
	 */
	private static final String SQL_INSERT_CONFIG_CONTENT = "INSERT INTO configuration_schema(config_content) VALUES (?);";
	/**
	 * SQL query for insert configuration information.
	 */
	private static final String SQL_INSERT_CONFIG_INFO = "INSERT INTO configuration(name, id_schema) VALUES(?, ?);";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Configuration> getAll() {

		List<Configuration> configurations = jdbcTemplate.query(SQL_ALL_CONFIGS,
				new BeanPropertyRowMapper<Configuration>(Configuration.class));

		return configurations;
	}

	@Override
	public Configuration getById(final int id) {
		Configuration configuration = jdbcTemplate.queryForObject(SQL_FIND_CONFIG, new Object[] { id },
				new RowMapper<Configuration>() {

					@Override
					public Configuration mapRow(final ResultSet rs, final int rowNum) throws SQLException {
						Configuration configuration = new Configuration();
						configuration.setId(rs.getInt(COL_ID));
						configuration.setName(rs.getString(COL_NAME));
						configuration.setConfigContent(rs.getString(COL_CONFIG_SCHEMA));
						return configuration;
					}

				});

		return configuration;
	}

	@Override
	@Transactional
	public boolean save(final Configuration configuration) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int updateContentAmountRows = jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(SQL_INSERT_CONFIG_CONTENT, new String[] { COL_ID });
				ps.setString(1, configuration.getConfigContent());
				return ps;
			}
		}, keyHolder);

		int updateInfoAmountRows = jdbcTemplate.update(SQL_INSERT_CONFIG_INFO,
				new Object[] { configuration.getName(), keyHolder.getKey() });
		return (updateContentAmountRows == 1 && updateInfoAmountRows == 1);
	}

	@Override
	public boolean update(final Configuration configuration) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(final int id) {
		// TODO Auto-generated method stub
		return false;
	}

}
