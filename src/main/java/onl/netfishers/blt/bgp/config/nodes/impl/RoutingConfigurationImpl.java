/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: org.bgp4.config.nodes.impl.RoutingConfigurationImpl.java 
 */
/**
 * Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
 * 
 * License: same as above
 * 
 * Modified to run as an independent java application, one that does not
 * require webserver or app server
 */
package onl.netfishers.blt.bgp.config.nodes.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import onl.netfishers.blt.bgp.config.nodes.AddressFamilyRoutingConfiguration;
import onl.netfishers.blt.bgp.config.nodes.RoutingConfiguration;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author rainer
 *
 */
public class RoutingConfigurationImpl implements RoutingConfiguration {

	private Set<AddressFamilyRoutingConfiguration> routingConfigurations = new TreeSet<AddressFamilyRoutingConfiguration>();
	
	RoutingConfigurationImpl() {}
	
	RoutingConfigurationImpl(Collection<AddressFamilyRoutingConfiguration> routingConfigurations) {
		this.routingConfigurations.addAll(routingConfigurations);
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4.config.nodes.RoutingConfiguration#getRoutingConfigurations()
	 */
	@Override
	public Set<AddressFamilyRoutingConfiguration> getRoutingConfigurations() {
		return routingConfigurations;
	}

	/**
	 * @param routingConfigurations the routingConfigurations to set
	 */
	void setRoutingConfigurations(Set<AddressFamilyRoutingConfiguration> routingConfigurations) {
		this.routingConfigurations.clear();
		
		if(routingConfigurations != null)
			this.routingConfigurations.addAll(routingConfigurations);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoutingConfiguration o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getRoutingConfigurations().size(), o.getRoutingConfigurations().size());
		
		if(builder.toComparison() == 0) {
			Iterator<AddressFamilyRoutingConfiguration> lit = getRoutingConfigurations().iterator();
			Iterator<AddressFamilyRoutingConfiguration> rit = o.getRoutingConfigurations().iterator();
			
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
		HashCodeBuilder builder = new HashCodeBuilder();
		
		for(AddressFamilyRoutingConfiguration route : getRoutingConfigurations())
			builder.append(route);
		
		return builder.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RoutingConfiguration))
			return false;
		
		return (compareTo((RoutingConfiguration)obj) == 0);
	}

}
