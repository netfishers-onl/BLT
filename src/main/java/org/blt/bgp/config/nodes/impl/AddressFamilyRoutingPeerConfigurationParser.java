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
package org.blt.bgp.config.nodes.impl;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.blt.bgp.BgpService;
import org.blt.bgp.config.nodes.AddressFamilyRoutingPeerConfiguration;
import org.blt.bgp.config.nodes.RoutingFilterConfiguration;
import org.blt.bgp.net.AddressFamily;
import org.blt.bgp.net.AddressFamilyKey;
import org.blt.bgp.net.SubsequentAddressFamily;


/**
 * @author rainer
 *
 */
public class AddressFamilyRoutingPeerConfigurationParser {

	private RoutingFilterConfigurationParser filterParser;
	private PathAttributeConfigurationParser pathAttributeParser;
	
    public AddressFamilyRoutingPeerConfigurationParser () {
        filterParser = new RoutingFilterConfigurationParser();
        pathAttributeParser = (PathAttributeConfigurationParser)BgpService.getInstance(PathAttributeConfigurationParser.class.getName());
    }

	AddressFamilyRoutingPeerConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		AddressFamilyRoutingPeerConfigurationImpl result = new AddressFamilyRoutingPeerConfigurationImpl();
		String addressFamily = config.getString("[@addressFamily]");
		String subsequentAddressFamily = config.getString("[@subsequentAddressFamily]");
		HierarchicalConfiguration localFilterConfiguration = first(config, "Local.Filters");
		HierarchicalConfiguration remoteFilterConfiguration = first(config, "Remote.Filters");
		HierarchicalConfiguration localPathAttributes = first(config, "Local.DefaultPathAttributes");
		HierarchicalConfiguration remotePathAttributes = first(config, "Remote.DefaultPathAttributes");

		try {
			result.setAddressFamilyKey(new AddressFamilyKey(AddressFamily.fromString(addressFamily), 
					SubsequentAddressFamily.fromString(subsequentAddressFamily)));
		} catch(IllegalArgumentException e) {
			throw new ConfigurationException("Invalid AddressFamilyKey given", e);
		}
		
		if(localFilterConfiguration != null)
			result.setLocalRoutingFilters(parsRoutingeFilters(localFilterConfiguration));
		
		if(remoteFilterConfiguration != null)
			result.setRemoteRoutingFilters(parsRoutingeFilters(remoteFilterConfiguration));
		
		if(localPathAttributes != null)
			result.setLocalDefaultPathAttributes(pathAttributeParser.parseConfiguration(localPathAttributes));
		
		if(remotePathAttributes != null)
			result.setRemoteDefaultPathAttributes(pathAttributeParser.parseConfiguration(remotePathAttributes));
		
		return result;
	}
	
	private HierarchicalConfiguration first(HierarchicalConfiguration config, String key) throws ConfigurationException {
		HierarchicalConfiguration result = null;
		List<HierarchicalConfiguration> childs = config.configurationsAt(key);
		
		if(childs.size() > 1)
			throw new ConfigurationException("Duplicate element " + key);
		else if(childs.size() == 1)
			result = childs.get(0);
		
		return result;
	}
	
	private Set<RoutingFilterConfiguration> parsRoutingeFilters(HierarchicalConfiguration config) throws ConfigurationException {
		Set<RoutingFilterConfiguration> result = new TreeSet<RoutingFilterConfiguration>();
		
		for(HierarchicalConfiguration subConfig : config.configurationsAt("Filter"))
			result.add(filterParser.parseConfiguration(subConfig));
		
		return result;
	}
}
