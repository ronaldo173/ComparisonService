package software.sigma.comparissonservice.protocol;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;
import org.hibernate.validator.constraints.NotEmpty;

@XmlRootElement(name = "input")
@XmlAccessorType(XmlAccessType.FIELD)
public class InputData {

	@NotEmpty
	@XmlCDATA
	private String dataForSort;

	@NotEmpty
	@XmlCDATA
	private String sortOrder;

	public String getDataForSort() {
		return dataForSort;
	}

	public void setDataForSort(String dataForSort) {
		this.dataForSort = dataForSort;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "InputData [dataForSort=" + dataForSort + ", sortOrder=" + sortOrder + "]";
	}

}
