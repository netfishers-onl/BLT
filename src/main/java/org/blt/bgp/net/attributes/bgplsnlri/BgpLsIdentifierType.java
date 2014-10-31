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

/**
 * Enumeration of various BGP identifier types
 * @author nitinb
 *
 */
public enum BgpLsIdentifierType {
	RESERVED,
	Instance,
	Domain,
	Area,
	OspfRouteType,
	MultiTopologyId;
	
	/**
	 * Converts the identifier type to an integer
	 * @return integer version of identifier
	 */
	public int toCode() {
		switch(this) {
		case RESERVED:
			return 0;
		case Instance:
			return 1;
		case Domain:
			return 2;
		case Area:
			return 3;
		case OspfRouteType:
			return 4;
		case MultiTopologyId:
			return 5;
		default:
			throw new IllegalArgumentException("unknown nlri type: " + this);
		}
	}
	
	/**
	 * Converts an integer form of the identifier to the enumerator type
	 * @param code Integer form of the identifier
	 * @return Identifier enumerator
	 */
	public static BgpLsIdentifierType fromCode(int code) {
		switch(code) {
		case 0:
			return RESERVED;
		case 1:
			return Instance;
		case 2:
			return Domain;
		case 3:
			return Area;
		case 4:
			return OspfRouteType;
		case 5:
			return MultiTopologyId;
		default:
			throw new IllegalArgumentException("unknown identifier type: " + code);
		}
	}
	
	/**
	 * Converts the identifier to a string
	 * @return String form of identifier
	 */
	public String toString() {
		switch(this) {
		case RESERVED:
			return "Reserved";
		case Instance:
			return "Instance";
		case Domain:
			return "Domain";
		case Area:
			return "Area";
		case OspfRouteType:
			return "OspfRouteType";
		case MultiTopologyId:
			return "MultiTopologyId";
		default:
			return "Unknown";
		}
	}
}
