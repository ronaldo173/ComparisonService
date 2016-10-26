package software.sigma.comparissonservice.model;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.http.HttpStatus;

/**
 * The information or errors in form:
 * 
 * status: the HTTP status code
 * 
 * message: the error message associated with
 * 
 * exception error: List of constructed error messages
 * 
 * @author alexandr.efimov
 *
 */
@XmlRootElement
public class ServiceError {
	private HttpStatus status;
	private String message;
	private List<String> errors;

	public ServiceError() {
	}

	public ServiceError(HttpStatus status, String message, List<String> errors) {
		this.status = status;
		this.message = message;
		this.errors = errors;
	}

	public ServiceError(HttpStatus status, String message, String error) {
		this.status = status;
		this.message = message;
		this.errors = Arrays.asList(error);
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
