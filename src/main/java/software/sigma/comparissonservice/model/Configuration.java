package software.sigma.comparissonservice.model;

/**
 * Entity for configuration.
 * 
 * @author alexandr.efimov
 *
 */
public class Configuration {

	/**
	 * Is id of configuration.
	 */
	private Integer id;
	/**
	 * Name of configuration.
	 */
	private String name;
	/**
	 * Configuration represented as byte array.
	 */
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
	public String toString() {
		return "Configuration [id=" + id + ", name=" + name + ", configContent=" + configContent + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configContent == null) ? 0 : configContent.hashCode());
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
		Configuration other = (Configuration) obj;
		if (configContent == null) {
			if (other.configContent != null)
				return false;
		} else if (!configContent.equals(other.configContent))
			return false;
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

}
