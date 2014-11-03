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
 * File: org.bgp4.config.nodes.impl.BgpClientConfigurationImpl.java 
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

import onl.netfishers.blt.bgp.config.nodes.ClientConfiguration;

import org.apache.commons.configuration.ConfigurationException;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BgpClientConfigurationImpl extends ClientConfigurationImpl {

	private static final int BGP_PORT = 179;
	
	public BgpClientConfigurationImpl() {}
	
	public BgpClientConfigurationImpl (ClientConfiguration config) throws ConfigurationException {
		super(config.getRemoteAddress().getAddress(), BGP_PORT);
	}
	
	public int getDefaultPort() {
		return BGP_PORT;
	}
}
