package software.sigma.comparissonservice.service;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.InputData;
import software.sigma.comparissonservice.protocol.Response;

public interface SortService {

	/**
	 * Validate content of input data.
	 * 
	 * @param inputData
	 *            object with input data
	 * @param response
	 *            is {@link Response} object for adding error messages about
	 *            validation if smth not valid
	 * @return true if input data is valid
	 */
	boolean validateInputData(InputData inputData, Response response);

	/**
	 * Sort input data.
	 * 
	 * @param inputData
	 *            object with input data for sort and sort order
	 * @throws ApplicationException
	 *             if smth happened
	 * @return response object with information about work
	 */
	Response sort(InputData inputData) throws ApplicationException;

}
