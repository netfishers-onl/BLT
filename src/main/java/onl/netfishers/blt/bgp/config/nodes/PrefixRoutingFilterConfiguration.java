/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
/**
 * 
 */
package onl.netfishers.blt.bgp.config.nodes;

import java.util.Set;

import onl.netfishers.blt.bgp.net.NetworkLayerReachabilityInformation;

/**
 * @author rainer
 *
 */
public interface PrefixRoutingFilterConfiguration extends RoutingFilterConfiguration {

	/**
	 * get the route prefixes which are filtered out 
	 * 
	 * @return
	 */
	public Set<NetworkLayerReachabilityInformation> getFilterPrefixes();
}
