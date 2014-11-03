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
package onl.netfishers.blt.bgp.netty.protocol.update;

import java.util.HashMap;
import java.util.Map;

import onl.netfishers.blt.bgp.net.AddressFamily;
import onl.netfishers.blt.bgp.net.AddressFamilyKey;
import onl.netfishers.blt.bgp.net.SubsequentAddressFamily;

/**
 * @author nitinb
 *
 */
public class MultiProtocolNLRIDecoderTypes {
	private static Map<AddressFamilyKey,String>nlriTypes = new HashMap<AddressFamilyKey,String>();
	
	static {
		nlriTypes.put(AddressFamilyKey.BGP_LS_TOPOLOGY, "onl.netfishers.blt.bgp.netty.protocol.update.bgplsnlri.BgpLsNLRICodec");
		nlriTypes.put(AddressFamilyKey.BGP_LS_VPN_TOPOLOGY, "onl.netfishers.blt.bgp.netty.protocol.update.bgplsnlri.BgpLsVpnNLRICodec");
	}

	/**
	 * Returns the name of the NLRI, given an AFI/SAFI combo
	 * @param addressFamily
	 * @param subsequentAddressFamily
	 * @return Name of NLRI
	 */
	public static String getSubClass(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily) {
		return nlriTypes.get(new AddressFamilyKey(addressFamily, subsequentAddressFamily));
	}
}
