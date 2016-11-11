package software.sigma.comparissonservice.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.hibernate.validator.constraints.NotEmpty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SortOrderFieldDTO {

	@NotEmpty
	@XmlPath("@name")
	private String name;

	@XmlPath("@ordering")
	private String ordering;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrdering() {
		return ordering;
	}

	public void setOrdering(String ordering) {
		this.ordering = ordering;
	}

	@Override
	public String toString() {
		return "SortOrderField [name=" + name + ", ordering=" + ordering + "]";
	}

}
