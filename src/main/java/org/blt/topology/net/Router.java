package org.blt.topology.net;

/*import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;*/
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


//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Marshaller;
//import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.transform.stream.StreamSource;


//import org.blt.netconf.message.Hello;
//import org.blt.netconf.message.Message;
//import org.blt.netconf.message.Hello.Capability;
import org.blt.topology.net.RouterInterface.RouterInterfaceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.jcraft.jsch.Channel;
//import com.jcraft.jsch.ChannelExec;
//import com.jcraft.jsch.JSch;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Router {

	static Logger logger = LoggerFactory.getLogger(Router.class);	
	//private final static int NCPORT = 22;
	//private final static int NCTIMEOUT = 60000;
	//private static JSch JSCH = new JSch();

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
			if (data.length != 4 && data.length != 8 && data.length != 6 && data.length != 7) {
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
			if (data.length == 4) {
				return String.format("%d.%d.%d.%d", data[0] & 0xFF, data[1] & 0xFF,
						data[2] & 0xFF, data[3] & 0xFF);
			}
			else if (data.length == 6) {
				return String.format("%02x%02x.%02x%02x.%02x%02x", data[0] & 0xFF, data[1] & 0xFF,
						data[2] & 0xFF, data[3] & 0xFF, data[4] & 0xFF, data[5] & 0xFF);
			}
			else if (data.length == 7) {
				return String.format("%02x%02x.%02x%02x.%02x%02x-%02x", data[0] & 0xFF, data[1] & 0xFF,
						data[2] & 0xFF, data[3] & 0xFF, data[4] & 0xFF, data[5] & 0xFF, data[6] & 0xFF);
			}
			else if (data.length == 8) {
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
			return (data.length != 4 && data.length != 6);
		}
		
	}

	private long id = 0;
	private RouterIdentifier routerId;
	private Network network;

	private long lastSnmpPollingTime = 0;
	private long lastNetconfTime = 0;
	private int x = 0;
	private int y = 0;

	//private com.jcraft.jsch.Session ncSession = null;
	//private Channel ncChannel = null;
	//protected InputStream ncInStream = null;
	//protected PrintStream ncOutStream = null;


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
	//private List<Ipv4AccessList> ipv4AccessLists = new CopyOnWriteArrayList<Ipv4AccessList>();
	//private List<String> policyMaps = new CopyOnWriteArrayList<String>();
	//private List<TePath> explicitPaths = new CopyOnWriteArrayList<TePath>();
	private List<RouterInterface> routerInterfaces = new CopyOnWriteArrayList<RouterInterface>();
	//private List<Ipv4Route> ipv4StaticReverseRoutes = new CopyOnWriteArrayList<Ipv4Route>();
    //private boolean ipv4MulticastEnabled = false;
    //private String ipv4MulticastSsmRangeAcl = "";
    //private String ipv4MulticastCoreTreeRsvpTeAcl = "";

	private boolean lost = true;
	private boolean deleted = false;
	private boolean needTeRefresh = false;

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

	public boolean isDeleted() {
		return deleted || network.isDeleted();
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setLost(boolean lost) {
		this.lost = lost;
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

	/*public void setLastNetconfDiscoverTime() {
		this.lastNetconfTime = System.currentTimeMillis();
	}*/

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

	/*public SshAccount findSshAccount() {
		List<SshAccount> accounts = new ArrayList<SshAccount>(network.getSshAccounts());
		Collections.sort(accounts);
		for (SshAccount account : accounts) {
			for (Ipv4Route prefix : ipv4IgpRoutes) {
				if (prefix.getMetric() <= 1 && account.getSubnet().contains(prefix.getSubnet())) {
					return new SshAccount(prefix.getSubnet(), account.getUsername(), account.getPassword());
				}
			}
		}
		return null;
	}

	public List<Ipv4AccessList> getIpv4AccessLists() {
		return ipv4AccessLists;
	}

	public void setIpv4AccessLists(List<Ipv4AccessList> ipv4AccessLists) {
		if (ipv4AccessLists != null && this.ipv4AccessLists != null) {
			for (Ipv4AccessList acl : ipv4AccessLists) {
				for (Ipv4AccessList oldAcl : this.ipv4AccessLists) {
					if (acl.equals(oldAcl)) {
						acl.setId(oldAcl.getId());
						break;
					}
				}
			}
		}
		this.ipv4AccessLists = ipv4AccessLists;
	}

	public void clearIpv4AccessLists() {
		this.ipv4AccessLists.clear();
	}

	public void addIpv4AccessList(Ipv4AccessList ipv4AccessList) {
		this.ipv4AccessLists.add(ipv4AccessList);
	}

	public List<String> getPolicyMaps() {
		return policyMaps;
	}

	public void setPolicyMaps(List<String> policyMaps) {
		this.policyMaps = policyMaps;
	}

	public void clearPolicyMaps() {
		this.policyMaps.clear();
	}

	public void addPolicyMap(String policyMap) {
		this.policyMaps.add(policyMap);
	}

	public List<TePath> getExplicitPaths() {
		return explicitPaths;
	}

	public void setExplicitPaths(List<TePath> explicitPaths) {
		if (explicitPaths != null && this.explicitPaths != null) {
			for (TePath tePath : explicitPaths) {
				for (TePath oldTePath : this.explicitPaths) {
					if (tePath.equals(oldTePath)) {
						tePath.setId(oldTePath.getId());
						break;
					}
				}
			}
		}
		this.explicitPaths = explicitPaths;
	}

	public void clearExplicitPaths() {
		this.explicitPaths.clear();
	}

	public void addExplicitPath(TePath explicitPath) {
		this.explicitPaths.add(explicitPath);
	}

	public TePath getExplicitPathById(long id) {
		for (TePath tePath : this.explicitPaths) {
			if (tePath.getId() == id) {
				return tePath;
			}
		}
		return null;
	}*/

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
						routerInterface.setIgmpStaticGroups(oldRouterInterface.getIgmpStaticGroups());
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

	/*public List<Ipv4Route> getIpv4StaticReverseRoutes() {
		return ipv4StaticReverseRoutes;
	}

	public void setIpv4StaticReverseRoutes(List<Ipv4Route> ipv4StaticReverseRoutes) {
		if (ipv4StaticReverseRoutes != null && this.ipv4StaticReverseRoutes != null) {
			for (Ipv4Route route : ipv4StaticReverseRoutes) {
				for (Ipv4Route oldRoute : this.ipv4StaticReverseRoutes) {
					if (route.equals(oldRoute)) {
						route.setId(oldRoute.getId());
						break;
					}
				}
			}
		}
		this.ipv4StaticReverseRoutes = ipv4StaticReverseRoutes;
	}

	public void clearIpv4StaticReverseRoutes() {
		this.ipv4StaticReverseRoutes.clear();
	}

	public void addIpv4StaticReverseRoute(Ipv4Route ipv4StaticReverseRoute) {
		this.ipv4StaticReverseRoutes.add(ipv4StaticReverseRoute);
	}
	
	public Ipv4Route getIpv4StaticReverseRouteById(long id) {
		for (Ipv4Route route : this.ipv4StaticReverseRoutes) {
			if (route.getId() == id) {
				return route;
			}
		}
		return null;
	}

	@XmlElement
	public boolean isIpv4MulticastEnabled() {
		return ipv4MulticastEnabled;
	}

	public void setIpv4MulticastEnabled(boolean ipv4MulticastEnabled) {
		this.ipv4MulticastEnabled = ipv4MulticastEnabled;
	}
	
	@XmlElement
	public String getIpv4MulticastSsmRangeAcl() {
		return ipv4MulticastSsmRangeAcl;
	}

	public void setIpv4MulticastSsmRangeAcl(String ipv4MulticastSsmRangeAcl) {
		this.ipv4MulticastSsmRangeAcl = ipv4MulticastSsmRangeAcl;
	}

	@XmlElement
	public String getIpv4MulticastCoreTreeRsvpTeAcl() {
		return ipv4MulticastCoreTreeRsvpTeAcl;
	}

	public void setIpv4MulticastCoreTreeRsvpTeAcl(
			String ipv4MulticastCoreTreeRsvpTeAcl) {
		this.ipv4MulticastCoreTreeRsvpTeAcl = ipv4MulticastCoreTreeRsvpTeAcl;
	}*/
	
//	private void ncConnect() throws IOException {
//		if (ncSession == null || !ncSession.isConnected()) {
//			ncChannel = null;
//			ncDisconnect();
//			SshAccount ncAccount = this.findSshAccount();
//			if (ncAccount == null) {
//				throw new IOException(String.format("No SSH account available to connect to router %s.", this.name));
//			}
//			logger.info("Starting a new Netconf/SSH session to {}.", ncAccount.getSubnet().getInetAddress());
//			try {
//				logger.debug("Will connect to {} on port {} using username {}",
//						ncAccount.getSubnet().getInetAddress(), NCPORT, ncAccount.getUsername());
//				ncSession = JSCH.getSession(ncAccount.getUsername(),
//						ncAccount.getSubnet().getInetAddress().getHostName(), NCPORT);
//				ncSession.setPassword(ncAccount.getPassword());
//				// Disable Strict Key checking
//				ncSession.setConfig("StrictHostKeyChecking", "no");
//				ncSession.setTimeout(NCTIMEOUT);
//				ncSession.connect(NCTIMEOUT);
//			} catch (Exception e) {
//				logger.error("Error while setting up the Netconf session.", e);
//				throw new IOException("Error while setting up the Netconf session.");
//			}
//		}
/*		if (ncChannel == null || !ncChannel.isConnected()) {
			try {
				ncChannel = ncSession.openChannel("exec");
				((ChannelExec)ncChannel).setCommand("netconf");
				ncInStream = ncChannel.getInputStream();
				ncOutStream = new PrintStream(ncChannel.getOutputStream());
				ncChannel.connect(NCTIMEOUT);

				try {
					logger.debug("Will wait for an Hello message from the server");
					Message remoteMessage = ncReceive();
					if (!(remoteMessage instanceof Hello)) {
						logger.error("Didn't get an Hello message");
						throw new IOException("Didn't receive an Hello message from remote Netconf server.");
					}
					Hello remoteHello = (Hello)remoteMessage;
					if (remoteHello.getSessionId() == null) {
						logger.error("The server didn't send a session-id");
						throw new IOException("The server didn't send a session-id.");
					}
					boolean candidate = false;
					boolean base = false;
					for (Capability capability : remoteHello.getCapabilities()) {
						if (Capability.BASE10.equals(capability.getCapability())) base = true;
						if (Capability.CANDIDATE10.equals(capability.getCapability())) candidate = true;
					}
					if (!base) {
						logger.error("The server doesn't have the base Netconf capability.");
						throw new IOException("Missing capability");
					}
					if (!candidate) {
						logger.error("The server doesn't have the candidate config capability.");
						throw new IOException("Missing capability");
					}
					Hello hello = new Hello();
					ncSend(hello);
				}
				catch (Exception e) {
					throw new IOException("Unable to exchange Hello messages.", e);
				}

			} catch (Exception e) {
				logger.error("Error while setting up the Netconf channel.", e);
				throw new IOException("Error while setting up the Netconf session.");
			}
		}

	}

	public void ncDisconnectChannel() {
		try {
			ncChannel.disconnect();
		}
		catch (Exception e) {
			this.ncDisconnect();
		}
	}

	public void ncDisconnect() {
		synchronized (this) {
			try {
				ncChannel.disconnect();
				ncSession.disconnect();
			}
			catch (Exception e) {
			}
		}
	}

	private void ncSend(Message rpcMessage) throws IOException {
		StringWriter writer = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(Message.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(rpcMessage, writer);
			writer.append(Message.RPCLIMIT);
			writer.append("\r");
			logger.debug("Generated Netconf XML message [{}].", writer.toString());
		}
		catch (Exception e) {
			logger.error("Unable to marshall Netconf message to XML.", e);
			throw new IOException("Unable to serialize Netconf message to XML");
		}
		try {
			ncOutStream.print(writer);
			ncOutStream.flush();
		}
		catch (Exception e) {
			logger.error("Unable to send XML data to Netconf server.", e);
			throw new IOException("Unable to send XML to Netconf server");
		}
	}

	private Message ncReceive() throws IOException {

		StringBuffer buffer = new StringBuffer();
		byte[] miniBuffer = new byte[4096];


		long maxTime = System.currentTimeMillis() + NCTIMEOUT;

		while (true) {
			try {
				while (this.ncInStream != null && this.ncInStream.available() > 0) {
					int length = this.ncInStream.read(miniBuffer);
					buffer.append(new String(miniBuffer, 0, length));
				}
			}
			catch (IOException e) {
				logger.error("Unable to receive data.", e);
				throw new IOException("Unable to receive Netconf data");
			}
			int end = buffer.indexOf(Message.RPCLIMIT);
			if (end > -1) {
				int start = buffer.indexOf("<?xml");
				if (start == -1) {
					start = 0;
				}
				String data = buffer.substring(start, end);
				logger.debug("Received Netconf message [{}].", data);
				try {
					JAXBContext context = JAXBContext.newInstance(Message.class);
					Unmarshaller unmarshaller = context.createUnmarshaller();
					return (Message)unmarshaller.unmarshal(new StreamSource(new StringReader(data)));
				}
				catch (Exception e) {
					logger.error("Unable to parse received data [{}].", data, e);
					throw new IOException("Unable to parse received data.");
				}

			}
			if (System.currentTimeMillis() > maxTime) {
				logger.error("Timeout ({}ms) while waiting for the command output.", NCTIMEOUT);
				throw new IOException("Timeout waiting for the command output.");
			}
		}

	}

	public Message ncRequest(Message message) throws IOException {
		synchronized (this) {
			this.ncConnect();
			this.ncSend(message);
			return this.ncReceive(); 
		}
	}*/


	public void init() {
	}

	/*public void kill() {
		this.ncDisconnect();
	}*/

	public String toString() {
		return String.format("Router %s (router-id %s)", name, (routerId == null ? "null" : routerId));
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
