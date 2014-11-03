package onl.netfishers.blt.topology.net;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SnmpCommunity implements Comparable<SnmpCommunity> {
	
	private static long idGenerator = 0;

	private long id = 0;
	private Ipv4Subnet subnet;
	private String community;

	public SnmpCommunity(Ipv4Subnet subnet, String community) {
		this.subnet = subnet;
		this.community = community;
	}

	protected SnmpCommunity() {

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
	public Ipv4Subnet getSubnet() {
		return subnet;
	}

	public void setSubnet(Ipv4Subnet subnet) {
		this.subnet = subnet;
	}

	@XmlElement
	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subnet == null) ? 0 : subnet.hashCode());
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
		SnmpCommunity other = (SnmpCommunity) obj;
		if (subnet == null) {
			if (other.subnet != null)
				return false;
		}
		else if (!subnet.equals(other.subnet))
			return false;
		return true;
	}

	@Override
	public int compareTo(SnmpCommunity o) {
		if (o == null) {
			return 1;
		}
		return (this.getSubnet().compareTo(o.getSubnet()));
	}

}
