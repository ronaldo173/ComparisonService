package software.sigma.comparissonservice.protocol;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * List with configuration.
 * 
 * @author alexandr.efimov
 *
 */
@XmlRootElement(name = "configurations")
@XmlSeeAlso({ ConfigurationDTO.class })
public class ConfigurationsDTO {
	private List<ConfigurationDTO> list;

	public ConfigurationsDTO(List<ConfigurationDTO> list) {
		this.list = list;
	}

	public ConfigurationsDTO() {
	}

	@XmlAnyElement
	public List<ConfigurationDTO> getList() {
		return list;
	}

	public void setList(List<ConfigurationDTO> list) {
		this.list = list;
	}

}
