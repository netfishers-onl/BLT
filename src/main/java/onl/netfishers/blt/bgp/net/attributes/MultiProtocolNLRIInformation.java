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
package onl.netfishers.blt.bgp.net.attributes;

import onl.netfishers.blt.bgp.net.AddressFamily;
import onl.netfishers.blt.bgp.net.SubsequentAddressFamily;

/**
 * Minimal representation of a BGP NLRI's data. This object should be extended
 * to represent the actual NLRI content. 
 * @author nitinb
 *
 */
public class MultiProtocolNLRIInformation {
	AddressFamily afi;
	SubsequentAddressFamily safi;
	
	/**
	 * @param afi BGP address family
	 * @param safi BGP subsequent address family
	 */
	protected MultiProtocolNLRIInformation(AddressFamily afi, SubsequentAddressFamily safi) {
		this.afi = afi;
		this.safi = safi;
	}
	
	/**
	 * Returns the subsequent address family associated with this object
	 * @return BGP subsequent address family
	 */
	protected SubsequentAddressFamily getSubsequentAddressFamily() {
		return safi;
	}
}
