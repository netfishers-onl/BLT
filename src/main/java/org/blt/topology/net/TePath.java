/*package org.blt.topology.net;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class TePath {

	public static enum TeHopType {
		STRICT,
		LOOSE
	}

	public static enum TePathType {
		DYNAMIC,
		EXPLICIT,
		LIVING
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class TeHop {
		private TeHopType type;
		private Ipv4Subnet ipAddress;

		protected TeHop() {

		}

		public TeHop(Ipv4Subnet ipAddress, TeHopType type) {
			this.type = type;
			this.ipAddress = ipAddress;
		}

		@XmlElement
		public TeHopType getType() {
			return type;
		}
		public void setType(TeHopType type) {
			this.type = type;
		}
		@XmlElement
		public Ipv4Subnet getIpAddress() {
			return ipAddress;
		}
		public void setIpAddress(Ipv4Subnet ipAddress) {
			this.ipAddress = ipAddress;
		}

		@Override
		public String toString() {
			return String.format("TeHop %s [%s]", ipAddress, type);
		}
	}

	private static long idGenerator = 0;

	private long id = ++idGenerator;
	private String name = "";
	private TePath.TePathType type = TePath.TePathType.DYNAMIC;
	private Map<Integer, TePath.TeHop> hops = new HashMap<Integer, TePath.TeHop>();

	protected TePath() {
	}

	public TePath(TePath.TePathType type) {
		this.type = type;
	}

	public TePath(TePath.TePathType type, Map<Integer, TePath.TeHop> hops) {
		this.type = type;
		this.hops = hops;
	}

	@XmlAttribute
	public long getId() {
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
	public Map<Integer, TePath.TeHop> getHops() {
		return hops;
	}

	public void setHops(Map<Integer, TePath.TeHop> hops) {
		this.hops = hops;
	}

	public void addHop(int index, TePath.TeHop hop) {
		this.hops.put(index, hop);
	}

	@XmlElement
	public TePath.TePathType getType() {
		return type;
	}

	public void setType(TePath.TePathType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return String.format("TePath '%s', %s, via {%s}", name, type,
				hops);
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
	  TePath other = (TePath) obj;
	  if (name == null) {
		  if (other.name != null)
			  return false;
	  }
	  else if (!name.equals(other.name))
		  return false;
	  return true;
  }

}*/