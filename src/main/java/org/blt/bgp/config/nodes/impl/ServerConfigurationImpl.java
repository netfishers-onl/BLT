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
package org.blt.bgp.config.nodes.impl;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.apache.commons.configuration.ConfigurationException;
import org.blt.bgp.config.nodes.ServerConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ServerConfigurationImpl implements ServerConfiguration {

	private InetSocketAddress listenAddress;
	private int port = 0;
	
	public ServerConfigurationImpl() {
		this.listenAddress = new InetSocketAddress(0);
	}

	public ServerConfigurationImpl(InetAddress addr) {
		this.listenAddress = new InetSocketAddress(addr, 0);
	}

	public ServerConfigurationImpl(InetAddress addr, int port) throws ConfigurationException {
		if(port < 0 || port > 65535)
			throw new ConfigurationException("port " + port + " not allowed");
		
		this.port = port;
		this.listenAddress = new InetSocketAddress(addr, port);
	}
	
	public ServerConfigurationImpl(InetSocketAddress listenAddress) {
		this.listenAddress = listenAddress;
	}

	public ServerConfiguration getServerConfiguration() {
		return this;
	}

    public void setServerConfiguration(ServerConfiguration config) {
        listenAddress = config.getListenAddress();
        port = config.getPort();
    }

	@Override
    public void setPort (int port) {
		this.port = port;
		listenAddress = new InetSocketAddress(listenAddress.getAddress(), port);
	}

	@Override
    public int getPort () {
        return port;
    }

	@Override
	public InetSocketAddress getListenAddress() {
		return listenAddress;
	}

	/**
	 * @param listenAddress the listenAddress to set
	 */
	void setListenAddress(InetSocketAddress listenAddress) {
		this.listenAddress = listenAddress;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((listenAddress == null) ? 0 : listenAddress.hashCode());
		return result;
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
		ServerConfigurationImpl other = (ServerConfigurationImpl) obj;
		if (listenAddress == null) {
			if (other.listenAddress != null)
				return false;
		} else if (!listenAddress.equals(other.listenAddress))
			return false;
		return true;
	}
}
