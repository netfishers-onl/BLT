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
 * File: org.bgp4.config.nodes.CapabilitiesList.java 
 */
/**
 * Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
 * 
 * License: same as above
 * 
 * Added support for BGP-LS
 */
package onl.netfishers.blt.bgp.net;

import org.apache.commons.lang3.StringUtils;

/**
 * Subsequent address family as defined in RFC 2858
 * 
 * @author rainer
 *
 */
public enum SubsequentAddressFamily {
	NLRI_UNICAST_FORWARDING,
	NLRI_MULTICAST_FORWARDING,
	NLRI_UNICAST_MULTICAST_FORWARDING,
	NLRI_BGP_LS,
	NLRI_MPLS_VPN;
	
	private static final String ENCODING_UNICAST_MULTICAST = "Unicast+Multicast";
	private static final String ENCODING_MULTICAST = "Multicast";
	private static final String ENCODING_UNICAST = "Unicast";
	private static final String ENCODING_BGP_LS = "BGP-LS";
	private static final String ENCODING_MPLS_VPN = "MPLS-VPN";

	public int toCode() {
		switch(this) {
		case NLRI_UNICAST_FORWARDING:
			return 1;
		case NLRI_MULTICAST_FORWARDING:
			return 2;
		case NLRI_UNICAST_MULTICAST_FORWARDING:
			return 3;
		case NLRI_BGP_LS:
			return 71;
		case NLRI_MPLS_VPN:
			return 128;
		default:
			throw new IllegalArgumentException("Unknown subsequent address family: " + this);
		}
	}
	
	public static SubsequentAddressFamily fromCode(int code) {
		switch(code) {
		case 1:
			return NLRI_UNICAST_FORWARDING;
		case 2:
			return NLRI_MULTICAST_FORWARDING;
		case 3:
			return NLRI_UNICAST_MULTICAST_FORWARDING;
		case 71:
			return NLRI_BGP_LS;
		case 128:
			return NLRI_MPLS_VPN;
		default:
			throw new IllegalArgumentException("Unknown subsequent address family code: " + code);
		}
	}
	
	public static SubsequentAddressFamily fromString(String value) {
		if(StringUtils.equalsIgnoreCase(ENCODING_UNICAST, value) || StringUtils.equalsIgnoreCase("NLRI_UNICAST_FORWARDING", value)) {
			return NLRI_UNICAST_FORWARDING;
		} else if(StringUtils.equalsIgnoreCase(ENCODING_MULTICAST, value) || StringUtils.equalsIgnoreCase("NLRI_MULTICAST_FORWARDING", value)) {
			return NLRI_MULTICAST_FORWARDING;
		} else if(StringUtils.equalsIgnoreCase(ENCODING_UNICAST_MULTICAST, value) || StringUtils.equalsIgnoreCase("NLRI_UNICAST_MULTICAST_FORWARDING", value)) {
			return NLRI_UNICAST_MULTICAST_FORWARDING;
		} else if(StringUtils.equalsIgnoreCase(ENCODING_BGP_LS, value) || StringUtils.equalsIgnoreCase("NLRI_BGP_LS", value)) {
			return NLRI_BGP_LS;
		} else if(StringUtils.equalsIgnoreCase(ENCODING_MPLS_VPN, value) || StringUtils.equalsIgnoreCase("NLRI_MPLS_VPN", value)) {
			return NLRI_MPLS_VPN;
		} else
			throw new IllegalArgumentException("Unknown subsequent address family: " + value);
	}
	
	public String toString() {
		switch(this) {
		case NLRI_UNICAST_FORWARDING:
			return ENCODING_UNICAST;
		case NLRI_MULTICAST_FORWARDING:
			return ENCODING_MULTICAST;
		case NLRI_UNICAST_MULTICAST_FORWARDING:
			return ENCODING_UNICAST_MULTICAST;
		case NLRI_BGP_LS:
			return ENCODING_BGP_LS;
		case NLRI_MPLS_VPN:
			return ENCODING_MPLS_VPN;
		default:
			throw new IllegalArgumentException("Unknown subsequent address family: " + this);
		}		
	}
}
