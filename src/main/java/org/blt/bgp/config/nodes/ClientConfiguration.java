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
 * File: org.bgp4.config.ServerConfiguration.java 
 */
package org.blt.bgp.config.nodes;

import java.net.InetSocketAddress;

/**
 * Configuration for generic network server
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public interface ClientConfiguration {
	/**
	 * get the listen address with the port number
	 * @return
	 */
	public InetSocketAddress getRemoteAddress();
		
	/**
	 * 
	 * @return
	 */
	@Override
	public boolean equals(Object other);
	
	/**
	 * 
	 * @return
	 */
	@Override
	public int hashCode();

}
