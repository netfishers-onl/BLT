package onl.netfishers.blt.topology.net;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import onl.netfishers.blt.bgp.net.attributes.bgplsnlri.BgpLsProtocolId;
import onl.netfishers.blt.topology.net.Router.RouterIdentifier;

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.NONE)
public class Link {
	
	private static long idGenerator = 0;

	private long id = 0;
	private RouterIdentifier localRouter;
	private RouterIdentifier remoteRouter;
	private String localInterfaceName;
	private String remoteInterfaceName;
	private BgpLsProtocolId protocolId;

	private boolean lost = true;

	private long adminGroup;
	private float maxLinkBandwidth;
	private float maxReservableLinkBandwidth;
	private float[] unreservedBandwidth;
	private int teDefaultMetric;
	private int metric;
	
	private Ipv4Subnet localAddress ;
	private Ipv4Subnet remoteAddress ;
	
	
	
	private List<Long> sharedRiskLinkGroups = new LinkedList<Long>();

	protected Link() {

	}
	
	public Link(RouterIdentifier localRouter, RouterIdentifier remoteRouter,
			Ipv4Subnet localAddress, Ipv4Subnet remoteAddress, BgpLsProtocolId protocolId) {
		super();
		this.localRouter = localRouter;
		this.remoteRouter = remoteRouter;
		this.localAddress = localAddress;
		this.remoteAddress = remoteAddress;
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
	public RouterIdentifier getLocalRouter() {
		return localRouter;
	}
	public void setLocalRouter(RouterIdentifier localRouter) {
		this.localRouter = localRouter;
	}
	@XmlElement
	public RouterIdentifier getRemoteRouter() {
		return remoteRouter;
	}
	public void setRemoteRouter(RouterIdentifier remoteRouter) {
		this.remoteRouter = remoteRouter;
	}

	@XmlElement
	public Ipv4Subnet getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(Ipv4Subnet localAddress) {
		this.localAddress = localAddress;
	}

	@XmlElement
	public Ipv4Subnet getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(Ipv4Subnet remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	
	@XmlElement
	public BgpLsProtocolId getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(BgpLsProtocolId protocolId) {
		this.protocolId = protocolId;
	}

	@XmlElement
	public boolean isLost() {
		return lost;
	}

	public void setLost(boolean lost) {
		this.lost = lost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((localAddress == null) ? 0 : localAddress.hashCode());
		result = prime * result
				+ ((localRouter == null) ? 0 : localRouter.hashCode());
		result = prime * result
				+ ((remoteAddress == null) ? 0 : remoteAddress.hashCode());
		result = prime * result
				+ ((remoteRouter == null) ? 0 : remoteRouter.hashCode());
		result = prime * result
				+ ((protocolId == null) ? 0 : protocolId.hashCode());
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
		Link other = (Link) obj;
		if (protocolId == null) {
			if (other.protocolId != null)
				return false;
		}
		else if (!protocolId.equals(other.protocolId))
			return false;
		
		if (localAddress == null) {
			if (other.localAddress != null)
				return false;
		}
		else if (!localAddress.equals(other.localAddress))
			return false;
		
		if (localRouter == null) {
			if (other.localRouter != null)
				return false;
		}
		else if (!localRouter.equals(other.localRouter))
			return false;
		if (remoteAddress == null) {
			if (other.remoteAddress != null)
				return false;
		}
		else if (!remoteAddress.equals(other.remoteAddress))
			return false;
		
		if (remoteRouter == null) {
			if (other.remoteRouter != null)
				return false;
		}
		else if (!remoteRouter.equals(other.remoteRouter))
			return false;
		return true;
	}
	
	@XmlElement
	public long getAdminGroup() {
		return adminGroup;
	}

	public void setAdminGroup(long adminGroup) {
		this.adminGroup = adminGroup;
	}

	@XmlElement
	public float getMaxLinkBandwidth() {
		return maxLinkBandwidth;
	}

	public void setMaxLinkBandwidth(float maxLinkBandwidth) {
		this.maxLinkBandwidth = maxLinkBandwidth;
	}

	@XmlElement
	public float getMaxReservableLinkBandwidth() {
		return maxReservableLinkBandwidth;
	}

	public void setMaxReservableLinkBandwidth(float maxReservableLinkBandwidth) {
		this.maxReservableLinkBandwidth = maxReservableLinkBandwidth;
	}

	@XmlElement
	public float[] getUnreservedBandwidth() {
		return unreservedBandwidth;
	}

	public void setUnreservedBandwidth(float[] unreservedBandwidth) {
		this.unreservedBandwidth = unreservedBandwidth;
	}

	@XmlElement
	public int getTeDefaultMetric() {
		return teDefaultMetric;
	}

	public void setTeDefaultMetric(int teDefaultMetric) {
		this.teDefaultMetric = teDefaultMetric;
	}
	
	@XmlElement
	public int getMetric() {
		return metric;
	}

	public void setMetric(int metric) {
		this.metric = metric;
	}
	
	@XmlElement
	public List<Long> getSharedRiskLinkGroups() {
		return sharedRiskLinkGroups;
	}

	public void setSharedRiskLinkGroups(List<Long> sharedRiskLinkGroups) {
		this.sharedRiskLinkGroups = sharedRiskLinkGroups;
	}

	@XmlElement
	public String getLocalInterfaceName() {
		return localInterfaceName;
	}
	public void setLocalInterfaceName(String localInterfaceName) {
		this.localInterfaceName = localInterfaceName;
	}

	@XmlElement
	public String getRemoteInterfaceName() {
		return remoteInterfaceName;
	}
	public void setRemoteInterfaceName(String remoteInterfaceName) {
		this.remoteInterfaceName = remoteInterfaceName;
	}

}
