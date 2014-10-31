/**
 * 
 */
package org.blt.bgp.config.nodes.impl;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.blt.bgp.config.nodes.PathAttributeConfiguration;
import org.blt.bgp.config.nodes.RouteConfiguration;
import org.blt.bgp.net.NetworkLayerReachabilityInformation;

/**
 * @author rainer
 *
 */
public class RouteConfigurationImpl implements RouteConfiguration {

	private NetworkLayerReachabilityInformation nlri;
	private PathAttributeConfiguration pathAttributes;
	
	public RouteConfigurationImpl() {}

	public RouteConfigurationImpl(NetworkLayerReachabilityInformation nlri, PathAttributeConfiguration pathAttributes) {
		this.nlri = nlri;
		this.pathAttributes = pathAttributes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RouteConfiguration o) {
		return (new CompareToBuilder())
				.append(getNlri(), o.getNlri())
				.append(getPathAttributes(), o.getPathAttributes())
				.toComparison();
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.nodes.RouteConfiguration#getNlri()
	 */
	@Override
	public NetworkLayerReachabilityInformation getNlri() {
		return this.nlri;
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.nodes.RouteConfiguration#getPathAttributes()
	 */
	@Override
	public PathAttributeConfiguration getPathAttributes() {
		return this.pathAttributes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(getNlri())
				.append(getPathAttributes())
				.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RouteConfiguration))
			return false;
		
		RouteConfiguration o = (RouteConfiguration)obj;
		
		return (new EqualsBuilder())
				.append(getNlri(), o.getNlri())
				.append(getPathAttributes(), o.getPathAttributes())
				.isEquals();
	}

	/**
	 * @param nlri the nlri to set
	 */
	void setNlri(NetworkLayerReachabilityInformation nlri) {
		this.nlri = nlri;
	}

	/**
	 * @param pathAttributes the pathAttributes to set
	 */
	void setPathAttributes(PathAttributeConfiguration pathAttributes) {
		this.pathAttributes = pathAttributes;
	}

}
