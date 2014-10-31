package org.blt.topology.net;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ipv4AccessList")
@XmlAccessorType(value = XmlAccessType.NONE)
public class Ipv4AccessList {
	
	private static long idGenerator = 0;

	@XmlAccessorType(value = XmlAccessType.NONE)
	public static enum AclAction {
		PERMIT,
		DENY
	}
	
	@XmlAccessorType(value = XmlAccessType.NONE)
	public static enum AclProtocol {
		IPV6,
		IPV4,
		TCP,
		UDP,
		OTHER
	}
	
	@XmlAccessorType(value = XmlAccessType.NONE)
	public static class Ipv4AccessListEntry {
		
		private int index;
		private AclAction action;
		private AclProtocol protocol;
		private Ipv4Subnet source;
		private Ipv4Subnet destination;
		
		@XmlElement
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		@XmlElement
		public AclAction getAction() {
			return action;
		}
		public void setAction(AclAction action) {
			this.action = action;
		}
		@XmlElement
		public AclProtocol getProtocol() {
			return protocol;
		}
		public void setProtocol(AclProtocol protocol) {
			this.protocol = protocol;
		}
		@XmlElement
		public Ipv4Subnet getSource() {
			return source;
		}
		public void setSource(Ipv4Subnet source) {
			this.source = source;
		}
		@XmlElement
		public Ipv4Subnet getDestination() {
			return destination;
		}
		public void setDestination(Ipv4Subnet destination) {
			this.destination = destination;
		}
	}
	
	
	private long id = 0;
	private String name;
	private List<Ipv4AccessListEntry> entries = new CopyOnWriteArrayList<Ipv4AccessListEntry>();

	protected Ipv4AccessList() {
		
	}
	
	public Ipv4AccessList(String name) {
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
	public List<Ipv4AccessListEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<Ipv4AccessListEntry> entries) {
		this.entries = entries;
	}
	
	public void clearEntries() {
		this.entries.clear();
	}
	
	public void addEntry(Ipv4AccessListEntry entry) {
		this.entries.add(entry);
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
	  Ipv4AccessList other = (Ipv4AccessList) obj;
	  if (name == null) {
		  if (other.name != null)
			  return false;
	  }
	  else if (!name.equals(other.name))
		  return false;
	  return true;
  }

}