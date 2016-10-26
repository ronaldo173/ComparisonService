package software.sigma.comparissonservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Configuration {

	private int id;
	private String name;
	@XmlElement(name = "config")
	private byte[] config;

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
		return config;
	}

	public void setConfiguration(byte[] configuration) {
		this.config = configuration;
	}

	@Override
	public String toString() {
		return "Configuration [id=" + id + ", name=" + name + "]";
	}

}
