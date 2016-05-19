/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
package onl.netfishers.blt.snmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import onl.netfishers.blt.Blt;
import onl.netfishers.blt.tasks.Task;
import onl.netfishers.blt.topology.net.Ipv4Subnet;
import onl.netfishers.blt.topology.net.Router;
import onl.netfishers.blt.topology.net.RouterInterface;
import onl.netfishers.blt.topology.net.SnmpCommunity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TreeEvent;
import org.snmp4j.util.TreeUtils;

import org.xbill.DNS.LOCRecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;
import org.quartz.SchedulerException;

public class SnmpPollingTask extends Task {

	private static Logger logger = LoggerFactory
			.getLogger(SnmpPollingTask.class);

	protected static final int PORT = 161;

	private static long THROTTLE_TIME = 20000L;
	private static int SNMP_RETRIES = 10;
	private static int SNMP_TIMEOUT = 2000;
	private static boolean LATLONG_LOOKUP = false;
	private static boolean SNMP_SKIP_INTERFACES = false;
	private static String LATLONG_METHOD = "snmp";
	private static String LATLONG_RESOLVER = "";
	private static String LATLONG_DOMAIN_NAME = "";

	static {
		try {
			THROTTLE_TIME = Long.parseLong(Blt.getConfig("blt.snmp.taskthrottletime", "20000"));
		}
		catch (Exception e) {
			logger.warn(
					"Invalid value for Netconf task throttle time (blt.snmp.taskthrottletime), defaulting to {}.",
					THROTTLE_TIME);
		}
		try {
			SNMP_RETRIES = Integer.parseInt(Blt.getConfig("blt.snmp.retries", "10"));
		}
		catch (Exception e) {
			logger.error("Unable to parse the SNMP retries option in configuration, using {}.", SNMP_RETRIES);
		}
		try {
			SNMP_TIMEOUT = Integer.parseInt(Blt.getConfig("blt.snmp.timeout", "2000"));
		}
		catch (Exception e) {
			logger.error("Unable to parse the SNMP timeout option in configuration, using {}.", SNMP_TIMEOUT);
		}
		try {
			SNMP_SKIP_INTERFACES = Boolean.parseBoolean(Blt.getConfig("blt.snmp.skipinterfaces"));
		}
		catch (Exception e){
			logger.error("Unable to parse the SNMP skip interfaces option in configuration, using '{}'.", SNMP_SKIP_INTERFACES);
		}
		try {
			LATLONG_LOOKUP = Boolean.parseBoolean(Blt.getConfig("blt.coordinates.lookup"));
		}
		catch (Exception e) {
			logger.error("Unable to parse the GPS coordinates lookup option in configuration, using '{}'.", LATLONG_LOOKUP);
		}
		if (LATLONG_LOOKUP) {
			try {
				LATLONG_METHOD = String.format(Blt.getConfig("blt.coordinates.collect"));
			}
			catch (Exception e) {
				logger.error("Unable to parse the GPS coordinates fetch method in configuration, using '{}'.", LATLONG_METHOD);
			}
			try {
				LATLONG_RESOLVER = String.format(Blt.getConfig("blt.coordinates.resolver"));
			}
			catch (Exception e) {
				logger.error("Unable to parse the DNS resolver in configuration, using '{}'.", LATLONG_RESOLVER);
			}
			try {
				LATLONG_DOMAIN_NAME = String.format(Blt.getConfig("blt.coordinates.domainname"));
			}
			catch (Exception e) {
				logger.error("Unable to parse the resolver domain name in configuration, using '{}'.", LATLONG_DOMAIN_NAME);
			}
		}
	}

	private Router router;
	protected Snmp snmp;
	protected Target snmpTarget;
	protected TransportMapping<UdpAddress> transport;

	public SnmpPollingTask(String description, Router router) {
		super(description);
		this.router = router;
	}

	protected String get(String oid) throws Exception {
		PDU pdu = new PDU();
		pdu.add(new VariableBinding(new OID(oid)));
		pdu.setType(PDU.GET);
		ResponseEvent event = snmp.send(pdu, snmpTarget);
		PDU response = event.getResponse();
		return response.get(0).getVariable().toString();
	}

	protected Map<String, String> walk(String oid) throws Exception {
		Map<String, String> results = new HashMap<String, String>();
		TreeUtils treeUtils = new TreeUtils(snmp, new DefaultPDUFactory());      
		List<TreeEvent> events = treeUtils.getSubtree(snmpTarget, new OID(oid));
		for (TreeEvent event : events) {
			if (event == null) {
				continue;
			}
			if (event.isError()) {
				logger.warn("Got SNMP error while polling {}: {}", router.getName(), event.getErrorMessage());
				continue;
			}
			VariableBinding[] varBindings = event.getVariableBindings();
			if (varBindings == null) {
				continue;
			}
			for (VariableBinding varBinding : varBindings) {
				String key = varBinding.getOid().toString();
				if (key.startsWith(oid)) {
					key = key.substring(oid.length() + 1);
				}
				String result = varBinding.getVariable().toString();
				results.put(key, result);
			}
		}
		return results;
	}
	
	
	private static final String sysName                     = "1.3.6.1.2.1.1.5.0";
	private static final String sysLocation                 = "1.3.6.1.2.1.1.6.0";
	private static final String ifIndex                     = "1.3.6.1.2.1.2.2.1.1";
	private static final String ifName                      = "1.3.6.1.2.1.31.1.1.1.1";
	private static final String ifAlias                     = "1.3.6.1.2.1.31.1.1.1.18";
	private static final String ipAdEntAddr					= "1.3.6.1.2.1.4.20.1.1";
	private static final String ipAdEntIfIndex				= "1.3.6.1.2.1.4.20.1.2";
	private static final String ipAdEntNetMask				= "1.3.6.1.2.1.4.20.1.3";

	@Override
	public void execute() {
		synchronized (router) {
			
			if (router.isLost()) {
				logger.info("Task '{}' cancelled, because router {} is lost.", this, router);
				this.setStatus(TaskStatus.CANCELLED);
				return;
			}
			if (this.creationTime < router.getLastSnmpPollingTime()) {
				logger.info("Task '{}' cancelled because a similar task has run meanwhile.", this);
				this.setStatus(TaskStatus.CANCELLED);
				return;
			}
			else if (System.currentTimeMillis() < router.getLastSnmpPollingTime() + THROTTLE_TIME) {
				logger.info("Task '{}' rescheduled to wait for throttle time.", this);
				try {
					this.schedule((int) (router.getLastSnmpPollingTime() + THROTTLE_TIME - System.currentTimeMillis() + 100));
					this.setStatus(TaskStatus.WAITING);
					return;
				}
				catch (SchedulerException e) {
					logger.warn("Unable to reschedule task '{}'.", this, e);
				}
			}

			logger.debug("Starting task {}.", this);

			router.setLastSnmpPollingTime();

			SnmpCommunity snmpCommunity = router.findSnmpCommunity();
			if (snmpCommunity == null) {
				logger.warn("No SNMP community found to poll router {}.", router);
			}

			List<RouterInterface> routerInterfaces = new ArrayList<RouterInterface>();

			try {
				this.snmpTarget = new CommunityTarget(new UdpAddress(snmpCommunity.getSubnet().getInetAddress(), PORT),
						new OctetString(snmpCommunity.getCommunity()));
				this.snmpTarget.setVersion(SnmpConstants.version2c);
				this.snmpTarget.setRetries(SNMP_RETRIES);
				this.snmpTarget.setTimeout(SNMP_TIMEOUT);
				transport = new DefaultUdpTransportMapping();
				snmp = new Snmp(transport);
				transport.listen();
				
				if (router.getName().length() == 0 || router.getName().equals("Unknown")) {
					String Name = null;
					try {
						Name = get(sysName); 
					} catch (Exception e1) {
						logger.warn("Error when polling router {} sysName", router, e1);
					}
					router.setName(Name);
				}
				else {
					logger.warn("We already know {}, skippping polling task", router);
				}
				
				String Location = null ;
				try {
					Location = get(sysLocation);
				} catch (Exception e1) {
					logger.warn("Error when polling router {} sysLocation using {}", router, snmpTarget, e1);
				}
				router.setLocation(Location);
				
				if ( LATLONG_LOOKUP) {
					
					Double Latitude = null;
					Double Longitude = null;
					
					if (LATLONG_METHOD.equals("snmp")) {
					   
						Pattern p = Pattern.compile("((-|)\\d+\\.\\d+)[^\\d-]+((-|)\\d+\\.\\d+)");
					    Matcher m = p.matcher(Location);
				        
					    if ( m.find() && m.groupCount() == 4 ) {
				        	Latitude = Double.parseDouble(m.group(1));
							Longitude = Double.parseDouble(m.group(3));
							router.setLatitude(Latitude);
				        	router.setLongitude(Longitude);
				        }  else {
				        	logger.warn("Can't get valid GPS coordinates from '{}' on '{}', Google Maps view will be disabled", Location, router); 
				        }
					}
					else if (LATLONG_METHOD.equals("dns")) {
						if (router.getName() != null) {
							String routerDnsName;
							if (LATLONG_DOMAIN_NAME != "") {
								routerDnsName = router.getName()+"."+LATLONG_DOMAIN_NAME;
							} else {
								routerDnsName = router.getName();
							}
							Lookup lookup = new Lookup(routerDnsName, Type.LOC);
							try{
								lookup.setResolver(new SimpleResolver(LATLONG_RESOLVER));
								Record[] result = lookup.run();
								LOCRecord locEntry = null;
								try {
									locEntry = (LOCRecord) result[0];
									router.setLatitude(locEntry.getLatitude());
						        	router.setLongitude(locEntry.getLongitude());
								} 
								catch (Exception e) {
									logger.warn("cannot get a LOC entry from router '{}' using resolver '{}'", router, LATLONG_RESOLVER);
								}
							} 
							catch (Exception e){
								logger.warn("'{}' does not seem to be a valid DNS resolver", LATLONG_RESOLVER);
							}
							
						} else {
							logger.warn("Cannot get name of router '{}' and then cannot use DNS, Google Maps view will be disabled", router);
						}
					}
					else {
						logger.warn("'{}' is not a valid option for 'blt.coordinates.collect', please check blt.conf",LATLONG_METHOD);
					}	
				}
				
				router.setNeedTeRefresh(true);
				
				if (! SNMP_SKIP_INTERFACES) {
				
					Map<String, String> ifIndices = walk(ifIndex);
					Map<String, String> ifNames = walk(ifName);
					Map<String, String> ifDescriptions = walk(ifAlias);
					Map<String, String> ipAddresses = walk(ipAdEntAddr);
					Map<String, String> ipAddIfIndices = walk(ipAdEntIfIndex);
					Map<String, String> ipAddNetMasks = walk(ipAdEntNetMask);
					
					for (String PrefixOid : ifIndices.keySet()) {
						RouterInterface itf = new RouterInterface();
							
						if (ifNames.get(PrefixOid) == null) {
							logger.warn("No name for interface {} on {}... skipping it.", PrefixOid, router);
							continue;
						}
						itf.setName(ifNames.get(PrefixOid));
						
						if (ifDescriptions.get(PrefixOid) == null) {
							logger.warn("No description for interface {} on {}... ", PrefixOid, router);
						}
						itf.setDescription(ifDescriptions.get(PrefixOid));
						
						for (String ipaddindex : ipAddIfIndices.keySet()) {
							if (ipAddIfIndices.get(ipaddindex).equals(PrefixOid)) {
								itf.setIpv4Address(new Ipv4Subnet((ipAddresses.get(ipaddindex)),(ipAddNetMasks.get(ipaddindex))));
								break ;
							}	
						}
						if ( ! itf.getName().isEmpty() && itf.getIpv4Address() != null) {
							if (router.getRouterInterfaceBySubnet(itf.getIpv4Address()) == null) {
								router.addRouterInterface(itf);
							}
						}
					}
				}
				this.setStatus(TaskStatus.SUCCESS);
			}
			catch (Exception e) {
				logger.warn("Error while polling router {} using {}", router, snmpTarget, e);
			}
			finally {
				try {
					snmp.close();
				}
				catch (Exception e) {
				}
			}
			
			router.setLiveRouterInterfaces(routerInterfaces);

		}
	}

	@Override
	public String toString() {
		return String.format("SNMP Polling task for router %s, creation time %d.",
				router, this.creationTime);
	}

}