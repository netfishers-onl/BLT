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
 * File: org.bgp4.config.nodes.impl.RoutingFilterConfigurationImpl.java 
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

import onl.netfishers.blt.bgp.config.nodes.RoutingFilterConfiguration;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author rainer
 *
 */
public abstract class RoutingFilterConfigurationImpl implements RoutingFilterConfiguration {

	private String name;

	protected RoutingFilterConfigurationImpl() {}
	
	protected RoutingFilterConfigurationImpl(String name) {
		setName(name);
	}

	@Override
	public String getName() {
		return name;
	}

	void setName(String name)  {
		this.name = name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(RoutingFilterConfiguration o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getName(), o.getName())
				.append(getType(), ((RoutingFilterConfigurationImpl)o).getType());
		
		if(builder.toComparison() == 0)
			subclassCompareTo(builder, o);
		
		return builder.toComparison();
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.config.nodes.RoutingFilterConfiguration#getName()
	 */


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder())
				.append(getName())
				.append(getType());
		
		subclassHashCode(builder);
		
		return builder.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RoutingFilterConfiguration))
			return false;
		
		RoutingFilterConfiguration o = (RoutingFilterConfiguration)obj;
		
		EqualsBuilder builder = (new EqualsBuilder())
				.append(getName(), o.getName())
				.append(getType(), ((RoutingFilterConfigurationImpl)o).getType());
		
		if(builder.isEquals())
			subclassEquals(builder, o);
		
		return builder.isEquals();
	}
	
	protected abstract RoutingFilterType getType();

	protected abstract void subclassCompareTo(CompareToBuilder builder, RoutingFilterConfiguration o); 

	protected abstract void subclassEquals(EqualsBuilder builder, RoutingFilterConfiguration o); 
	
	protected abstract void subclassHashCode(HashCodeBuilder builder);
}
