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
 * File: onl.netfishers.blt.bgp.netty.protocol.refresh.ORFType.java 
 */
package onl.netfishers.blt.bgp.net;

import org.apache.commons.lang3.StringUtils;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public enum ORFType {
	ADDRESS_PREFIX_BASED;

	/** Address prefix based outbound route filter type (RFC 5292) */
	private static final int BGP_OUTBOUND_ROUTE_FILTER_TYPE_ADDRESS_PREFIX_BASED = 64;
	
	public int toCode() {
		switch(this) {
		case ADDRESS_PREFIX_BASED:
			return BGP_OUTBOUND_ROUTE_FILTER_TYPE_ADDRESS_PREFIX_BASED;
		default:
			throw new IllegalArgumentException("cannot encode OutboudRouteFilter type " + this);
		}
	}
	
	public static ORFType fromCode(int code) {
		switch(code) {
		case BGP_OUTBOUND_ROUTE_FILTER_TYPE_ADDRESS_PREFIX_BASED:
			return ADDRESS_PREFIX_BASED;
		default:
			throw new IllegalArgumentException("cannot encode OutboudRouteFilter type code " + code);
		}
	}
	
	public static ORFType fromString(String value) {
		if(StringUtils.equalsIgnoreCase("addressPrefixBased", value)) {
			return ADDRESS_PREFIX_BASED;
		} else
			throw new IllegalArgumentException("cannot encode OutboudRouteFilter type: " + value);
	}
}
