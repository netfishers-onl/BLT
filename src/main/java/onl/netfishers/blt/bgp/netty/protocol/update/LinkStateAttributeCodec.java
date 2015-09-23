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
package onl.netfishers.blt.bgp.netty.protocol.update;

import onl.netfishers.blt.bgp.net.attributes.LinkStateAttribute;
import onl.netfishers.blt.bgp.net.attributes.bgplsnlri.BgpLsType;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nitinb
 *
 */
public class LinkStateAttributeCodec {

	private static final Logger log = LoggerFactory.getLogger(UpdatePacketDecoder.class);

	public LinkStateAttributeCodec() {}

	public void decodeAttr(LinkStateAttribute attr, int type, ChannelBuffer buffer) {
		switch (BgpLsType.fromCode(type)) {
		
		// Router Attribute TLVs
		case MultiTopologyID:
			decodeMultiTopologyId(attr, buffer);
			break;
		case NodeFlagBits:
			decodeNodeFlagBits(attr, buffer);
			break;
		case OpaqueNodeProperties:
			attr.setOpaqueNodeProperties(decodeOpaqueAttribute(buffer));
			break;
		case NodeName:
			decodeNodeName(attr, buffer);
			break;
		case IsisAreaIdentifier:
			decodeIsisAreaIdentifier(attr, buffer);
			break;
		case LocalNodeIPv4RouterID:
			decodeLocalNodeIPv4RouterID(attr, buffer);
			break;
		case LocalNodeIPv6RouterID:
			decodeLocalNodeIPv6RouterID(attr, buffer);
			break;
		
		// Link Attribute TLVs
		case RemoteNodeIPv4RouterID:
			decodeRemoteNodeIPv4RouterID(attr, buffer);
			break;
		case RemoteNodeIPv6RouterID:
			decodeRemoteNodeIPv6RouterID(attr, buffer);
			break;
		case AdministrativeGroup:
			decodeAdminGroup(attr, buffer);
			break;
		case MaximumLinkBandwith:
			decodeMaxLinkBandwidth(attr, buffer);
			break;
		case MaxReservableLinkBandwidth:
			decodeMaxReservableLinkBandwidth(attr, buffer);
			break;
		case UnreservedBandwidth:
			decodeUnreservedBandwidth(attr, buffer);
			break;
		case TEDefaultMetric:
			decodeTEDefaultMetric(attr, buffer);
			break;
		case LinkProtectionType:
			decodeLinkProtectionType(attr, buffer);
			break;
		case MPLSProtocolMask:
			decodeMPLSProtocolMask(attr, buffer);
			break;
		case Metric:
			decodeMetric(attr, buffer);
			break;
		case SharedRiskLinkGroup:
			decodeSharedRiskLinkGroup(attr, buffer);
			break;
		case OpaqueLinkAttribute:
			attr.setOpaqueLinkAttribute(decodeOpaqueAttribute(buffer));
			break;
		case LinkNameAttribute:
			break;
		
		// Prefix Attribute TLVs
		case IGPFlags:
			decodeIgpFlags(attr, buffer);
			break;
		case RouteTag:
			decodeRouteTag(attr, buffer);
			break;
		case ExtendedTag:
			decodeExtendedTag(attr, buffer);
			break;
		case PrefixMetric:
			decodePrefixMetric(attr, buffer);
			break;
		case OSPFForwardingAddress:
			decodeOspfForwardingAddress(attr, buffer);
			break;
		case OpaquePrefixAttribute:
			attr.setOpaquePrefixAttribute(decodeOpaqueAttribute(buffer));
			break;

	
		case Unknown:
		default:
			log.info("Unknown Link state attribute sub-type {}", type);
			break;
		}
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeOspfForwardingAddress(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			byte[] ospfForwardingAddress = new byte[length];
			
			buffer.readBytes(ospfForwardingAddress);
			attr.setOspfForwardingAddress(ospfForwardingAddress);
			
		} catch(RuntimeException e) {
			log.error("failed to decode OSPF forwarding address LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodePrefixMetric(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			if (buffer.readableBytes() != 4) {
				log.error("Invalid length ({}) for prefix metric", buffer.readableBytes());
				throw new OptionalAttributeErrorException();
			}
			long prefixMetric = buffer.readUnsignedInt();
			attr.setPrefixMetric(prefixMetric);
			
		} catch(RuntimeException e) {
			log.error("failed to decode Prefix metric LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}				
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeExtendedTag(LinkStateAttribute attr, ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			if (buffer.readableBytes() != 8) {
				log.error("Invalid length ({}) for extended tag", buffer.readableBytes());
				throw new OptionalAttributeErrorException();
			}
			byte[] extendedTag = new byte[length];
			
			buffer.readBytes(extendedTag);
			attr.setExtendedTag(extendedTag);
			
		} catch(RuntimeException e) {
			log.error("failed to decode Extended tag LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}				
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeRouteTag(LinkStateAttribute attr, ChannelBuffer buffer) {
		try {
			if (buffer.readableBytes() != 4) {
				log.error("Invalid length ({}) for route tag", buffer.readableBytes());
				throw new OptionalAttributeErrorException();
			}
			long routeTag = buffer.readUnsignedInt();
			attr.setRouteTag(routeTag);
			
		} catch(RuntimeException e) {
			log.error("failed to decode Route tag LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}				
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeIgpFlags(LinkStateAttribute attr, ChannelBuffer buffer) {
		try {
			if (buffer.readableBytes() != 1) {
				log.error("Invalid length ({}) for IGP flags", buffer.readableBytes());
				throw new OptionalAttributeErrorException();
			}
			short igpFlags = buffer.readUnsignedByte();
			attr.setIgpFlags(igpFlags);
			
		} catch(RuntimeException e) {
			log.error("failed to decode IGP Flags LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeNodeFlagBits(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			if (buffer.readableBytes() != 1) {
				log.error("Invalid length (" + buffer.readableBytes() + ") for node flag bits");
				throw new OptionalAttributeErrorException();
			}
			short nodeFlagBits = buffer.readUnsignedByte();
			attr.setNodeFlagBits(nodeFlagBits);
			
		} catch(RuntimeException e) {
			log.error("failed to decode MPLS protocol mask LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
		
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeMultiTopologyId(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			if (length % 2 != 0) {
				log.error("Invalid length ({}) for node multi-topology id", length);
				throw new OptionalAttributeErrorException();
			}
			int[] nodeMultiTopologyIds = new int[length];
			for (int i = 0; i < length / 2; i++) {
				nodeMultiTopologyIds[i] = buffer.readUnsignedShort();
			}
			attr.setMultiTopologyId(nodeMultiTopologyIds);
			
		} catch(RuntimeException e) {
			log.error("failed to decode MPLS protocol mask LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeSharedRiskLinkGroup(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		
		try {
			int bufferSize = buffer.readableBytes();
			if (bufferSize % 4 != 0) {
				log.error("SRLG link attribute length ({}) is not a multiple of 4", bufferSize);
				throw new OptionalAttributeErrorException();
			}
			int numGroups = bufferSize / 4;
			
			for(int i = 0; i < numGroups; i++) {
				attr.addSharedRiskLinkGroup(buffer.readUnsignedInt());
			}
		} catch(RuntimeException e) {
			log.error("failed to decode SRLG LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeMetric(LinkStateAttribute attr, ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			if (length == 1) {
				int metric = buffer.readUnsignedByte();
				attr.setMetric(metric);
			}
			else if (length == 2) {
				int metric = buffer.readUnsignedShort();
				attr.setMetric(metric);
			}
			else if (length == 3) {
				int metric = buffer.readUnsignedShort();
				metric = (metric << 8) | buffer.readUnsignedByte();
				attr.setMetric(metric);
			}
			else {
				log.error("Invalid length ({}) for metric", buffer.readableBytes());
				throw new OptionalAttributeErrorException();
			}
			
		} catch(RuntimeException e) {
			log.error("failed to decode Metric LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeMPLSProtocolMask(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			if (buffer.readableBytes() != 1) {
				log.error("Invalid length (" + buffer.readableBytes() + ") for MPLS protocol mask");
				throw new OptionalAttributeErrorException();
			}
			short mplsProtocolMask = buffer.readUnsignedByte();
			attr.setMplsProtocolMask(mplsProtocolMask);
			
		} catch(RuntimeException e) {
			log.error("failed to decode MPLS protocol mask LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeLinkProtectionType(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			if (buffer.readableBytes() != 2) {
				log.error("Invalid length (" + buffer.readableBytes() + ") for link protection type");
				throw new OptionalAttributeErrorException();
			}
			
			short linkProtectionType = buffer.readUnsignedByte();
			attr.setLinkProtectionType(linkProtectionType);
			
		} catch(RuntimeException e) {
			log.error("failed to decode Link protection type LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeTEDefaultMetric(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			if (length == 1) {
				int teDefaultMetric = buffer.readUnsignedByte();
				attr.setTeDefaultMetric(teDefaultMetric);
			}
			else if (length == 2) {
				int teDefaultMetric = buffer.readUnsignedShort();
				attr.setTeDefaultMetric(teDefaultMetric);
			}
			else if (length == 3) {
				int teDefaultMetric = buffer.readUnsignedShort();
				teDefaultMetric = (teDefaultMetric << 8) | buffer.readUnsignedByte();
				attr.setTeDefaultMetric(teDefaultMetric);
			}
			else {
				log.error("Invalid length ({}) for teDefaultMetric", buffer.readableBytes());
				throw new OptionalAttributeErrorException();
			}
			
		} catch(RuntimeException e) {
			log.error("failed to decode TE default metric LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeUnreservedBandwidth(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			if (buffer.readableBytes() != 8*4) {
				log.error("Invalid length (" + buffer.readableBytes() + ") for Unreserved bandwidth");
				throw new OptionalAttributeErrorException();
			}
			
			float[] unreservedBandwidth = new float[8];
			for (int i = 0; i < 8; i++) {
				unreservedBandwidth[i] = readIEEEfloat(buffer) * 8;
			}
			attr.setUnreservedBandwidth(unreservedBandwidth);
			
		} catch(RuntimeException e) {
			log.error("failed to decode Unreserved bandwidth LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeMaxLinkBandwidth(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			if (buffer.readableBytes() != 4) {
				log.error("Invalid length ({}) for max link bandwidth", buffer.readableBytes());
				throw new OptionalAttributeErrorException();
			}
			attr.setMaxLinkBandwidth(readIEEEfloat(buffer) * 8);
			
		} catch(RuntimeException e) {
			log.error("failed to decode Max link bandwidth LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}

	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeMaxReservableLinkBandwidth(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			if (buffer.readableBytes() != 4) {
				log.error("Invalid length (" + buffer.readableBytes() + ") for max link bandwidth");
				throw new OptionalAttributeErrorException();
			}
			attr.setMaxReservableLinkBandwidth(readIEEEfloat(buffer) * 8);
			
		} catch(RuntimeException e) {
			log.error("failed to decode Max link bandwidth LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}
	
	/**
	 * @param attr
	 * @param buffer
	 */
	private void decodeAdminGroup(LinkStateAttribute attr, ChannelBuffer buffer) {
		try {
			if (buffer.readableBytes() != 4) {
				log.error("Invalid length ({}) for admin group", buffer.readableBytes());
				throw new OptionalAttributeErrorException();
			}
			
			long adminGroup = buffer.readUnsignedInt();
			attr.setAdminGroup(adminGroup);
			
		} catch(RuntimeException e) {
			log.error("failed to decode Admin group LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}
	
	/**
	 * Reads a floating point value encoded in IEEE format and then
	 * converts it to a regular float
	 * @param buffer
	 * @return java float
	 */
	private float readIEEEfloat(ChannelBuffer buffer) {
		int value = buffer.readInt();
		return Float.intBitsToFloat(value);
	}
	
	
	private void decodeLocalNodeIPv4RouterID(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			byte[] routerId = new byte[length];
			
			buffer.readBytes(routerId);
			attr.addLocalNodeIPv4RouterID(routerId);
			
		} catch(RuntimeException e) {
			log.error("Failed to decode local node IPv4 Router ID LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}
	
	private void decodeLocalNodeIPv6RouterID(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			byte[] routerId = new byte[length];
			
			buffer.readBytes(routerId);
			attr.addLocalNodeIPv6RouterID(routerId);
			
		} catch(RuntimeException e) {
			log.error("Failed to decode local node IPv6 Router ID LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}
	
	private void decodeRemoteNodeIPv4RouterID(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			byte[] routerId = new byte[length];
			
			buffer.readBytes(routerId);
			attr.addRemoteNodeIPv4RouterID(routerId);
			
		} catch(RuntimeException e) {
			log.error("Failed to decode remote node IPv4 Router ID LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}
	
	private void decodeRemoteNodeIPv6RouterID(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			byte[] routerId = new byte[length];
			
			buffer.readBytes(routerId);
			attr.addRemoteNodeIPv6RouterID(routerId);
			
		} catch(RuntimeException e) {
			log.error("Failed to decode remote node IPv6 Router ID LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}
	
	private byte[] decodeOpaqueAttribute(ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			byte[] opaque = new byte[length];
			
			buffer.readBytes(opaque);
			return opaque;
			
		} catch(RuntimeException e) {
			log.error("Failed to decode remote node IPv6 Router ID LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}		
	}
	private void decodeIsisAreaIdentifier(LinkStateAttribute attr,
			ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			byte[] isisAreaIdentifier = new byte[length];
			
			buffer.readBytes(isisAreaIdentifier);
			attr.addIsisAreaIdentifier(isisAreaIdentifier);
			
		} catch(RuntimeException e) {
			log.error("Failed to decode Isis Area Identifier attribute", e);
			throw new OptionalAttributeErrorException();
		}
		
	}

	private void decodeNodeName(LinkStateAttribute attr, ChannelBuffer buffer) {
		try {
			int length = buffer.readableBytes();
			byte[] bytes = new byte[length];
			
			buffer.readBytes(bytes);
			String nodeName = new String(bytes);
			attr.setNodeName(nodeName);
			
		} catch(RuntimeException e) {
			log.error("Failed to decode Node Name attribute", e);
			throw new OptionalAttributeErrorException();
		}
		
	}

}
