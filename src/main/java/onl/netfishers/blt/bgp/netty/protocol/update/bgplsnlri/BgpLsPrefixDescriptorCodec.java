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
package onl.netfishers.blt.bgp.netty.protocol.update.bgplsnlri;

import onl.netfishers.blt.bgp.net.AddressFamily;
import onl.netfishers.blt.bgp.net.attributes.bgplsnlri.BgpLsPrefixDescriptor;
import onl.netfishers.blt.bgp.net.attributes.bgplsnlri.BgpLsType;
import onl.netfishers.blt.bgp.net.attributes.bgplsnlri.IPPrefix;
import onl.netfishers.blt.bgp.net.attributes.bgplsnlri.BgpLsPrefixDescriptor.OspfRouteType;
import onl.netfishers.blt.bgp.netty.protocol.update.OptionalAttributeErrorException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decodes a link descriptor tlv
 * @author nitinb
 *
 */
public class BgpLsPrefixDescriptorCodec {
	private static final Logger log = LoggerFactory.getLogger(BgpLsPrefixDescriptorCodec.class);


	public static void decodePrefixDescriptor(ChannelBuffer buffer, BgpLsPrefixDescriptor pd,
			AddressFamily addressFamily) {
		
		while(buffer.readable()) {
			int type = buffer.readUnsignedShort();
			int valueLength = buffer.readUnsignedShort();
			
			ChannelBuffer valueBuffer = ChannelBuffers.buffer(valueLength);
			buffer.readBytes(valueBuffer);
			
			switch(BgpLsType.fromCode(type)) {
			case MultiTopologyID:
				decodePrefixMultiTopologyId(valueBuffer, pd);
				break;
			case OSPFRouteType:
				decodeOspfRouteType(valueBuffer, pd);
				break;
			case IPReachabilityInformation:
				decodeIpPrefix(valueBuffer, pd, addressFamily);
				break;
			
			default:
				log.error("Unsupported descriptor type {}", type);
				break;
			}
		}
	}

	/**
	 * Decodes the multi-topology id sub-tlv
	 * @param buffer Data stream containing the sub-tlv
	 * @param ld Link descriptor to update
	 */
	private static void decodePrefixMultiTopologyId(ChannelBuffer buffer,
			BgpLsPrefixDescriptor pd) {
		try {
			if (buffer.readableBytes() != 2) {
				log.error("Invalid length {} for prefix multi-topology id", buffer.readableBytes());
				throw new OptionalAttributeErrorException();
			}
			int multiTopologyId = buffer.readUnsignedShort();
			pd.setMultiTopologyId(multiTopologyId);
			
		} catch(RuntimeException e) {
			log.error("failed to decode link multi-topology id for link descriptor", e);
			throw new OptionalAttributeErrorException();
		}				
	}
	
	private static void decodeOspfRouteType(ChannelBuffer buffer,
			BgpLsPrefixDescriptor pd) {
		try {
			if (buffer.readableBytes() != 1) {
				log.error("Invalid length {} for OSPF route type", buffer.readableBytes());
				throw new OptionalAttributeErrorException();
			}
			int code = buffer.readUnsignedByte();
			pd.setOspfRouteType(OspfRouteType.fromCode(code));
		} catch(RuntimeException e) {
			log.error("failed to decode link multi-topology id for link descriptor", e);
			throw new OptionalAttributeErrorException();
		}
	}
	
	private static void decodeIpPrefix(ChannelBuffer buffer, BgpLsPrefixDescriptor pd,
			AddressFamily addressFamily) {
		try {
			short prefixLength = buffer.readUnsignedByte();
			int dataLength = buffer.readableBytes();
			if (dataLength * 8 < prefixLength) {
				log.error("Not enough data ({} bytes) for this prefix length ({} bits)",
						dataLength, prefixLength);
				throw new OptionalAttributeErrorException();
			}
			
			byte[] prefix = new byte[dataLength];
			buffer.readBytes(prefix);
			pd.addPrefix(new IPPrefix(prefixLength, prefix, addressFamily));
		} catch(RuntimeException e) {
			log.error("failed to decode link multi-topology id for link descriptor", e);
			throw new OptionalAttributeErrorException();
		}
	}

}
