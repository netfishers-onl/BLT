/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
/**
 * 
 */
package onl.netfishers.blt.bgp.config.nodes;

/**
 * @author rainer
 *
 */
public interface RoutingFilterConfiguration extends	Comparable<RoutingFilterConfiguration> {

	/**
	 * get the routing filter name
	 * 
	 * @return
	 */
	public String getName();
	
}
