package software.sigma.comparissonservice;

import javax.sql.DataSource;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import software.sigma.comparissonservice.dao.ConfigurationDao;
import software.sigma.comparissonservice.dao.ConfigurationDaoImpl;
import software.sigma.comparissonservice.service.ConfigurationService;
import software.sigma.comparissonservice.service.ConfigurationServiceImpl;
import software.sigma.comparissonservice.service.SortService;
import software.sigma.comparissonservice.service.SortServiceImpl;

/**
 * Context for test service.
 * 
 * @author alexandr.efimov
 *
 */
public class TestContext {

	@Bean
	ConfigurationService configurationServiceImpl() {
		return Mockito.mock(ConfigurationServiceImpl.class);
	}

	@Bean
	ConfigurationDao configurationDaoImpl() {
		return Mockito.mock(ConfigurationDaoImpl.class);
	}

	@Bean
	JdbcTemplate jdbcTemplate() {
		return Mockito.mock(JdbcTemplate.class);
	}

	@Bean
	DataSource dataSource() {
		return Mockito.mock(DataSource.class);
	}

	@Bean
	SortService sortServiceImpl() {
		return Mockito.mock(SortServiceImpl.class);
	}
}
