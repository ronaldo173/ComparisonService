package software.sigma.comparissonservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "This configuration is not found")
public class ConfigurationNotFoundException extends Exception {
	private static final long serialVersionUID = -5323904178710664096L;

}
