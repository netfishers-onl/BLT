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
 * File: org.bgp4.config.nodes.impl.ServerConfigurationParser.java 
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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import onl.netfishers.blt.bgp.config.nodes.ServerConfiguration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parse a generic server configuration
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ServerConfigurationParser {
	private static final Logger log = LoggerFactory.getLogger(ServerConfigurationParser.class);

    public ServerConfigurationParser() {}

	public ServerConfiguration parseConfig(HierarchicalConfiguration config) throws ConfigurationException {
		InetAddress address = null;
		int port =  config.getInt("[@port]", 0);
		String listenRep = config.getString("[@listen]");
		
		if(StringUtils.isNotBlank(listenRep)) {
			try {
				address = InetAddress.getByName(listenRep);
			} catch (UnknownHostException e) {
				log.error("failed to parse list address: " + listenRep, e);
				
				throw new ConfigurationException(e);
			}
		}
		
		return new ServerConfigurationImpl(new InetSocketAddress(address, port));
	}
}
