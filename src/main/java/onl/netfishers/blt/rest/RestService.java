package onl.netfishers.blt.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import onl.netfishers.blt.Blt;
import onl.netfishers.blt.aaa.User;
import onl.netfishers.blt.tasks.Task;
import onl.netfishers.blt.tasks.TaskManager;
import onl.netfishers.blt.topology.TopologyService;
import onl.netfishers.blt.topology.net.Ipv4Route;
import onl.netfishers.blt.topology.net.Ipv4Subnet;
import onl.netfishers.blt.topology.net.Link;
import onl.netfishers.blt.topology.net.Network;
import onl.netfishers.blt.topology.net.Router;
import onl.netfishers.blt.topology.net.RouterInterface;
import onl.netfishers.blt.topology.net.SnmpCommunity;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
//import onl.netfishers.blt.aaa.Radius;
//import onl.netfishers.blt.licensing.License;
//import onl.netfishers.blt.licensing.License.Validity;
//import onl.netfishers.blt.licensing.License;
//import onl.netfishers.blt.licensing.License.Validity;
/*import onl.netfishers.blt.netconf.NetconfConfigurationTask;
import onl.netfishers.blt.netconf.message.IPExplicitPaths;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration;
import onl.netfishers.blt.netconf.message.Ipv4AclsPrefixLists;
import onl.netfishers.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessListEntry.Ipv4AceRule;
import onl.netfishers.blt.netconf.message.MulticastRouting;
import onl.netfishers.blt.netconf.message.MulticastRouting.MulticastDefaultVrf;
import onl.netfishers.blt.netconf.message.MulticastRouting.MulticastInterface;
import onl.netfishers.blt.netconf.message.MulticastRouting.MulticastVrfIpv4;
import onl.netfishers.blt.netconf.message.MulticastRouting.StaticRpfRule;
import onl.netfishers.blt.netconf.message.RouterIgmp;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.Ipv4PacketFilter;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.PathOption;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.PathOption.IgpType;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.PathOption.Lockdown;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.PathOption.PathType;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.PathOption.Verbatim;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.PathOptionProtect;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.PathOptionProtectTable;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.Qos;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.TeAutoBandwidth;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.TeAutoBandwidth.AdjustmentThreshold;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.TeAutoBandwidth.BandwidthLimits;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.TeDestination;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.TeDestinationLeaf;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.TeDestinationTable;
import onl.netfishers.blt.netconf.message.InterfaceConfiguration.TeLogging;
import onl.netfishers.blt.netconf.message.InterfaceConfigurationTable;
import onl.netfishers.blt.netconf.message.Path.Hop;
import onl.netfishers.blt.netconf.message.Path.Hop.HopType;
import onl.netfishers.blt.netconf.message.RouterIgmp.IgmpInterface;
import onl.netfishers.blt.netconf.message.RouterIgmp.IgmpStaticGroup;
import onl.netfishers.blt.netconf.message.RouterStatic;
import onl.netfishers.blt.netconf.message.RouterStatic.DefaultVrf;
import onl.netfishers.blt.netconf.message.RouterStatic.VrfIpv4;
import onl.netfishers.blt.netconf.message.RouterStatic.VrfUnicast;
import onl.netfishers.blt.netconf.message.VrfPrefix;*/
//import onl.netfishers.blt.topology.net.Ipv4AccessList;
//import onl.netfishers.blt.topology.net.Ipv4AccessList.AclProtocol;
//import onl.netfishers.blt.topology.net.Ipv4AccessList.Ipv4AccessListEntry;
//import onl.netfishers.blt.topology.net.Ipv4Route;
//import onl.netfishers.blt.topology.net.Ipv4StaticGroup;
//import onl.netfishers.blt.topology.net.Ipv4Subnet.MalformedIpv4SubnetException;
//import onl.netfishers.blt.topology.net.P2mpTeTunnel;
//import onl.netfishers.blt.topology.net.P2pTeTunnel;
//import onl.netfishers.blt.topology.net.TeTunnel;
//import onl.netfishers.blt.topology.net.RouterInterface.RouterInterfaceType;
//import onl.netfishers.blt.topology.net.SshAccount;
//import onl.netfishers.blt.topology.net.TePath;
//import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

@Path("/")
public class RestService extends Thread {

	private String httpStaticPath;
	private String httpApiPath;
	private String httpBaseUrl;
	private String httpSslKeystoreFile;
	private String httpSslKeystorePass;
	private int httpBasePort;
	@Context private HttpServletRequest request;

	private static RestService restService;

	private static Logger logger = LoggerFactory.getLogger(RestService.class);
	private static Logger aaaLogger = LoggerFactory.getLogger("AAA");

	public static void init() {
		RestService.restService = new RestService();
		restService.setUncaughtExceptionHandler(Blt.exceptioHandler);
		restService.start();
	}
	
	public RestService() {
		this.setName("REST Service");
		httpStaticPath = Blt.getConfig("blt.http.staticpath", "/");
		httpApiPath = Blt.getConfig("blt.http.apipath", "/api");
		httpBaseUrl = Blt.getConfig("blt.http.baseurl", "http://localhost");
		httpSslKeystoreFile = Blt.getConfig("blt.http.ssl.keystore.file", "blt.jks");
		httpSslKeystorePass = Blt.getConfig("blt.http.ssl.keystore.pass", "password");
		httpBasePort = 8443;
		try {
			httpBasePort = Integer.parseInt(Blt.getConfig("blt.http.baseport",
					Integer.toString(httpBasePort)));
		}
		catch (Exception e) {
			logger.warn("Unable to understand the HTTP base port configuration, using {}.",
					httpBasePort);
		}

	}

	public static class BltWebApplication extends ResourceConfig {
		public BltWebApplication() {
			registerClasses(RestService.class, SecurityFilter.class);
			register(RolesAllowedDynamicFeature.class);
			property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, "true");
			property(ServerProperties.APPLICATION_NAME, "Blt");
			//property(ServerProperties.TRACING, "ALL");
			register(MoxyJsonFeature.class);
			register(new MoxyJsonConfig().setFormattedOutput(true)
					.property(JAXBContextProperties.JSON_WRAPPER_AS_ARRAY_NAME, true).resolver());
			register(new MoxyXmlFeature());
		}
	}

	@Override
	public void run() {
		logger.info("Starting the Web/REST service.");
		try {
			SSLContextConfigurator sslContext = new SSLContextConfigurator();
			sslContext.setKeyStoreFile(httpSslKeystoreFile);
			sslContext.setKeyStorePass(httpSslKeystorePass);
			
			if (!sslContext.validateConfiguration(true)) {
				throw new RuntimeException(
						"Invalid SSL settings for the embedded HTTPS server.");
			}
			SSLEngineConfigurator sslConfig = new SSLEngineConfigurator(sslContext)
				.setClientMode(false).setNeedClientAuth(false).setWantClientAuth(false);
			URI url = UriBuilder.fromUri(httpBaseUrl).port(httpBasePort).build();
			HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
					url, (GrizzlyHttpContainer) null, true, sslConfig, false);

			WebappContext context = new WebappContext("GrizzlyContext", httpApiPath);
			ServletRegistration registration = context.addServlet("Jersey", ServletContainer.class);
			registration.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS,
					BltWebApplication.class.getName());
			registration.addMapping(httpApiPath);
			context.deploy(server);
			HttpHandler staticHandler = new CLStaticHttpHandler(Blt.class.getClassLoader(), "/www/");
			server.getServerConfiguration().addHttpHandler(staticHandler, httpStaticPath);

			
			
			server.start();
			synchronized (this) {
				while (true) {
					this.wait();
				}
			}
		}
		catch (Exception e) {
			logger.error(MarkerFactory.getMarker("FATAL"),
					"Fatal error with the REST service.", e);
			throw new RuntimeException(
					"Error with the REST service, see logs for more details.");
		}
	}


	@XmlRootElement(name = "error")
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsErrorBean {

		/** The error message. */
		private String errorMsg;

		/** The error code. */
		private int errorCode;

		/**
		 * Instantiates a new error bean.
		 */
		public RsErrorBean() {
		}

		/**
		 * Instantiates a new error bean.
		 *
		 * @param errorMsg the error msg
		 * @param errorCode the error code
		 */
		public RsErrorBean(String errorMsg, int errorCode) {
			super();
			this.errorMsg = errorMsg;
			this.errorCode = errorCode;
		}

		/**
		 * Gets the error message.msg
		 *
		 * @return the error message
		 */
		@XmlElement
		public String getErrorMsg() {
			return errorMsg;
		}

		/**
		 * Sets the error message.
		 *
		 * @param errorMsg the new error message
		 */
		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
		}

		/**
		 * Gets the error code.
		 *
		 * @return the error code
		 */
		@XmlElement
		public int getErrorCode() {
			return errorCode;
		}

		/**
		 * Sets the error code.
		 *
		 * @param errorCode the new error code
		 */
		public void setErrorCode(int errorCode) {
			this.errorCode = errorCode;
		}
	}

	/**
	 * The BltBadRequestException class, a WebApplication exception
	 * embedding an error message, to be sent to the REST client.
	 */
	static public class BltBadRequestException extends
	WebApplicationException {

		private static final long serialVersionUID = -4538169756895835286L;

		public static final int INVALID_IPV4_ADDRESS = 10;
		public static final int INVALID_IPV4_SUBNET = 11;
		public static final int INVALID_IPV4_ROUTE = 15;
		public static final int UNKNOWN_IPV4_ROUTE = 17;
		public static final int INVALID_IPV4_STATIC_GROUP = 19;
		public static final int UNKNOWN_IPV4_STATIC_GROUP = 20;
		public static final int UNKNOWN_IPV4_REVERSE_ROUTE = 21;
		public static final int INVALID_IPV4_REVERSE_ROUTE = 22;
		public static final int UNABLE_TO_RETRIEVE_DATA = 100;
		public static final int UNKNOWN_NETWORK = 200;
		public static final int INVALID_NETWORK_PARAMS = 205;
		public static final int UNKNOWN_SNMP_COMMUNITY = 210;
		public static final int INVALID_SNMP_COMMUNITY = 211;
		public static final int UNKNOWN_SSH_ACCOUNT = 215;
		public static final int INVALID_SSH_ACCOUNT = 216;
		public static final int UNKNOWN_ROUTER = 300;
		public static final int UNKNOWN_LINK = 400;
		public static final int INVALID_EXPLICITPATH_NAME = 500;
		public static final int INVALID_EXPLICITPATH_PARAMS = 501;
		public static final int UNKNOWN_EXPLICITPATH = 502;
		public static final int UNKNOWN_IPV4_ACCESS_LIST = 520;
		public static final int UNKNOWN_INTERFACE = 550;
		public static final int INVALID_INTERFACE_PARAMS = 560;
		public static final int INVALID_TUNNEL_PARAMS = 565;
		public static final int SCHEDULE_ERROR = 600;
		public static final int UNKNOWN_TASK = 610;
		public static final int INVALID_USER_PARAMS = 700;
		public static final int INVALID_USER = 701;
		public static final int UNKNOWN_USER = 702;
		public static final int INVALID_LICENSE = 710;
		public static final int UNKNOWN_LICENSE = 711;

		public BltBadRequestException(String message, int errorCode) {
			super(Response.status(Response.Status.BAD_REQUEST)
					.entity(new RsErrorBean(message, errorCode)).build());
		}
	}

	@XmlRootElement(name = "text")
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsText {
		private String text;

		public RsText(String text) {
			this.text = text;
		}
		protected RsText() {

		}
		@XmlValue
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
	}

	private static List<RsText> wrapTextList(Collection<String> list) {
		List<RsText> result = new ArrayList<RsText>();
		for (String item : list) {
			result.add(new RsText(item));
		}
		return result;
	}


	@GET
	@Path("networks")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<Network> getNetworks() throws WebApplicationException {
		return TopologyService.getService().getNetworks();
	}

	@GET
	@Path("networks/{nid}")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Network getNetwork(@PathParam("nid") Long nid) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		return network;
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsNetwork {
		private String name;
		private String bgpPeerAddress;
		private String bgpAs;
		@XmlElement
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@XmlElement
		public String getBgpPeerAddress() {
			return bgpPeerAddress;
		}
		public void setBgpPeerAddress(String bgpPeerAddress) {
			this.bgpPeerAddress = bgpPeerAddress;
		}
		@XmlElement
		public String getBgpAs() {
			return bgpAs;
		}
		public void setBgpAs(String bgpAs) {
			this.bgpAs = bgpAs;
		}

	}


	@POST
	@Path("networks")
	@RolesAllowed("admin")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Network addNetwork(RsNetwork rsNetwork) {
		if (rsNetwork.getName() == null || rsNetwork.getName().trim().equals("")) {
			throw new BltBadRequestException("The name can't be empty.",
					BltBadRequestException.INVALID_NETWORK_PARAMS);
		}
		if (rsNetwork.getBgpPeerAddress() == null || rsNetwork.getBgpPeerAddress().trim().equals("")) {
			throw new BltBadRequestException("The IP address can't be empty.",
					BltBadRequestException.INVALID_NETWORK_PARAMS);
		}
		if (rsNetwork.getBgpAs() == null) {
			throw new BltBadRequestException("The AS number can't be empty.",
					BltBadRequestException.INVALID_NETWORK_PARAMS);
		}
		int bgpAs = 0;
		try {
			bgpAs = Integer.parseInt(rsNetwork.getBgpAs());
		}
		catch (Exception e) {
			throw new BltBadRequestException("Invalid BGP AS.",
					BltBadRequestException.INVALID_NETWORK_PARAMS);
		}
		Ipv4Subnet bgpAddress;
		try {
			bgpAddress = new Ipv4Subnet(rsNetwork.getBgpPeerAddress());
		}
		catch (Exception e) {
			throw new BltBadRequestException("Invalid IPv4 address.",
					BltBadRequestException.INVALID_IPV4_ADDRESS);
		}
		Network network = new Network(rsNetwork.getName(), bgpAddress, bgpAs);
		try {
			TopologyService.getService().addNetwork(network);
		}
		catch (Exception e) {
			throw new BltBadRequestException(e.getMessage(),
					BltBadRequestException.INVALID_NETWORK_PARAMS);
		}
		return network;
	}


	@DELETE
	@Path("networks/{nid}")
	@RolesAllowed("admin")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteNetwork(@PathParam("nid") Long nid) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		TopologyService.getService().removeNetwork(network);
	}



	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsSnmpCommunity {
		private String subnet;
		private String community;
		@XmlElement
		public String getSubnet() {
			return subnet;
		}
		public void setSubnet(String subnet) {
			this.subnet = subnet;
		}
		@XmlElement
		public String getCommunity() {
			return community;
		}
		public void setCommunity(String community) {
			this.community = community;
		}
	}


	@POST
	@Path("networks/{nid}/communities")
	@RolesAllowed("admin")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void addCommunity(@PathParam("nid") Long nid, RsSnmpCommunity rsCommunity) {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		if (rsCommunity.getCommunity() == null || rsCommunity.getCommunity().equals("")) {
			throw new BltBadRequestException("The community can't be empty.",
					BltBadRequestException.INVALID_SNMP_COMMUNITY);
		}
		if (rsCommunity.getSubnet() == null || rsCommunity.getSubnet().equals("")) {
			throw new BltBadRequestException("The matching subnet can't be empty.",
					BltBadRequestException.INVALID_SNMP_COMMUNITY);
		}
		Ipv4Subnet subnet;
		try {
			subnet = new Ipv4Subnet(rsCommunity.getSubnet().trim());
		}
		catch (Exception e) {
			throw new BltBadRequestException("Invalid matching subnet.",
					BltBadRequestException.INVALID_SNMP_COMMUNITY);
		}
		SnmpCommunity community = new SnmpCommunity(subnet,
				rsCommunity.getCommunity().trim());
		try {
			TopologyService.getService().addSnmpCommunity(network, community);
		}
		catch (Exception e) {
			throw new BltBadRequestException(e.getMessage(),
					BltBadRequestException.INVALID_SNMP_COMMUNITY);
		}
	}



	@DELETE
	@Path("networks/{nid}/communities/{id}")
	@RolesAllowed("admin")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteCommunity(@PathParam("nid") Long nid, @PathParam("id") Long id) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		SnmpCommunity community = network.getCommunityById(id);
		if (community == null) {
			throw new BltBadRequestException("The community doesn't exist.",
					BltBadRequestException.UNKNOWN_SNMP_COMMUNITY);
		}
		TopologyService.getService().removeSnmpCommunity(community);
	}

	@GET
	@Path("networks/{nid}/routers/{rid}")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Router getRouter(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		Router router = network.getRouterById(rid);
		if (router == null) {
			throw new BltBadRequestException("The router doesn't exist.",
					BltBadRequestException.UNKNOWN_ROUTER);
		}
		return router;
	}

	@GET
	@Path("networks/{nid}/routers")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<Router> getNetworkRouters(@PathParam("nid") Long nid) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}

		return network.getRouters();
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsNetworkPositions {
		private List<RsRouterPosition> positions = new ArrayList<RsRouterPosition>();

		@XmlElement
		public List<RsRouterPosition> getPositions() {
			return positions;
		}

		public void setPositions(List<RsRouterPosition> positions) {
			this.positions = positions;
		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)	
	public static class RsRouterPosition {
		int router = -1;
		int x = 0;
		int y = 0;
		@XmlElement
		public int getRouter() {
			return router;
		}
		public void setRouter(int router) {
			this.router = router;
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
	}

	@POST
	@Path("networks/{nid}/positions")
	@RolesAllowed("readwrite")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void editRouterPositions(@PathParam("nid") Long nid, RsNetworkPositions positions) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		for (RsRouterPosition position : positions.getPositions()) {
			Router router = network.getRouterById(position.getRouter());
			if (router != null) {
				router.setX(position.getX());
				router.setY(position.getY());
			}
		}
	}

	@DELETE
	@Path("networks/{nid}/routers/{rid}")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteRouter(@PathParam("nid") Long nid, @PathParam("rid") Long rid) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		Router router = network.getRouterById(rid);
		if (router == null) {
			throw new BltBadRequestException("The router doesn't exist.",
					BltBadRequestException.UNKNOWN_ROUTER);
		}
		if (!router.isLost()) {
			throw new BltBadRequestException("The router is still detected in the IGP topology.",
					BltBadRequestException.UNKNOWN_ROUTER);
		}
		TopologyService.getService().removeRouter(router);
	}




	@GET
	@Path("networks/{nid}/links/{id}")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Link getLink(@PathParam("nid") Long nid, @PathParam("id") Long id) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		Link link = network.getLinkById(id);
		if (link == null) {
			throw new BltBadRequestException("The link doesn't exist.",
					BltBadRequestException.UNKNOWN_LINK);
		}
		return link;
	}

	@GET
	@Path("networks/{nid}/links")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<Link> getNetworkLinks(@PathParam("nid") Long nid) throws WebApplicationException {
		try {
			Network network = TopologyService.getService().getNetworkById(nid);
			if (network == null) {
				throw new BltBadRequestException("The network doesn't exist.",
						BltBadRequestException.UNKNOWN_NETWORK);
			}
			network.fillLinkInterfaceNames();
			return network.getLinks();
		}
		catch (BltBadRequestException e) {
			throw e;
		}
		catch (Exception e) {
			throw new BltBadRequestException("Unable to retrieve the links.",
					BltBadRequestException.UNABLE_TO_RETRIEVE_DATA);
		}
	}

	@DELETE
	@Path("networks/{nid}/links/{id}")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteLink(@PathParam("nid") Long nid, @PathParam("id") Long id) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		Link link = network.getLinkById(id);
		if (link == null) {
			throw new BltBadRequestException("The link doesn't exist.",
					BltBadRequestException.UNKNOWN_LINK);
		}
		TopologyService.getService().removeLink(link);
	}


	@GET
	@Path("networks/{nid}/routers/{rid}/interfaces")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<RouterInterface> getRouterInterfaces(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		Router router = network.getRouterById(rid);
		if (router == null) {
			throw new BltBadRequestException("The router doesn't exist.",
					BltBadRequestException.UNKNOWN_ROUTER);
		}
		List<RouterInterface> routerInterfaces = router.getRouterInterfaces();
		return routerInterfaces;
	}
	
	@GET
	@Path("networks/{nid}/routers/{rid}/interfaces/ethernet")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<RouterInterface> getRouterEthernetInterfaces(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid) throws WebApplicationException {
		List<RouterInterface> routerAllInterfaces = this.getRouterInterfaces(nid, rid);
		List<RouterInterface> routerInterfaces = new ArrayList<RouterInterface>();
		for (RouterInterface routerInterface : routerAllInterfaces) {
			if (routerInterface.getClass().equals(RouterInterface.class)) {
				routerInterfaces.add(routerInterface);
			}
		}
		return routerInterfaces;
	}
	
	@GET
	@Path("networks/{nid}/routers/{rid}/interfaces/{id}")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public RouterInterface getRouterInterface(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, @PathParam("id") Long id) throws WebApplicationException {

		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		Router router = network.getRouterById(rid);
		if (router == null) {
			throw new BltBadRequestException("The router doesn't exist.",
					BltBadRequestException.UNKNOWN_ROUTER);
		}
		RouterInterface routerInterface = router.getRouterInterfaceById(id);
		if (routerInterface == null) {
			throw new BltBadRequestException("The interface doesn't exist.",
					BltBadRequestException.UNKNOWN_INTERFACE);
		}
		return routerInterface;
	}
	
	@GET
	@Path("networks/{nid}/routers/{rid}/ipv4igproutes")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<Ipv4Route> getRouterIpv4IgpRoutes(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		Router router = network.getRouterById(rid);
		if (router == null) {
			throw new BltBadRequestException("The router doesn't exist.",
					BltBadRequestException.UNKNOWN_ROUTER);
		}
		return router.getIpv4IgpRoutes();
	}

	/*@GET
	@Path("networks/{nid}/routers/{rid}/ipv4staticroutes")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<Ipv4Route> getRouterIpv4StaticRoutes(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		Router router = network.getRouterById(rid);
		if (router == null) {
			throw new BltBadRequestException("The router doesn't exist.",
					BltBadRequestException.UNKNOWN_ROUTER);
		}
		return router.getIpv4StaticRoutes();
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)	
	public static class RsIpv4StaticRoute {

		private String subnet;
		private String next;

		@XmlElement
		public String getSubnet() {
			return subnet;
		}
		public void setSubnet(String subnet) {
			this.subnet = subnet;
		}
		@XmlElement
		public String getNext() {
			return next;
		}
		public void setNext(String next) {
			this.next = next;
		}
	}*/

	@GET
	@Path("tasks/{id}")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task getTask(@PathParam("id") Long id) throws WebApplicationException {
		Task task = TaskManager.getTaskById(id);
		if (task == null) {
			throw new BltBadRequestException("Unable to find the task.",
					BltBadRequestException.UNKNOWN_TASK);
		}
		return task;
	}

	@GET
	@Path("tasks")
	@RolesAllowed("admin")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Task> getTasks() throws WebApplicationException {
		List<Task> tasks = new ArrayList<Task>();
		tasks.addAll(TaskManager.getTasks());
		return tasks;
	}
	
	@GET
	@Path("users")
	@RolesAllowed("admin")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<User> getUsers() throws WebApplicationException {
		return TopologyService.getService().getUsers();
	}
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsUser {
		private String name;
		private String password;
		private int level;
		
		@XmlElement
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@XmlElement
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		@XmlElement
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
	}
	
	@POST
	@Path("users")
	@RolesAllowed("admin")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void addUser(RsUser rsUser) {
		if (rsUser.getName() == null || rsUser.getName().trim().equals("")) {
			throw new BltBadRequestException("The name can't be empty.",
					BltBadRequestException.INVALID_USER_PARAMS);
		}
		if (rsUser.getPassword() == null || rsUser.getPassword().trim().equals("")) {
			throw new BltBadRequestException("The password can't be empty.",
					BltBadRequestException.INVALID_USER_PARAMS);
		}
		if (TopologyService.getService().getUserByName(rsUser.getName()) != null) {
			throw new BltBadRequestException("This user already exists.",
					BltBadRequestException.INVALID_USER_PARAMS);
		}
		User user = new User(rsUser.getName(), rsUser.getPassword(), rsUser.getLevel());
		TopologyService.getService().addUser(user);
	}
	
	@PUT
	@Path("users/{id}")
	@RolesAllowed("admin")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void editUser(@PathParam("id") Long id, RsUser rsUser) {
		User user = TopologyService.getService().getUserById(id);
		if (user == null) {
			throw new BltBadRequestException("The user doesn't exist.",
					BltBadRequestException.UNKNOWN_USER);
		}
		if (!user.getName().equals(rsUser.getName())) {
			throw new BltBadRequestException("The name can't be changed.",
					BltBadRequestException.INVALID_USER_PARAMS);
		}
		if (rsUser.getPassword() != null) {
			if (rsUser.getPassword().trim().equals("")) {
				throw new BltBadRequestException("The password can't be empty.",
						BltBadRequestException.INVALID_USER_PARAMS);
			}
			user.setPassword(rsUser.getPassword());
		}
		user.setLevel(rsUser.getLevel());
		TopologyService.getService().addUser(user);
	}
	
	@DELETE
	@Path("users/{id}")
	@RolesAllowed("admin")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteUser(@PathParam("id") Long id) throws WebApplicationException {
		User user = TopologyService.getService().getUserById(id);
		if (user == null) {
			throw new BltBadRequestException("The user doesn't exist.",
					BltBadRequestException.UNKNOWN_USER);
		}
		if (user.equals(request.getSession().getAttribute("user"))) {
			throw new BltBadRequestException("You can't kill yourself.",
					BltBadRequestException.INVALID_USER);
		}
		TopologyService.getService().removeUser(user);
	}
	
	@XmlRootElement @XmlAccessorType(value = XmlAccessType.NONE)
	public static class RsLogin {
		
		/** The username. */
		private String username;
		
		/** The password. */
		private String password;
		
		/** The new password. */
		private String newPassword = "";
		
		/**
		 * Gets the username.
		 *
		 * @return the username
		 */
		@XmlElement
		public String getUsername() {
			return username;
		}
		
		/**
		 * Sets the username.
		 *
		 * @param username the new username
		 */
		public void setUsername(String username) {
			this.username = username;
		}
		
		/**
		 * Gets the password.
		 *
		 * @return the password
		 */
		@XmlElement
		public String getPassword() {
			return password;
		}
		
		/**
		 * Sets the password.
		 *
		 * @param password the new password
		 */
		public void setPassword(String password) {
			this.password = password;
		}
		
		/**
		 * Gets the new password.
		 *
		 * @return the new password
		 */
		@XmlElement
		public String getNewPassword() {
			return newPassword;
		}
		
		/**
		 * Sets the new password.
		 *
		 * @param newPassword the new new password
		 */
		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}
	}
	
	@GET
	@Path("user")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User getCurrentUser() throws WebApplicationException {
		return (User) request.getSession().getAttribute("user");
	}
	
	private String getCurrentUserName() {
		String name = "Unknown";
		try {
			User user = this.getCurrentUser();
			name = user.getName();
		}
		catch (Exception e) {
		}
		return name;
	}
	
	private void logUserAction(String message, Object... args) {
		try {
			aaaLogger.info(MarkerFactory.getMarker("AAA"), message, args);
		}
		catch (Exception e) {
			aaaLogger.error("Error while logging user activity.", e);
		}
	}
	
	@DELETE
	@Path("user")
	@PermitAll
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void logout() throws WebApplicationException {
		logger.debug("REST logout request.");
		logUserAction("The user {} is logging out.", getCurrentUserName());
			
		HttpSession httpSession = request.getSession();
		httpSession.invalidate();
	}
	
	@POST
	@Path("user")
	@PermitAll
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User login(RsLogin rsLogin)
			throws WebApplicationException {
		if (rsLogin.getUsername() == null || rsLogin.getUsername().isEmpty()) {
			throw new BltBadRequestException("You must provide the username.",
					BltBadRequestException.INVALID_USER);
		}
		if (rsLogin.getPassword() == null || rsLogin.getPassword().isEmpty()) {
			throw new BltBadRequestException("You must provide the password.",
					BltBadRequestException.INVALID_USER);
		}

		logUserAction("Authentication attempt from {}, username {}.",
				request.getRemoteAddr(), rsLogin.getUsername());
		User user = TopologyService.getService().getUserByName(rsLogin.getUsername());
		if (user != null) {
			if (!user.authenticate(rsLogin.getPassword())) {
				throw new BltBadRequestException("Invalid username and/or password.",
						BltBadRequestException.INVALID_USER);
			}
		}
		if (user == null) {
			throw new BltBadRequestException("Invalid username and/or password.",
					BltBadRequestException.INVALID_USER);
		}
		request.getSession().setAttribute("user", user);
		request.getSession().setMaxInactiveInterval(User.MAX_IDLE_TIME);
		return user;
	}
	
	@PUT
	@Path("user")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void changePassword(RsLogin rsLogin)
			throws WebApplicationException {
		User knownUser = (User) request.getSession().getAttribute("user");
		if (knownUser.isRemote()) {
			throw new BltBadRequestException("Can't change the password of a remotely logged-in user.",
					BltBadRequestException.INVALID_USER);
		}
		User user = login(rsLogin);
		if (!user.equals(knownUser)) {
			throw new BltBadRequestException("Invalid user.",
					BltBadRequestException.INVALID_USER);
		}
		if (rsLogin.getNewPassword() == null || rsLogin.getNewPassword().trim().isEmpty()) {
			throw new BltBadRequestException("The new password can't be empty.",
					BltBadRequestException.INVALID_USER);
		}
		if (rsLogin.getNewPassword().equals(rsLogin.getPassword())) {
			throw new BltBadRequestException("The old and new passwords are the same.",
					BltBadRequestException.INVALID_USER);
		}
		user.setPassword(rsLogin.getNewPassword());
		TopologyService.getService().writeToDisk();
		logUserAction("The local user {} has changed his password.", rsLogin.getUsername());
	}
	
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsGlobalStatus {
		
		private String version;
		private String features;
		private boolean licensed = true ;
		private String machine;
		
		public RsGlobalStatus() {
			this.version = Blt.VERSION;
			this.features = Blt.FEATURES;
		}
		
		@XmlElement
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		@XmlElement
		public boolean isLicensed() {
			return licensed;
		}
		public void setLicensed(boolean licensed) {
			this.licensed = licensed;
		}
		@XmlElement
		public String getFeatures() {
			return features;
		}
		public void setFeatures(String features) {
			this.features = features;
		}
		@XmlElement
		public String getMachine() {
			return machine;
		}
		public void setMachine(String machine) {
			this.machine = machine;
		}
	}
	
	@GET
	@Path("status")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public RsGlobalStatus getGlobalStatus() throws WebApplicationException {
		return new RsGlobalStatus();
	}

}
