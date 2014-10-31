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
 * File: org.blt.bgp.netty.NLRICodec.java 
 */
package org.blt.bgp.netty;

import org.blt.bgp.net.NetworkLayerReachabilityInformation;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NLRICodec {

	public NetworkLayerReachabilityInformation decodeNLRI(ChannelBuffer buffer) {
		NetworkLayerReachabilityInformation nlri = new NetworkLayerReachabilityInformation();
		int prefixLength = buffer.readUnsignedByte();
		byte[] prefixBytes = null;
		
		if(prefixLength > 0) {
			prefixBytes = new byte[NetworkLayerReachabilityInformation.calculateOctetsForPrefixLength(prefixLength)];
			
			buffer.readBytes(prefixBytes);
		}
		nlri.setPrefix(prefixLength, prefixBytes);
		
		return nlri;
	}

	public int calculateEncodedNLRILength(NetworkLayerReachabilityInformation nlri) {
		return NetworkLayerReachabilityInformation.calculateOctetsForPrefixLength(nlri.getPrefixLength()) + 1;
	}

	public ChannelBuffer encodeNLRI(NetworkLayerReachabilityInformation nlri) {
		ChannelBuffer buffer = ChannelBuffers.buffer(calculateEncodedNLRILength(nlri));
		
		buffer.writeByte(nlri.getPrefixLength());
		if(nlri.getPrefixLength() > 0) {
			buffer.writeBytes(nlri.getPrefix());
		}
		
		return buffer;
	}

}
