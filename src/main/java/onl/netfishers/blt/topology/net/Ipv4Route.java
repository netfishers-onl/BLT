package onl.netfishers.blt.topology.net;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import onl.netfishers.blt.bgp.net.attributes.bgplsnlri.BgpLsProtocolId;

@XmlRootElement(name = "ipv4Route")
@XmlAccessorType(value = XmlAccessType.NONE)
public class Ipv4Route {
	
	private static long idGenerator = 0;

	private long id = 0;

	private Ipv4Subnet subnet;
	private long metric;
	private Ipv4Subnet nextHop;
	private String nextInterface;
	private BgpLsProtocolId protocolId;

	protected Ipv4Route() {

	}

	public Ipv4Route(Ipv4Subnet subnet, long metric, Ipv4Subnet nextHop,
			String nextInterface, BgpLsProtocolId protocolId) {
		this.subnet = subnet;
		this.metric = metric;
		this.nextHop = nextHop;
		this.nextInterface = nextInterface;
		this.protocolId = protocolId;
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
	public long getMetric() {
		return metric;
	}
	public void setMetric(long metric) {
		this.metric = metric;
	}

	@XmlElement
	public Ipv4Subnet getNextHop() {
		return nextHop;
	}

	public void setNextHop(Ipv4Subnet nextHop) {
		this.nextHop = nextHop;
	}

	@XmlElement
	public String getNextInterface() {
		return nextInterface;
	}

	public void setNextInterface(String nextInterface) {
		this.nextInterface = nextInterface;
	}
	
	@XmlElement
	public BgpLsProtocolId getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(BgpLsProtocolId protocolId) {
		this.protocolId = protocolId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (metric ^ (metric >>> 32));
		result = prime * result + ((nextHop == null) ? 0 : nextHop.hashCode());
		result = prime * result + ((nextInterface == null) ? 0 : nextInterface.hashCode());
		result = prime * result + ((subnet == null) ? 0 : subnet.hashCode());
		result = prime * result + ((protocolId == null) ? 0 : protocolId.hashCode());
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
		Ipv4Route other = (Ipv4Route) obj;
		if (nextHop == null) {
			if (other.nextHop != null)
				return false;
		}
		else if (!nextHop.equals(other.nextHop))
			return false;
		if (nextInterface == null) {
			if (other.nextInterface != null)
				return false;
		}
		else if (!nextInterface.equals(other.nextInterface))
			return false;
		if (subnet == null) {
			if (other.subnet != null)
				return false;
		}
		else if (!subnet.equals(other.subnet))
			return false;
		if (protocolId == null) {
			if (other.protocolId != null)
				return false;
		}
		else if (!protocolId.equals(other.protocolId))
			return false;
		return true;
	}

}