/**
 * 
 */
package onl.netfishers.blt.bgp.config.nodes;

import java.util.Set;

/**
 * @author rainer
 *
 */
public interface RoutingPeerConfiguration extends Comparable<RoutingPeerConfiguration> {

	/**
	 * get the peer name.
	 * 
	 * @return
	 */
	public String getPeerName();

	/**
	 * get the routing configurations specific per AddressFamilyKey
	 * 
	 * @return
	 */
	public Set<AddressFamilyRoutingPeerConfiguration> getAddressFamilyConfigrations();
}
