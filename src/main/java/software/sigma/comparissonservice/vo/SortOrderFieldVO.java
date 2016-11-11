package software.sigma.comparissonservice.vo;

public class SortOrderFieldVO {

	private String name;

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
