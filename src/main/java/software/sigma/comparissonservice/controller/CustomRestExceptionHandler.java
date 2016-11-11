package software.sigma.comparissonservice.controller;

import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.ResponseDTO;
import software.sigma.comparissonservice.vo.ResponseVO;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = Logger.getLogger(CustomRestExceptionHandler.class);

	/**
	 * Response if not correct format of id in request to service.
	 * 
	 * @param exception
	 * @return {@link ResponseVO} object with information
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseBody
	public ResponseDTO handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException exception) {
		String errorMessage = exception.getName() + " should be of type " + exception.getRequiredType().getName();

		LOGGER.debug(exception.getMessage());
		return new ResponseDTO(false, errorMessage);
	}

	@ExceptionHandler(ApplicationException.class)
	@ResponseBody
	public ResponseDTO handlePersistException(final ApplicationException exception) {
		LOGGER.debug(exception.getMessage());
		return new ResponseDTO(false, exception.getMessage());

	}

	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseBody
	public ResponseDTO handlePersistDaoExceptionDublicateKey(final DuplicateKeyException exception) {
		LOGGER.debug(exception.getMessage());
		String message = exception.getMostSpecificCause().getMessage();
		return new ResponseDTO(false, message);

	}

}
