/**
 *  Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
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
 */
package onl.netfishers.blt.bgp.net.attributes.bgplsnlri;

import java.net.InetAddress;

/**
 * Link descriptor object uniquely identifies a link between a pair of routers.
 * @author nitinb
 *
 */
public class BgpLsLinkDescriptor {
	private long linkLocalIdentifier = 0;
	private long linkRemoteIdentifier = 0;
	private InetAddress ipv4InterfaceAddress;
	private InetAddress ipv4NeighborAddress;
	private InetAddress ipv6InterfaceAddress;
	private InetAddress ipv6NeighborAddress;
	
	private int multiTopologyId = 0;
	
	
	private boolean validLinkLocalIdentifier = false;
	private boolean validLinkRemoteIdentifier = false;
	private boolean validIPv4InterfaceAddress = false;
	private boolean validIPv4NeighborAddress = false;
	private boolean validIPv6InterfaceAddress = false;
	private boolean validIPv6NeighborAddress = false;
	private boolean validMultiTopologyId = false;
	
	public BgpLsLinkDescriptor() {}
	
	/**
	 * Sets the link local identifier
	 * @param linkLocalIdentifier link local identifier to set
	 */
	public void setLinkLocalIdentifier(long linkLocalIdentifier) {
		this.linkLocalIdentifier = linkLocalIdentifier;
		validLinkLocalIdentifier = true;
	}
	
	/**
	 * Gets the link local identifier
	 * @return link local identifier
	 */
	public long getLinkLocalIdentifier() {
		return linkLocalIdentifier;
	}
	
	/**
	 * Returns TRUE if the link local identifier is valid
	 * @return
	 */
	public boolean isValidLinkLocalIdentifier() {
		return validLinkLocalIdentifier;
	}
	
	/**
	 * Sets the link remote identifier
	 * @param linkRemoteIdentifier link remote identifier
	 */
	public void setLinkRemoteIdentifier(long linkRemoteIdentifier) {
		this.linkRemoteIdentifier = linkRemoteIdentifier;
		validLinkRemoteIdentifier = true;
	}
	
	/**
	 * Gets the link remote identifier
	 * @return link remote identifier
	 */
	public long getLinkRemoteIdentifier() {
		return linkRemoteIdentifier;
	}
	
	/**
	 * Returns TRUE if the link remote identifier is valid
	 * @return
	 */
	public boolean isValidLinkRemoteIdentifier() {
		return validLinkRemoteIdentifier;
	}
	
	/**
	 * Sets the IPv4 interface address associated with the link
	 * @param address IPv4 address
	 */
	public void setIPv4InterfaceAddress(InetAddress address) {
		this.ipv4InterfaceAddress = address;
		validIPv4InterfaceAddress = true;
	}
	
	/**
	 * Returns the IPv4 interface address associated with the link
	 * @return IPv4 address
	 */
	public InetAddress getIPv4InterfaceAddress() {
		return ipv4InterfaceAddress;
	}
	
	/**
	 * Returns TRUE if the IPv4 interface address is valid
	 * @return
	 */
	public boolean isValidIPv4InterfaceAddress() {
		return validIPv4InterfaceAddress;
	}
	
	/**
	 * Sets the IPv4 neighbor address associated with the link
	 * @param address IPv4 neighbor address
	 */
	public void setIPv4NeighborAddress(InetAddress address) {
		this.ipv4NeighborAddress = address;
		validIPv4NeighborAddress = true;
	}
	
	/**
	 * Gets the IPv4 neighbor address
	 * @return IPv4 neighbor address
	 */
	public InetAddress getIPv4NeighborAddress() {
		return ipv4NeighborAddress;
	}
	
	/**
	 * Returns TRUE if the IPv4 neighbor address if valid
	 * @return
	 */
	public boolean isValidIPv4NeighborAddress() {
		return validIPv4NeighborAddress;
	}
	
	/**
	 * Sets the IPv6 interface address associated with the link
	 * @param address IPv6 interface address
	 */
	public void setIPv6InterfaceAddress(InetAddress address) {
		this.ipv6InterfaceAddress = address;
		validIPv6InterfaceAddress = true;
	}
	
	/**
	 * Gets the IPv6 interface address associated with the link
	 * @return IPv6 interface address
	 */
	public InetAddress getIPv6InterfaceAddress() {
		return ipv6InterfaceAddress;
	}
	
	/**
	 * Returns TRUE if the IPv6 interface address is valid
	 * @return
	 */
	public boolean isValidIPv6InterfaceAddress() {
		return validIPv6InterfaceAddress;
	}
	
	/**
	 * Sets the IPv6 neighbor address associated with the link
	 * @param address IPv6 neighbor address
	 */
	public void setIPv6NeighborAddress(InetAddress address) {
		this.ipv6NeighborAddress = address;
		validIPv6NeighborAddress = true;
	}
	
	/**
	 * Gets the IPv6 neighbor address associated with the link
	 * @return IPv6 neighbor address
	 */
	public InetAddress getIPv6NeighborAddress() {
		return ipv6NeighborAddress;
	}
	
	/**
	 * Returns TRUE if the IPv6 neighbor address is valid
	 * @return
	 */
	public boolean isValidIPv6NeighborAddress() {
		return validIPv6NeighborAddress;
	}
	
	/**
	 * Sets the multi-topology id associated with the link
	 * @param multiTopologyId multi-topology id
	 */
	public void setMultiTopologyId(int multiTopologyId) {
		this.multiTopologyId = multiTopologyId;
		validMultiTopologyId = true;
	}
	
	/**
	 * Gets the multi-topology id associated with the link
	 * @return multi-topology id
	 */
	public int getMultiTopologyId() {
		return multiTopologyId;
	}
	
	/**
	 * Returns TRUE if the multi-topology id is valid
	 * @return
	 */
	public boolean isValidMultiTopologyId() {
		return validMultiTopologyId;
	}
}
