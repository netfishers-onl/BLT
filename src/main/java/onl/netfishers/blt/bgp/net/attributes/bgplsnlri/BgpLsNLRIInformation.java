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

import onl.netfishers.blt.bgp.net.AddressFamily;
import onl.netfishers.blt.bgp.net.SubsequentAddressFamily;
import onl.netfishers.blt.bgp.net.attributes.MultiProtocolNLRIInformation;

/**
 * This object extends the MultiProtocol NLRI object with the BGP-LS information.
 * @author nitinb
 *
 */
public class BgpLsNLRIInformation extends MultiProtocolNLRIInformation {
	
	public static final long INSTANCE_IDENTIFIER_L3_PACKET_TOPOLOGY = 0;
	public static final long INSTANCE_IDENTIFIER_L1_OPTICAL_TOPOLOGY = 1;
	
	private BgpLsNLRIType nlriType;
	private byte[] routeDistinguisher;
		
	/**
	 * @param safi  BGP subsequent address family
	 * @param nlriType Type of BGP NLRI
	 */
	public BgpLsNLRIInformation(SubsequentAddressFamily safi, BgpLsNLRIType nlriType) {
		super(AddressFamily.BGP_LS, safi);
		this.nlriType = nlriType;
	}


	/**
	 * Gets the BGP NLRI type associated with the object
	 * @return nlriType
	 */
	public BgpLsNLRIType getNlriType() {
		return nlriType;
	}

	/**
	 * Gets the BGP link state route distinguisher
	 * @return route distinguisher
	 */
	public byte[] getRouteDistinguisher() {
		return routeDistinguisher;
	}

	/**
	 * Sets the route distinguisher for the BGP link state object
	 * @param routeDistinguisher the route distinguisher
	 */
	public void setRouteDistinguisher(byte[] routeDistinguisher) {
		if (getSubsequentAddressFamily() == SubsequentAddressFamily.NLRI_MPLS_VPN) {
			this.routeDistinguisher = routeDistinguisher;
		}
	}
}