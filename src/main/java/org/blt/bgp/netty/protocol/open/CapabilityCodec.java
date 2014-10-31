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
 * File: org.blt.bgp.netty.protocol.open.CapabilityCodec.java 
 */
package org.blt.bgp.netty.protocol.open;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.blt.bgp.net.AddressFamily;
import org.blt.bgp.net.ORFSendReceive;
import org.blt.bgp.net.ORFType;
import org.blt.bgp.net.SubsequentAddressFamily;
import org.blt.bgp.net.capabilities.AutonomousSystem4Capability;
import org.blt.bgp.net.capabilities.Capability;
import org.blt.bgp.net.capabilities.MultiProtocolCapability;
import org.blt.bgp.net.capabilities.OutboundRouteFilteringCapability;
import org.blt.bgp.net.capabilities.RouteRefreshCapability;
import org.blt.bgp.net.capabilities.UnknownCapability;
import org.blt.bgp.netty.BGPv4Constants;
import org.blt.bgp.netty.fsm.InternalFSM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilityCodec {

	private static final Logger log = LoggerFactory.getLogger(InternalFSM.class);
	
	public static List<Capability> decodeCapabilities(ChannelBuffer buffer) {
		List<Capability> caps = new LinkedList<Capability>();
		
		while(buffer.readable()) {
			caps.add(decodeCapability(buffer));
		}
		
		return caps;
	}

	public static Capability decodeCapability(ChannelBuffer buffer) { 
		Capability cap = null;
	
		try {
			buffer.markReaderIndex();
			
			int type = buffer.readUnsignedByte();
			
			switch(type) {
			case BGPv4Constants.BGP_CAPABILITY_TYPE_MULTIPROTOCOL:
				cap = decodeMultiProtocolCapability(buffer);
				break;
			case BGPv4Constants.BGP_CAPABILITY_TYPE_ROUTE_REFRESH:
				cap = decodeRouteRefreshCapability(buffer);
				break;
			case BGPv4Constants.BGP_CAPABILITY_TYPE_AS4_NUMBERS:
				cap = decodeAutonomousSystem4Capability(buffer);
				break;
			case BGPv4Constants.BGP_CAPABILITY_TYPE_OUTBOUND_ROUTE_FILTERING:
				cap = decodeOutboundRouteFilteringCapability(buffer);
				break;
			default:
				cap = decodeUnknownCapability(type, buffer);
				break;
			}
		} catch(CapabilityException e) {
			buffer.resetReaderIndex();
			
			int type = buffer.readUnsignedByte();
			int capLength = buffer.readUnsignedByte();
			
			byte[] capPacket = new byte[capLength+2];
			
			buffer.readBytes(capPacket, 2, capLength);
			capPacket[0] = (byte)type;
			capPacket[1] = (byte)capLength;
			
			e.setCapability(capPacket);
			throw e;
		}
		
		return cap;
	}

	private static Capability decodeUnknownCapability(int type, ChannelBuffer buffer) {
		UnknownCapability cap = new UnknownCapability();
		
		cap.setCapabilityType(type);
		int parameterLength = buffer.readUnsignedByte();
		
		try {
			if(parameterLength > 0) {
				byte[] value = new byte[parameterLength];
			
				buffer.readBytes(value);
				cap.setValue(value);
			}
		} catch (Exception e) {
			log.error("Unable to decode unknown capability sent in notify msg by peer");
			return null;
		}
		return cap;
	}

	private static Capability decodeOutboundRouteFilteringCapability(ChannelBuffer buffer) {
		OutboundRouteFilteringCapability cap = new OutboundRouteFilteringCapability();
		
		assertMinimalLength(buffer, 5); // 2 octest AFI + 1 octet reserved + 1 octet SAFI + 1 octet number of (ORF type, Send/Receive) tuples
		
		cap.setAddressFamily(AddressFamily.fromCode(buffer.readUnsignedShort()));
		buffer.readByte();
		cap.setSubsequentAddressFamily(SubsequentAddressFamily.fromCode(buffer.readUnsignedByte()));
		
		int orfs = buffer.readUnsignedByte();
		
		if(buffer.readableBytes() != 2*orfs)
			throw new UnspecificOpenPacketException("Expected " + (2*orfs) + " octets parameter, got " + buffer.readableBytes() + " octets");
		
		try {
			cap.getFilters().put(ORFType.fromCode(buffer.readUnsignedByte()), ORFSendReceive.fromCode(buffer.readUnsignedByte()));
		} catch(IllegalArgumentException e) {
			throw new UnspecificOpenPacketException(e);
		}
		return cap;
	}

	private static Capability decodeAutonomousSystem4Capability(ChannelBuffer buffer) {
		AutonomousSystem4Capability cap = new AutonomousSystem4Capability();
		
		assertFixedLength(buffer, BGPv4Constants.BGP_CAPABILITY_LENGTH_AS4_NUMBERS);
		cap.setAutonomousSystem((int)buffer.readUnsignedInt());

		return cap;
	}

	private static Capability decodeRouteRefreshCapability(ChannelBuffer buffer) {
		RouteRefreshCapability cap = new RouteRefreshCapability();
		
		assertEmptyParameter(buffer);

		return cap;
	}

	private static Capability decodeMultiProtocolCapability(ChannelBuffer buffer) {
		MultiProtocolCapability cap = new MultiProtocolCapability();
		
		assertFixedLength(buffer, BGPv4Constants.BGP_CAPABILITY_LENGTH_MULTIPROTOCOL);
		
		cap.setAfi(AddressFamily.fromCode(buffer.readShort()));
		buffer.readByte(); // reserved
		cap.setSafi(SubsequentAddressFamily.fromCode(buffer.readByte()));

		return cap;
	}

	public static ChannelBuffer encodeCapabilities(Collection<Capability> caps) {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);
		
		if(caps != null) {
			for (Capability cap : caps)
				buffer.writeBytes(encodeCapability(cap));
		}
		
		return buffer;
	}

	public static ChannelBuffer encodeCapability(Capability cap) {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_CAPABILITY_HEADER_LENGTH + BGPv4Constants.BGP_CAPABILITY_MAX_VALUE_LENGTH);
		ChannelBuffer value = null;
		int capType = -1;
		
		if(cap instanceof MultiProtocolCapability) {
			value = encodeMultiprotocolCapability((MultiProtocolCapability)cap);
			capType = BGPv4Constants.BGP_CAPABILITY_TYPE_MULTIPROTOCOL;
		} else if(cap instanceof RouteRefreshCapability) {
			value = encodeRouteRefreshCapability((RouteRefreshCapability)cap);
			capType = BGPv4Constants.BGP_CAPABILITY_TYPE_ROUTE_REFRESH;
		} else if(cap instanceof AutonomousSystem4Capability) {
			value = encodeAutonomousSystem4Capability((AutonomousSystem4Capability)cap);
			capType = BGPv4Constants.BGP_CAPABILITY_TYPE_AS4_NUMBERS;
		} else if(cap instanceof OutboundRouteFilteringCapability) {
			value = encodeOutboundRouteFilteringCapability((OutboundRouteFilteringCapability)cap);
			capType = BGPv4Constants.BGP_CAPABILITY_TYPE_OUTBOUND_ROUTE_FILTERING;
		} else if(cap instanceof UnknownCapability) {
			value = encodeUnknownCapability((UnknownCapability)cap);
			capType = ((UnknownCapability)cap).getCapabilityType();
		}
		
		int valueSize = (value != null) ? value.readableBytes() : 0;
		
		buffer.writeByte(capType);
		buffer.writeByte(valueSize);
		if(value != null)
			buffer.writeBytes(value);
		
		return buffer;

	}

	private static ChannelBuffer encodeUnknownCapability(UnknownCapability cap) {
		ChannelBuffer buffer = null;
		
		if(cap.getValue() != null && cap.getValue().length > 0) {
			buffer = ChannelBuffers.buffer(cap.getValue().length);
			
			buffer.writeBytes(cap.getValue());
		}
		
		return buffer;
	}

	private static ChannelBuffer encodeOutboundRouteFilteringCapability(
			OutboundRouteFilteringCapability cap) {
		ChannelBuffer buffer = ChannelBuffers.buffer(5 + 2*cap.getFilters().size());
		
		buffer.writeShort(cap.getAddressFamily().toCode());
		buffer.writeByte(0);
		buffer.writeByte(cap.getSubsequentAddressFamily().toCode());
		buffer.writeByte(cap.getFilters().size());
		
		for(ORFType type : cap.getFilters().keySet()) {
			buffer.writeByte(type.toCode());
			buffer.writeByte(cap.getFilters().get(type).toCode());
		}
		
		return buffer;
	}

	private static ChannelBuffer encodeAutonomousSystem4Capability(AutonomousSystem4Capability cap) {
		ChannelBuffer buffer = ChannelBuffers.buffer(4);
		
		buffer.writeInt(cap.getAutonomousSystem());
		
		return buffer;
	}

	private static ChannelBuffer encodeRouteRefreshCapability(RouteRefreshCapability cap) {
		return null;
	}

	private static ChannelBuffer encodeMultiprotocolCapability(
			MultiProtocolCapability cap) {
		ChannelBuffer buffer = ChannelBuffers.buffer(4);
		
		if(cap.getAfi() != null)
			buffer.writeShort(cap.getAfi().toCode());
		else
			buffer.writeShort(AddressFamily.RESERVED.toCode());
		
		buffer.writeByte(0); // reserved
		
		if(cap.getSafi() != null)
			buffer.writeByte(cap.getSafi().toCode());
		else
			buffer.writeByte(0);
		
		return buffer;
	}
	
	private static final void assertEmptyParameter(ChannelBuffer buffer) {
		int parameterLength = buffer.readUnsignedByte();
		
		if(parameterLength != 0)
			throw new UnspecificOpenPacketException("Expected zero-length parameter, got " + parameterLength + " octets");
	}

	private static final void assertFixedLength(ChannelBuffer buffer, int length) {
		int parameterLength = buffer.readUnsignedByte();
		
		if(parameterLength != length)
			throw new UnspecificOpenPacketException("Expected " + length + " octets parameter, got " + parameterLength + " octets");
	}

	private static final void assertMinimalLength(ChannelBuffer buffer, int length) {
		int parameterLength = buffer.readUnsignedByte();
		
		if(parameterLength < length)
			throw new UnspecificOpenPacketException("Expected " + length + " octets parameter, got " + parameterLength + " octets");
	}

}
