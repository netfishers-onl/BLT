/**
 *  Copyright 2012, 2014 Rainer Bieniek (Rainer.Bieniek@web.de)
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
 * File: org.bgp4.config.nodes.impl.RoutingProcessorConfigurationImpl.java 
 */
package onl.netfishers.blt.bgp.config.nodes.impl;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import onl.netfishers.blt.bgp.config.nodes.RoutingInstanceConfiguration;
import onl.netfishers.blt.bgp.config.nodes.RoutingProcessorConfiguration;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author rainer
 *
 */
class RoutingProcessorConfigurationImpl implements RoutingProcessorConfiguration {

	private Set<RoutingInstanceConfiguration> routingInstances = new TreeSet<RoutingInstanceConfiguration>();
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.config.nodes.RoutingProcessorConfiguration#getRoutingInstances()
	 */
	@Override
	public Set<RoutingInstanceConfiguration> getRoutingInstances() {
		return routingInstances;
	}


	/**
	 * @param routingInstances the routingInstances to set
	 */
	void setRoutingInstances(Set<RoutingInstanceConfiguration> routingInstances) {
		this.routingInstances.clear();
		
		if(routingInstances != null)
			this.routingInstances.addAll(routingInstances);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoutingProcessorConfiguration o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getRoutingInstances().size(), o.getRoutingInstances().size());
		
		if(builder.toComparison() == 0) {
			Iterator<RoutingInstanceConfiguration> lit = getRoutingInstances().iterator();
			Iterator<RoutingInstanceConfiguration> rit = o.getRoutingInstances().iterator();
			
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
		
		for(RoutingInstanceConfiguration instance : getRoutingInstances())
			builder.append(instance);
		
		return builder.toHashCode();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RoutingProcessorConfiguration))
			return false;
		
		RoutingProcessorConfiguration o = (RoutingProcessorConfiguration)obj;

		EqualsBuilder builder = (new EqualsBuilder())
				.append(getRoutingInstances().size(), o.getRoutingInstances().size());
		
		if(builder.isEquals()) {
			Iterator<RoutingInstanceConfiguration> lit = getRoutingInstances().iterator();
			Iterator<RoutingInstanceConfiguration> rit = o.getRoutingInstances().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}

		return builder.isEquals();
	}
}
