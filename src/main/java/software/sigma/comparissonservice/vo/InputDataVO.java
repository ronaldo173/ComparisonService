package software.sigma.comparissonservice.vo;

import java.util.List;

/**
 * Value object of input data.
 * 
 * @author alexandr.efimov
 *
 */
public class InputDataVO {

	private String dataForSort;

	private List<SortOrderFieldVO> sortOrder;

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

	public List<SortOrderFieldVO> getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(List<SortOrderFieldVO> sortOrder) {
		this.sortOrder = sortOrder;
	}

	@Override
	public String toString() {
		return "InputData [dataForSort=" + dataForSort + ", sortOrder=" + sortOrder + ", configName=" + configName
				+ "]";
	}

}
