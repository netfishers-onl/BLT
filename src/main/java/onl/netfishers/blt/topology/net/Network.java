/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/

package onl.netfishers.blt.topology.net;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import onl.netfishers.blt.bgp.BgpService;
import onl.netfishers.blt.topology.TopologyService.TopologyException;
import onl.netfishers.blt.topology.net.Router.RouterIdentifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Network {

	private static Logger logger = LoggerFactory
			.getLogger(Network.class);
	
	private static long idGenerator = 0;

	private long id = 0;
	private String name;

	private Set<Router> routers = new CopyOnWriteArraySet<Router>();
	private Set<Link> links = new CopyOnWriteArraySet<Link>();

	private Set<SnmpCommunity> snmpCommunities = new CopyOnWriteArraySet<SnmpCommunity>();

	private Ipv4Subnet bgpPeer;
	private int bgpAs;

	private boolean deleted = false;

	protected Network() {

	}

	public Network(String name, Ipv4Subnet bgpPeer, int bgpAs) {
		this.name = name;
		this.bgpPeer = bgpPeer;
		this.bgpAs = bgpAs;
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
	public Ipv4Subnet getBgpPeer() {
		return bgpPeer;
	}
	public void setBgpPeer(Ipv4Subnet bgpPeer) {
		this.bgpPeer = bgpPeer;
	}

	@XmlElement
	public int getBgpAs() {
		return bgpAs;
	}
	public void setBgpAs(int bgpAs) {
		this.bgpAs = bgpAs;
	}

	public Set<Router> getRouters() {
		return routers;
	}

	public void setRouters(Set<Router> routers) {
		this.routers = routers;
	}

	public Router findOrAddRouter(RouterIdentifier routerId) {
		for (Router router : this.routers) {
			if (router.getRouterId().equals(routerId)) {
				return router;
			}
		}
		Router router = new Router(routerId, this);
		this.addRouter(router);
		return router;
	}

	public void addRouter(Router router) {
		routers.add(router);
		router.init();
	}

	public Router getRouterById(long id) {
		for (Router router : routers) {
			if (router.getId() == id) {
				return router;
			}
		}
		return null;
	}
	
	public Router getPseudonode(Router r) {
		for (Router router : routers) {
			if (router.isPseudonodeOf(r)) {
				return router;
			}
		}
		return null;
	}
	
	@XmlElementWrapper
	@XmlElement(name = "snmpCommunity")
	public Set<SnmpCommunity> getSnmpCommunities() {
		return snmpCommunities;
	}

	public void setSnmpCommunities(Set<SnmpCommunity> communities) {
		this.snmpCommunities = communities;
	}

	public void addCommunity(SnmpCommunity community) throws TopologyException {
		if (!this.snmpCommunities.add(community)) {
			throw new TopologyException(
					String.format("A community already exists for this subnet %s.",
							community.getSubnet().toString()));
		}
	}

	public SnmpCommunity getCommunityById(long id) {
		for (SnmpCommunity community : snmpCommunities) {
			if (community.getId() == id) {
				return community;
			}
		}
		return null;
	}

	public Set<Link> getLinks() {
		return links;
	}

	public void setLinks(Set<Link> links) {
		this.links = links;
	}

	public void addLink(Link link) {
		this.links.add(link);
	}

	public Link findOrAddLink(Link link) {
		for (Link l : this.links) {
			if (l.equals(link)) {
				return l;
			}
		}
		this.links.add(link);
		return link;
	}

	public Link getLinkById(long id) {
		for (Link link : links) {
			if (link.getId() == id) {
				return link;
			}
		}
		return null;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	private void startBgpSession() {
		try {
			BgpService.startSession(this.getBgpPeer().getInetAddress(), this.getBgpAs(), this.getName());
		}
		catch (Exception e) {
			logger.error("Unable to start the BGP session for network {}.", this.getName());
		}
	}

	private void stopBgpSession() {
		BgpService.stopSession(this.getBgpPeer().getInetAddress());
	}

	public void init() {
		this.startBgpSession();
		for (Router router : routers) {
			router.setNetwork(this);
			router.init();
		}
	}
	
	public void fillLinkInterfaceNames() {
		for (Link link : links) {
			for (Router router : routers) {
				if (router.getRouterId().equals(link.getLocalRouter())) {
					RouterInterface routerInterface = router.getRouterInterfaceBySubnet(link.getLocalAddress());
					if (routerInterface != null) {
						link.setLocalInterfaceName(routerInterface.getName());
						link.setLocalInterfaceDescription(routerInterface.getDescription());
					}
				}
				if (router.getRouterId().equals(link.getRemoteRouter())) {
					RouterInterface routerInterface = router.getRouterInterfaceBySubnet(link.getRemoteAddress());
					if (routerInterface != null) {
						link.setRemoteInterfaceName(routerInterface.getName());
						link.setRemoteInterfaceDescription(routerInterface.getDescription());
					}
				}
			}
		}
	}

	public void kill() {
		this.stopBgpSession();
		for (Router router : routers) {
			router.kill();
		}
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
		Network other = (Network) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

}
