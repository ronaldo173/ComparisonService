package software.sigma.comparissonservice.protocol;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sortOrder")
@XmlAccessorType(XmlAccessType.FIELD)
public class SortOrderFieldsDTO {

	@NotNull
	@XmlElement(name = "field")
	private List<SortOrderFieldDTO> fields;

	public List<SortOrderFieldDTO> getFields() {
		return fields;
	}

	public void setFields(List<SortOrderFieldDTO> fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		return "SortOrderFields [fields=" + fields + "]";
	}

}
