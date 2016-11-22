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

	/**
	 * Create exception object.
	 * 
	 * @param message
	 *            is message with description
	 * @param cause
	 *            is cause of exception
	 */
	public ApplicationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create exception object.
	 * 
	 * @param cause
	 *            is cause of exception
	 */
	public ApplicationException(final Throwable cause) {
		super(cause);
	}

	/**
	 * Create exception object.
	 * 
	 * @param message
	 *            is message with description
	 */
	public ApplicationException(final String message) {
		super(message);
	}

}
