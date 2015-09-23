/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
/**
 * 
 */
package onl.netfishers.blt.bgp.config.nodes.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import onl.netfishers.blt.bgp.config.nodes.AddressFamilyRoutingConfiguration;
import onl.netfishers.blt.bgp.config.nodes.RouteConfiguration;
import onl.netfishers.blt.bgp.net.AddressFamilyKey;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author rainer
 *
 */
public class AddressFamilyRoutingConfigurationImpl implements AddressFamilyRoutingConfiguration {

	private AddressFamilyKey key;
	private Set<RouteConfiguration> routes = new TreeSet<RouteConfiguration>();

	AddressFamilyRoutingConfigurationImpl() {}
	
	AddressFamilyRoutingConfigurationImpl(AddressFamilyKey key, Collection<RouteConfiguration> routes) {
		setKey(key);
		
		if(routes != null)
			this.routes.addAll(routes);
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4.config.nodes.AddressFamilyRoutingConfiguration#getKey()
	 */
	@Override
	public AddressFamilyKey getKey() {
		return key;
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.nodes.AddressFamilyRoutingConfiguration#getRoutes()
	 */
	@Override
	public Set<RouteConfiguration> getRoutes() {
		return routes;
	}

	/**
	 * @param key the key to set
	 */
	void setKey(AddressFamilyKey key) {
		this.key = key;
	}

	/**
	 * @param routes the routes to set
	 */
	void setRoutes(Set<RouteConfiguration> routes) {
		this.routes.clear();
		
		if(routes != null)
			this.routes.addAll(routes);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AddressFamilyRoutingConfiguration o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getKey(), o.getKey())
				.append(getRoutes().size(), o.getRoutes().size());
		
		if(builder.toComparison() == 0) {
			Iterator<RouteConfiguration> lit = getRoutes().iterator();
			Iterator<RouteConfiguration> rit = o.getRoutes().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.toComparison();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder()).append(getKey());
		
		for(RouteConfiguration route : getRoutes())
			builder.append(route);
		
		return builder.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AddressFamilyRoutingConfiguration))
			return false;
		
		return (compareTo((AddressFamilyRoutingConfiguration)obj) == 0);
	}

}
