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
package org.blt.bgp.net;

import org.apache.commons.lang3.StringUtils;

/**
 * Address families as defined in RFC 1700
 * @author rainer
 *
 */
public enum AddressFamily {
	RESERVED,
	IPv4,
	IPv6,
	NSAP,
	HDLC,
	BBN1882,
	IEEE802,
	E163,
	E164,
	F69,
	X121,
	IPX,
	APPLETALK,
	DECNET4,
	BANYAN,
	BGP_LS,
	RESERVED2;
	
	public int toCode() {
		switch(this) {
		case RESERVED:
			return 0;
		case IPv4:
			return 1;
		case IPv6:
			return 2;
		case NSAP:
			return 3;
		case HDLC:
			return 4;
		case BBN1882:
			return 5;
		case IEEE802:
			return 6;
		case E163:
			return 7;
		case E164:
			return 8;
		case F69:
			return 9;
		case X121:
			return 10;
		case IPX:
			return 11;
		case APPLETALK:
			return 12;
		case DECNET4:
			return 13;
		case BANYAN:
			return 14;
		case BGP_LS:
			return 16388;
		case RESERVED2:
			return 65535;
		default:
			throw new IllegalArgumentException("unknown address family: " + this);
		}
	}
	
	public static AddressFamily fromCode(int code) {
		switch(code) {
		case 0:
			return RESERVED;
		case 1:
			return IPv4;
		case 2:
			return IPv6;
		case 3:
			return NSAP;
		case 4:
			return HDLC;
		case 5:
			return BBN1882;
		case 6:
			return IEEE802;
		case 7:
			return E163;
		case 8:
			return E164;
		case 9:
			return F69;
		case 10:
			return X121;
		case 11:
			return IPX;
		case 12:
			return APPLETALK;
		case 13:
			return DECNET4;
		case 14:
			return BANYAN;
		case 16388:
			return BGP_LS;
		case 65535:
			return RESERVED2;
		default:
			throw new IllegalArgumentException("unknown address family code: " + code);
		}
	}
	
	public static AddressFamily fromString(String value) {
		if(StringUtils.equalsIgnoreCase("ipv4", value)) {
			return IPv4;
		} else if(StringUtils.equalsIgnoreCase("ipv6", value)) {
			return IPv6;
		} else if(StringUtils.equalsIgnoreCase("nsap", value)) {
			return NSAP;
		} else if(StringUtils.equalsIgnoreCase("hdlc", value)) {
			return HDLC;
		} else if(StringUtils.equalsIgnoreCase("bbn1882", value)) {
			return BBN1882;
		} else if(StringUtils.equalsIgnoreCase("ieee802", value)) {
			return IEEE802;
		} else if(StringUtils.equalsIgnoreCase("e163", value)) {
			return E163;
		} else if(StringUtils.equalsIgnoreCase("e164", value)) {
			return E164;
		} else if(StringUtils.equalsIgnoreCase("f69", value)) {
			return F69;
		} else if(StringUtils.equalsIgnoreCase("x121", value)) {
			return X121;
		} else if(StringUtils.equalsIgnoreCase("ipx", value)) {
			return IPX;
		} else if(StringUtils.equalsIgnoreCase("appletalk", value)) {
			return APPLETALK;
		} else if(StringUtils.equalsIgnoreCase("decnet4", value)) {
			return DECNET4;
		} else if(StringUtils.equalsIgnoreCase("banyan", value)) {
			return BANYAN;
		} else if(StringUtils.equalsIgnoreCase("bgp_ls", value)) {
			return BGP_LS;
		} else
			throw new IllegalArgumentException("unknown address family: " + value);
	}
}
