package software.sigma.comparissonservice.model;

public class Configuration {

	private int id;
	private String name;
	private byte[] configuration;

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

	public byte[] getConfiguration() {
		return configuration;
	}

	public void setConfiguration(byte[] configuration) {
		this.configuration = configuration;
	}

	@Override
	public String toString() {
		return "Configuration [id=" + id + ", name=" + name + "]";
	}

}
