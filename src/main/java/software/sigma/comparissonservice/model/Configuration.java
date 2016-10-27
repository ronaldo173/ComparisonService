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
	private int id;
	/**
	 * Name of configuration.
	 */
	private String name;
	/**
	 * Configuration represented as byte array.
	 */
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
		return "Configuration [id=" + id + ", name=" + name + ", configContent=" + configContent + "]";
	}

}
