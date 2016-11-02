package software.sigma.comparissonservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import software.sigma.comparissonservice.exception.ApplicationException;
import software.sigma.comparissonservice.protocol.InputData;
import software.sigma.comparissonservice.service.SortService;

@RestController
public class SortRestController {

	@Autowired
	private SortService sortService;

	public void setSortService(SortService sortService) {
		this.sortService = sortService;
	}

	@RequestMapping(path = "/sort", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
	public String sortData(@Valid @RequestBody final InputData inputData) throws ApplicationException {

		sortService.sort(inputData);
		return inputData.toString();
	}
}