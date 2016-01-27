/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
package onl.netfishers.blt.topology.net;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.googlecode.ipv6.IPv6Network;

import onl.netfishers.blt.bgp.net.attributes.bgplsnlri.BgpLsProtocolId;

@XmlRootElement(name = "ipv6Route")
@XmlAccessorType(value = XmlAccessType.NONE)
public class Ipv6Route {
	
	private static long idGenerator = 0;

	private long id = 0;

	private IPv6Network subnet;
	private long metric;
	private IPv6Network nextHop;
	private String nextInterface;
	private BgpLsProtocolId protocolId;
	private long dateTicks;
	private boolean isJustNew;
	private boolean isJustLost;

	protected Ipv6Route() {

	}

	public Ipv6Route(IPv6Network subnet, long metric, IPv6Network nextHop,
			String nextInterface, BgpLsProtocolId protocolId, long dateTicks, boolean isLost, boolean isNew) {
		this.subnet = subnet;
		this.metric = metric;
		this.nextHop = nextHop;
		this.nextInterface = nextInterface;
		this.protocolId = protocolId;
		this.dateTicks = dateTicks;
		this.isJustLost = isLost;
		this.isJustNew = isNew;
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
	public IPv6Network getSubnet() {
		return subnet;
	}
	public void setSubnet(IPv6Network subnet) {
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
	public IPv6Network getNextHop() {
		return nextHop;
	}

	public void setNextHop(IPv6Network nextHop) {
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

	@XmlElement
	public long getDate() {
		return dateTicks;
	}

	public void setDate(long dateTicks) {
		this.dateTicks = dateTicks;
	}

	@XmlElement
	public boolean isJustLost() {
		return isJustLost;
	}
	
	public void setLost(boolean isLost) {
		this.isJustLost = isLost;
	}

	@XmlElement
	public boolean isJustNew() {
		return isJustNew;
	}
	
	public void setNew(boolean isNew) {
		this.isJustNew = isNew;
	}

	@Override
	public int hashCode() {
		final int prime = 32;
		int result = 1;
		result = prime * result + ((nextHop == null) ? 0 : nextHop.hashCode());
		result = prime * result
				+ ((nextInterface == null) ? 0 : nextInterface.hashCode());
		result = prime * result
				+ ((protocolId == null) ? 0 : protocolId.hashCode());
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
		Ipv6Route other = (Ipv6Route) obj;
		if (nextHop == null) {
			if (other.nextHop != null)
				return false;
		} else if (!nextHop.equals(other.nextHop))
			return false;
		if (nextInterface == null) {
			if (other.nextInterface != null)
				return false;
		} else if (!nextInterface.equals(other.nextInterface))
			return false;
		if (protocolId != other.protocolId)
			return false;
		if (subnet == null) {
			if (other.subnet != null)
				return false;
		} else if (!subnet.equals(other.subnet))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IPv6 route " + subnet + " via " + nextInterface + " next hop " +
				nextHop + ", metric " + metric + " age in ticks " + dateTicks + " (" + protocolId + ")";
	}
	

}