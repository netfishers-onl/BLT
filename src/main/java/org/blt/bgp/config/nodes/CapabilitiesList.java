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
 * File: org.bgp4.config.nodes.CapabilitiesList.java 
 */
/**
 * Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
 * 
 * License: same as above
 * 
 * Modified to run as an independent java application, one that does not
 * require webserver or app server
 */
package org.blt.bgp.config.nodes;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.blt.bgp.net.capabilities.Capability;

/**
 * @author nitinb
 *
 */
public class CapabilitiesList implements Capabilities {

	private TreeSet<Capability> requiredCapabilities = new TreeSet<Capability>();
	private TreeSet<Capability> optionalCapabilities = new TreeSet<Capability>();

	public CapabilitiesList() {}
	
	public CapabilitiesList(Capability[] requiredCaps) {
		for(Capability cap : requiredCaps) {
			requiredCapabilities.add(cap);
		}
	}

	public CapabilitiesList(Capability[] requiredCaps, Capability[] optionalCaps) {
		for(Capability cap : requiredCaps) {
			requiredCapabilities.add(cap);
		}
		for(Capability cap : optionalCaps) {
			optionalCapabilities.add(cap);
		}
	}

	/* (non-Javadoc)
	 * @see org.blt.bgp.config.nodes.Capabilities#getRequiredCapabilities()
	 */
	@Override
	public Set<Capability> getRequiredCapabilities() {
		return Collections.unmodifiableSet(requiredCapabilities);
	}

	/* (non-Javadoc)
	 * @see org.blt.bgp.config.nodes.Capabilities#getOptionalCapabilities()
	 */
	@Override
	public Set<Capability> getOptionalCapabilities() {
		return Collections.unmodifiableSet(optionalCapabilities);
	}
	
	public void addRequiredCapability(Capability cap) {
		requiredCapabilities.add(cap);
	}
	
	public void addOptionalCapability(Capability cap) {
		optionalCapabilities.add(cap);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Capabilities))
			return false;

		Set<Capability> otherCaps = ((Capabilities)obj).getRequiredCapabilities();
		
		if(otherCaps.size() != requiredCapabilities.size())
			return false;
		
		for(Capability cap : requiredCapabilities)
			if(!otherCaps.contains(cap))
				return false;
		
		otherCaps = ((Capabilities)obj).getOptionalCapabilities();
		
		if(otherCaps.size() != optionalCapabilities.size())
			return false;
		
		for(Capability cap : optionalCapabilities)
			if(!otherCaps.contains(cap))
				return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		
		for(Capability cap : requiredCapabilities)
			hcb.append(cap).append(false);

		for(Capability cap : optionalCapabilities)
			hcb.append(cap).append(true);

		return hcb.toHashCode();
	}
}
