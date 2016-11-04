package software.sigma.comparissonservice.controller;

import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.Response;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = Logger.getLogger(CustomRestExceptionHandler.class);

	/**
	 * Response if not correct format of id in request to service.
	 * 
	 * @param exception
	 * @return {@link Response} object with information
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseBody
	public Response handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException exception) {
		String errorMessage = exception.getName() + " should be of type " + exception.getRequiredType().getName();

		LOGGER.debug(exception.getMessage());
		return new Response(false, errorMessage);
	}

	@ExceptionHandler(ApplicationException.class)
	@ResponseBody
	public Response handlePersistException(final ApplicationException exception) {
		LOGGER.debug(exception.getMessage());
		return new Response(false, exception.getMessage());

	}

	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseBody
	public Response handlePersistDaoExceptionDublicateKey(final DuplicateKeyException exception) {
		LOGGER.debug(exception.getMessage());
		String message = exception.getMostSpecificCause().getMessage();
		return new Response(false, message);

	}

}
