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
 * File: org.bgp4.config.nodes.impl.RoutingFilterConfigurationParser.java 
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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.blt.bgp.BgpService;
import org.blt.bgp.config.nodes.RoutingFilterConfiguration;

/**
 * @author rainer
 *
 */
public class RoutingFilterConfigurationParser {

	private NetworkLayerReachabilityParser nlriParser;

	public RoutingFilterConfigurationParser() {
		nlriParser = (NetworkLayerReachabilityParser)BgpService.getInstance(NetworkLayerReachabilityParser.class.getName());
	}

	RoutingFilterConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		List<HierarchicalConfiguration> prefixList = config.configurationsAt("Prefixes");
		RoutingFilterConfigurationImpl rfc = null;
	
		if(prefixList.size() > 1)
			throw new ConfigurationException("more then one subnode specified");
		
		if(prefixList.size() == 1)
			rfc = parsePrefixFilter(prefixList.get(0));
		
		if(rfc == null)
			throw new ConfigurationException("no filter type specified");

		rfc.setName(config.getString("[@name]", ""));
		
		return rfc;
	}
	
	private RoutingFilterConfigurationImpl parsePrefixFilter(HierarchicalConfiguration config) throws ConfigurationException {
		PrefixRoutingFilterConfigurationImpl prfc = new PrefixRoutingFilterConfigurationImpl();
		
		for(HierarchicalConfiguration subConfig : config.configurationsAt("Prefix")) {
			String rep = subConfig.getString("[@value]");
			
			if(StringUtils.isBlank(rep))
				throw new ConfigurationException("empty prefix specified");
			
			prfc.getFilterPrefixes().add(nlriParser.parseNlri(rep));
		}
		
		return prfc;
	}
}
