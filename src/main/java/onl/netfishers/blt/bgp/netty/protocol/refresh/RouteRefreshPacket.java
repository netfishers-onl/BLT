/**
 *  Copyright 2012, 2014 Rainer Bieniek (Rainer.Bieniek@web.de)
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
 * File: onl.netfishers.blt.bgp.netty.protocol.RouteRefreshPacket.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.refresh;

import onl.netfishers.blt.bgp.net.AddressFamily;
import onl.netfishers.blt.bgp.net.ORFEntry;
import onl.netfishers.blt.bgp.net.ORFType;
import onl.netfishers.blt.bgp.net.OutboundRouteFilter;
import onl.netfishers.blt.bgp.net.SubsequentAddressFamily;
import onl.netfishers.blt.bgp.netty.BGPv4Constants;
import onl.netfishers.blt.bgp.netty.protocol.BGPv4Packet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class RouteRefreshPacket extends BGPv4Packet {

	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	private OutboundRouteFilter outboundRouteFilter; 
	
	public RouteRefreshPacket() {}
	
	public RouteRefreshPacket(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily) {
		setAddressFamily(addressFamily);
		setSubsequentAddressFamily(subsequentAddressFamily);
	}
	
	public RouteRefreshPacket(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, OutboundRouteFilter outboundRouteFilter) {
		this(addressFamily, subsequentAddressFamily);
		
		setOutboundRouteFilter(outboundRouteFilter);
	}
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.BGPv4Packet#encodePayload()
	 */
	@Override
	protected ChannelBuffer encodePayload() {
		
		ChannelBuffer buffer = ChannelBuffers.buffer(calculateEncodingLength());

		buffer.writeShort(getAddressFamily().toCode());
		buffer.writeByte(0);
		buffer.writeByte(getSubsequentAddressFamily().toCode());
		
		if(outboundRouteFilter != null) {
			buffer.writeByte(outboundRouteFilter.getRefreshType().toCode());
			
			for(ORFType type : outboundRouteFilter.getEntries().keySet()) {
				int entriesLength=0;  
				
				for(ORFEntry entry : outboundRouteFilter.getEntries().get(type))
					entriesLength += ORFEntryCodec.calculateEncodingLength(entry);

				buffer.writeByte(type.toCode());
				buffer.writeShort(entriesLength);

				for(ORFEntry entry : outboundRouteFilter.getEntries().get(type)) {
					buffer.writeBytes(ORFEntryCodec.encodeORFEntry(entry));
				}
				
			}
		}
		
		return buffer;
	}
	
	public int calculateEncodingLength() {
		int size = 4; // 2 octet AFI + 1 octet reserved + 1 octet SAFI

		if(this.outboundRouteFilter != null) {
			size++; // when-to-refresh-octet
			
			for(ORFType type : outboundRouteFilter.getEntries().keySet()) {
				size += 3;  // 1 octet ORF type + 2 octets ORF entries length
			
				for(ORFEntry entry : outboundRouteFilter.getEntries().get(type))
					size += ORFEntryCodec.calculateEncodingLength(entry);
			}
		}

		return size;
	}
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.BGPv4Packet#getType()
	 */
	@Override
	public int getType() {
		return BGPv4Constants.BGP_PACKET_TYPE_ROUTE_REFRESH;
	}

	/**
	 * @return the addressFamily
	 */
	public AddressFamily getAddressFamily() {
		return addressFamily;
	}

	/**
	 * @param addressFamily the addressFamily to set
	 */
	public void setAddressFamily(AddressFamily addressFamily) {
		this.addressFamily = addressFamily;
	}

	/**
	 * @return the subsequentAddressFamily
	 */
	public SubsequentAddressFamily getSubsequentAddressFamily() {
		return subsequentAddressFamily;
	}

	/**
	 * @param subsequentAddressFamily the subsequentAddressFamily to set
	 */
	public void setSubsequentAddressFamily(
			SubsequentAddressFamily subsequentAddressFamily) {
		this.subsequentAddressFamily = subsequentAddressFamily;
	}

	/**
	 * @return the outboundRouteFilter
	 */
	public OutboundRouteFilter getOutboundRouteFilter() {
		return outboundRouteFilter;
	}

	/**
	 * @param outboundRouteFilter the outboundRouteFilter to set
	 */
	public void setOutboundRouteFilter(OutboundRouteFilter outboundRouteFilter) {
		this.outboundRouteFilter = outboundRouteFilter;
	}

	@Override
	public String toString() {
		return (new ToStringBuilder(this))
				.append("type", getType())
				.append("addressFamiliy", addressFamily)
				.append("outboundRouteFilter", outboundRouteFilter)
				.append("subsequentAddressFamily", subsequentAddressFamily)
				.toString();
	}
}
