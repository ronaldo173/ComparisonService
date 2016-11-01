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
import org.springframework.jdbc.core.PreparedStatementSetter;
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

	private static final String SQL_UPDATE_CONFIG_CONTENT = "UPDATE configuration_schema SET config_content =? WHERE id = ?;";
	private static final String SQL_UPDATE_CONFIG_INFO = "UPDATE configuration SET name = ? WHERE id = ?;";
	private static final String SQL_SELECT_ID_OF_CONFIG_SCHEMA_WHERE_ID = "SELECT id_schema FROM configuration where id = ";
	private static final String SQL_DELETE_CONFIG_CONTENT = "DELETE FROM configuration_schema WHERE id = ?;";
	private static final String SQL_DELETE_CONFIG_INFO = "DELETE FROM configuration WHERE id = ?;";

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
		Configuration configuration = null;
		List<Configuration> listConfigs = jdbcTemplate.query(SQL_FIND_CONFIG, new Object[] { id },
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
		if (listConfigs.size() == 1) {
			configuration = listConfigs.get(0);
		} else {
			configuration = new Configuration();
		}
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
	@Transactional
	public boolean update(final Configuration configuration) {

		// first update config_content from table configuration_schema
		// get id of current config schema
		String sqlGetIdOfSchemaForConfig = SQL_SELECT_ID_OF_CONFIG_SCHEMA_WHERE_ID + configuration.getId();
		final int idOfSchema = jdbcTemplate.queryForObject(sqlGetIdOfSchemaForConfig, Integer.class);

		// update config_content
		int updateConfigContentRowsAffected = jdbcTemplate.update(SQL_UPDATE_CONFIG_CONTENT,
				new PreparedStatementSetter() {
					@Override
					public void setValues(final PreparedStatement ps) throws SQLException {
						ps.setString(1, configuration.getConfigContent());
						ps.setInt(2, idOfSchema);
					}
				});

		// update configuration info
		int updateConfigInfoRowsAffected = jdbcTemplate.update(SQL_UPDATE_CONFIG_INFO, new PreparedStatementSetter() {
			@Override
			public void setValues(final PreparedStatement ps) throws SQLException {
				ps.setString(1, configuration.getName());
				ps.setInt(2, configuration.getId());
			}

		});
		return updateConfigContentRowsAffected == 1 && updateConfigInfoRowsAffected == 1;
	}

	@Override
	public boolean delete(final int id) {

		// first delete config_content from table configuration_schema
		String sqlGetIdOfSchemaForConfig = SQL_SELECT_ID_OF_CONFIG_SCHEMA_WHERE_ID + id;
		final int idOfSchema = jdbcTemplate.queryForObject(sqlGetIdOfSchemaForConfig, Integer.class);

		int deleteConfigContentRowsAffected = jdbcTemplate.update(SQL_DELETE_CONFIG_CONTENT, idOfSchema);
		jdbcTemplate.update(SQL_DELETE_CONFIG_INFO, id);
		return deleteConfigContentRowsAffected == 1;
	}

}
