/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
/**
 * 
 */
package onl.netfishers.blt.bgp.config.nodes.impl;

import java.util.HashSet;
import java.util.Set;

import onl.netfishers.blt.bgp.config.nodes.AddressFamilyRoutingConfiguration;
import onl.netfishers.blt.bgp.config.nodes.RoutingConfiguration;
import onl.netfishers.blt.bgp.net.AddressFamilyKey;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;

/**
 * @author rainer
 *
 */
public class RoutingConfigurationParser {

	private AddressFamilyRoutingConfigurationParser afParser;

	public RoutingConfigurationParser() {
		afParser = new AddressFamilyRoutingConfigurationParser();
	}
	
	public RoutingConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		RoutingConfigurationImpl result = new RoutingConfigurationImpl();
		Set<AddressFamilyKey> keys = new HashSet<AddressFamilyKey>();

		for(HierarchicalConfiguration afConfig: config.configurationsAt("MulticastAddressFamily")) {
			AddressFamilyRoutingConfiguration afRouting = afParser.parseConfiguration(afConfig);
			
			if(keys.contains(afRouting.getKey()))
				throw new ConfigurationException("Duplicate address family: " + afRouting.getKey());
			
			result.getRoutingConfigurations().add(afRouting);
			keys.add(afRouting.getKey());
		}
		
		return result;
	}
}
