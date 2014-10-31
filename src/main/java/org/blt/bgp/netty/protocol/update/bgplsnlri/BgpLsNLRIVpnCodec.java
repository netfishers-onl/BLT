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
package org.blt.bgp.netty.protocol.update.bgplsnlri;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.blt.bgp.net.SubsequentAddressFamily;
import org.blt.bgp.net.attributes.MultiProtocolNLRIInformation;
import org.blt.bgp.net.attributes.bgplsnlri.BgpLsNLRIInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used for decoding a BGP link-state NLRI for VPNs.
 * VPN topology is a special case of non-VPN topology, containing VPN
 * specific attributes, in addition to the regular topology data.
 * @author nitinb
 *
 */
public class BgpLsNLRIVpnCodec extends BgpLsNLRICodec {

	private static final Logger log = LoggerFactory.getLogger(BgpLsNLRIVpnCodec.class);

	/**
	 * Decodes the data portion of a MP-BGP link state NLRI
	 * @param buffer Data stream containing the tlv
	 */
	public MultiProtocolNLRIInformation decodeNLRI(ChannelBuffer buffer) {
		
		BgpLsNLRIInformation nlriInfo;
		
		int type = buffer.readUnsignedShort();
		int length = buffer.readUnsignedShort();
	
		// not enough bytes in the buffer to read the NLRI
		if (buffer.readableBytes() < length) {
			log.error("Failed to decode BGP-LS NLRI type " + type + " due to completely read NLRI");
			return null;
		}
	
		// get the route distinguisher
		byte[] routeDistinguisher = new byte[8];
		buffer.readBytes(routeDistinguisher);
		
		ChannelBuffer valueBuffer = ChannelBuffers.buffer(length - 8);
		buffer.readBytes(valueBuffer);
	
		nlriInfo = decodeNLRIInternal(valueBuffer, SubsequentAddressFamily.NLRI_MPLS_VPN, type);
		if (nlriInfo != null) {
			nlriInfo.setRouteDistinguisher(routeDistinguisher);
		}
		
		return nlriInfo;
	}
}
