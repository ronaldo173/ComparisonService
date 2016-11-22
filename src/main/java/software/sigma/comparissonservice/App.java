package software.sigma.comparissonservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Enter point to application. Start application as SpringBootApplication.
 * 
 * @author alexandr.efimov
 *
 */
@SpringBootApplication
public class App {

	/**
	 * For context setup. Use static methods.
	 */
	public App() {// NOSONAR
	}

	/**
	 * Enter point for run Spring Boot application.
	 * 
	 * @param args
	 *            expect nothing
	 */
	public static void main(final String[] args) {
		SpringApplication.run(App.class, args);// NOSONAR
	}

}
