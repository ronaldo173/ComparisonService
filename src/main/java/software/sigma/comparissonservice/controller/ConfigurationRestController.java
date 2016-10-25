package software.sigma.comparissonservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import software.sigma.comparissonservice.model.Configuration;
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

	@GetMapping("/configurations")
	public List<Configuration> getAllConfigs() {
		System.err.println("CONF SERVICE: --->" + service);
		return service.getAllConfigs();
	}

}
