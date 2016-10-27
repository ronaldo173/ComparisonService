package software.sigma.comparissonservice.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import software.sigma.comparissonservice.model.Configuration;

/**
 * Entity for representation {@link Configuration} in output format.
 * 
 * @author alexandr.efimov
 *
 */
@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigurationProtocol {

	/**
	 * Is id of configuration.
	 */
	private int id;
	/**
	 * Name of configuration.
	 */
	private String name;
	/**
	 * Configuration represented as byte array.
	 */
	@XmlElement(name = "configContent")
	private String configContent;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfigContent() {
		return configContent;
	}

	public void setConfigContent(String configContent) {
		this.configContent = configContent;
	}

	@Override
	public String toString() {
		return "ConfigurationProtocol [id=" + id + ", name=" + name + ", configContent=" + configContent + "]";
	}

}
