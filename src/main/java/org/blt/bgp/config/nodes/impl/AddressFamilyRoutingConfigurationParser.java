/**
 * 
 */
package org.blt.bgp.config.nodes.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.blt.bgp.BgpService;
import org.blt.bgp.config.nodes.AddressFamilyRoutingConfiguration;
import org.blt.bgp.config.nodes.RouteConfiguration;
import org.blt.bgp.net.AddressFamily;
import org.blt.bgp.net.AddressFamilyKey;
import org.blt.bgp.net.SubsequentAddressFamily;

/**
 * @author rainer
 *
 */
public class AddressFamilyRoutingConfigurationParser {

	private RouteConfigurationParser routeParser;
	
	public AddressFamilyRoutingConfigurationParser() {
		routeParser = (RouteConfigurationParser)BgpService.getInstance(RouteConfigurationParser.class.getName());
	}
	
	AddressFamilyRoutingConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		try {
			AddressFamilyKey key = new AddressFamilyKey(AddressFamily.fromString(config.getString("[@addressFamily]")), 
					SubsequentAddressFamily.fromString(config.getString("[@subsequentAddressFamily]")));
			
			List<RouteConfiguration> routes = new LinkedList<RouteConfiguration>();
			
			for(HierarchicalConfiguration routeConfig : config.configurationsAt("Route"))
				routes.add(routeParser.parseConfiguration(routeConfig));
			
			return new AddressFamilyRoutingConfigurationImpl(key, routes);
		} catch(IllegalArgumentException  e) {
			throw new ConfigurationException("Invalid value: ", e);
		}
	}
}
