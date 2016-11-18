package software.sigma.comparissonservice.exception;

/**
 * Exception from service -> means that something happened, description in
 * message inside.
 * 
 * @author alexandr.efimov
 *
 */
public class ApplicationException extends Exception {

	private static final long serialVersionUID = 6140332572083713742L;

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String message) {
		super(message);
	}

}
