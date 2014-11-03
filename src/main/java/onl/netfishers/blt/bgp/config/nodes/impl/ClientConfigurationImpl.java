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
 * File: org.bgp4.config.nodes.impl.ServerConfigurationImpl.java 
 */
package onl.netfishers.blt.bgp.config.nodes.impl;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import onl.netfishers.blt.bgp.config.nodes.ClientConfiguration;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ClientConfigurationImpl implements ClientConfiguration {

	private InetSocketAddress remoteAddress;
	
	public ClientConfigurationImpl() {}
	
	public ClientConfigurationImpl(InetAddress addr) {
		this.remoteAddress = new InetSocketAddress(addr, 0);
	}

    public ClientConfigurationImpl(ClientConfiguration c) {
            this.remoteAddress = c.getRemoteAddress();
    }
	
	public ClientConfigurationImpl(InetAddress addr, int port) throws ConfigurationException {
		if(addr == null)
			throw new ConfigurationException("null remote address not allowed");
		if(addr.isAnyLocalAddress())
			throw new ConfigurationException("wildcard remote address not allowed");
		if(port < 0 || port > 65535)
			throw new ConfigurationException("port " + port + " not allowed");
			
		this.remoteAddress = new InetSocketAddress(addr, port);
	}
	
	public ClientConfigurationImpl(InetSocketAddress remoteAddress) throws ConfigurationException {
		if(remoteAddress.getAddress().isAnyLocalAddress())
			throw new ConfigurationException("wildcard remote address not allowed");
		
		this.remoteAddress = remoteAddress;
	}
	
	@Override
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	/**
	 * @param listenAddress the listenAddress to set
	 */
	void setRemoteAddress(InetSocketAddress listenAddress) {
		this.remoteAddress = listenAddress;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(remoteAddress).toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientConfigurationImpl other = (ClientConfigurationImpl) obj;
		if (remoteAddress == null) {
			if (other.remoteAddress != null)
				return false;
		} else if (!remoteAddress.equals(other.remoteAddress))
			return false;
		return true;
	}

}
