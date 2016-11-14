package software.sigma.comparissonservice.comparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;

/**
 * Comparator for comparing XML Nodes. Compare result depends on sort order,
 * passed in constructor as {@link LinkedHashMap} object with data in format:
 * 
 * key - name of field, value - type of sort(asc, desc), if null or not correct
 * - descending by default
 * 
 * Compare nodes depends on order of elements in map.
 * 
 * @author alexandr.efimov
 *
 */
public class NodeComparatorByOrder implements Comparator<Node>, Serializable {

	private static final long serialVersionUID = -8860004823204427025L;
	/**
	 * Name of param for ascending type of sorting.
	 */
	private static final String SORT_TYPE_ASCENDING = "ASC";
	/**
	 * Map with order-> name of field, order type(asc, desc).
	 */
	private Map<String, String> mapOrderNamesOrdering;

	public NodeComparatorByOrder(LinkedHashMap<String, String> mapOrderNamesOrdering) {
		this.mapOrderNamesOrdering = mapOrderNamesOrdering;
	}

	@Override
	public int compare(final Node node1, final Node node2) {
		final XPath xPath = XPathFactory.newInstance().newXPath();
		int compareResult = 0;
		try {
			for (Entry<String, String> entry : mapOrderNamesOrdering.entrySet()) {
				String orderElementName = entry.getKey();
				String orderElementOrderings = entry.getValue();

				Node node1ForComparing = (Node) xPath.compile(orderElementName).evaluate(node1, XPathConstants.NODE);
				Node node2ForCOmparing = (Node) xPath.compile(orderElementName).evaluate(node2, XPathConstants.NODE);

				if (node1ForComparing != null && node2ForCOmparing != null && node1ForComparing.hasChildNodes()
						&& node2ForCOmparing.hasChildNodes()) {
					String node1Value = node1ForComparing.getFirstChild().getNodeValue();
					String node2Value = node2ForCOmparing.getFirstChild().getNodeValue();

					if (orderElementOrderings != null && orderElementOrderings.equalsIgnoreCase(SORT_TYPE_ASCENDING)) {
						compareResult = compareValuesDependsOnType(node1Value, node2Value);
					} else {
						compareResult = compareValuesDependsOnType(node2Value, node1Value);
					}
				}

				if (compareResult != 0) {
					break;
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return compareResult;
	}

	/**
	 * Compare strings according to it's type - if numbers inside - compare as
	 * numbers, else as strings.
	 * 
	 * @param str1
	 * @param str2
	 * @return result of comparing
	 */
	private int compareValuesDependsOnType(final String str1, final String str2) {
		if (isNumeric(str1) && isNumeric(str2)) {
			return Double.compare(Double.valueOf(str1), Double.valueOf(str2));
		}
		return str1.compareTo(str2);
	}

	/**
	 * Check if string a number.
	 * 
	 * @param strForCheck
	 *            is string for check
	 * @return true if number
	 */
	private boolean isNumeric(final String strForCheck) {
		try {
			Double.parseDouble(strForCheck);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
