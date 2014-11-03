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
 * File: onl.netfishers.blt.bgp.netty.protocol.refresh.RouteRefreshPacketDecoder.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.refresh;

import java.util.LinkedList;
import java.util.List;

import onl.netfishers.blt.bgp.BgpService;
import onl.netfishers.blt.bgp.net.AddressFamily;
import onl.netfishers.blt.bgp.net.AddressPrefixBasedORFEntry;
import onl.netfishers.blt.bgp.net.ORFAction;
import onl.netfishers.blt.bgp.net.ORFEntry;
import onl.netfishers.blt.bgp.net.ORFMatch;
import onl.netfishers.blt.bgp.net.ORFRefreshType;
import onl.netfishers.blt.bgp.net.ORFType;
import onl.netfishers.blt.bgp.net.OutboundRouteFilter;
import onl.netfishers.blt.bgp.net.SubsequentAddressFamily;
import onl.netfishers.blt.bgp.netty.NLRICodec;
import onl.netfishers.blt.bgp.netty.protocol.BGPv4Packet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class RouteRefreshPacketDecoder {
	private static final Logger log = LoggerFactory.getLogger(RouteRefreshPacketDecoder.class);
	private static NLRICodec codec = (NLRICodec)BgpService.getInstance(NLRICodec.class.getName());

    public RouteRefreshPacketDecoder () {
    }

	/**
	 * decode the UPDATE network packet. The passed channel buffer MUST point to the first packet octet AFTER the type octet.
	 * 
	 * @param buffer the buffer containing the data. 
	 * @return the decoded packet or null on decoding problems. Neither RFC2918 nor RFC5291 nor RFC4271 describe an error
	 * handling procedure, so best advise is to ignore invalid packets for now.
	 */
	public BGPv4Packet decodeRouteRefreshPacket(ChannelBuffer buffer) {
		RouteRefreshPacket packet = null;
		
		try {
			AddressFamily af = AddressFamily.fromCode(buffer
					.readUnsignedShort());

			buffer.readByte(); // swallow reserved octet

			SubsequentAddressFamily saf = SubsequentAddressFamily
					.fromCode(buffer.readUnsignedByte());

			packet = new RouteRefreshPacket(af, saf);

			if (buffer.readable()) {
				// we have outbound router filter rules here
				OutboundRouteFilter orf = new OutboundRouteFilter(af, saf);
				
				orf.setRefreshType(ORFRefreshType.fromCode(buffer.readUnsignedByte()));
				
				while(buffer.readable()) {
					ORFType orfType = ORFType.fromCode(buffer.readUnsignedByte());
					ChannelBuffer entriesBuffer = ChannelBuffers.buffer(buffer.readUnsignedShort());
					
					buffer.readBytes(entriesBuffer);
					orf.addAllORFEntries(decodeORFEntries(entriesBuffer, orfType));
				}
				
				packet.setOutboundRouteFilter(orf);
			}
		} catch(Exception e) {
			log.error("cannot decode ROUTE_REFRESH packet, suppressing it from further processing", e);
			
			packet = null;
		}
		
		return packet;
	}
	
	private List<ORFEntry> decodeORFEntries(ChannelBuffer buffer, ORFType orfType) {
		List<ORFEntry> entries = new LinkedList<ORFEntry>();

		while(buffer.readable()) {
			int actionMatch = buffer.readUnsignedByte();
			ORFAction action = ORFAction.fromCode((actionMatch >> 6) & 0x03);
			ORFMatch match = ORFMatch.fromCode((actionMatch >> 5) & 0x01);
			
			switch(orfType) {
			case ADDRESS_PREFIX_BASED:
				entries.add(decodeAddressPrefixBasedORFEntry(buffer, action, match));
				break;
			default:
				throw new IllegalArgumentException("cannot decode OutboudRouteFilter entries of type " + orfType);
			}
			
		}
		
		return entries;
	}

	
	private ORFEntry decodeAddressPrefixBasedORFEntry(ChannelBuffer buffer, ORFAction action, ORFMatch match) {
		AddressPrefixBasedORFEntry entry = new AddressPrefixBasedORFEntry(action, match);
		
		if(action != ORFAction.REMOVE_ALL) {
			entry.setSequence((int)buffer.readUnsignedInt());
			entry.setMinLength(buffer.readUnsignedByte());
			entry.setMaxLength(buffer.readUnsignedByte());
			entry.setPrefix(codec.decodeNLRI(buffer));
		}
		
 		return entry;
	}
}
