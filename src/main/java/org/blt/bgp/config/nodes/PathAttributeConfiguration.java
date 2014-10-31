/**
 * 
 */
package org.blt.bgp.config.nodes;

import java.util.Set;

import org.blt.bgp.net.attributes.PathAttribute;

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
