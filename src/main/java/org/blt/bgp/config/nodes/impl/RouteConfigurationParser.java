/**
 * 
 */
package org.blt.bgp.config.nodes.impl;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.blt.bgp.BgpService;
import org.blt.bgp.config.nodes.PathAttributeConfiguration;
import org.blt.bgp.config.nodes.RouteConfiguration;

/**
 * @author rainer
 *
 */
public class RouteConfigurationParser {
	private PathAttributeConfigurationParser pathAttrParser;
	private NetworkLayerReachabilityParser nlriParser;
	
	public RouteConfigurationParser() {
		nlriParser = (NetworkLayerReachabilityParser)BgpService.getInstance(NetworkLayerReachabilityParser.class.getName());
		pathAttrParser = (PathAttributeConfigurationParser)BgpService.getInstance(PathAttributeConfigurationParser.class.getName());
	}

	public RouteConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		String prefix = config.getString("Prefix[@value]");
		List<HierarchicalConfiguration> pa = config.configurationsAt("PathAttributes");
		
		if(StringUtils.isBlank(prefix))
			throw new ConfigurationException("NLRI missing");
		if(pa.size() > 1)
			throw new ConfigurationException("PathAttributes missing of given multiple times");
		else {
			PathAttributeConfiguration pac = new PathAttributeConfigurationImpl();
			
			if(pa.size() == 1)
				pac = pathAttrParser.parseConfiguration(pa.get(0));
				
			return new RouteConfigurationImpl(nlriParser.parseNlri(prefix), pac);			
		}
	}
	
}
