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

import onl.netfishers.blt.bgp.BgpService;
import onl.netfishers.blt.bgp.net.attributes.MultiProtocolNLRI;
import onl.netfishers.blt.bgp.net.attributes.MultiProtocolNLRIInformation;
import onl.netfishers.blt.bgp.net.attributes.PathAttributeType;
import onl.netfishers.blt.bgp.netty.BGPv4Constants;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author nitinb
 *
 */
public class MultiProtocolNLRICodecHandler extends PathAttributeCodecHandler<MultiProtocolNLRI> {

	private static MultiProtocolNLRICodec codec = (MultiProtocolNLRICodec)BgpService.getInstance(MultiProtocolNLRICodec.class.getName());

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.update.PathAttributeCodecHandler#typeCode(onl.netfishers.blt.bgp.netty.protocol.update.PathAttribute)
	 */
	@Override
	public int typeCode(MultiProtocolNLRI attr) {
		if (attr.getPathAttributeType() == PathAttributeType.MULTI_PROTOCOL_REACHABLE) {
			return BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MP_REACH_NLRI;
		} else {
			return BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_MP_UNREACH_NLRI;
		}
	}

	protected int valueLengthReachableNLRI(MultiProtocolNLRI attr) {
		int size = 5; // 2 octets AFI +  1 octet SAFI + 1 octet NextHop address length + 1 octet reserved
		
		if(attr.getNextHop() != null)
			size += attr.getNextHop().getAddress().length;
		
		if(attr.getNlris() != null) {
			for(MultiProtocolNLRIInformation nlri : attr.getNlris())
				size += codec.calculateEncodedNLRILength(nlri);
		}
		
		return size;
	}
	
	protected int valueLengthUnreachableNLRI(MultiProtocolNLRI attr) {
		int size = 3; // 2 octets AFI +  1 octet SAFI 
		
		if(attr.getNlris() != null) {
			for(MultiProtocolNLRIInformation nlri : attr.getNlris())
				size += codec.calculateEncodedNLRILength(nlri);
		}
		
		return size;
	}
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.update.PathAttributeCodecHandler#valueLength(onl.netfishers.blt.bgp.netty.protocol.update.PathAttribute)
	 */
	@Override
	public int valueLength(MultiProtocolNLRI attr) {
		if (attr.getPathAttributeType() == PathAttributeType.MULTI_PROTOCOL_REACHABLE) {
			return valueLengthReachableNLRI(attr);
		} else {
			return valueLengthUnreachableNLRI(attr);
		}
	}

	protected ChannelBuffer encodeReachableNLRIValue(MultiProtocolNLRI attr) {
		ChannelBuffer buffer = ChannelBuffers.buffer(valueLength(attr));
		
		buffer.writeShort(attr.getAddressFamily().toCode());
		buffer.writeByte(attr.getSubsequentAddressFamily().toCode());
		
		if(attr.getNextHop() != null) {
			buffer.writeByte(attr.getNextHop().getAddress().length);
			buffer.writeBytes(attr.getNextHop().getAddress());
		} else {
			buffer.writeByte(0);
		}

		buffer.writeByte(0); // write reserved field

		if(attr.getNlris() != null) {
			for(MultiProtocolNLRIInformation nlri : attr.getNlris())
				buffer.writeBytes(codec.encodeNLRI(nlri));
		}

		return buffer;
	}
	
	protected ChannelBuffer encodeUnreachableNLRIValue(MultiProtocolNLRI attr) {
		ChannelBuffer buffer = ChannelBuffers.buffer(valueLength(attr));
		
		buffer.writeShort(attr.getAddressFamily().toCode());
		buffer.writeByte(attr.getSubsequentAddressFamily().toCode());
		
		if(attr.getNlris() != null) {
			for(MultiProtocolNLRIInformation nlri : attr.getNlris())
				buffer.writeBytes(codec.encodeNLRI(nlri));
		}

		return buffer;
	}
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.update.PathAttributeCodecHandler#encodeValue(onl.netfishers.blt.bgp.netty.protocol.update.PathAttribute)
	 */
	@Override
	public ChannelBuffer encodeValue(MultiProtocolNLRI attr) {
		if (attr.getPathAttributeType() == PathAttributeType.MULTI_PROTOCOL_REACHABLE) {
			return encodeReachableNLRIValue(attr);
		} else {
			return encodeUnreachableNLRIValue(attr);
		}
	}
}
