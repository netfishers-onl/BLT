/**
 * 
 */
package org.blt.bgp.config.nodes.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.blt.bgp.config.nodes.AddressFamilyRoutingConfiguration;
import org.blt.bgp.config.nodes.RoutingConfiguration;
import org.blt.bgp.net.AddressFamilyKey;

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
