package software.sigma.comparissonservice.controller;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import software.sigma.comparissonservice.constants.ErrorConstants;
import software.sigma.comparissonservice.model.Response;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = Logger.getLogger(CustomRestExceptionHandler.class);

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseBody
	public Response handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception,
			WebRequest request) {
		String error = exception.getName() + ErrorConstants.ERR_SHOULD_BE_TYPE.getValue()
				+ exception.getRequiredType().getName();

		LOGGER.error(exception.getName(), exception.getCause());
		return new Response(false, error);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseBody
	public Response handleEmptyDataException(EmptyResultDataAccessException exception) {

		LOGGER.error(exception.getMessage(), exception.getCause());
		return new Response(false, ErrorConstants.ERR_NO_DATA_FOUND.getValue());

	}
}
