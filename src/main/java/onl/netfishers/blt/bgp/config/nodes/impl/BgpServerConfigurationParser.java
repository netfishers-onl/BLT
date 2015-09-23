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
 * File: org.bgp4.config.impl.BgpServerConfigurationParser.java 
 */
package onl.netfishers.blt.bgp.config.nodes.impl;

import java.util.List;

import onl.netfishers.blt.bgp.config.nodes.ServerConfiguration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BgpServerConfigurationParser extends ServerConfigurationParser {

    public BgpServerConfigurationParser () {
    }
	
	public ServerConfiguration parseConfig(HierarchicalConfiguration config) throws ConfigurationException {
		BgpServerConfigurationImpl result = new BgpServerConfigurationImpl();
		List<HierarchicalConfiguration> serverConfig = config.configurationsAt("Server");
		
		if(serverConfig.size() == 1) {
			result.setServerConfiguration(super.parseConfig(serverConfig.get(0)));
        } else if(serverConfig.size() > 1) {
			throw new ConfigurationException("duplicate <Server/> element");
        }
		
		return result;
	}
}
