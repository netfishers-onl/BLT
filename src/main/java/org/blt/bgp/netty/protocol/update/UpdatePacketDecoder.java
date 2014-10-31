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
 * File: org.blt.bgp.netty.protocol.update.UpdatePacketDecoder.java 
 */
/**
 * Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
 * 
 * License: same as above
 * 
 * Modified to run as an independent java application, one that does not
 * require webserver or app server.
 * Added support for decoding MP-BGP NLRIs
 */
package org.blt.bgp.netty.protocol.update;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.blt.bgp.BgpService;
import org.blt.bgp.net.ASType;
import org.blt.bgp.net.AddressFamily;
import org.blt.bgp.net.NetworkLayerReachabilityInformation;
import org.blt.bgp.net.PathSegment;
import org.blt.bgp.net.SubsequentAddressFamily;
import org.blt.bgp.net.attributes.ASPathAttribute;
import org.blt.bgp.net.attributes.AggregatorPathAttribute;
import org.blt.bgp.net.attributes.AtomicAggregatePathAttribute;
import org.blt.bgp.net.attributes.ClusterListPathAttribute;
import org.blt.bgp.net.attributes.CommunityMember;
import org.blt.bgp.net.attributes.CommunityPathAttribute;
import org.blt.bgp.net.attributes.LinkStateAttribute;
import org.blt.bgp.net.attributes.LocalPrefPathAttribute;
import org.blt.bgp.net.attributes.MultiExitDiscPathAttribute;
import org.blt.bgp.net.attributes.MultiProtocolNLRI;
import org.blt.bgp.net.attributes.NextHopPathAttribute;
import org.blt.bgp.net.attributes.OriginPathAttribute;
import org.blt.bgp.net.attributes.OriginatorIDPathAttribute;
import org.blt.bgp.net.attributes.PathAttribute;
import org.blt.bgp.net.attributes.PathAttributeType;
import org.blt.bgp.net.attributes.UnknownPathAttribute;
import org.blt.bgp.netty.BGPv4Constants;
import org.blt.bgp.netty.NLRICodec;
import org.blt.bgp.netty.protocol.BGPv4Packet;
import org.blt.bgp.netty.protocol.NotificationPacket;
import org.blt.bgp.netty.protocol.ProtocolPacketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdatePacketDecoder {
	private static final Logger log = LoggerFactory.getLogger(UpdatePacketDecoder.class);
	private static NLRICodec codec = (NLRICodec)BgpService.getInstance(NLRICodec.class.getName());

    public UpdatePacketDecoder () {
    }

	/**
	 * decode the UPDATE network packet. The passed channel buffer MUST point to the first packet octet AFTER the type octet.
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return
	 */
	public BGPv4Packet decodeUpdatePacket(ChannelBuffer buffer) {
		UpdatePacket packet = new UpdatePacket();
		
		ProtocolPacketUtils.verifyPacketSize(buffer, BGPv4Constants.BGP_PACKET_MIN_SIZE_UPDATE, -1);
		
		if(buffer.readableBytes() < 2)
			throw new MalformedAttributeListException();
		
		// handle withdrawn routes
		int withdrawnOctets = buffer.readUnsignedShort();
		
		// sanity checking
		if(withdrawnOctets > buffer.readableBytes())
			throw new MalformedAttributeListException();
		
		ChannelBuffer withdrawnBuffer = null;		
		
		if(withdrawnOctets > 0) {
			withdrawnBuffer = ChannelBuffers.buffer(withdrawnOctets);
			
			buffer.readBytes(withdrawnBuffer);
		}

		// sanity checking
		if(buffer.readableBytes() < 2)
			throw new MalformedAttributeListException();

		// handle path attributes
		int pathAttributeOctets =  buffer.readUnsignedShort();
		
		// sanity checking
		if(pathAttributeOctets > buffer.readableBytes())
			throw new MalformedAttributeListException();
			
		ChannelBuffer pathAttributesBuffer = null;
		
		if(pathAttributeOctets > 0) {
			pathAttributesBuffer = ChannelBuffers.buffer(pathAttributeOctets);
			
			buffer.readBytes(pathAttributesBuffer);
		}
		
		if(withdrawnBuffer != null) {
			try {
				packet.getWithdrawnRoutes().addAll(decodeWithdrawnRoutes(withdrawnBuffer));
			} catch(IndexOutOfBoundsException e) {
				throw new MalformedAttributeListException();
			}
		}

		if(pathAttributesBuffer != null) {
			try {
				packet.getPathAttributes().addAll(decodePathAttributes(pathAttributesBuffer));
			} catch (IndexOutOfBoundsException ex) {
				throw new MalformedAttributeListException();
			}
		}
		
		// handle network layer reachability information
		if(buffer.readableBytes() > 0) {
			try {
				while (buffer.readable()) {
					packet.getNlris().add(codec.decodeNLRI(buffer));
				}
			} catch (IndexOutOfBoundsException e) {
				throw new InvalidNetworkFieldException();
			} catch(IllegalArgumentException e) {
				throw new InvalidNetworkFieldException();				
			}
		}
		
		return packet;
	}

	/**
	 * decode a NOTIFICATION packet that corresponds to UPDATE apckets. The passed channel buffer MUST point to the first packet octet AFTER the terror sub code.
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return
	 */
	public NotificationPacket decodeUpdateNotification(ChannelBuffer buffer, int errorSubcode) {
		UpdateNotificationPacket packet = null;
		byte[] offendingAttribute = null;

		if(buffer.readable()) {
			offendingAttribute = new byte[buffer.readableBytes()];

			buffer.readBytes(offendingAttribute);
		}

		switch(errorSubcode) {
		case UpdateNotificationPacket.SUBCODE_MALFORMED_ATTRIBUTE_LIST:
			packet = new MalformedAttributeListNotificationPacket();
			break;
		case UpdateNotificationPacket.SUBCODE_UNRECOGNIZED_WELL_KNOWN_ATTRIBUTE:
			packet = new UnrecognizedWellKnownAttributeNotificationPacket(offendingAttribute);
			break;
		case UpdateNotificationPacket.SUBCODE_MISSING_WELL_KNOWN_ATTRIBUTE:
			packet = new MissingWellKnownAttributeNotificationPacket(0);
			break;
		case UpdateNotificationPacket.SUBCODE_ATTRIBUTE_FLAGS_ERROR:
			packet = new AttributeFlagsNotificationPacket(offendingAttribute);
			break;
		case UpdateNotificationPacket.SUBCODE_ATTRIBUTE_LENGTH_ERROR:
			packet = new AttributeLengthNotificationPacket(offendingAttribute);
			break;
		case UpdateNotificationPacket.SUBCODE_INVALID_ORIGIN_ATTRIBUTE:
			packet = new InvalidOriginNotificationPacket(offendingAttribute);
			break;
		case UpdateNotificationPacket.SUBCODE_INVALID_NEXT_HOP_ATTRIBUTE:
			packet = new InvalidNextHopNotificationPacket(offendingAttribute);
			break;
		case UpdateNotificationPacket.SUBCODE_OPTIONAL_ATTRIBUTE_ERROR:
			packet = new OptionalAttributeErrorNotificationPacket(offendingAttribute);
			break;
		case UpdateNotificationPacket.SUBCODE_INVALID_NETWORK_FIELD:
			packet = new InvalidNetworkFieldNotificationPacket();
			break;
		case UpdateNotificationPacket.SUBCODE_MALFORMED_AS_PATH:
			packet = new MalformedASPathAttributeNotificationPacket(offendingAttribute);
			break;
		}
		
		return packet;
	}

	private ASPathAttribute decodeASPathAttribute(ChannelBuffer buffer, ASType asType) {
		ASPathAttribute attr = new ASPathAttribute(asType);

		while(buffer.readable()) {
			if(buffer.readableBytes() < 2)
				throw new MalformedASPathAttributeException();
			
			int segmentType = buffer.readUnsignedByte();
			int pathLength = buffer.readUnsignedByte();
			int pathOctetLength = (pathLength * (asType == ASType.AS_NUMBER_4OCTETS ? 4 : 2));
			
			if(buffer.readableBytes() < pathOctetLength)
				throw new MalformedASPathAttributeException();
			
			PathSegment segment =  new PathSegment(asType);

			try {
				segment.setPathSegmentType(PathSegmentTypeCodec.fromCode(segmentType));
			} catch (IllegalArgumentException e) {
				log.error("cannot convert AS_PATH type", e);

				throw new MalformedASPathAttributeException();
			}
			
			for(int i=0; i<pathLength; i++) {
				if(asType == ASType.AS_NUMBER_4OCTETS)
					segment.getAses().add((int)buffer.readUnsignedInt());
				else
					segment.getAses().add(buffer.readUnsignedShort());
			}
			
			attr.getPathSegments().add(segment);
		}
		
		return attr;
	}

	private OriginPathAttribute decodeOriginPathAttribute(ChannelBuffer buffer) {
		OriginPathAttribute attr = new OriginPathAttribute();
		
		if(buffer.readableBytes() != 1)
			throw new AttributeLengthException();
		
		try {
			attr.setOrigin(OriginCodec.fromCode(buffer.readUnsignedByte()));
		} catch(IllegalArgumentException e) {
			log.error("cannot convert ORIGIN code", e);
			
			throw new InvalidOriginException();
		}
		
		return attr;
	}

	private MultiExitDiscPathAttribute decodeMultiExitDiscPathAttribute(ChannelBuffer buffer) {
		MultiExitDiscPathAttribute attr = new MultiExitDiscPathAttribute();
		
		if(buffer.readableBytes() != 4)
			throw new AttributeLengthException();
		
		attr.setDiscriminator((int)buffer.readUnsignedInt());
		
		return attr;
	}

	private LocalPrefPathAttribute decodeLocalPrefPathAttribute(ChannelBuffer buffer) {
		LocalPrefPathAttribute attr = new LocalPrefPathAttribute();
		
		if(buffer.readableBytes() != 4)
			throw new AttributeLengthException();
		
		attr.setLocalPreference((int)buffer.readUnsignedInt());
		
		return attr;
	}

	private NextHopPathAttribute decodeNextHopPathAttribute(ChannelBuffer buffer) {
		NextHopPathAttribute attr = new NextHopPathAttribute();
		
		if(buffer.readableBytes() != 4)
			throw new AttributeLengthException();
		
		try {
			byte[] addr = new byte[4];
			
			buffer.readBytes(addr);
			attr.setNextHop((Inet4Address)Inet4Address.getByAddress(addr));
		} catch(IllegalArgumentException e) {
			throw new InvalidNextHopException();
		} catch (UnknownHostException e) {
			throw new InvalidNextHopException();
		}
		
		return attr;
	}

	private AtomicAggregatePathAttribute decodeAtomicAggregatePathAttribute(ChannelBuffer buffer) {
		AtomicAggregatePathAttribute attr = new AtomicAggregatePathAttribute();
		
		if(buffer.readableBytes() != 0)
			throw new AttributeLengthException();
		
		return attr;
	}

	private AggregatorPathAttribute decodeAggregatorPathAttribute(ChannelBuffer buffer, ASType asType) {
		AggregatorPathAttribute attr = new AggregatorPathAttribute(asType);
		
		if(buffer.readableBytes() != PathAttributeCodec.valueLength(attr))
			throw new AttributeLengthException();		
		
		if(asType == ASType.AS_NUMBER_4OCTETS)
			attr.setAsNumber((int)buffer.readUnsignedInt());
		else
			attr.setAsNumber(buffer.readUnsignedShort());
		
		try {
			byte[] addr = new byte[4];
			
			buffer.readBytes(addr);
			attr.setAggregator((Inet4Address)Inet4Address.getByAddress(addr));
		} catch (UnknownHostException e) {
			throw new OptionalAttributeErrorException();
		}

		return attr;
	}

	private CommunityPathAttribute decodeCommunityPathAttribute(ChannelBuffer buffer) {
		CommunityPathAttribute attr = new CommunityPathAttribute();
		
		if(buffer.readableBytes() < 4 || (buffer.readableBytes() % 4 != 0))
			throw new OptionalAttributeErrorException();
		
		attr.setCommunity((int)buffer.readUnsignedInt());
		while(buffer.readable()) {
			CommunityMember member = new CommunityMember();
			
			member.setAsNumber(buffer.readUnsignedShort());
			member.setMemberFlags(buffer.readUnsignedShort());
			
			attr.getMembers().add(member);
		}
		
		return attr;
	}

	private MultiProtocolNLRI decodeMpNlriPathAttribute(int type, ChannelBuffer buffer) {
		MultiProtocolNLRI attr = new MultiProtocolNLRI();
		MultiProtocolNLRICodec mpcodec = null;
		
		AddressFamily addressFamily;
		SubsequentAddressFamily subsequentAddressFamily;
		
		addressFamily = AddressFamily.fromCode(buffer.readUnsignedShort());
		subsequentAddressFamily = SubsequentAddressFamily.fromCode(buffer.readUnsignedByte());
		
		/*
		 * Get decoder for decoding information in the NLRI
		 */
		try {
			String classname = MultiProtocolNLRIDecoderTypes.getSubClass(addressFamily, subsequentAddressFamily);
			mpcodec = (MultiProtocolNLRICodec)BgpService.getInstance(classname);
		} catch (Exception e) {
			log.error("Failed to get decode codec for AFI " + addressFamily + "SAFI" + subsequentAddressFamily);
			throw new OptionalAttributeErrorException();
		}
		
		try {
			attr.setAddressFamily(addressFamily);
			attr.setSubsequentAddressFamily(subsequentAddressFamily);
			
			if (type == BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MP_REACH_NLRI) {
				attr.setPathAttributeType(PathAttributeType.MULTI_PROTOCOL_REACHABLE);

				int nextHopLength = buffer.readUnsignedByte();
			
				if(nextHopLength > 0) {
					byte[] nextHop = new byte[nextHopLength];
				
					buffer.readBytes(nextHop);
					attr.setNextHopAddress(nextHop);
				}
						
				buffer.readByte(); // reserved
				
			} else {
				attr.setPathAttributeType(PathAttributeType.MULTI_PROTOCOL_UNREACHABLE);
			}
			
			while(buffer.readable()) {
				attr.getNlris().add(mpcodec.decodeNLRI(buffer));
			}
		} catch(RuntimeException e) {
			log.error("failed to decode MP_REACH_NLRI path attribute", e);
			
			throw new OptionalAttributeErrorException();
		}
		
		return attr;
	}
	
	private OriginatorIDPathAttribute decodeOriginatorIDPathAttribute(ChannelBuffer buffer) {
		OriginatorIDPathAttribute attr = new OriginatorIDPathAttribute();
		
		try {
			attr.setOriginatorID((int)buffer.readUnsignedInt());
		} catch(RuntimeException e) {
			log.error("failed to decode ORIGINATOR_ID attribute", e);
			
			throw new OptionalAttributeErrorException();
		}
		
		return attr;
	}

	private ClusterListPathAttribute decodeClusterListPathAttribute(ChannelBuffer buffer) {
		ClusterListPathAttribute attr = new ClusterListPathAttribute();
		
		try {
			while(buffer.readable()) {
				attr.getClusterIds().add((int)buffer.readUnsignedInt());
			}
		} catch(RuntimeException e) {
			log.error("failed to decode ORIGINATOR_ID attribute", e);
			
			throw new OptionalAttributeErrorException();
		}
		return attr;
	}
	
	/**
	 * @param valueBuffer
	 * @return
	 */
	private PathAttribute decodeLinkStateAttribute(ChannelBuffer buffer) {
		LinkStateAttributeCodec lscodec;
		LinkStateAttribute attr = new LinkStateAttribute();
		int valueLength;
		int type;
		
		lscodec = (LinkStateAttributeCodec)BgpService.getInstance(LinkStateAttributeCodec.class.getName());
		
		try {
			while(buffer.readable()) {
				type = buffer.readUnsignedShort();
				valueLength = buffer.readUnsignedShort();
				
				ChannelBuffer valueBuffer = ChannelBuffers.buffer(valueLength);
				buffer.readBytes(valueBuffer);
				
				lscodec.decodeAttr(attr, type, valueBuffer);
			}
		} catch(RuntimeException e) {
			log.error("failed to decode LINK_STATE attribute", e);
			throw new OptionalAttributeErrorException();
		}
		return attr;
	}

	private List<PathAttribute> decodePathAttributes(ChannelBuffer buffer) {
		List<PathAttribute> attributes = new LinkedList<PathAttribute>();
		
		while(buffer.readable()) {
			buffer.markReaderIndex();
	
			try {
				int flagsType = buffer.readUnsignedShort();
				boolean optional = ((flagsType & BGPv4Constants.BGP_PATH_ATTRIBUTE_OPTIONAL_BIT) != 0);
				boolean transitive = ((flagsType & BGPv4Constants.BGP_PATH_ATTRIBUTE_TRANSITIVE_BIT) != 0);
				boolean partial = ((flagsType & BGPv4Constants.BGP_PATH_ATTRIBUTE_PARTIAL_BIT) != 0);
				int typeCode = (flagsType & BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MASK);
				int valueLength = 0;
	
				if ((flagsType & BGPv4Constants.BGP_PATH_ATTRIBUTE_EXTENDED_LENGTH_BIT) != 0)
					valueLength = buffer.readUnsignedShort();
				else
					valueLength = buffer.readUnsignedByte();
	
				ChannelBuffer valueBuffer = ChannelBuffers.buffer(valueLength);
	
				buffer.readBytes(valueBuffer);
	
				PathAttribute attr = null;
			
				switch (typeCode) {
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AGGREGATOR:
					attr = decodeAggregatorPathAttribute(valueBuffer, ASType.AS_NUMBER_2OCTETS);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_AGGREGATOR:
					attr = decodeAggregatorPathAttribute(valueBuffer, ASType.AS_NUMBER_4OCTETS);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH:
					attr = decodeASPathAttribute(valueBuffer, ASType.AS_NUMBER_4OCTETS);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH:
					attr = decodeASPathAttribute(valueBuffer, ASType.AS_NUMBER_2OCTETS);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ATOMIC_AGGREGATE:
					attr = decodeAtomicAggregatePathAttribute(valueBuffer);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_COMMUNITIES:
					attr = decodeCommunityPathAttribute(valueBuffer);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_LOCAL_PREF:
					attr = decodeLocalPrefPathAttribute(valueBuffer);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MULTI_EXIT_DISC:
					attr = decodeMultiExitDiscPathAttribute(valueBuffer);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_NEXT_HOP:
					attr = decodeNextHopPathAttribute(valueBuffer);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGIN:
					attr = decodeOriginPathAttribute(valueBuffer);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MP_REACH_NLRI:
					attr = decodeMpNlriPathAttribute(typeCode, valueBuffer);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MP_UNREACH_NLRI:
					attr = decodeMpNlriPathAttribute(typeCode, valueBuffer);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_ORIGINATOR_ID:
					attr = decodeOriginatorIDPathAttribute(valueBuffer);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_CLUSTER_LIST:
					attr = decodeClusterListPathAttribute(valueBuffer);
					break;
				case BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_LINK_STATE:
					attr = decodeLinkStateAttribute(valueBuffer);
					break;
				default: 
				    {
						byte[] value = new byte[valueBuffer.readableBytes()];
					
						valueBuffer.readBytes(value);
						attr = new UnknownPathAttribute(typeCode, value);
				    }
					break;
				}
				attr.setOptional(optional);
				attr.setTransitive(transitive);
				attr.setPartial(partial);
				
				attributes.add(attr);
			} catch(AttributeException ex) {
				int endReadIndex = buffer.readerIndex();
				
				buffer.resetReaderIndex();
				
				int attributeLength = endReadIndex - buffer.readerIndex();
				byte[] packet = new byte[attributeLength];
				
				buffer.readBytes(packet);
				ex.setOffendingAttribute(packet);
				
				throw ex;
			} catch(IndexOutOfBoundsException ex) {
				int endReadIndex = buffer.readerIndex();
				
				buffer.resetReaderIndex();
				
				int attributeLength = endReadIndex - buffer.readerIndex();
				byte[] packet = new byte[attributeLength];
				
				buffer.readBytes(packet);
	
				throw new AttributeLengthException(packet);
			}
			
		}
		
		return attributes;
	}

	private List<NetworkLayerReachabilityInformation> decodeWithdrawnRoutes(ChannelBuffer buffer)  {
		List<NetworkLayerReachabilityInformation> routes = new LinkedList<NetworkLayerReachabilityInformation>();
		
		while(buffer.readable()) {
			routes.add(codec.decodeNLRI(buffer));			
		}
		return routes;
	}
}
