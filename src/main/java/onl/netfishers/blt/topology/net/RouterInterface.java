package onl.netfishers.blt.topology.net;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorNode;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

@XmlDiscriminatorNode("@category")
@XmlDiscriminatorValue("Ethernet")
@XmlRootElement()
@XmlAccessorType(value = XmlAccessType.NONE)
public class RouterInterface {

	public static enum RouterInterfaceType {
		CONFIGURED,
		LIVE
	}
	private static long idGenerator = 0;

	private long id = 0;
	protected String name;
	protected String description;
	protected Ipv4Subnet ipv4Address;
	protected boolean shutdown = false;
	protected RouterInterface.RouterInterfaceType type = RouterInterfaceType.CONFIGURED;
	
	public RouterInterface() {
	}

	public RouterInterface(String name) {
		this.name = name;
	}
	
	@XmlAttribute
	public long getId() {
		if (id == 0) {
			id = ++idGenerator;
		}
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
		if (id > idGenerator) {
			idGenerator = id;
		}
	}


	@XmlElement
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlElement
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@XmlElement
	public Ipv4Subnet getIpv4Address() {
		return ipv4Address;
	}
	public void setIpv4Address(Ipv4Subnet ipv4Address) {
		this.ipv4Address = ipv4Address;
	}
	@XmlElement
	public boolean isShutdown() {
		return shutdown;
	}
	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}

	@XmlElement
	public RouterInterface.RouterInterfaceType getType() {
		return type;
	}

	public void setType(RouterInterface.RouterInterfaceType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
	  final int prime = 31;
	  int result = 1;
	  result = prime * result + ((name == null) ? 0 : name.hashCode());
	  return result;
	}

	@Override
	public boolean equals(Object obj) {
	  if (this == obj)
		  return true;
	  if (obj == null)
		  return false;
	  if (getClass() != obj.getClass())
		  return false;
	  RouterInterface other = (RouterInterface) obj;
	  if (name == null) {
		  if (other.name != null)
			  return false;
	  }
	  else if (!name.equals(other.name))
		  return false;
	  return true;
  }
}