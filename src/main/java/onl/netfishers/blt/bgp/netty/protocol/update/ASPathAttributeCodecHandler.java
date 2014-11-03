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
 * File: onl.netfishers.blt.bgp.netty.protocol.update.ASPathAttributeCodecHandler.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.update;

import onl.netfishers.blt.bgp.net.ASType;
import onl.netfishers.blt.bgp.net.PathSegment;
import onl.netfishers.blt.bgp.net.attributes.ASPathAttribute;
import onl.netfishers.blt.bgp.netty.BGPv4Constants;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ASPathAttributeCodecHandler extends PathAttributeCodecHandler<ASPathAttribute> {

	static class PathSegmentCodec {
		static int getValueLength(PathSegment segment) {
			int size = 2; // type + length field

			if(segment.getAses() != null && segment.getAses().size() > 0) {
				size += segment.getAses().size() * (segment.getAsType() == ASType.AS_NUMBER_4OCTETS ? 4 : 2);
			}
			
			return size;
		}

		static ChannelBuffer encodeValue(PathSegment segment) {
			ChannelBuffer buffer = ChannelBuffers.buffer(getValueLength(segment));
			
			buffer.writeByte(PathSegmentTypeCodec.toCode(segment.getPathSegmentType()));
			if(segment.getAses() != null && segment.getAses().size() > 0) {
				buffer.writeByte(segment.getAses().size());
				
				for(int as : segment.getAses()) {
					if(segment.getAsType() == ASType.AS_NUMBER_4OCTETS) 
						buffer.writeInt(as);
					else
						buffer.writeShort(as);
				}
					
				
			} else {
				buffer.writeByte(0);
			}
			return buffer;
		}

	}
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.update.PathAttributeCodecHandler#typeCode(onl.netfishers.blt.bgp.netty.protocol.update.PathAttribute)
	 */
	@Override
	public int typeCode(ASPathAttribute attr) {
		return (attr.isFourByteASNumber() 
				? BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_PATH 
						: BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS_PATH);
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.update.PathAttributeCodecHandler#valueLength(onl.netfishers.blt.bgp.netty.protocol.update.PathAttribute)
	 */
	@Override
	public int valueLength(ASPathAttribute attr) {
		int size = 0; // type + length field

		if(attr.getPathSegments() != null) {
			for(PathSegment seg : attr.getPathSegments())
				size += PathSegmentCodec.getValueLength(seg);
		}
		
		return size;
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.update.PathAttributeCodecHandler#encodeValue(onl.netfishers.blt.bgp.netty.protocol.update.PathAttribute)
	 */
	@Override
	public ChannelBuffer encodeValue(ASPathAttribute attr) {
		ChannelBuffer buffer = ChannelBuffers.buffer(valueLength(attr));
		
		if(attr.getPathSegments() != null && attr.getPathSegments().size() > 0) {
			for(PathSegment seg : attr.getPathSegments())
				buffer.writeBytes(PathSegmentCodec.encodeValue(seg));
		}
		
		return buffer;
	}

}
