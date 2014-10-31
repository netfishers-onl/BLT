/**
 * 
 */
package org.blt.bgp.config.nodes.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.blt.bgp.config.nodes.PathAttributeConfiguration;
import org.blt.bgp.net.ASType;
import org.blt.bgp.net.Origin;
import org.blt.bgp.net.PathSegment;
import org.blt.bgp.net.PathSegmentType;
import org.blt.bgp.net.attributes.ASPathAttribute;
import org.blt.bgp.net.attributes.LocalPrefPathAttribute;
import org.blt.bgp.net.attributes.MultiExitDiscPathAttribute;
import org.blt.bgp.net.attributes.OriginPathAttribute;

/**
 * @author rainer
 *
 */
public class PathAttributeConfigurationParser {

	PathAttributeConfiguration parseConfiguration(HierarchicalConfiguration config) throws ConfigurationException {
		PathAttributeConfigurationImpl result = new PathAttributeConfigurationImpl();
		
		for(String key : stripKeys(config.getKeys())) {
			parseConfiguration(config.configurationsAt(key), key, result);
		}
		
		return result;
	}

	
	private void parseConfiguration(List<HierarchicalConfiguration> configurations, String key, PathAttributeConfigurationImpl result) throws ConfigurationException {
		if(StringUtils.equals(key, "LocalPreference")) {
			parseLocalPreference(first(configurations, key), result, key);
		} else if(StringUtils.equals(key, "MultiExitDisc")) {
			parseMultiExitDiscPreference(first(configurations, key), result, key);			
		} else if(StringUtils.equals(key, "Origin")) {
			parseOrigin(first(configurations, key), result, key);
		} else if(StringUtils.equals(key, "ASPath")) {
			parseASPath(first(configurations, key), result, key);
		} else
			throw new ConfigurationException("Unknown path attribute: " + key);
	}

	private void parseASPath(HierarchicalConfiguration config, PathAttributeConfigurationImpl result, String key) throws ConfigurationException {
		String asTypeRep = config.getString("[@asType]", "as2");
		ASType type;
		
		if(StringUtils.equalsIgnoreCase(asTypeRep, "as2"))
			type = ASType.AS_NUMBER_2OCTETS;
		else if(StringUtils.equalsIgnoreCase(asTypeRep, "as4"))
			type = ASType.AS_NUMBER_4OCTETS;
		else
			throw new ConfigurationException("unknown AS type: " + asTypeRep);
		
		ASPathAttribute pa = new ASPathAttribute(type);
		
		for(HierarchicalConfiguration segConfig : config.configurationsAt("PathSegment")) {
			PathSegment segment = new PathSegment(type);
			String segType = segConfig.getString("[@type]");
			
			if(StringUtils.equalsIgnoreCase(segType, "set"))
				segment.setPathSegmentType(PathSegmentType.AS_SET);
			else if(StringUtils.equalsIgnoreCase(segType, "sequence"))
				segment.setPathSegmentType(PathSegmentType.AS_SEQUENCE);
			else if(StringUtils.equalsIgnoreCase(segType, "confed_set"))
				segment.setPathSegmentType(PathSegmentType.AS_CONFED_SET);
			else if(StringUtils.equalsIgnoreCase(segType, "confed_sequence"))
				segment.setPathSegmentType(PathSegmentType.AS_CONFED_SEQUENCE);
			else
				throw new ConfigurationException("Unknown path segment type: " + segType);

			for(HierarchicalConfiguration asConfig : segConfig.configurationsAt("As")) {
				int asNumber = asConfig.getInt("[@value]", -1);
				
				if(asNumber < 0 || (type == ASType.AS_NUMBER_2OCTETS && asNumber > 65536))
					throw new ConfigurationException("Invalid AS number: " + asNumber);
				
				segment.getAses().add(asNumber);
			}
			
			pa.getPathSegments().add(segment);
		}
		
		result.getAttributes().add(pa);
	}
	

	private void parseMultiExitDiscPreference(HierarchicalConfiguration config, PathAttributeConfigurationImpl result, String key) throws ConfigurationException {
		result.getAttributes().add(new MultiExitDiscPathAttribute(parseValue(config, key)));
	}


	private void parseLocalPreference(HierarchicalConfiguration config, PathAttributeConfigurationImpl result, String key) throws ConfigurationException {
		result.getAttributes().add(new LocalPrefPathAttribute(parseValue(config, key)));
	}

	private void parseOrigin(HierarchicalConfiguration config, PathAttributeConfigurationImpl result, String key) throws ConfigurationException {
		try {
			result.getAttributes().add(new OriginPathAttribute(Origin.fromString(config.getString("[@value]"))));			
		} catch(IllegalArgumentException e) {
			throw new ConfigurationException(e);
		}
	}


	private HierarchicalConfiguration first(List<HierarchicalConfiguration> configurations, String key) throws ConfigurationException {
		if(configurations.size() == 0)
			return null;
		else if(configurations.size() > 1)
			throw new ConfigurationException("duplicate element: " + key);
		else
			return configurations.get(0);
	}
	
	private int parseValue(HierarchicalConfiguration config, String key) throws ConfigurationException {
		int value = config.getInt("[@value]", -1);
		
		if(value < 0)
			throw new ConfigurationException("Invalid or missing value given for key " + key);
		
		return value;
	}
	
	private Set<String> stripKeys(Iterator<String> keys) {
		Set<String> cleaned = new HashSet<String>();
		
		while(keys.hasNext()) {
			String key = keys.next();
			
			if(StringUtils.contains(key, "[@")) {
				int index = StringUtils.indexOf(key, "[@");
				
				key = StringUtils.substring(key, 0, index);
			}
			if(StringUtils.contains(key, ".")) {
				int index = StringUtils.indexOf(key, ".");
				
				key = StringUtils.substring(key, 0, index);
			}

			
			cleaned.add(key);
		}
		
		return cleaned;
	}
}
