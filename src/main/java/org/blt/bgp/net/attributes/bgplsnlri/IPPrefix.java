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
package org.blt.bgp.net.attributes.bgplsnlri;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

import org.blt.bgp.net.AddressFamily;

/**
 * This object represents a IP topology prefix
 * @author nitinb
 *
 */
public class IPPrefix {
	private short prefixLength;
	private byte[] prefix;
	AddressFamily addressFamily;
	
	/**
	 * 
	 * @param prefixLength prefix length
	 * @param prefix the IP prefix itself
	 * @param addressFamily address family (IPv4 or IPv6)
	 */
	public IPPrefix(short prefixLength, byte[] prefix, AddressFamily addressFamily) {
		this.prefixLength = prefixLength;
		this.prefix = prefix;
		this.addressFamily = addressFamily;
	}
	
	/**
	 * Checks if the IP prefix is a valid prefix
	 * @return
	 */
	public boolean isValidPrefix() {
		if (prefixLength == 0 || prefixLength > 128) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns the prefix length
	 * @return prefix length
	 */
	public short getPrefixLength() {
		return prefixLength;
	}
	
	/**
	 * Gets the prefix value
	 * @return prefix
	 */
	public byte[] getPrefix() {
		return prefix;
	}
	
	/**
	 * Gets the address family associated with the prefix
	 * @return address family
	 */
	public AddressFamily getAddressFamily() {
		return addressFamily;
	}
	
	/**
	 * Converts a IPv4 prefix into a string
	 * @return String form of prefix
	 */
	private String ipv4ToString() {
		byte ipData[];
		InetAddress addr;
		
		if (prefix.length != 4) {
			ipData = new byte[4];
			System.arraycopy(prefix, 0, ipData, 0, prefix.length); 
		} else {
			ipData = prefix;
		}
		
		try {
			addr = Inet4Address.getByAddress(ipData);
			return addr.toString().concat("/").concat(String.valueOf(prefixLength));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Converts a IPv6 prefix into a string
	 * @return String form of prefix
	 */
	private String ipv6ToString() {
		byte ipData[];
		InetAddress addr;
		
		if (prefix.length != 16) {
			ipData = new byte[16];
			System.arraycopy(prefix, 0, ipData, 0, prefix.length); 
		} else {
			ipData = prefix;
		}
		
		try {
			addr = Inet6Address.getByAddress(ipData);
			return addr.toString().concat("/").concat(String.valueOf(prefixLength));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Converts a IP prefix into a string
	 * @return String form of prefix
	 */
	public String toString() {
		switch (addressFamily) {
		case IPv4:
			return ipv4ToString();
		case IPv6:
			return ipv6ToString();
		default:
			return null;
		}
	}
}
