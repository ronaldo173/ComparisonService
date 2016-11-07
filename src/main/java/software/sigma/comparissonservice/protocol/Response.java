package software.sigma.comparissonservice.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity for representing response result.
 * 
 * @author alexandr.efimov
 *
 */
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {

	/**
	 * Is success of something.
	 */
	private boolean isSuccess;
	/**
	 * List with errors.
	 */
	private List<String> errors;

	private List<String> informationMessages;

	public Response() {
		errors = new ArrayList<>();
	}

	public Response(boolean isSuccess, List<String> errors) {
		this.isSuccess = isSuccess;
		this.errors = errors;
	}

	public Response(boolean isSuccess, String error) {
		this.isSuccess = isSuccess;
		this.errors = Arrays.asList(error);
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public List<String> getInformationMessages() {
		return informationMessages;
	}

	public void setInformationMessages(List<String> informationMessages) {
		this.informationMessages = informationMessages;
	}

	@Override
	public String toString() {
		return "Response [isSuccess=" + isSuccess + ", errors=" + errors + ", informationMessages="
				+ informationMessages + "]";
	}

}
