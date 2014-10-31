/**
 * 
 */
package org.blt.bgp.config.nodes;

import java.util.Set;

import org.blt.bgp.net.AddressFamilyKey;

/**
 * @author rainer
 *
 */
public interface AddressFamilyRoutingPeerConfiguration extends Comparable<AddressFamilyRoutingPeerConfiguration> {

	/**
	 * get the address family key for this rooting confguration
	 * 
	 * @return
	 */
	public AddressFamilyKey getAddressFamilyKey();
	
	/**
	 * get the path attributes which are inserted into routes injected into the
	 * local-side routing information base of a peer
	 * 
	 * @return
	 */
	public PathAttributeConfiguration getLocalDefaultPathAttributes();
	
	/**
	 * get the filters to be applied on routes before they are inserted into the 
	 * local-side routing information base of a peer
	 * 
	 * @return
	 */
	public Set<RoutingFilterConfiguration> getLocalRoutingFilters();

	/**
	 * get the path attributes which are inserted into routes received from the
	 * remote-side routing information base of a peer
	 * 
	 * @return
	 */
	public PathAttributeConfiguration getRemoteDefaultPathAttributes();

	/**
	 * get the filters to be applied on routes after they have been received from the remote-side
	 * routing information base of a peer
	 * 
	 * @return
	 */
	public Set<RoutingFilterConfiguration> getRemoteRoutingFilters();

}
