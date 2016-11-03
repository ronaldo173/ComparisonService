package software.sigma.comparissonservice.service;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.InputData;

public interface SortService {

	/**
	 * Validate content for sort and sort order.
	 * 
	 * @param inputData
	 *            object with input data
	 * @return true if all is valid
	 * @throws ApplicationException
	 *             if smth not valid/some problem found, description in message
	 */
	boolean validateInputData(InputData inputData) throws ApplicationException;

	/**
	 * Sort input data.
	 * 
	 * @param inputData
	 *            object with input data for sort and sort order
	 * @throws ApplicationException
	 *             if smth happened
	 */
	void sort(InputData inputData) throws ApplicationException;

}
