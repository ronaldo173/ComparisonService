package software.sigma.comparissonservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.ConverterDtoVo;
import software.sigma.comparissonservice.protocol.InputDataDTO;
import software.sigma.comparissonservice.protocol.ResponseDTO;
import software.sigma.comparissonservice.service.SortService;
import software.sigma.comparissonservice.vo.InputDataVO;
import software.sigma.comparissonservice.vo.ResponseVO;

@RestController
public class SortRestController {

	@Autowired
	private SortService sortService;

	public void setSortService(SortService sortService) {
		this.sortService = sortService;
	}

	@RequestMapping(path = "/sort", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseDTO sortData(@Valid @RequestBody final InputDataDTO inputDataDto) throws ApplicationException {

		InputDataVO inputData = ConverterDtoVo.convert(inputDataDto);
		ResponseVO response = new ResponseVO();

		boolean isValidData = sortService.validateInputData(inputData, response);

		if (isValidData) {
			String sortedData = sortService.sort(inputData);
			response.setSortedData(sortedData);
		}

		response.setSuccess(isValidData);
		return ConverterDtoVo.convert(response);
	}

}