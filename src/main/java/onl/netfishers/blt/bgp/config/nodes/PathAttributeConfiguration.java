/**
 * 
 */
package onl.netfishers.blt.bgp.config.nodes;

import java.util.Set;

import onl.netfishers.blt.bgp.net.attributes.PathAttribute;

/**
 * @author rainer
 *
 */
public interface PathAttributeConfiguration extends Comparable<PathAttributeConfiguration>{

	/**
	 * 
	 * @return
	 */
	public Set<PathAttribute> getAttributes();
}
