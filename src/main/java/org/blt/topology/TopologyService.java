package org.blt.topology;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.blt.Blt;
import org.blt.aaa.User;
import org.blt.tasks.TaskManager;
import org.blt.topology.net.Ipv4Route;
import org.blt.topology.net.Ipv4Subnet;
import org.blt.topology.net.Link;
import org.blt.topology.net.Network;
import org.blt.topology.net.Router;
import org.blt.topology.net.RouterInterface;
import org.blt.topology.net.SnmpCommunity;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement(name = "topology")
@XmlAccessorType(XmlAccessType.NONE)
public class TopologyService {

	private static final File dumpFile = new File(Blt.getConfig("blt.dump.filename", "blt-data.xml"));
	private static final String[] dumpSettings = new String[] {
		"org/blt/topology/net/dump.xml",
		"org/blt/aaa/dump.xml",
	};

	private static TopologyService topologyService;

	static Logger logger = LoggerFactory
			.getLogger(TopologyService.class);

	public static void init() {
		TopologyService.topologyService = TopologyService.readFromDisk();
		for (Network network : TopologyService.getService().getNetworks() ) {
			network.init();
		}
	}

	public static TopologyService getService() {
		return topologyService;
	}

	public static class TopologyException extends Exception {

		private static final long serialVersionUID = -6652303716411702337L;

		public TopologyException() {
			super();
		}
		public TopologyException(String message) {
			super(message);
		}

	}

	private Set<Network> networks = new CopyOnWriteArraySet<Network>();
	private Set<User> users = new CopyOnWriteArraySet<User>();

	@XmlElementWrapper(name = "networks")
	@XmlElement(name = "network")
	public Set<Network> getNetworks() {
		return networks;
	}

	public Router findRouter(Ipv4Subnet ip) {
		for (Network network : networks) {
			for (Router router : network.getRouters()) {
				for (RouterInterface routerInterface : router.getRouterInterfaces()) {
					if (routerInterface.getIpv4Address() != null &&
							routerInterface.getIpv4Address().contains(ip)) {
						return router;
					}
				}
			}
		}
		for (Network network : networks) {
			for (Router router : network.getRouters()) {
				for (Ipv4Route route : router.getIpv4IgpRoutes()) {
					if (route.getMetric() <= 1 && route.getSubnet().contains(ip)) {
						return router;
					}
				}
			}
		}
		return null;
	}

	public Network getNetworkById(long id) {
		for (Network network : networks) {
			if (network.getId() == id) {
				return network;
			}
		}
		return null;
	}

	public void addNetwork(Network network) throws IllegalArgumentException {
		for (Network n : networks) {
			if (n.getName().equals(network.getName())) {
				throw new IllegalArgumentException("Please choose another name, this one is already in use.");
			}
			if (n.getBgpPeer().equals(network.getBgpPeer())) {
				throw new IllegalArgumentException(
						String.format("This BGP peer IP address is already used, by network %s.", n.getName()));
			}
		}
		networks.add(network);
		this.writeToDisk();
		network.init();
	}

	public void removeNetwork(Network network) {
		networks.remove(network);
		this.writeToDisk();
		network.kill();
	}

	public void removeRouter(Router router) {
		for (Network network : networks) {
			if (network.getRouters().remove(router)) {
				//router.kill();
				this.writeToDisk();
			}
		}
	}

	public void removeLink(Link link) {
		for (Network network : networks) {
			network.getLinks().remove(link);
			this.writeToDisk();
		}
	}

	public void removeSnmpCommunity(SnmpCommunity community) {
		for (Network network : networks) {
			if (network.getSnmpCommunities().remove(community)) {
				this.writeToDisk();
			}
		}
	}

	public void addSnmpCommunity(Network network, SnmpCommunity community) throws TopologyException {
		network.addCommunity(community);
		this.writeToDisk();
	}

	@XmlElementWrapper(name = "users")
	@XmlElement(name = "user")
	public Set<User> getUsers() {
		return users;
	}
	
	public User getUserById(long id) {
		for (User user : this.users) {
			if (user.getId() == id) {
				return user;
			}
		}
		return null;
	}
	
	public User getUserByName(String name) {
		for (User user : this.users) {
			if (user.getName().equals(name)) {
				return user;
			}
		}
		return null;
	}
	
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	public void addUser(User user) {
		this.users.add(user);
		this.writeToDisk();
	}
	
	public void removeUser(User user) {
		this.users.remove(user);
		this.writeToDisk();
	}
	

	public void writeToDisk() {
		try {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, Arrays.asList(dumpSettings));
			JAXBContext context = JAXBContext.newInstance(new Class[] { TopologyService.class }, properties);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(this, dumpFile);
			logger.info("Blt data has been successfully saved to {}.", dumpFile);
		}
		catch (Exception e) {
			logger.error("Unable to save Blt data to file {}.", dumpFile, e);
		}
	}

	public static TopologyService readFromDisk() {
		try {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, Arrays.asList(dumpSettings));
			JAXBContext context = JAXBContext.newInstance(new Class[] { TopologyService.class }, properties);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			TopologyService topologyService = (TopologyService)unmarshaller.unmarshal(dumpFile);
			logger.info("Blt data successfully loaded from {}.", dumpFile);
			return topologyService;
		}
		catch (Exception e) {
			logger.error("Unable to read Blt data from file {}. Starting without data.", dumpFile, e);
		}
		return new TopologyService();
	}


}
