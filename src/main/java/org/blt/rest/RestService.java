package org.blt.rest;

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
import org.blt.Blt;
//import org.blt.aaa.Radius;
import org.blt.aaa.User;
//import org.blt.licensing.License;
//import org.blt.licensing.License.Validity;
//import org.blt.licensing.License;
//import org.blt.licensing.License.Validity;
/*import org.blt.netconf.NetconfConfigurationTask;
import org.blt.netconf.message.IPExplicitPaths;
import org.blt.netconf.message.InterfaceConfiguration;
import org.blt.netconf.message.Ipv4AclsPrefixLists;
import org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessListEntry.Ipv4AceRule;
import org.blt.netconf.message.MulticastRouting;
import org.blt.netconf.message.MulticastRouting.MulticastDefaultVrf;
import org.blt.netconf.message.MulticastRouting.MulticastInterface;
import org.blt.netconf.message.MulticastRouting.MulticastVrfIpv4;
import org.blt.netconf.message.MulticastRouting.StaticRpfRule;
import org.blt.netconf.message.RouterIgmp;
import org.blt.netconf.message.InterfaceConfiguration.Ipv4PacketFilter;
import org.blt.netconf.message.InterfaceConfiguration.PathOption;
import org.blt.netconf.message.InterfaceConfiguration.PathOption.IgpType;
import org.blt.netconf.message.InterfaceConfiguration.PathOption.Lockdown;
import org.blt.netconf.message.InterfaceConfiguration.PathOption.PathType;
import org.blt.netconf.message.InterfaceConfiguration.PathOption.Verbatim;
import org.blt.netconf.message.InterfaceConfiguration.PathOptionProtect;
import org.blt.netconf.message.InterfaceConfiguration.PathOptionProtectTable;
import org.blt.netconf.message.InterfaceConfiguration.Qos;
import org.blt.netconf.message.InterfaceConfiguration.TeAutoBandwidth;
import org.blt.netconf.message.InterfaceConfiguration.TeAutoBandwidth.AdjustmentThreshold;
import org.blt.netconf.message.InterfaceConfiguration.TeAutoBandwidth.BandwidthLimits;
import org.blt.netconf.message.InterfaceConfiguration.TeDestination;
import org.blt.netconf.message.InterfaceConfiguration.TeDestinationLeaf;
import org.blt.netconf.message.InterfaceConfiguration.TeDestinationTable;
import org.blt.netconf.message.InterfaceConfiguration.TeLogging;
import org.blt.netconf.message.InterfaceConfigurationTable;
import org.blt.netconf.message.Path.Hop;
import org.blt.netconf.message.Path.Hop.HopType;
import org.blt.netconf.message.RouterIgmp.IgmpInterface;
import org.blt.netconf.message.RouterIgmp.IgmpStaticGroup;
import org.blt.netconf.message.RouterStatic;
import org.blt.netconf.message.RouterStatic.DefaultVrf;
import org.blt.netconf.message.RouterStatic.VrfIpv4;
import org.blt.netconf.message.RouterStatic.VrfUnicast;
import org.blt.netconf.message.VrfPrefix;*/
import org.blt.tasks.Task;
import org.blt.tasks.TaskManager;
import org.blt.topology.TopologyService;
import org.blt.topology.net.Ipv4Route;
//import org.blt.topology.net.Ipv4AccessList;
//import org.blt.topology.net.Ipv4AccessList.AclProtocol;
//import org.blt.topology.net.Ipv4AccessList.Ipv4AccessListEntry;
//import org.blt.topology.net.Ipv4Route;
//import org.blt.topology.net.Ipv4StaticGroup;
import org.blt.topology.net.Ipv4Subnet;
//import org.blt.topology.net.Ipv4Subnet.MalformedIpv4SubnetException;
import org.blt.topology.net.Link;
import org.blt.topology.net.Network;
//import org.blt.topology.net.P2mpTeTunnel;
//import org.blt.topology.net.P2pTeTunnel;
import org.blt.topology.net.Router;
import org.blt.topology.net.RouterInterface;
//import org.blt.topology.net.TeTunnel;
//import org.blt.topology.net.RouterInterface.RouterInterfaceType;
import org.blt.topology.net.SnmpCommunity;
//import org.blt.topology.net.SshAccount;
//import org.blt.topology.net.TePath;
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





	/*@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsSshAccount {
		private String subnet;
		private String username;
		private String password;
		@XmlElement
		public String getSubnet() {
			return subnet;
		}
		public void setSubnet(String subnet) {
			this.subnet = subnet;
		}
		@XmlElement
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		@XmlElement
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
	}
*/

	/*@POST
	@Path("networks/{nid}/accounts")
	@RolesAllowed("admin")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void addAccount(@PathParam("nid") Long nid, RsSshAccount rsAccount) {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		if (rsAccount.getUsername() == null || rsAccount.getUsername().equals("")) {
			throw new BltBadRequestException("The username must be present and can't be empty.",
					BltBadRequestException.INVALID_SSH_ACCOUNT);
		}
		if (rsAccount.getUsername() == null) {
			throw new BltBadRequestException("The password must be present.",
					BltBadRequestException.INVALID_SSH_ACCOUNT);
		}
		if (rsAccount.getSubnet() == null || rsAccount.getSubnet().equals("")) {
			throw new BltBadRequestException("The matching subnet can't be empty.",
					BltBadRequestException.INVALID_SSH_ACCOUNT);
		}
		Ipv4Subnet subnet;
		try {
			subnet = new Ipv4Subnet(rsAccount.getSubnet());
		}
		catch (Exception e) {
			throw new BltBadRequestException("Invalid matching subnet.",
					BltBadRequestException.INVALID_SSH_ACCOUNT);
		}
		SshAccount account = new SshAccount(subnet, rsAccount.getUsername(),
				rsAccount.getPassword());
		try {
			TopologyService.getService().addSshAccount(network, account);
		}
		catch (Exception e) {
			throw new BltBadRequestException(e.getMessage(),
					BltBadRequestException.INVALID_SSH_ACCOUNT);
		}
	}
*/


	/*@DELETE
	@Path("networks/{nid}/accounts/{id}")
	@RolesAllowed("admin")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteAccount(@PathParam("nid") Long nid, @PathParam("id") Long id) throws WebApplicationException {
		Network network = TopologyService.getService().getNetworkById(nid);
		if (network == null) {
			throw new BltBadRequestException("The network doesn't exist.",
					BltBadRequestException.UNKNOWN_NETWORK);
		}
		SshAccount account = network.getAccountById(id);
		if (account == null) {
			throw new BltBadRequestException("The account doesn't exist.",
					BltBadRequestException.UNKNOWN_SSH_ACCOUNT);
		}
		TopologyService.getService().removeSshAccount(account);
	}	
*/




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
			throw new BltBadRequestException("The is still detected in the IGP topology.",
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
	
	/*@GET
	@Path("networks/{nid}/routers/{rid}/interfaces/p2pte")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<P2pTeTunnel> getTeP2pTeTunnels(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid) throws WebApplicationException {
		List<RouterInterface> routerAllInterfaces = this.getRouterInterfaces(nid, rid);
		List<P2pTeTunnel> routerInterfaces = new ArrayList<P2pTeTunnel>();
		for (RouterInterface routerInterface : routerAllInterfaces) {
			if (routerInterface instanceof P2pTeTunnel) {
				routerInterfaces.add((P2pTeTunnel) routerInterface);
			}
		}
		return routerInterfaces;
	}*/
	
	/*@GET
	@Path("networks/{nid}/routers/{rid}/interfaces/p2mpte")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<P2mpTeTunnel> getTeP2mpTeTunnels(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid) throws WebApplicationException {
		List<RouterInterface> routerAllInterfaces = this.getRouterInterfaces(nid, rid);
		List<P2mpTeTunnel> routerInterfaces = new ArrayList<P2mpTeTunnel>();
		for (RouterInterface routerInterface : routerAllInterfaces) {
			if (routerInterface instanceof P2mpTeTunnel) {
				routerInterfaces.add((P2mpTeTunnel) routerInterface);
			}
		}
		return routerInterfaces;
	}*/
	
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
	

	/*@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static abstract class RsRouterInterface {

		protected String name;
		protected String description;
		protected String inAcl;
		protected String inQoS;
		protected Boolean shutdown;
		protected Boolean multicastInterface;

		@XmlElement
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@XmlElement
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		@XmlElement
		public String getInAcl() {
			return inAcl;
		}
		public void setInAcl(String inAcl) {
			this.inAcl = inAcl;
		}
		@XmlElement
		public String getInQoS() {
			return inQoS;
		}
		public void setInQoS(String inQoS) {
			this.inQoS = inQoS;
		}
		@XmlElement
		public Boolean getShutdown() {
			return shutdown;
		}
		public void setShutdown(Boolean shutdown) {
			this.shutdown = shutdown;
		}
		@XmlElement
		public Boolean getMulticastInterface() {
			return multicastInterface;
		}
		public void setMulticastInterface(Boolean multicastInterface) {
			this.multicastInterface = multicastInterface;
		}

		public MulticastRouting toMulticastConfig(Router router) {
			if (this.getMulticastInterface() != null) {
				MulticastInterface multicastInterface = new MulticastInterface(this.getName());
				if (!this.getMulticastInterface()) {
					multicastInterface.setDelete(true);
				}
				MulticastVrfIpv4 vrf = new MulticastVrfIpv4();
				vrf.addMulticastInterface(multicastInterface);
				MulticastRouting multicastRouting = new MulticastRouting();
				multicastRouting.setDefaultVrf(new MulticastDefaultVrf(vrf));
				return multicastRouting;
			}
			return null;
		}



		public InterfaceConfiguration toInterfaceConfig(Router router)
				throws BltBadRequestException {
			InterfaceConfiguration interfaceConfig = new InterfaceConfiguration(this.getName());
			interfaceConfig.setDescription(this.getDescription());
			interfaceConfig.setShutdown(this.getShutdown());

			if (this.getInAcl() != null) {
				if (!this.getInAcl().equals("")) {
					boolean found = false;
					for (Ipv4AccessList acl : router.getIpv4AccessLists()) {
						if (this.getInAcl().equals(acl.getName())) {
							found = true;
							break;
						}
					}
					if (!found) {
						throw new BltBadRequestException("The provided inbound ACL doesn't exist.",
								BltBadRequestException.INVALID_INTERFACE_PARAMS);
					}
				}
				interfaceConfig.setIpv4PacketFilter(new Ipv4PacketFilter(this.getInAcl(), null));
			}

			if (this.getInQoS() != null) {
				if (!this.getInQoS().equals("")) {
					boolean found = false;
					for (String policyMap : router.getPolicyMaps()) {
						if (this.getInQoS().equals(policyMap)) {
							found = true;
							break;
						}
					}
					if (!found) {
						throw new BltBadRequestException("The provided inbound policy-map doesn't exist.",
								BltBadRequestException.INVALID_INTERFACE_PARAMS);
					}
				}
				interfaceConfig.setQos(new Qos(this.getInQoS(), null));
			}
			return interfaceConfig;
		}
	}
*/
	/*@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsRouterEthernetInterface extends RsRouterInterface {

		private Integer dot1qTag;
		private String ipv4Address;

		public RsRouterEthernetInterface() {
		}

		@XmlElement
		public Integer getDot1qTag() {
			return dot1qTag;
		}
		public void setDot1qTag(Integer dot1qTag) {
			this.dot1qTag = dot1qTag;
		}
		@XmlElement
		public String getIpv4Address() {
			return ipv4Address;
		}
		public void setIpv4Address(String ipv4Address) {
			this.ipv4Address = ipv4Address;
		}

		@Override
		public InterfaceConfiguration toInterfaceConfig(Router router)
				throws BltBadRequestException {
			InterfaceConfiguration interfaceConfig = super.toInterfaceConfig(router);
			interfaceConfig.setVlanSubConfiguration(this.getDot1qTag());
			if (this.getIpv4Address() != null) {
				try {
					Ipv4Subnet address = new Ipv4Subnet(this.getIpv4Address());
					if (!address.isNormalUnicast()) {
						throw new MalformedIpv4SubnetException();
					}
					for (RouterInterface otherInterface : router.getRouterInterfaces()) {
						if (otherInterface.getName().equals(this.getName())) {
							continue;
						}
						if (otherInterface.getIpv4Address() != null &&
								address.contains(otherInterface.getIpv4Address())) {
							throw new BltBadRequestException(String.format(
									"This IP address overlaps with %s.", otherInterface.getName()),
									BltBadRequestException.INVALID_INTERFACE_PARAMS);
						}
					}
					interfaceConfig.setIpv4Network(address);
				}
				catch (MalformedIpv4SubnetException e) {
					throw new BltBadRequestException("Invalid IP address.",
							BltBadRequestException.INVALID_INTERFACE_PARAMS);
				}
			}
			return interfaceConfig;
		}
	}*/

	/*@POST
	@Path("networks/{nid}/routers/{rid}/interfaces/ethernet")
	@RolesAllowed("readwrite")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task addRouterEthernetInterface(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, RsRouterEthernetInterface rsInterface) {

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
		if (rsInterface.getDot1qTag() == null || rsInterface.getDot1qTag() < 1 ||
				rsInterface.getDot1qTag() > 4094) {
			throw new BltBadRequestException("Invalid VLAN.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		if (rsInterface.getName() == null) {
			throw new BltBadRequestException("Missing interface name.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		Pattern namePattern = Pattern.compile("(?<intf>.*(TenGig|Ether).*?)\\.(?<sub>[0-9]{1,8})");
		Matcher nameMatcher = namePattern.matcher(rsInterface.getName());
		if (!nameMatcher.matches()) {
			throw new BltBadRequestException("The name isn't that of an Ethernet subinterface.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		String physicalName = nameMatcher.group("intf");
		boolean physicalExists = false;
		for (RouterInterface routerInterface : router.getRouterInterfaces()) {
			if (routerInterface.getType() != RouterInterfaceType.CONFIGURED) {
				continue;
			}
			if (rsInterface.getName().equals(routerInterface.getName())) {
				throw new BltBadRequestException("This interface already exists.",
						BltBadRequestException.INVALID_INTERFACE_PARAMS);
			}
			if (routerInterface.getName().startsWith(physicalName + ".") &&
					rsInterface.getDot1qTag().equals(routerInterface.getDot1qTag())) {
				throw new BltBadRequestException(
						String.format("This VLAN is already bound to subinterface %s.",
								routerInterface.getName()),
								BltBadRequestException.INVALID_INTERFACE_PARAMS);
			}
			if (routerInterface.getName().equals(physicalName) &&
					routerInterface.getDot1qTag() == 0) {
				physicalExists = true;
			}
		}
		if (!physicalExists) {
			throw new BltBadRequestException("The physical interface doesn't exist.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		if (rsInterface.getInAcl() == "") {
			rsInterface.setInAcl(null);
		}
		if (rsInterface.getInQoS() == "") {
			rsInterface.setInQoS(null);
		}

		InterfaceConfiguration interfaceConfig = rsInterface.toInterfaceConfig(router);
		interfaceConfig.setInterfaceModeNonPhysical("Default");
		InterfaceConfigurationTable interfaceConfigTable = new InterfaceConfigurationTable();
		interfaceConfigTable.addInterfaceConfiguration(interfaceConfig);

		MulticastRouting multicastRouting = rsInterface.toMulticastConfig(router);

		logUserAction("The user {} is adding the Ethernet subinterface {} to router {}.",
				getCurrentUserName(), rsInterface.getName(), router.getName());
		
		try {
			Task task = new NetconfConfigurationTask("Add Ethernet subinterface", router, interfaceConfigTable, multicastRouting);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}

	@PUT
	@Path("networks/{nid}/routers/{rid}/interfaces/ethernet/{id}")
	@RolesAllowed("readwrite")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task editRouterEthernetInterface(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, @PathParam("id") Long id,
			RsRouterEthernetInterface rsInterface) {

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
		if (routerInterface.getType() != RouterInterfaceType.CONFIGURED) {
			throw new BltBadRequestException("This is not a configured interface.",
					BltBadRequestException.UNKNOWN_INTERFACE);
		}
		if (routerInterface instanceof TeTunnel) {
			throw new BltBadRequestException("This is not an Ethernet interface.",
					BltBadRequestException.UNKNOWN_INTERFACE);
		}
		if (!routerInterface.getName().equals(rsInterface.getName())) {
			throw new BltBadRequestException("The interface name isn't correct.",
					BltBadRequestException.UNKNOWN_INTERFACE);
		}
		if (rsInterface.getInAcl() == "" && routerInterface.getInAccessGroup() == null) {
			rsInterface.setInAcl(null);
		}
		if (rsInterface.getInQoS() == "" && routerInterface.getInServicePolicy() == null) {
			rsInterface.setInQoS(null);
		}
		InterfaceConfiguration interfaceConfig = rsInterface.toInterfaceConfig(router);
		InterfaceConfigurationTable interfaceConfigTable = null;
		if (!interfaceConfig.isBlank()) {
			interfaceConfigTable = new InterfaceConfigurationTable();
			interfaceConfigTable.addInterfaceConfiguration(interfaceConfig);
		}
		MulticastRouting multicastRouting = rsInterface.toMulticastConfig(router);

		if (interfaceConfigTable == null && multicastRouting == null) {
			throw new BltBadRequestException("Nothing to change.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		
		logUserAction("The user {} is editing the Ethernet interface {} on router {}.",
				getCurrentUserName(), routerInterface.getName(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Edit Ethernet interface", router, interfaceConfigTable, multicastRouting);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}

	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static abstract class RsRouterTeTunnel extends RsRouterInterface {
		private Integer signalledBandwidth;
		private Boolean logging;

		private Integer setupPriority;
		private Integer holdPriority;

		@XmlElement
		public Integer getSignalledBandwidth() {
			return signalledBandwidth;
		}
		public void setSignalledBandwidth(Integer signalledBandwidth) {
			this.signalledBandwidth = signalledBandwidth;
		}
		@XmlElement
		public Boolean getLogging() {
			return logging;
		}
		public void setLogging(Boolean logging) {
			this.logging = logging;
		}
		@XmlElement
		public Integer getSetupPriority() {
			return setupPriority;
		}
		public void setSetupPriority(Integer setupPriority) {
			this.setupPriority = setupPriority;
		}
		@XmlElement
		public Integer getHoldPriority() {
			return holdPriority;
		}
		public void setHoldPriority(Integer holdPriority) {
			this.holdPriority = holdPriority;
		}


		@Override
		public InterfaceConfiguration toInterfaceConfig(Router router)
				throws BltBadRequestException {
			InterfaceConfiguration interfaceConfig = super.toInterfaceConfig(router);

			if (this.getSetupPriority() != null || this.getHoldPriority() != null) {
				if (this.getSetupPriority() == null || this.getSetupPriority() < 0 ||
						this.getSetupPriority() > 7) {
					throw new BltBadRequestException("Invalid setup priority (should be between 0 and 7).",
							BltBadRequestException.INVALID_TUNNEL_PARAMS);

				}
				if (this.getHoldPriority() == null || this.getHoldPriority() < 0 ||
						this.getHoldPriority() > 7) {
					throw new BltBadRequestException("Invalid hold priority (should be between 0 and 7).",
							BltBadRequestException.INVALID_TUNNEL_PARAMS);
				}
				if (this.getHoldPriority() > this.getSetupPriority()) {
					throw new BltBadRequestException("Hold priority may not be higher than setup priority.",
							BltBadRequestException.INVALID_TUNNEL_PARAMS);
				}
			}
			if (this.getSignalledBandwidth() != null && this.getSignalledBandwidth() < 0) {
				throw new BltBadRequestException("The signalled bandwidth must be positive.",
						BltBadRequestException.INVALID_TUNNEL_PARAMS);
			}
			return interfaceConfig;

		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsRouterP2pTeTunnel extends RsRouterTeTunnel {
		private RsTeDestination destination;

		private Boolean autoBwEnabled;
		private Integer autoBwMinLimit;
		private Integer autoBwMaxLimit;
		private Integer autoBwAdjustmentThresholdPercent;
		private Integer autoBwAdjustmentThresholdValue;
		private Integer autoBwApplicationFrequency;

		@XmlElement
		public RsTeDestination getDestination() {
			return destination;
		}
		public void setDestination(RsTeDestination destination) {
			this.destination = destination;
		}
		@XmlElement
		public Integer getAutoBwMinLimit() {
			return autoBwMinLimit;
		}
		public void setAutoBwMinLimit(Integer autoBwMinLimit) {
			this.autoBwMinLimit = autoBwMinLimit;
		}
		@XmlElement
		public Integer getAutoBwMaxLimit() {
			return autoBwMaxLimit;
		}
		public void setAutoBwMaxLimit(Integer autoBwMaxLimit) {
			this.autoBwMaxLimit = autoBwMaxLimit;
		}
		@XmlElement
		public Integer getAutoBwAdjustmentThresholdPercent() {
			return autoBwAdjustmentThresholdPercent;
		}
		public void setAutoBwAdjustmentThresholdPercent(
				Integer autoBwAdjustmentThresholdPercent) {
			this.autoBwAdjustmentThresholdPercent = autoBwAdjustmentThresholdPercent;
		}
		@XmlElement
		public Integer getAutoBwAdjustmentThresholdValue() {
			return autoBwAdjustmentThresholdValue;
		}
		public void setAutoBwAdjustmentThresholdValue(
				Integer autoBwAdjustmentThresholdValue) {
			this.autoBwAdjustmentThresholdValue = autoBwAdjustmentThresholdValue;
		}
		@XmlElement
		public Integer getAutoBwApplicationFrequency() {
			return autoBwApplicationFrequency;
		}
		public void setAutoBwApplicationFrequency(Integer autoBwApplicationFrequency) {
			this.autoBwApplicationFrequency = autoBwApplicationFrequency;
		}
		@XmlElement
		public Boolean getAutoBwEnabled() {
			return autoBwEnabled;
		}
		public void setAutoBwEnabled(Boolean autoBwEnabled) {
			this.autoBwEnabled = autoBwEnabled;
		}
		@Override
		public InterfaceConfiguration toInterfaceConfig(Router router)
				throws BltBadRequestException {
			InterfaceConfiguration interfaceConfig = super.toInterfaceConfig(router);

			if (this.getAutoBwEnabled() != null && this.getAutoBwEnabled()) {
				if (this.getAutoBwAdjustmentThresholdPercent() != null &&
						(this.getAutoBwAdjustmentThresholdPercent() < 1 ||
								this.getAutoBwAdjustmentThresholdPercent() > 100)) {
					throw new BltBadRequestException("Invalid auto-bw adjustment threshold percent value.",
							BltBadRequestException.INVALID_TUNNEL_PARAMS);
				}
				if (this.getAutoBwAdjustmentThresholdValue() != null &&
						this.getAutoBwAdjustmentThresholdValue() < 10) {
					throw new BltBadRequestException("Invalid auto-bw adjustment threshold value.",
							BltBadRequestException.INVALID_TUNNEL_PARAMS);
				}
				if ((this.getAutoBwMaxLimit() != null) ^ (this.getAutoBwMinLimit() != null)) {
					throw new BltBadRequestException("Missing min or max auto-bw limit value.",
							BltBadRequestException.INVALID_TUNNEL_PARAMS);
				}
				if (this.getAutoBwMaxLimit() != null && this.getAutoBwMinLimit() != null &&
						this.getAutoBwMaxLimit() <= this.getAutoBwMinLimit()) {
					throw new BltBadRequestException("Invalid min and max auto-bw limit values.",
							BltBadRequestException.INVALID_TUNNEL_PARAMS);
				}
				if (this.getAutoBwApplicationFrequency() != null &&
						(this.getAutoBwApplicationFrequency() < 5 || this.getAutoBwApplicationFrequency() > 10080)) {
					throw new BltBadRequestException("Invalid auto-bw application frequency value.",
							BltBadRequestException.INVALID_TUNNEL_PARAMS);
				}

				if ((this.getAutoBwAdjustmentThresholdPercent() != null ||
						this.getAutoBwAdjustmentThresholdValue() != null ||
						this.getAutoBwApplicationFrequency() != null ||
						this.getAutoBwMaxLimit() != null ||
						this.getAutoBwMinLimit() != null)) {

					TeAutoBandwidth autoBandwidth = new TeAutoBandwidth();
					autoBandwidth.setApplicationFrequency(this.getAutoBwApplicationFrequency());
					if (this.getAutoBwMaxLimit() != null ||
							this.getAutoBwMinLimit() != null) {
						autoBandwidth.setBandwidthLimits(new BandwidthLimits(this.getAutoBwMinLimit(),
								this.getAutoBwMaxLimit()));
					}
					if (this.getAutoBwAdjustmentThresholdPercent() != null ||
							this.getAutoBwAdjustmentThresholdValue() != null) {
						autoBandwidth.setAdjustementThreshold(new AdjustmentThreshold(
								this.getAutoBwAdjustmentThresholdPercent(),
								this.getAutoBwAdjustmentThresholdValue()));
					}
					interfaceConfig.addTunnelTeAttribute(autoBandwidth);
				}
			}
			else if (this.getAutoBwEnabled() != null && !this.getAutoBwEnabled()) {
				TeAutoBandwidth autoBandwidth = new TeAutoBandwidth();
				autoBandwidth.setDelete(true);
				interfaceConfig.addTunnelTeAttribute(autoBandwidth);
			}

			if (this.getSignalledBandwidth() != null) {
				interfaceConfig.addTunnelTeAttribute(
						new InterfaceConfiguration.TeBandwidth(0, this.getSignalledBandwidth()));
			}

			if (this.getSetupPriority() != null || this.getHoldPriority() != null) {
				interfaceConfig.addTunnelTeAttribute(new InterfaceConfiguration.TePriority(
						this.getSetupPriority(), this.getHoldPriority()));
			}

			if (this.getLogging() != null && this.getLogging()) {
				interfaceConfig.addTunnelTeAttribute(new TeLogging());
			}

			if (this.getDestination() != null) {
				Ipv4Subnet ipDestination = parseAddress(this.getDestination().getDestination());
				if (!ipDestination.isNormalUnicast()) {
					throw new BltBadRequestException("Invalid destination IP address.",
							BltBadRequestException.INVALID_TUNNEL_PARAMS);
				}
				interfaceConfig.addTunnelTeAttribute(new TeDestination(ipDestination.getIp()));
				if (this.getDestination().getPaths() == null || this.getDestination().getPaths().size() == 0) {
					throw new BltBadRequestException("The path options are missing.",
							BltBadRequestException.INVALID_TUNNEL_PARAMS);
				}
				PathOptionProtect protect = new PathOptionProtect();
				int index = 0;
				for (String path : this.getDestination().getPaths()) {
					index += 10;
					if (path.equals("")) { // Dynamic
						protect.addPathOption(new PathOption(index, PathType.DYNAMIC, IgpType.NONE,
								null, null));
					}
					else {
						boolean found = false;
						for (TePath tePath : router.getExplicitPaths()) {
							if (path.equals(tePath.getName())) {
								found = true;
								break;
							}
						}
						if (!found) {
							throw new BltBadRequestException(
									String.format("Unknown explicit path name '%s'.", path),
									BltBadRequestException.INVALID_TUNNEL_PARAMS);
						}
						protect.addPathOption(new PathOption(index, PathType.EXPLICITNAME,
								path, IgpType.NONE, null, null));
					}
				}
				PathOptionProtectTable protectTable = new PathOptionProtectTable();
				PathOptionProtect clearProtect = new PathOptionProtect();
				clearProtect.setDelete(true);
				protectTable.addPathOptionProtect(clearProtect);
				protectTable.addPathOptionProtect(protect);
				interfaceConfig.addTunnelTeAttribute(protectTable);
			}
			return interfaceConfig;

		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsRouterP2mpTeTunnel extends RsRouterTeTunnel {
		private List<RsTeDestination> destinations;

		@XmlElement
		public List<RsTeDestination> getDestinations() {
			return destinations;
		}
		public void setDestinations(List<RsTeDestination> destinations) {
			this.destinations = destinations;
		}

		@Override
		public InterfaceConfiguration toInterfaceConfig(Router router)
				throws BltBadRequestException {
			InterfaceConfiguration interfaceConfig = super.toInterfaceConfig(router);

			if (this.getSignalledBandwidth() != null) {
				interfaceConfig.addTunnelMteAttribute(
						new InterfaceConfiguration.TeBandwidth(0, this.getSignalledBandwidth()));
			}

			if (this.getSetupPriority() != null || this.getHoldPriority() != null) {
				interfaceConfig.addTunnelMteAttribute(new InterfaceConfiguration.TePriority(
						this.getSetupPriority(), this.getHoldPriority()));
			}

			if (this.getLogging() != null && this.getLogging()) {
				interfaceConfig.addTunnelMteAttribute(new TeLogging());
			}

			if (this.getDestinations() != null) {
				TeDestinationTable teDestinationTable = new TeDestinationTable();
				for (RsTeDestination rsDestination : this.getDestinations()) {
					Ipv4Subnet ipDestination = parseAddress(rsDestination.getDestination());
					if (!ipDestination.isNormalUnicast()) {
						throw new BltBadRequestException("Invalid destination IP address.",
								BltBadRequestException.INVALID_TUNNEL_PARAMS);
					}
					TeDestinationLeaf teDestination = new TeDestinationLeaf(ipDestination);
					if (rsDestination.getPaths() == null || rsDestination.getPaths().size() == 0) {
						throw new BltBadRequestException("The path options are missing.",
								BltBadRequestException.INVALID_TUNNEL_PARAMS);
					}
					int index = 0;
					for (String path : rsDestination.getPaths()) {
						index += 10;
						if (path.equals("")) { // Dynamic
							teDestination.addPathOption(new PathOption(index, PathType.DYNAMIC, null,
									Verbatim.NONE, Lockdown.NONE));
						}
						else {
							boolean found = false;
							for (TePath tePath : router.getExplicitPaths()) {
								if (path.equals(tePath.getName())) {
									found = true;
									break;
								}
							}
							if (!found) {
								throw new BltBadRequestException(
										String.format("Unknown explicit path name '%s'.", path),
										BltBadRequestException.INVALID_TUNNEL_PARAMS);
							}
							teDestination.addPathOption(new PathOption(index, PathType.EXPLICITNAME,
									path, null, Verbatim.NONE, Lockdown.NONE));
						}
					}
					teDestinationTable.addDestinationLeaf(teDestination);
				}
				TeDestinationTable clearDestinationTable = new TeDestinationTable();
				clearDestinationTable.setDelete(true);
				interfaceConfig.addTunnelMteAttribute(clearDestinationTable);
				interfaceConfig.addTunnelMteAttribute(teDestinationTable);
			}
			return interfaceConfig;
		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsTeDestination {
		private String destination;
		private List<String> paths;

		@XmlElement
		public String getDestination() {
			return destination;
		}
		public void setDestination(String destination) {
			this.destination = destination;
		}
		@XmlElement
		public List<String> getPaths() {
			return paths;
		}
		public void setPaths(List<String> paths) {
			this.paths = paths;
		}
	}

	@POST
	@Path("networks/{nid}/routers/{rid}/interfaces/p2pte")
	@RolesAllowed("readwrite")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task addRouterTeP2pTeTunnel(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, RsRouterP2pTeTunnel rsTunnel) {

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
		if (rsTunnel.getName() == null) {
			throw new BltBadRequestException("Missing interface name.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		Pattern namePattern = Pattern.compile("tunnel-te(?<id>[0-9]{1,8})");
		Matcher nameMatcher = namePattern.matcher(rsTunnel.getName());
		if (!nameMatcher.matches()) {
			throw new BltBadRequestException("The name isn't that of a P2P TE tunnel.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		int tunnelId = Integer.parseInt(nameMatcher.group("id"));
		if (tunnelId < 0 || tunnelId > 65535) {
			throw new BltBadRequestException("The tunnel number should be between 0 and 65535.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		if (rsTunnel.getDestination() == null) {
			throw new BltBadRequestException("The tunnel must have a destination.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		for (RouterInterface routerInterface : router.getRouterInterfaces()) {
			if (routerInterface.getType() != RouterInterfaceType.CONFIGURED) {
				continue;
			}
			if (rsTunnel.getName().equals(routerInterface.getName())) {
				throw new BltBadRequestException("This interface already exists.",
						BltBadRequestException.INVALID_INTERFACE_PARAMS);
			}
		}
		if (rsTunnel.getAutoBwEnabled() != null && !rsTunnel.getAutoBwEnabled()) {
			rsTunnel.setAutoBwEnabled(null);
		}
		if (rsTunnel.getSetupPriority() == null || rsTunnel.getHoldPriority() == null) {
			throw new BltBadRequestException("You must specify setup and hold priorities.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		if (rsTunnel.getSignalledBandwidth() == null) {
			throw new BltBadRequestException("You must specify a signalled bandwidth.",
					BltBadRequestException.INVALID_TUNNEL_PARAMS);
		}

		InterfaceConfiguration interfaceConfig = rsTunnel.toInterfaceConfig(router);
		interfaceConfig.setInterfaceVirtual(true);
		InterfaceConfigurationTable interfaceConfigTable = new InterfaceConfigurationTable();
		interfaceConfigTable.addInterfaceConfiguration(interfaceConfig);

		MulticastRouting multicastRouting = rsTunnel.toMulticastConfig(router);
		
		logUserAction("The user {} is adding a P2P TE Tunnel {} to router {}.",
				getCurrentUserName(), rsTunnel.getName(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Add P2P TE tunnel", router, interfaceConfigTable, multicastRouting);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}

	@PUT
	@Path("networks/{nid}/routers/{rid}/interfaces/p2pte/{id}")
	@RolesAllowed("readwrite")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task editRouterTeP2pTeTunnel(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, @PathParam("id") Long id,
			RsRouterP2pTeTunnel rsTunnel) {

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
		if (routerInterface.getType() != RouterInterfaceType.CONFIGURED) {
			throw new BltBadRequestException("This is not a configured interface.",
					BltBadRequestException.UNKNOWN_INTERFACE);
		}
		if (!(routerInterface instanceof P2pTeTunnel)) {
			throw new BltBadRequestException("This is not a P2P TE tunnel.",
					BltBadRequestException.UNKNOWN_INTERFACE);
		}
		if (!routerInterface.getName().equals(rsTunnel.getName())) {
			throw new BltBadRequestException("The interface name isn't correct.",
					BltBadRequestException.UNKNOWN_INTERFACE);
		}

		InterfaceConfiguration interfaceConfig = rsTunnel.toInterfaceConfig(router);
		InterfaceConfigurationTable interfaceConfigTable = null;
		if (!interfaceConfig.isBlank()) {
			interfaceConfigTable = new InterfaceConfigurationTable();
			interfaceConfigTable.addInterfaceConfiguration(interfaceConfig);
		}
		MulticastRouting multicastRouting = rsTunnel.toMulticastConfig(router);
		
		logUserAction("The user {} is editing the P2P TE tunnel {} on router {}.",
				getCurrentUserName(), routerInterface.getName(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Edit P2P TE tunnel", router, interfaceConfigTable, multicastRouting);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}

	@POST
	@Path("networks/{nid}/routers/{rid}/interfaces/p2mpte")
	@RolesAllowed("readwrite")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task addRouterTeP2mpTeTunnel(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, RsRouterP2mpTeTunnel rsTunnel) {

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
		if (rsTunnel.getName() == null) {
			throw new BltBadRequestException("Missing interface name.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		Pattern namePattern = Pattern.compile("tunnel-mte(?<id>[0-9]{1,8})");
		Matcher nameMatcher = namePattern.matcher(rsTunnel.getName());
		if (!nameMatcher.matches()) {
			throw new BltBadRequestException("The name isn't that of a P2MP TE tunnel.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		int tunnelId = Integer.parseInt(nameMatcher.group("id"));
		if (tunnelId < 0 || tunnelId > 65535) {
			throw new BltBadRequestException("The tunnel number should be between 0 and 65535.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		if (rsTunnel.getDestinations() == null) {
			throw new BltBadRequestException("The tunnel must have (a) destination(s).",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		for (RouterInterface routerInterface : router.getRouterInterfaces()) {
			if (routerInterface.getType() != RouterInterfaceType.CONFIGURED) {
				continue;
			}
			if (rsTunnel.getName().equals(routerInterface.getName())) {
				throw new BltBadRequestException("This interface already exists.",
						BltBadRequestException.INVALID_INTERFACE_PARAMS);
			}
		}
		if (rsTunnel.getSetupPriority() == null || rsTunnel.getHoldPriority() == null) {
			throw new BltBadRequestException("You must specify setup and hold priorities.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		if (rsTunnel.getSignalledBandwidth() == null) {
			throw new BltBadRequestException("You must specify a signalled bandwidth.",
					BltBadRequestException.INVALID_TUNNEL_PARAMS);
		}

		InterfaceConfiguration interfaceConfig = rsTunnel.toInterfaceConfig(router);
		interfaceConfig.setInterfaceVirtual(true);
		InterfaceConfigurationTable interfaceConfigTable = new InterfaceConfigurationTable();
		interfaceConfigTable.addInterfaceConfiguration(interfaceConfig);

		MulticastRouting multicastRouting = rsTunnel.toMulticastConfig(router);
		
		logUserAction("The user {} is adding a P2MP TE Tunnel {} to router {}.",
				getCurrentUserName(), rsTunnel.getName(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Edit P2MP TE tunnel", router, interfaceConfigTable, multicastRouting);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}

	@PUT
	@Path("networks/{nid}/routers/{rid}/interfaces/p2mpte/{id}")
	@RolesAllowed("readwrite")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task editRouterTeP2mpTeTunnel(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, @PathParam("id") Long id,
			RsRouterP2mpTeTunnel rsTunnel) {

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
		if (routerInterface.getType() != RouterInterfaceType.CONFIGURED) {
			throw new BltBadRequestException("This is not a configured interface.",
					BltBadRequestException.UNKNOWN_INTERFACE);
		}
		if (!(routerInterface instanceof P2mpTeTunnel)) {
			throw new BltBadRequestException("This is not a P2MP TE tunnel.",
					BltBadRequestException.UNKNOWN_INTERFACE);
		}
		if (!routerInterface.getName().equals(rsTunnel.getName())) {
			throw new BltBadRequestException("The interface name isn't correct.",
					BltBadRequestException.UNKNOWN_INTERFACE);
		}

		InterfaceConfiguration interfaceConfig = rsTunnel.toInterfaceConfig(router);
		InterfaceConfigurationTable interfaceConfigTable = null;
		if (!interfaceConfig.isBlank()) {
			interfaceConfigTable = new InterfaceConfigurationTable();
			interfaceConfigTable.addInterfaceConfiguration(interfaceConfig);
		}
		MulticastRouting multicastRouting = rsTunnel.toMulticastConfig(router);

		if (interfaceConfigTable == null && multicastRouting == null) {
			throw new BltBadRequestException("Nothing to change.",
					BltBadRequestException.INVALID_INTERFACE_PARAMS);
		}
		
		logUserAction("The user {} is editing the P2MP TE tunnel {} on router {}.",
				getCurrentUserName(), routerInterface.getName(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Edit P2MP TE tunnel", router, interfaceConfigTable, multicastRouting);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}


	@DELETE
	@Path("networks/{nid}/routers/{rid}/interfaces/{id}")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task deleteRouterInterface(@PathParam("nid") Long nid,
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
		if (routerInterface.getType() != RouterInterfaceType.CONFIGURED) {
			throw new BltBadRequestException("This is not a configured interface.",
					BltBadRequestException.UNKNOWN_INTERFACE);
		}
		InterfaceConfiguration interfaceConfig = new InterfaceConfiguration(
				routerInterface.getName());
		interfaceConfig.setDelete(true);
		InterfaceConfigurationTable interfaceConfigTable = new InterfaceConfigurationTable();
		interfaceConfigTable.addInterfaceConfiguration(interfaceConfig);
		
		MulticastInterface multicastInterface = new MulticastInterface(routerInterface.getName());
		multicastInterface.setDelete(true);
		MulticastVrfIpv4 vrf = new MulticastVrfIpv4();
		vrf.addMulticastInterface(multicastInterface);
		MulticastRouting multicastRouting = new MulticastRouting();
		multicastRouting.setDefaultVrf(new MulticastDefaultVrf(vrf));
		
		logUserAction("The user {} is deleting the interface {} from router {}.", getCurrentUserName(),
				routerInterface.getName(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Delete interface", router, interfaceConfigTable, multicastRouting);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}

	@GET
	@Path("networks/{nid}/routers/{rid}/interfaces/{id}/igmpstaticgroups")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Ipv4StaticGroup> getRouterInterfaceIgmpStaticGroups(@PathParam("nid") Long nid,
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
		return routerInterface.getIgmpStaticGroups();
	}
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsInterfaceWithIgmpStaticGroups {
		
		private RouterInterface routerInterface;
		
		public RsInterfaceWithIgmpStaticGroups(RouterInterface routerInterface) {
			this.routerInterface = routerInterface;
		}
		protected RsInterfaceWithIgmpStaticGroups() {
		}
		
		@XmlElement
		public RouterInterface getRouterInterface() {
			return this.routerInterface;
		}
		public void setRouterInterface(RouterInterface routerInterface) {
			this.routerInterface = routerInterface;
		}
		
		@XmlElement
		public List<Ipv4StaticGroup> getStaticGroups() {
			return this.routerInterface.getIgmpStaticGroups();
		}
		public void setStaticGroups() {
		}
		
	}

	@GET
	@Path("networks/{nid}/routers/{rid}/interfaces/withigmpstaticgroups")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<RsInterfaceWithIgmpStaticGroups> getRouterInterfacesWithIgmpStaticGroups(@PathParam("nid") Long nid,
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
		List<RsInterfaceWithIgmpStaticGroups> routerInterfaces = new ArrayList<RsInterfaceWithIgmpStaticGroups>();
		for (RouterInterface routerInterface : router.getRouterInterfaces()) {
			if (routerInterface.getIgmpStaticGroups().size() > 0) {
				routerInterfaces.add(new RsInterfaceWithIgmpStaticGroups(routerInterface));
			}
		}
		return routerInterfaces;
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsInterfaceIgmpStaticGroup {

		private String group;
		private String source;

		@XmlElement
		public String getGroup() {
			return group;
		}
		public void setGroup(String group) {
			this.group = group;
		}
		@XmlElement
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}

	}

	@POST
	@Path("networks/{nid}/routers/{rid}/interfaces/{id}/igmpstaticgroups")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task addRouterInterfaceIgmpStaticGroup(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, @PathParam("id") Long id,
			RsInterfaceIgmpStaticGroup rsGroup) throws WebApplicationException {
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
		if (rsGroup.getGroup() == null) {
			throw new BltBadRequestException("You must provide a group IP address.",
					BltBadRequestException.INVALID_IPV4_ADDRESS);
		}
		if (rsGroup.getSource() == null) {
			throw new BltBadRequestException("You must provide a source IP address.",
					BltBadRequestException.INVALID_IPV4_ADDRESS);
		}

		Ipv4Subnet group;
		Ipv4Subnet source;
		try {
			group = new Ipv4Subnet(rsGroup.getGroup());
			if (group.getPrefixLength() != 32) {
				throw new BltBadRequestException("The group must be a /32.",
						BltBadRequestException.INVALID_IPV4_ADDRESS);
			}
			if (!group.isMulticast()) {
				throw new BltBadRequestException("The group must be a multicast IP address.",
						BltBadRequestException.INVALID_IPV4_ADDRESS);
			}
		}
		catch (BltBadRequestException e) {
			throw e;
		}
		catch (Exception e) {
			throw new BltBadRequestException("Invalid group IP address.",
					BltBadRequestException.INVALID_IPV4_ADDRESS);
		}
		try {
			source = new Ipv4Subnet(rsGroup.getSource());
			if (source.getPrefixLength() != 32) {
				throw new BltBadRequestException("The source must be a /32.",
						BltBadRequestException.INVALID_IPV4_ADDRESS);
			}
			if (!source.isNormalUnicast()) {
				throw new BltBadRequestException("The source must be an unicast IP address.",
						BltBadRequestException.INVALID_IPV4_ADDRESS);
			}
		}
		catch (BltBadRequestException e) {
			throw e;
		}
		catch (Exception e) {
			throw new BltBadRequestException("Invalid source IP address.",
					BltBadRequestException.INVALID_IPV4_ADDRESS);
		}

		IgmpInterface igmpInterface = new IgmpInterface(routerInterface.getName());
		igmpInterface.addStaticGroup(new IgmpStaticGroup(group, null, source));
		RouterIgmp routerIgmp = new RouterIgmp();
		routerIgmp.getDefaultVrf().addIgmpInterface(igmpInterface);
		
		logUserAction("The user {} is adding the IGMP static group {} to interface {} on router {}.",
				getCurrentUserName(), group, routerInterface.getName(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Add IGMP static group", router, routerIgmp);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}

	@DELETE
	@Path("networks/{nid}/routers/{rid}/interfaces/{id}/igmpstaticgroups/{gid}")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task deleteRouterInterfaceIgmpStaticGroup(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, @PathParam("id") Long id,
			@PathParam("gid") Long gid) throws WebApplicationException {
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
		Ipv4StaticGroup group = routerInterface.getIgmpStaticGroupById(gid);
		if (group == null) {
			throw new BltBadRequestException("The group doesn't exist.",
					BltBadRequestException.UNKNOWN_IPV4_STATIC_GROUP);
		}


		IgmpInterface igmpInterface = new IgmpInterface(routerInterface.getName());
		IgmpStaticGroup igmpStaticGroup = new IgmpStaticGroup(group.getGroup(), group.getGroupMask(), group.getSource());
		igmpStaticGroup.setDelete(true);
		igmpInterface.addStaticGroup(igmpStaticGroup);
		RouterIgmp routerIgmp = new RouterIgmp();
		routerIgmp.getDefaultVrf().addIgmpInterface(igmpInterface);
		
		logUserAction("The user {} is deleting the IGMP static group {} from router {}.", getCurrentUserName(),
				routerInterface.getName(), group.getGroup(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Delete IGMP static group", router, routerIgmp);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}



	@GET
	@Path("networks/{nid}/routers/{rid}/explicitpaths")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<TePath> getRouterPaths(@PathParam("nid") Long nid,
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
		return router.getExplicitPaths();
	}


	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)	
	public static class RsPath {

		private String name = null;

		private List<String> hops = new ArrayList<String>();

		@XmlElement
		public String getName() {
			return name;
		}

		public void setName(String name) {
			if (name != null && name.matches("[A-Za-z0-9\\-_]+")) {
				this.name = name;
			}
		}

		@XmlElement
		public List<String> getHops() {
			return hops;
		}

		public void setHops(List<String> hops) {
			this.hops = hops;
		}
	}

	private static Ipv4Subnet parseAddress(String address) throws BltBadRequestException {
		Ipv4Subnet subnet;
		try {
			subnet = new Ipv4Subnet(address);
		}
		catch (Exception e) {
			throw new BltBadRequestException(String.format("Invalid IP address '%s'.",
					address), BltBadRequestException.INVALID_IPV4_ADDRESS);
		}
		if (subnet.getPrefixLength() != 32) {
			throw new BltBadRequestException(String.format(
					"Invalid IP address %s, it must be a single (/32) IP address.", subnet.toString()),
					BltBadRequestException.INVALID_IPV4_ADDRESS);
		}
		return subnet;
	}

	private static org.blt.netconf.message.Path parsePath(RsPath rsPath)
			throws WebApplicationException {
		org.blt.netconf.message.Path path = new org.blt.netconf.message.Path(rsPath.getName());
		Pattern hopPattern = Pattern.compile("(?<index>[0-9]+) (?<ip>[0-9\\.]+) *(?<loose>loose)?");
		for (String rsHop : rsPath.getHops()) {
			try {
				Matcher m = hopPattern.matcher(rsHop);
				if (m.matches()) {
					Hop hop = new Hop(Integer.parseInt(m.group("index")), parseAddress(m.group("ip")).getIp());
					HopType hopType = ("loose".equals(m.group("loose")) ? HopType.NEXTLOOSE : HopType.NEXTSTRICT);
					hop.setHopType(hopType);
					path.addHop(hop);
					continue;
				}
			}
			catch (Exception e) {
			}
			throw new BltBadRequestException("Invalid hop.",
					BltBadRequestException.INVALID_EXPLICITPATH_PARAMS);
		}
		if (path.getHops().size() == 0) {
			throw new BltBadRequestException("The path must contain at least one hop.",
					BltBadRequestException.INVALID_EXPLICITPATH_PARAMS);
		}
		return path;
	}

	@POST
	@Path("networks/{nid}/routers/{rid}/explicitpaths")
	@RolesAllowed("readwrite")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task addExplicitPath(@PathParam("nid") Long nid, @PathParam("rid") Long rid,
			RsPath rsPath) throws WebApplicationException {

		if (rsPath.getName() == null) {
			throw new BltBadRequestException("Invalid name for the explicit path.",
					BltBadRequestException.INVALID_EXPLICITPATH_NAME);
		}

		if (rsPath.getHops() == null || rsPath.getHops().size() == 0) {
			throw new BltBadRequestException("The explicit path must contain at least one hop.",
					BltBadRequestException.INVALID_EXPLICITPATH_PARAMS);
		}

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

		for (TePath tePath : router.getExplicitPaths()) {
			if (rsPath.getName().equals(tePath.getName())) {
				throw new BltBadRequestException("An explicit path of this name already exists.",
						BltBadRequestException.INVALID_EXPLICITPATH_NAME);
			}
		}
		


		IPExplicitPaths explicitPaths = new IPExplicitPaths();
		org.blt.netconf.message.Path path = parsePath(rsPath);
		explicitPaths.addPath(path);
		
		logUserAction("The user {} is adding an explicit path of name {} to router {}.",
				getCurrentUserName(), rsPath.getName(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Add explicit path", router, explicitPaths);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}

	@DELETE
	@Path("networks/{nid}/routers/{rid}/explicitpaths/{id}")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task deleteExplicitPath(@PathParam("nid") Long nid, @PathParam("rid") Long rid,
			@PathParam("id") Long id) throws WebApplicationException {

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
		TePath tePath = router.getExplicitPathById(id);
		if (tePath == null) {
			throw new BltBadRequestException("The explicit path doesn't exist.",
					BltBadRequestException.UNKNOWN_EXPLICITPATH);
		}

		IPExplicitPaths explicitPaths = new IPExplicitPaths();
		org.blt.netconf.message.Path path = new org.blt.netconf.message.Path(tePath.getName());
		path.setDelete(true);
		explicitPaths.addPath(path);
		
		logUserAction("The user {} is deleting the explicit path of name {} from router {}.",
				getCurrentUserName(), tePath.getName(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Delete explicit path", router, explicitPaths);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}

	}

	@PUT
	@Path("networks/{nid}/routers/{rid}/explicitpaths/{id}")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task editExplicitPath(@PathParam("nid") Long nid, @PathParam("rid") Long rid,
			@PathParam("id") Long id, RsPath rsPath) throws WebApplicationException {

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
		TePath tePath = router.getExplicitPathById(id);
		if (tePath == null) {
			throw new BltBadRequestException("The explicit path doesn't exist.",
					BltBadRequestException.UNKNOWN_EXPLICITPATH);
		}

		IPExplicitPaths explicitPaths = new IPExplicitPaths();
		{
			org.blt.netconf.message.Path path = new org.blt.netconf.message.Path(tePath.getName());
			path.setDelete(true);
			explicitPaths.addPath(path);
		}
		{
			org.blt.netconf.message.Path path = parsePath(rsPath);
			path.getNaming().setName(tePath.getName());
			explicitPaths.addPath(path);
		}
		
		logUserAction("The user {} is editing the explicit path {} on router {}.",
				getCurrentUserName(), tePath.getName(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Edit explicit path", router, explicitPaths);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}

	}	

	@GET
	@Path("networks/{nid}/routers/{rid}/ipv4accesslists")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Ipv4AccessList> getRouterIpv4AccessLists(@PathParam("nid") Long nid,
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
		return router.getIpv4AccessLists();
	}*/

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

	@GET
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
	}
/*
	@POST
	@Path("networks/{nid}/routers/{rid}/ipv4staticroutes")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task addRouterIpv4StaticRoute(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, RsIpv4StaticRoute rsRoute) throws WebApplicationException {

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
		Ipv4Subnet subnet;
		try {
			subnet = new Ipv4Subnet(rsRoute.getSubnet());
		}
		catch (MalformedIpv4SubnetException e) {
			throw new BltBadRequestException("Invalid subnet.",
					BltBadRequestException.INVALID_IPV4_SUBNET);
		}
		VrfPrefix vrfPrefix = null;

		try {
			Ipv4Subnet nextHop = new Ipv4Subnet(rsRoute.getNext());
			if (nextHop.getPrefixLength() == 32 && nextHop.isNormalUnicast()) {
				vrfPrefix = new VrfPrefix(subnet, nextHop);
			}
		}
		catch (MalformedIpv4SubnetException e) {
		}
		if (vrfPrefix == null) {
			for (RouterInterface routerInterface : router.getRouterInterfaces()) {
				if (routerInterface.getName().equals(rsRoute.getNext())) {
					vrfPrefix = new VrfPrefix(subnet, rsRoute.getNext());
					break;
				}
			}
		}
		if (vrfPrefix == null) {
			throw new BltBadRequestException("Invalid next hop IP address or interface.",
					BltBadRequestException.INVALID_IPV4_ROUTE);
		}

		RouterStatic routerStatic = new RouterStatic();
		routerStatic.setDefaultVrf(new DefaultVrf(new VrfIpv4(new VrfUnicast(vrfPrefix))));
		
		logUserAction("The user {} is adding an IPv4 static route {} to router {}.",
				getCurrentUserName(), subnet, router.getName());

		try {
			Task task = new NetconfConfigurationTask("Add a static route", router, routerStatic);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}

	}

	@DELETE
	@Path("networks/{nid}/routers/{rid}/ipv4staticroutes/{id}")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task deleteRouterIpv4StaticRoute(@PathParam("nid") Long nid,
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
		Ipv4Route route = router.getIpv4StaticRouteById(id);
		if (route == null) {
			throw new BltBadRequestException("The route doesn't exist.",
					BltBadRequestException.UNKNOWN_IPV4_ROUTE);
		}

		VrfPrefix vrfPrefix;
		if (route.getNextHop() != null) {
			vrfPrefix = new VrfPrefix(route.getSubnet(), route.getNextHop());
		}
		else {
			vrfPrefix = new VrfPrefix(route.getSubnet(), route.getNextInterface());
		}
		vrfPrefix.getVrfRoute().getVrfNextHopTable().get(0).setDelete(true);

		RouterStatic routerStatic = new RouterStatic();
		routerStatic.setDefaultVrf(new DefaultVrf(new VrfIpv4(new VrfUnicast(vrfPrefix))));
		
		logUserAction("The user {} is deleting the IPv4 static route {} from router {}.",
				getCurrentUserName(), route.getSubnet(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Delete static route", router, routerStatic);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}

	}

	@GET
	@Path("networks/{nid}/routers/{rid}/ipv4reversestaticroutes")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Ipv4Route> getRouterIpv4StaticReverseRoutes(@PathParam("nid") Long nid,
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
		return router.getIpv4StaticReverseRoutes();
	}

	@POST
	@Path("networks/{nid}/routers/{rid}/ipv4reversestaticroutes")
	@RolesAllowed("readwrite")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task addRouterIpv4StaticReverseRoute(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, RsIpv4StaticRoute rsRoute) throws WebApplicationException {
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
		Ipv4Subnet subnet;
		try {
			subnet = new Ipv4Subnet(rsRoute.getSubnet());
			if (subnet.getPrefixLength() != 32) {
				throw new BltBadRequestException("Only /32 RPF routes can be configured.",
						BltBadRequestException.INVALID_IPV4_REVERSE_ROUTE);
			}
		}
		catch (MalformedIpv4SubnetException e) {
			throw new BltBadRequestException("Invalid subnet.",
					BltBadRequestException.INVALID_IPV4_REVERSE_ROUTE);
		}
		Ipv4Subnet neighbor;
		try {
			neighbor = new Ipv4Subnet(rsRoute.getNext());
			if (subnet.getPrefixLength() != 32) {
				throw new BltBadRequestException("The RPF neighbor should be a /32 IP address.",
						BltBadRequestException.INVALID_IPV4_REVERSE_ROUTE);
			}
		}
		catch (MalformedIpv4SubnetException e) {
			throw new BltBadRequestException("Invalid RPF neighbor address.",
					BltBadRequestException.INVALID_IPV4_REVERSE_ROUTE);
		}
		for (Ipv4Route existingRoute : router.getIpv4StaticReverseRoutes()) {
			if (existingRoute.getSubnet().equals(subnet)) {
				throw new BltBadRequestException("A RPF route already exists for this subnet.",
						BltBadRequestException.INVALID_IPV4_REVERSE_ROUTE);
			}
		}

		StaticRpfRule staticRpfRule = new StaticRpfRule(subnet, neighbor);
		MulticastVrfIpv4 vrf = new MulticastVrfIpv4();
		vrf.addStaticRpfRule(staticRpfRule);
		MulticastRouting multicastRouting = new MulticastRouting();
		multicastRouting.setDefaultVrf(new MulticastDefaultVrf(vrf));
		
		logUserAction("The user {} is adding an IPv4 static reverse route {} to router {}.",
				getCurrentUserName(), subnet, router.getName());

		try {
			Task task = new NetconfConfigurationTask("Add RPF static route", router, multicastRouting);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}

	@DELETE
	@Path("networks/{nid}/routers/{rid}/ipv4reversestaticroutes/{id}")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task deleteRouterIpv4StaticReverseRoute(@PathParam("nid") Long nid,
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
		Ipv4Route route = router.getIpv4StaticReverseRouteById(id);
		if (route == null) {
			throw new BltBadRequestException("The reverse route doesn't exist.",
					BltBadRequestException.UNKNOWN_IPV4_REVERSE_ROUTE);
		}

		StaticRpfRule staticRpfRule = new StaticRpfRule(route.getSubnet(), null);
		staticRpfRule.setDelete(true);
		MulticastVrfIpv4 vrf = new MulticastVrfIpv4();
		vrf.addStaticRpfRule(staticRpfRule);
		MulticastRouting multicastRouting = new MulticastRouting();
		multicastRouting.setDefaultVrf(new MulticastDefaultVrf(vrf));
		
		logUserAction("The user {} is deleting the IPv4 static reverse route {} from router {}.",
				getCurrentUserName(), route.getSubnet(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Delete RPF static route", router, multicastRouting);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}


	@GET
	@Path("networks/{nid}/routers/{rid}/policymaps")
	@RolesAllowed("readonly")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<RsText> getRouterPolicyMaps(@PathParam("nid") Long nid,
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
		return wrapTextList(router.getPolicyMaps());
	}



	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)	
	public static class RsIpv4SsmRsvpGroup {

		private String group;
		private boolean ssm = true;
		private boolean rsvp = true;

		@XmlElement
		public String getGroup() {
			return group;
		}
		public void setGroup(String group) {
			this.group = group;
		}
		
		@XmlElement
		public boolean isSsm() {
			return ssm;
		}
		public void setSsm(boolean ssm) {
			this.ssm = ssm;
		}
		@XmlElement
		public boolean isRsvp() {
			return rsvp;
		}
		public void setRsvp(boolean rsvp) {
			this.rsvp = rsvp;
		}
	}




	private org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessList completeAcl(
			Router router, String aclName, Ipv4Subnet group) throws BltBadRequestException {

		for (Ipv4AccessList acl : router.getIpv4AccessLists()) {
			if (acl.getName().equals(router.getIpv4MulticastSsmRangeAcl())) {
				int lastIndex = 0;
				for (Ipv4AccessListEntry entry : acl.getEntries()) {
					if (entry.getDestination() != null && entry.getDestination().equals(group) &&
							entry.getProtocol() == AclProtocol.IPV4) {
						return null;
					}
					lastIndex = Math.max(lastIndex, entry.getIndex());
				}
				org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessListEntry ncEntry =
						new org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessListEntry(lastIndex + 10);
				try {
					ncEntry.setAceRule(new Ipv4AceRule(group));
				}
				catch (MalformedIpv4SubnetException e) {
					throw new BltBadRequestException("Unable to prepare ACL update.",
							BltBadRequestException.INVALID_IPV4_ADDRESS);
				}
				org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessList ncAcl =
						new org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessList(aclName);
				ncAcl.addEntry(ncEntry);
				return ncAcl;
			}
		}
		throw new BltBadRequestException(
				String.format("Cannot find the ACL '%s' among the configured ACLs.", aclName),
				BltBadRequestException.UNKNOWN_IPV4_ACCESS_LIST);
	}

	@POST
	@Path("networks/{nid}/routers/{rid}/ipv4ssmrsvpgroups")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task addRouterIpv4SsmRsvpGroup(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, RsIpv4SsmRsvpGroup rsGroup) throws WebApplicationException {
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
		if (rsGroup.getGroup() == null) {
			throw new BltBadRequestException("Please provide the group information.",
					BltBadRequestException.INVALID_IPV4_STATIC_GROUP);
		}
		
		if (!rsGroup.isRsvp() && !rsGroup.isSsm()) {
			throw new BltBadRequestException("Please select at least RSVP or SSM.",
					BltBadRequestException.INVALID_IPV4_STATIC_GROUP);
		}

		Ipv4AclsPrefixLists acls = null;

		try {
			Ipv4Subnet group = new Ipv4Subnet(rsGroup.getGroup());
			if ((group.getPrefixLength() != 32 && rsGroup.isRsvp()) || !group.isMulticast()) {
				throw new BltBadRequestException("The group should be a /32 multicast IP.",
						BltBadRequestException.INVALID_IPV4_REVERSE_ROUTE);
			}
			if (rsGroup.isSsm() && router.getIpv4MulticastSsmRangeAcl() != null &&
					!router.getIpv4MulticastSsmRangeAcl().equals("")) {
				org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessList updateSsmAcl =
						completeAcl(router, router.getIpv4MulticastSsmRangeAcl(), group);
				if (updateSsmAcl != null) {
					if (acls == null) {
						acls = new Ipv4AclsPrefixLists();
					}
					acls.addAccessList(updateSsmAcl);
				}
			}
			if (rsGroup.isRsvp() && router.getIpv4MulticastCoreTreeRsvpTeAcl() != null &&
					!router.getIpv4MulticastCoreTreeRsvpTeAcl().equals("")) {
				org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessList updateRsvpAcl =
						completeAcl(router, router.getIpv4MulticastCoreTreeRsvpTeAcl(), group);
				if (updateRsvpAcl != null) {
					if (acls == null) {
						acls = new Ipv4AclsPrefixLists();
					}
					acls.addAccessList(updateRsvpAcl);
				}
			}

		}
		catch (MalformedIpv4SubnetException e) {
			throw new BltBadRequestException("Invalid group.",
					BltBadRequestException.INVALID_IPV4_REVERSE_ROUTE);
		}
		
		if (acls == null) {
			throw new BltBadRequestException(
					"You must pre-configure the router with a multicast core tree protocol ACL and an SSM ACL.",
					BltBadRequestException.INVALID_NETWORK_PARAMS);
		}

		logUserAction("The user {} is adding the group {} to SSM and RSVP to router {}.",
				getCurrentUserName(), rsGroup.getGroup(), router.getName());

		try {
			Task task = new NetconfConfigurationTask("Add SSM and RSVP-TE multicast group", router, acls);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}


	private org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessList cleanupAcl(
			Router router, String aclName, Ipv4Subnet group) throws BltBadRequestException {

		for (Ipv4AccessList acl : router.getIpv4AccessLists()) {
			if (acl.getName().equals(aclName)) {
				for (Ipv4AccessListEntry entry : acl.getEntries()) {
					if (entry.getDestination() != null && entry.getDestination().equals(group) &&
							entry.getProtocol() == AclProtocol.IPV4) {

						org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessListEntry ncEntry =
								new org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessListEntry(entry.getIndex());
						ncEntry.setDelete(true);
						org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessList ncAcl =
								new org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessList(aclName);
						ncAcl.addEntry(ncEntry);
						return ncAcl;

					}
				}
				return null;
			}
		}
		throw new BltBadRequestException(
				String.format("Cannot find the ACL '%s' among the configured ACLs.", aclName),
				BltBadRequestException.UNKNOWN_IPV4_ACCESS_LIST);
	}



	@DELETE
	@Path("networks/{nid}/routers/{rid}/ipv4ssmrsvpgroups/{group}")
	@RolesAllowed("readwrite")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Task deleteRouterIpv4SsmRsvpGroup(@PathParam("nid") Long nid,
			@PathParam("rid") Long rid, @PathParam("group") String rsGroup) throws WebApplicationException {
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
		if (rsGroup == null) {
			throw new BltBadRequestException("The group must be provided.",
					BltBadRequestException.INVALID_IPV4_STATIC_GROUP);
		}

		Ipv4AclsPrefixLists acls = null;

		try {
			Ipv4Subnet group = new Ipv4Subnet(rsGroup.replace("-", "/"));
			if (!group.isMulticast()) {
				throw new BltBadRequestException("The group should be a multicast IP.",
						BltBadRequestException.INVALID_IPV4_REVERSE_ROUTE);
			}
			if (router.getIpv4MulticastSsmRangeAcl() != null &&
					!router.getIpv4MulticastSsmRangeAcl().equals("")) {
				org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessList updateSsmAcl =
						cleanupAcl(router, router.getIpv4MulticastSsmRangeAcl(), group);
				if (updateSsmAcl != null) {
					if (acls == null) {
						acls = new Ipv4AclsPrefixLists();
					}
					acls.addAccessList(updateSsmAcl);
				}
			}
			if (router.getIpv4MulticastCoreTreeRsvpTeAcl() != null &&
					!router.getIpv4MulticastCoreTreeRsvpTeAcl().equals("")) {
				org.blt.netconf.message.Ipv4AclsPrefixLists.Ipv4AccessList updateRsvpAcl =
						cleanupAcl(router, router.getIpv4MulticastCoreTreeRsvpTeAcl(), group);
				if (updateRsvpAcl != null) {
					if (acls == null) {
						acls = new Ipv4AclsPrefixLists();
					}
					acls.addAccessList(updateRsvpAcl);
				}
			}

		}
		catch (MalformedIpv4SubnetException e) {
			throw new BltBadRequestException("Invalid group.",
					BltBadRequestException.INVALID_IPV4_REVERSE_ROUTE);
		}
		
		if (acls == null || acls.getAccessLists().isEmpty()) {
			throw new BltBadRequestException("No match, no ACL would be updated.",
					BltBadRequestException.INVALID_IPV4_STATIC_GROUP);
		}

		logUserAction("The user {} is deleting the IPv4 SSM/RSVP group {} from router {}.",
				getCurrentUserName(), rsGroup, router.getName());

		try {
			Task task = new NetconfConfigurationTask("Delete SSM and RSVP-TE multicast group", router, acls);
			task.schedule(0);
			return task;
		}
		catch (SchedulerException e) {
			throw new BltBadRequestException("Unable to schedule the configuration task.",
					BltBadRequestException.SCHEDULE_ERROR);
		}
	}
*/
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
		/*else {
			user = Radius.authenticate(rsLogin.getUsername(), rsLogin.getPassword());
		}*/
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
	
	
	/*@GET
	@Path("licenses")
	@RolesAllowed("admin")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Set<License> getLicenses() throws WebApplicationException {
		return TopologyService.getService().getLicenses();
	}
	
	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class RsLicense {
		
		private String text;

		@XmlElement
		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}
	
	@POST
	@Path("licenses")
	@RolesAllowed("admin")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void addLicense(RsLicense rsLicense) {
		if (rsLicense.getText() == null) {
			throw new BltBadRequestException("The license can't be empty.",
					BltBadRequestException.INVALID_LICENSE);
		}
		License license = License.fromString(rsLicense.getText());
		if (license == null || license.getValidity() == Validity.INCOMPLETE) {
			throw new BltBadRequestException("This is not a valid license.",
					BltBadRequestException.INVALID_LICENSE);
		}
		if (license.getValidity() == Validity.NONGENUINE) {
			throw new BltBadRequestException("The license's signature is not valid.",
					BltBadRequestException.INVALID_LICENSE);
		}
		if (TopologyService.getService().containsLicense(license)) {
			throw new BltBadRequestException("This license is already registered on the server.",
					BltBadRequestException.INVALID_LICENSE);
		}
		TopologyService.getService().addLicense(license);
	}
	
	@DELETE
	@Path("licenses/{id}")
	@RolesAllowed("admin")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteLicense(@PathParam("id") Long id) throws WebApplicationException {
		License license = TopologyService.getService().getLicenseById(id);
		if (license == null) {
			throw new BltBadRequestException("The license doesn't exist.",
					BltBadRequestException.UNKNOWN_LICENSE);
		}
		TopologyService.getService().removeLicense(license);
	}*/
	
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
			/*this.licensed = TopologyService.getService().isLicensed();
			try {
				this.machine = License.computeMachineUuid().toString();
			}
			catch (Exception e) {
			}*/
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
