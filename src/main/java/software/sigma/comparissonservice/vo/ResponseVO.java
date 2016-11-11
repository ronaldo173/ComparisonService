package software.sigma.comparissonservice.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Value object for response.
 * 
 * @author alexandr.efimov
 *
 */
public class ResponseVO {

	/**
	 * Is success of something.
	 */
	private boolean isSuccess;
	/**
	 * List with errors.
	 */
	private List<String> errors;

	private String sortedData;

	private String informationMessage;

	public ResponseVO() {
		errors = new ArrayList<>();
	}

	public ResponseVO(boolean isSuccess, List<String> errors) {
		this.isSuccess = isSuccess;
		this.errors = errors;
	}

	public ResponseVO(boolean isSuccess, String error) {
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

	public String getInformationMessage() {
		return informationMessage;
	}

	public void setInformationMessage(String informationMessage) {
		this.informationMessage = informationMessage;
	}

	@Override
	public String toString() {
		return "Response [isSuccess=" + isSuccess + ", errors=" + errors + ", sortedData=" + sortedData + "]";
	}

}
