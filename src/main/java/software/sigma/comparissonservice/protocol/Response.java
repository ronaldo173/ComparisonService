package software.sigma.comparissonservice.protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

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

	@XmlCDATA
	private String sortedData;

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

	public String getSortedData() {
		return sortedData;
	}

	public void setSortedData(String sortedData) {
		this.sortedData = sortedData;
	}

	@Override
	public String toString() {
		return "Response [isSuccess=" + isSuccess + ", errors=" + errors + ", sortedData=" + sortedData + "]";
	}

}
