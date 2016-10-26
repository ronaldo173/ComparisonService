package software.sigma.comparissonservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import software.sigma.comparissonservice.model.Configuration;
import software.sigma.comparissonservice.model.EntityList;
import software.sigma.comparissonservice.model.ServiceError;
import software.sigma.comparissonservice.service.ConfigurationService;

@RestController
public class ConfigurationRestController {

	@Autowired
	private ConfigurationService service;

	public void setService(ConfigurationService service) {
		this.service = service;
	}

	@GetMapping("/test")
	public String test() {
		return "hello hello";
	}

	@GetMapping("/testException")
	public ServiceError testException() {
		ServiceError serviceError = new ServiceError(HttpStatus.BAD_GATEWAY, "message", "errors");
		return serviceError;
	}

	@GetMapping("/configuration")
	@RequestMapping(produces = "application/xml")
	public EntityList<Configuration> getAllConfigs() {
		List<Configuration> allConfigs = service.getAllConfigs();
		return new EntityList<Configuration>(allConfigs);
	}

	@GetMapping("/configuration/{id}")
	public Configuration getConfiguration(@PathVariable("id") Integer id) {
		return service.getConfig(id);
	}

}
