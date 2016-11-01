package software.sigma.comparissonservice.constants;

/**
 * Constants for producing information about errors.
 * 
 * @author alexandr.efimov
 *
 */
public enum ErrorConstants {
	/**
	 * Constants with error messages inside. Message can be retrieved with
	 * getter.
	 */
	ERR_SHOULD_BE_TYPE(" should be of type "), ERR_NOT_VALID_CONFIG_CONTENT("Configuration content(xsd schema) is not valid");

	/**
	 * Message for retrieving.
	 */
	private String value;

	/**
	 * Create constant object with message inside.
	 * 
	 * @param value
	 *            is value for constant
	 */
	ErrorConstants(final String value) {
		this.value = value;
	}

	/**
	 * Get message of error.
	 * 
	 * @return {@link String} value of error message.
	 */
	public String getValue() {
		return value;
	}

}
