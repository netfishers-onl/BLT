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
 * This enumerator represents the protocol types from which the topology
 * information can be learnt.
 * @author nitinb
 *
 */
public enum BgpLsProtocolId {
	Unknown,
	ISIS_Level1,
	ISIS_Level2,
	OSPF,
	Direct,
	Static;
	
	/**
	 * Coverts the protocol id to an integer value
	 * @return short value
	 */
	public short toCode() {
		switch(this) {
		case Unknown:
			return 0;
		case ISIS_Level1:
			return 1;
		case ISIS_Level2:
			return 2;
		case OSPF:
			return 3;
		case Direct:
			return 4;
		case Static:
			return 5;
		default:
			throw new IllegalArgumentException("unknown protocol-id : " + this);
		}
	}
	
	/**
	 * Converts a short value into the enumerator
	 * @param code short value
	 * @return the enumerator
	 */
	public static BgpLsProtocolId fromCode(short code) {
		switch(code) {
		case 0:
			return Unknown;
		case 1:
			return ISIS_Level1;
		case 2:
			return ISIS_Level2;
		case 3:
			return OSPF;
		case 4:
			return Direct;
		case 5:
			return Static;
		default:
			throw new IllegalArgumentException("unknown protocol id : " + code);
		}
	}
	
	/**
	 * Converts the enumerator to a string
	 * @return string form of enumerator
	 */
	public String toString() {
		switch(this) {
		case Unknown:
			return "Unknown";
		case ISIS_Level1:
			return "ISIS_Level1";
		case ISIS_Level2:
			return "ISIS_Level2";
		case OSPF:
			return "OSPF";
		case Direct:
			return "Direct";
		case Static:
			return "Static";
		default:
			throw new IllegalArgumentException("invalid protocol id : " + this);
		}
	}
}
