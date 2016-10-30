package software.sigma.comparissonservice.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;
import org.hibernate.validator.constraints.NotEmpty;

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
	private Integer id;
	/**
	 * Name of configuration.
	 */
	@NotEmpty
	private String name;
	/**
	 * Configuration represented as byte array.
	 */
	@XmlCDATA
	@NotEmpty
	private String configContent;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigurationProtocol other = (ConfigurationProtocol) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConfigurationProtocol [id=" + id + ", name=" + name + ", configContent=" + configContent + "]";
	}

}
