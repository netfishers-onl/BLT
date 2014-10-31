package org.blt.topology.net;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorNode;
import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

@XmlDiscriminatorNode("@category")
@XmlDiscriminatorValue("Ethernet")
@XmlRootElement()
@XmlAccessorType(value = XmlAccessType.NONE)
//@XmlSeeAlso({ TeTunnel.class, P2pTeTunnel.class, P2mpTeTunnel.class })
public class RouterInterface {

	public static enum RouterInterfaceType {
		CONFIGURED,
		LIVE
	}
	private static long idGenerator = 0;

	private long id = 0;
	protected String name;
	protected String description;
	protected String inAccessGroup;
	protected String inServicePolicy;
	protected Ipv4Subnet ipv4Address;
	protected int dot1qTag = 0;
	protected boolean shutdown = false;
	protected RouterInterface.RouterInterfaceType type = RouterInterfaceType.CONFIGURED;
	protected boolean igmpInterface = false;
	protected boolean multicastInterface = false;
	protected List<Ipv4StaticGroup> igmpStaticGroups = new CopyOnWriteArrayList<Ipv4StaticGroup>();

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
	public String getInAccessGroup() {
		return inAccessGroup;
	}
	public void setInAccessGroup(String inAccessGroup) {
		this.inAccessGroup = inAccessGroup;
	}
	@XmlElement
	public String getInServicePolicy() {
		return inServicePolicy;
	}
	public void setInServicePolicy(String inServicePolicy) {
		this.inServicePolicy = inServicePolicy;
	}
	@XmlElement
	public Ipv4Subnet getIpv4Address() {
		return ipv4Address;
	}
	public void setIpv4Address(Ipv4Subnet ipv4Address) {
		this.ipv4Address = ipv4Address;
	}
	@XmlElement
	public int getDot1qTag() {
		return dot1qTag;
	}
	public void setDot1qTag(int dot1qTag) {
		this.dot1qTag = dot1qTag;
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

	public List<Ipv4StaticGroup> getIgmpStaticGroups() {
		return igmpStaticGroups;
	}

	public void setIgmpStaticGroups(List<Ipv4StaticGroup> igmpStaticGroups) {
		if (igmpStaticGroups != null && this.igmpStaticGroups != null) {
			for (Ipv4StaticGroup group : igmpStaticGroups) {
				for (Ipv4StaticGroup oldGroup : this.igmpStaticGroups) {
					if (oldGroup.equals(group)) {
						group.setId(oldGroup.getId());
					}
				}
			}
		}
		this.clearIgmpStaticGroups();
		this.igmpStaticGroups.addAll(igmpStaticGroups);
	}

	public void clearIgmpStaticGroups() {
		this.igmpStaticGroups.clear();
	}

	public void addIgmpStaticGroup(Ipv4StaticGroup igmpStaticGroup) {
		this.igmpStaticGroups.add(igmpStaticGroup);
	}
	
	public Ipv4StaticGroup getIgmpStaticGroupById(long id) {
		for (Ipv4StaticGroup group : this.igmpStaticGroups) {
			if (group.getId() == id) {
				return group;
			}
		}
		return null;
	}

	@XmlElement
	public boolean isIgmpInterface() {
		return igmpInterface;
	}

	public void setIgmpInterface(boolean igmpInterface) {
		this.igmpInterface = igmpInterface;
	}

	@XmlElement
	public boolean isMulticastInterface() {
		return multicastInterface;
	}

	public void setMulticastInterface(boolean multicastInterface) {
		this.multicastInterface = multicastInterface;
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