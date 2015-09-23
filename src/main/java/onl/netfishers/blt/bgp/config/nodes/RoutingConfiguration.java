/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
/**
 * 
 */
package onl.netfishers.blt.bgp.config.nodes;

import java.util.Set;

/**
 * @author rainer
 *
 */
public interface RoutingConfiguration extends Comparable<RoutingConfiguration> {

	/**
	 * get the routing confguration per Adress and Subsequent Address family per 
	 * 
	 * @return
	 */
	public Set<AddressFamilyRoutingConfiguration> getRoutingConfigurations();
}
