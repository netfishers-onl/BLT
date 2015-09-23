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
 */
package onl.netfishers.blt.bgp.netty;

import onl.netfishers.blt.bgp.net.ASType;


/**
 * This bean contains all information about the BGP connection that needs to be accessible 
 * by channel handlers. It is stored in the ChannelHandlerContext after the OPEN handshake.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public interface PeerConnectionInformation {
	public ASType getAsTypeInUse() ;

	/**
	 * 
	 * @return
	 */
	public int getLocalAS();
	
	/**
	 * 
	 * @return
	 */
	public int getRemoteAS();
	
	/**
	 * Test if the connection describes an IBGP connection (peers in the same AS)
	 * 
	 * @return <code>true</code> if IBGP connection, <code>false</code> otherwise
	 */
	public boolean isIBGPConnection();

	/**
	 * Test if the connection describes an EBGP connection (peers in the same AS)
	 * 
	 * @return <code>true</code> if EBGP connection, <code>false</code> otherwise
	 */
	public boolean isEBGPConnection();
	
	/**
	 * Test if this connection uses 4 octet AS numbers
	 * 
	 * @return
	 */
	public boolean isAS4OctetsInUse();

	/**
	 * @return the localBgpIdentifier
	 */
	public long getLocalBgpIdentifier();

	/**
	 * @return the remoteBgpIdentifier
	 */
	public long getRemoteBgpIdentifier();
}
