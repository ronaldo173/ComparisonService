package software.sigma.comparissonservice.protocol;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;
import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.hibernate.validator.constraints.NotEmpty;

@XmlRootElement(name = "input")
@XmlAccessorType(XmlAccessType.FIELD)
public class InputData {

	@NotEmpty
	@XmlCDATA
	private String dataForSort;

	@NotNull
	private SortOrderFields sortOrder;

	@XmlPath("dataForSort/@configName")
	private String configName;

	public String getDataForSort() {
		return dataForSort;
	}

	public void setDataForSort(String dataForSort) {
		this.dataForSort = dataForSort;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public SortOrderFields getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrderFields sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "InputData [dataForSort=" + dataForSort + ", sortOrder=" + sortOrder + ", configName=" + configName
				+ "]";
	}

}
