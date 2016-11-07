package software.sigma.comparissonservice.service;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.InputData;
import software.sigma.comparissonservice.protocol.Response;

public interface SortService {

	/**
	 * Validate content for sort and sort order.
	 * 
	 * @param inputData
	 *            object with input data
	 * @param response
	 *            is {@link Response} object for adding messages about
	 *            validation
	 * @return true if all is valid
	 * @throws ApplicationException
	 *             if smth not valid/some problem found, description in message
	 */
	boolean validateInputData(InputData inputData, Response response) throws ApplicationException;

	/**
	 * Sort input data.
	 * 
	 * @param inputData
	 *            object with input data for sort and sort order
	 * @return
	 * @throws ApplicationException
	 *             if smth happened
	 * @return response object with information about work
	 */
	Response sort(InputData inputData) throws ApplicationException;

}
