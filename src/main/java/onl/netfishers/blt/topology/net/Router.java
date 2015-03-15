package onl.netfishers.blt.topology.net;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import onl.netfishers.blt.topology.net.RouterInterface.RouterInterfaceType;
import onl.netfishers.blt.bgp.net.attributes.bgplsnlri.BgpLsNodeDescriptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Router {

	static Logger logger = LoggerFactory.getLogger(Router.class);	
	
	private static long idGenerator = 0;

	@XmlRootElement
	@XmlAccessorType(value = XmlAccessType.NONE)
	public static class RouterIdentifier {

		public static class InvalidRouterIdException extends Exception {
			private static final long serialVersionUID = 3930555303178612522L;
			public InvalidRouterIdException(String message) {
				super(message);
			}
		}

		private byte[] data;
		private long autonomousSystem = 0;
		private long areaId = 0;
		private long lsIdentifier = 0;

		protected RouterIdentifier() {
			
		}

		public RouterIdentifier(byte[] data, long autonomousSystem, long areaId,
				long lsIdentifier) throws InvalidRouterIdException {
			if (data.length != BgpLsNodeDescriptor.IGPROUTERID_ISISISONODEID_LENGTH &&
				data.length != BgpLsNodeDescriptor.IGPROUTERID_ISISPSEUDONODE_LENGTH &&
				data.length != BgpLsNodeDescriptor.IGPROUTERID_OSPFPSEUDONODE_LENGTH &&
				data.length != BgpLsNodeDescriptor.IGPROUTERID_OSPFROUTERID_LENGTH) {
				
				throw new InvalidRouterIdException("Invalid router id, wrong length");
			}
			this.data = data.clone();
			this.autonomousSystem = autonomousSystem;
			this.areaId = areaId;
			this.lsIdentifier = lsIdentifier;
		}

		@XmlElement
		public String getIdentifier() {
			return toString();
		}

		public void setIdentifier(String identifier) {
			if (identifier == null) {
				return;
			}
			Pattern[] patterns = new Pattern[] {
					Pattern.compile("([0-9]+)\\.([0-9]+)\\.([0-9]+)\\.([0-9]+)"),
					Pattern.compile("([0-9]+)\\.([0-9]+)\\.([0-9]+)\\.([0-9]+)-([0-9]+)\\.([0-9]+)\\.([0-9]+)\\.([0-9]+)"),
					Pattern.compile("([0-9a-fA-F]{2})([0-9a-fA-F]{2})\\.([0-9a-fA-F]{2})([0-9a-fA-F]{2})\\.([0-9a-fA-F]{2})([0-9a-fA-F]{2})"),
					Pattern.compile("([0-9a-fA-F]{2})([0-9a-fA-F]{2})\\.([0-9a-fA-F]{2})([0-9a-fA-F]{2})\\.([0-9a-fA-F]{2})([0-9a-fA-F]{2})-([0-9a-fA-F]{2})"),
			};
			data = new byte[0];
			int[] radix = new int[] { 10, 10, 16, 16 };
			for (int i = 0; i < 4; i++) {
				Matcher matcher = patterns[i].matcher(identifier);
				if (matcher.matches()) {
					data = new byte[matcher.groupCount()];
					ByteBuffer buffer = ByteBuffer.allocate(4);
					for (int j = 0; j < matcher.groupCount(); j++) {
						int value = Integer.parseInt(matcher.group(j + 1), radix[i]);
						buffer.putInt(0, value);
						data[j] = buffer.get(3);
					}
				}
			}
		}

		public String toString() {
			if (data.length == BgpLsNodeDescriptor.IGPROUTERID_OSPFROUTERID_LENGTH) {
				return String.format("%d.%d.%d.%d", data[0] & 0xFF, data[1] & 0xFF,
						data[2] & 0xFF, data[3] & 0xFF);
			}
			else if (data.length == BgpLsNodeDescriptor.IGPROUTERID_ISISISONODEID_LENGTH) {
				return String.format("%02x%02x.%02x%02x.%02x%02x", data[0] & 0xFF, data[1] & 0xFF,
						data[2] & 0xFF, data[3] & 0xFF, data[4] & 0xFF, data[5] & 0xFF);
			}
			else if (data.length == BgpLsNodeDescriptor.IGPROUTERID_ISISPSEUDONODE_LENGTH) {
				return String.format("%02x%02x.%02x%02x.%02x%02x-%02x", data[0] & 0xFF, data[1] & 0xFF,
						data[2] & 0xFF, data[3] & 0xFF, data[4] & 0xFF, data[5] & 0xFF, data[6] & 0xFF);
			}
			else if (data.length == BgpLsNodeDescriptor.IGPROUTERID_OSPFPSEUDONODE_LENGTH) {
				return String.format("%d.%d.%d.%d-%d.%d.%d.%d", data[0] & 0xFF, data[1] & 0xFF,
						data[2] & 0xFF, data[3] & 0xFF, data[4] & 0xFF, data[5] & 0xFF,
						data[6] & 0xFF, data[7] & 0xFF);
			}
			return "Invalid";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (areaId ^ (areaId >>> 32));
			result = prime * result
					+ (int) (autonomousSystem ^ (autonomousSystem >>> 32));
			result = prime * result + Arrays.hashCode(data);
			result = prime * result + (int) (lsIdentifier ^ (lsIdentifier >>> 32));
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
			RouterIdentifier other = (RouterIdentifier) obj;
			if (areaId != other.areaId)
				return false;
			if (autonomousSystem != other.autonomousSystem)
				return false;
			if (!Arrays.equals(data, other.data))
				return false;
			if (lsIdentifier != other.lsIdentifier)
				return false;
			return true;
		}

		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}

		@XmlElement
		public long getAutonomousSystem() {
			return autonomousSystem;
		}

		public void setAutonomousSystem(long autonomousSystem) {
			this.autonomousSystem = autonomousSystem;
		}

		@XmlElement
		public long getAreaId() {
			return areaId;
		}

		public void setAreaId(long areaId) {
			this.areaId = areaId;
		}

		@XmlElement
		public long getLsIdentifier() {
			return lsIdentifier;
		}

		public void setLsIdentifier(long lsIdentifier) {
			this.lsIdentifier = lsIdentifier;
		}

		public boolean isVirtual() {
			return (data.length != BgpLsNodeDescriptor.IGPROUTERID_ISISISONODEID_LENGTH &&
					data.length != BgpLsNodeDescriptor.IGPROUTERID_OSPFROUTERID_LENGTH);
		}
		
	}

	private long id = 0;
	private RouterIdentifier routerId;
	private Network network;

	private long lastSnmpPollingTime = 0;
	private long lastNetconfTime = 0;
	private int x = 0;
	private int y = 0;

	protected Router() {

	}

	public Router(RouterIdentifier routerId, Network network) {
		this.routerId = routerId;
		this.network = network;
	}

	@XmlElement
	public RouterIdentifier getRouterId() {
		return routerId;
	}

	protected void setRouterId(RouterIdentifier routerId) {
		this.routerId = routerId;
	}

	private Set<Ipv4Route> ipv4StaticRoutes = new HashSet<Ipv4Route>();
	private Set<Ipv4Route> ipv4IgpRoutes = new HashSet<Ipv4Route>();
	private List<RouterInterface> routerInterfaces = new CopyOnWriteArrayList<RouterInterface>();

	private boolean lost = true;
	private boolean deleted = false;
	private boolean needTeRefresh = false;
	//private boolean pseudonode = false ;

	private String name = "Unknown";


	public Set<Ipv4Route> getIpv4StaticRoutes() {
		return ipv4StaticRoutes;
	}

	public void setIpv4StaticRoutes(Set<Ipv4Route> ipv4Routes) {
		if (ipv4Routes != null && this.ipv4StaticRoutes != null) {
			for (Ipv4Route route : ipv4Routes) {
				for (Ipv4Route oldRoute : this.ipv4StaticRoutes) {
					if (route.equals(oldRoute)) {
						route.setId(oldRoute.getId());
						break;
					}
				}
			}
		}
		this.ipv4StaticRoutes = ipv4Routes;
	}

	public void clearIpv4StaticRoutes() {
		this.ipv4StaticRoutes.clear();
	}

	public void addIpv4StaticRoute(Ipv4Route ipv4Route) {
		this.ipv4StaticRoutes.add(ipv4Route);
	}

	public Ipv4Route getIpv4StaticRouteById(long id) {
		for (Ipv4Route route : this.ipv4StaticRoutes) {
			if (route.getId() == id) {
				return route;
			}
		}
		return null;
	}

	public Set<Ipv4Route> getIpv4IgpRoutes() {
		return ipv4IgpRoutes;
	}

	public void setIpv4IgpRoutes(Set<Ipv4Route> ipv4Routes) {
		if (ipv4Routes != null && this.ipv4IgpRoutes != null) {
			for (Ipv4Route route : ipv4Routes) {
				for (Ipv4Route oldRoute : this.ipv4IgpRoutes) {
					if (route.equals(oldRoute)) {
						route.setId(oldRoute.getId());
						break;
					}
				}
			}
		}
		this.ipv4IgpRoutes = ipv4Routes;
	}

	public void clearIpv4IgpRoutes() {
		this.ipv4IgpRoutes.clear();
	}

	public void addIpv4IgpRoute(Ipv4Route ipv4Route) {
		this.ipv4IgpRoutes.add(ipv4Route);
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
	public boolean isLost() {
		return lost;
	}
	
	public void setLost(boolean lost) {
		this.lost = lost;
	}
	
	public boolean isDeleted() {
		return deleted || network.isDeleted();
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isVirtual() {
		return (routerId.isVirtual());
	}

	@XmlElement
	public boolean isNeedTeRefresh() {
		return needTeRefresh;
	}

	public long getLastSnmpPollingTime() {
		return lastSnmpPollingTime;
	}

	public long getLastNetconfTime() {
		return lastNetconfTime;
	}

	public void setLastSnmpPollingTime() {
		this.lastSnmpPollingTime = System.currentTimeMillis();
	}

	public void setNeedTeRefresh(boolean needTeRefresh) {
		this.needTeRefresh = needTeRefresh;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public SnmpCommunity findSnmpCommunity() {
		List<SnmpCommunity> communities = new ArrayList<SnmpCommunity>(network.getSnmpCommunities());
		Collections.sort(communities);
		for (SnmpCommunity community : communities) {
			for (Ipv4Route prefix : ipv4IgpRoutes) {
				if (prefix.getMetric() <= 1 && community.getSubnet().contains(prefix.getSubnet())) {
					return new SnmpCommunity(prefix.getSubnet(), community.getCommunity());
				}
			}
		}
		return null;
	}

	@XmlElement
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	@XmlElement
	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public List<RouterInterface> getRouterInterfaces() {
		return routerInterfaces;
	}
	
	@XmlElement
	public List<RouterInterface> getIpv4Interfaces() {
		List<RouterInterface> ipv4Interfaces = new ArrayList<RouterInterface>();
		for (RouterInterface routerInterface : routerInterfaces) {
			if (routerInterface.getIpv4Address() != null) {
				ipv4Interfaces.add(routerInterface);
			}
		}
		return ipv4Interfaces;
	}

	public void setRouterInterfaces(List<RouterInterface> routerInterfaces) {
		this.routerInterfaces = routerInterfaces;
	}

	public void setConfiguredRouterInterfaces(List<RouterInterface> routerInterfaces) {
		if (routerInterfaces != null && this.routerInterfaces != null) {
			for (RouterInterface routerInterface : routerInterfaces) {
				for (RouterInterface oldRouterInterface : this.routerInterfaces) {
					if (oldRouterInterface.equals(routerInterface)) {
						routerInterface.setId(oldRouterInterface.getId());
						//routerInterface.setIgmpStaticGroups(oldRouterInterface.getIgmpStaticGroups());
						break;
					}
				}
			}
		}
		this.clearConfiguredRouterInterfaces();
		this.routerInterfaces.addAll(routerInterfaces);
	}

	public void setLiveRouterInterfaces(List<RouterInterface> routerInterfaces) {
		if (routerInterfaces != null && this.routerInterfaces != null) {
			for (RouterInterface routerInterface : routerInterfaces) {
				for (RouterInterface oldRouterInterface : this.routerInterfaces) {
					if (oldRouterInterface.equals(routerInterface)) {
						routerInterface.setId(oldRouterInterface.getId());
						break;
					}
				}
			}
		}
		this.clearLiveRouterInterfaces();
		this.routerInterfaces.addAll(routerInterfaces);
	}

	public void clearConfiguredRouterInterfaces() {
		List<RouterInterface> toRemove = new ArrayList<RouterInterface>();
		for (RouterInterface routerInterface : this.routerInterfaces) {
			if (routerInterface.getType() == RouterInterfaceType.CONFIGURED) {
				toRemove.add(routerInterface);
			}
		}
		this.routerInterfaces.removeAll(toRemove);
	}

	public void clearLiveRouterInterfaces() {
		List<RouterInterface> toRemove = new ArrayList<RouterInterface>();
		for (RouterInterface routerInterface : this.routerInterfaces) {
			if (routerInterface.getType() == RouterInterfaceType.LIVE) {
				toRemove.add(routerInterface);
			}
		}
		this.routerInterfaces.removeAll(toRemove);
	}

	public void addRouterInterface(RouterInterface routerInterface) {
		this.routerInterfaces.add(routerInterface);
	}

	public RouterInterface getRouterInterfaceById(long id) {
		for (RouterInterface routerInterface : this.routerInterfaces) {
			if (routerInterface.getId() == id) {
				return routerInterface;
			}
		}
		return null;
	}
	
	public RouterInterface getRouterInterfaceBySubnet(Ipv4Subnet subnet) {
		for (RouterInterface routerInterface : this.routerInterfaces) {
			if (routerInterface.getIpv4Address() != null && routerInterface.getIpv4Address().contains(subnet)) {
				return routerInterface;
			}
		}
		return null;
	}

	public void init() {
	}
	
	public void kill() {
	}
	
	public String toString() {
		return String.format("Router %s (router-id %s)", 
				name, (routerId == null ? "null" : routerId));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((routerId == null) ? 0 : routerId.hashCode());
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
		Router other = (Router) obj;
		if (routerId == null) {
			if (other.routerId != null)
				return false;
		}
		else if (!routerId.equals(other.routerId))
			return false;
		return true;
	}
}