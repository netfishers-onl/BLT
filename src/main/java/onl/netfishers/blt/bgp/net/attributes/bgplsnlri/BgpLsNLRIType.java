/**
 *  Copyright 2013, 2014 Nitin Bahadur (nitinb@gmail.com)
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

/**
 * Enumerator for various BGP link state object (NLRI) type
 * @author nitinb
 *
 */
public enum BgpLsNLRIType {
	RESERVED,
	LINK_NLRI,
	NODE_NLRI,
	IPV4_TOPOLOGY_PREFIX_NLRI,
	IPV6_TOPOLOGY_PREFIX_NLRI;
	
	/**
	 * Converts the enumerator into an integer code
	 * @return integer value
	 */
	public int toCode() {
		switch(this) {
		case RESERVED:
			return 0;
		case NODE_NLRI:
			return 1;
		case LINK_NLRI:
			return 2;
		case IPV4_TOPOLOGY_PREFIX_NLRI:
			return 3;
		case IPV6_TOPOLOGY_PREFIX_NLRI:
			return 4;
		default:
			throw new IllegalArgumentException("unknown nlri type: " + this);
		}
	}
	
	/**
	 * Given an integer value, converts it to the enumerator
	 * @param code integer value
	 * @return Bgp link state object type
	 */
	public static BgpLsNLRIType fromCode(int code) {
		switch(code) {
		case 0:
			return RESERVED;
		case 1:
			return NODE_NLRI;
		case 2:
			return LINK_NLRI;
		case 3:
			return IPV4_TOPOLOGY_PREFIX_NLRI;
		case 4:
			return IPV6_TOPOLOGY_PREFIX_NLRI;
		default:
			throw new IllegalArgumentException("unknown address family code: " + code);
		}
	}
	
	/**
	 * Converts the enumerator to a String form
	 * @return string representation of enumerator 
	 */
	public String toString() {
		switch(this) {
		case RESERVED:
			return "Reserved";
		case LINK_NLRI:
			return "Link_NLRI";
		case NODE_NLRI:
			return "Node_NLRI";
		case IPV4_TOPOLOGY_PREFIX_NLRI:
			return "IPv4_TOPOLOGY_PREFIX_NLRI";
		case IPV6_TOPOLOGY_PREFIX_NLRI:
			return "IPv6_TOPOLOGY_PREFIX_NLRI";
		default:
			return "Unknown";
		}
	}
}
