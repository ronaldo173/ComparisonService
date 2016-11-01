package software.sigma.comparissonservice.exception;

import org.apache.log4j.Logger;

/**
 * Exception from service -> means that there is some problem.
 * 
 * @author alexandr.efimov
 *
 */
public class ApplicationException extends Exception {

	private static final Logger LOGGER = Logger.getLogger(ApplicationException.class);
	private static final long serialVersionUID = 6140332572083713742L;

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
		LOGGER.info(cause);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
		LOGGER.info(cause);
	}

	public ApplicationException(String message) {
		super(message);
	}

}
