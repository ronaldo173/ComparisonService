package software.sigma.comparissonservice.service;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.vo.InputDataVO;
import software.sigma.comparissonservice.vo.ResponseVO;

/**
 * Service connected to sortings.
 * 
 * @author alexandr.efimov
 *
 */
public interface SortService {

	/**
	 * Validate content of input data.
	 * 
	 * @param inputData
	 *            object with input data
	 * @param response
	 *            is {@link ResponseVO} object for adding error messages about
	 *            validation if smth not valid
	 * @return true if input data is valid
	 */
	boolean validateInputData(InputDataVO inputData, ResponseVO response);

	/**
	 * Sort input data according to content of input object. Before sorting data
	 * should be validated by: {@code validateInputData}
	 * 
	 * @param inputData
	 *            object with input data for sort and sort order
	 * @throws ApplicationException
	 *             if smth happened
	 * @return string with sorted information inside
	 */
	String sort(InputDataVO inputData) throws ApplicationException;

}
