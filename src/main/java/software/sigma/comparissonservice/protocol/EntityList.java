package software.sigma.comparissonservice.protocol;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * List with entities of any type.
 * 
 * @author alexandr.efimov
 *
 * @param <T>
 *            is type of entity.
 */
@XmlRootElement(name = "configurations")
@XmlSeeAlso({ ConfigurationProtocol.class })
public class EntityList<T> {
	private List<T> list;

	public EntityList(List<T> list) {
		this.list = list;
	}

	public EntityList() {
	}

	@XmlAnyElement
	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
