package software.sigma.comparissonservice.vo;

/**
 * Sort order vo.
 * 
 * @author alexandr.efimov
 *
 */
public class SortOrderFieldVO {

	private String name;

	private String ordering;

	/**
	 * Create VO.
	 */
	public SortOrderFieldVO() {
		super();
	}

	/**
	 * Create VO by field name.
	 * 
	 * @param name
	 *            is name of field
	 */
	public SortOrderFieldVO(String name) {
		super();
		this.name = name;
	}

	/**
	 * Create VO by field name and ordering type.
	 * 
	 * @param name
	 *            is name of field
	 * @param ordering
	 *            is order type of field
	 */
	public SortOrderFieldVO(String name, String ordering) {
		super();
		this.name = name;
		this.ordering = ordering;
	}

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
