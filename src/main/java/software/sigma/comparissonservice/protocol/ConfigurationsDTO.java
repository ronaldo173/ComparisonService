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

	/**
	 * Create by list with DTO.
	 * 
	 * @param list
	 *            is list with DTO's.
	 */
	public ConfigurationsDTO(List<ConfigurationDTO> list) {
		this.list = list;
	}

	/**
	 * Create list with DTO.
	 */
	public ConfigurationsDTO() {
		super();
	}

	@XmlAnyElement
	public List<ConfigurationDTO> getList() {
		return list;
	}

	public void setList(List<ConfigurationDTO> list) {
		this.list = list;
	}

}
