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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.blt.bgp.net.attributes.bgplsnlri.BgpLsLinkDescriptor;
import org.blt.bgp.net.attributes.bgplsnlri.BgpLsType;
import org.blt.bgp.netty.protocol.update.OptionalAttributeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decodes a link descriptor tlv
 * @author nitinb
 *
 */
public class BgpLsLinkDescriptorCodec {
	private static final Logger log = LoggerFactory.getLogger(BgpLsNodeDescriptorCodec.class);

	/**
	 * Decodes a link descriptor tlv and updates the object provided as input param
	 * @param buffer Data stream of data containing the tlv
	 * @param ld link descriptor to update
	 */
	public static void decodeLinkDescriptor(ChannelBuffer buffer, BgpLsLinkDescriptor ld) {
		
		while(buffer.readable()) {
			int type = buffer.readUnsignedShort();
			int valueLength = buffer.readUnsignedShort();
			
			ChannelBuffer valueBuffer = ChannelBuffers.buffer(valueLength);
			buffer.readBytes(valueBuffer);
			
			switch(BgpLsType.fromCode(type)) {
			case LinkLocalRemoteIdentifiers:
				decodeLinkIdentifiers(valueBuffer, ld);
				break;
			case IPv4InterfaceAddress:
				decodeIPv4InterfaceAddress(valueBuffer, ld);
				break;
			case IPv4NeighborAddress:
				decodeIPv4NeighborAddress(valueBuffer, ld);
				break;
			case IPv6InterfaceAddress:
				decodeIPv6InterfaceAddress(valueBuffer, ld);
				break;
			case IPv6NeighborAddress:
				decodeIPv6NeighborAddress(valueBuffer, ld);
				break;
			case MultiTopologyID:
				decodeLinkMultiTopologyId(valueBuffer, ld);
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
	private static void decodeLinkMultiTopologyId(ChannelBuffer buffer,
			BgpLsLinkDescriptor ld) {
		try {
			if (buffer.readableBytes() != 2) {
				log.error("Invalid length {} for link multi-topology id", buffer.readableBytes());
				throw new OptionalAttributeErrorException();
			}
			int multiTopologyId = buffer.readUnsignedShort();
			ld.setMultiTopologyId(multiTopologyId);
			
		} catch(RuntimeException e) {
			log.error("failed to decode link multi-topology id for link descriptor", e);
			throw new OptionalAttributeErrorException();
		}				
	}

	/**
	 * Decodes the IPv6 neighbor address sub-tlv
	 * @param buffer Data stream containing the sub-tlv
	 * @param ld Link descriptor to update
	 */
	private static void decodeIPv6NeighborAddress(ChannelBuffer buffer,
			BgpLsLinkDescriptor ld) {
		try {
			if (buffer.readableBytes() != 16) {
				log.error("Invalid length (" + buffer.readableBytes() + ") for ipv6 neighbor address");
				throw new OptionalAttributeErrorException();
			}
			byte[] neighborAddress = new byte[16];
			buffer.readBytes(neighborAddress);
			InetAddress ipaddress = Inet6Address.getByAddress(neighborAddress);
			ld.setIPv6NeighborAddress(ipaddress);
			
		} catch(RuntimeException e) {
			log.error("failed to decode ipv6 neighbor address link descriptor", e);
			throw new OptionalAttributeErrorException();
		} catch(Exception e) {
			log.error("failed to decode ipv6 neighbor address link descriptor", e);
		}
	}

	/**
	 * Decodes the IPv6 interface address sub-tlv
	 * @param buffer Data stream containing the sub-tlv
	 * @param ld Link descriptor to update
	 */
	private static void decodeIPv6InterfaceAddress(ChannelBuffer buffer,
			BgpLsLinkDescriptor ld) {
		try {
			if (buffer.readableBytes() != 16) {
				log.error("Invalid length (" + buffer.readableBytes() + ") for ipv6 interface address");
				throw new OptionalAttributeErrorException();
			}
			byte[] interfaceAddress = new byte[16];
			buffer.readBytes(interfaceAddress);
			InetAddress ipaddress = Inet6Address.getByAddress(interfaceAddress);
			ld.setIPv6InterfaceAddress(ipaddress);
			
		} catch(RuntimeException e) {
			log.error("failed to decode ipv6 interface address link descriptor", e);
			throw new OptionalAttributeErrorException();
		} catch(Exception e) {
			log.error("failed to decode ipv6 interface address link descriptor", e);
		}		
	}

	/**
	 * Decodes the IPv4 neighbor address sub-tlv
	 * @param buffer Data stream containing the sub-tlv
	 * @param ld Link descriptor to update
	 */
	private static void decodeIPv4NeighborAddress(ChannelBuffer buffer,
			BgpLsLinkDescriptor ld) {
		try {
			if (buffer.readableBytes() != 4) {
				log.error("Invalid length (" + buffer.readableBytes() + ") for ipv4 neighbor address");
				throw new OptionalAttributeErrorException();
			}
			byte[] neighborAddress = new byte[4];
			buffer.readBytes(neighborAddress);
			InetAddress ipaddress = Inet4Address.getByAddress(neighborAddress);
			ld.setIPv4NeighborAddress(ipaddress);
			
		} catch(RuntimeException e) {
			log.error("failed to decode ipv4 neighbor address link descriptor", e);
			throw new OptionalAttributeErrorException();
		} catch(Exception e) {
			log.error("failed to decode ipv4 neighbor address link descriptor", e);
		}		
	}

	/**
	 * Decodes the IPv4 interface address sub-tlv
	 * @param buffer Data stream containing the sub-tlv
	 * @param ld Link descriptor to update
	 */
	private static void decodeIPv4InterfaceAddress(ChannelBuffer buffer,
			BgpLsLinkDescriptor ld) {
		try {
			if (buffer.readableBytes() != 4) {
				log.error("Invalid length (" + buffer.readableBytes() + ") for ipv4 interface address");
				throw new OptionalAttributeErrorException();
			}
			byte[] interfaceAddress = new byte[4];
			buffer.readBytes(interfaceAddress);
			InetAddress ipaddress = Inet4Address.getByAddress(interfaceAddress);
			ld.setIPv4InterfaceAddress(ipaddress);
			
		} catch(RuntimeException e) {
			log.error("failed to decode ipv4 interface address link descriptor", e);
			throw new OptionalAttributeErrorException();
		} catch(Exception e) {
			log.error("failed to decode ipv4 interface address link descriptor", e);
		}		
	}

	/**
	 * Decodes the link identifiers
	 * @param buffer Data stream containing the sub-tlv
	 * @param ld Link descriptor to update
	 */
	private static void decodeLinkIdentifiers(ChannelBuffer buffer,
			BgpLsLinkDescriptor ld) {
		try {
			if (buffer.readableBytes() != 8) {
				log.error("Invalid length (" + buffer.readableBytes() + ") for link identifier");
				throw new OptionalAttributeErrorException();
			}
			long linkIdentifier;
			linkIdentifier = buffer.readUnsignedInt();
			ld.setLinkLocalIdentifier(linkIdentifier);
			linkIdentifier = buffer.readUnsignedInt();
			ld.setLinkRemoteIdentifier(linkIdentifier);
			
		} catch(RuntimeException e) {
			log.error("failed to decode link identifier link descriptor", e);
			throw new OptionalAttributeErrorException();
		}
	}
}
