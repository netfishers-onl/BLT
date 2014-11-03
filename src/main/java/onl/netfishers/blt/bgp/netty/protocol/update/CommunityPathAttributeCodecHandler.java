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
 * File: onl.netfishers.blt.bgp.netty.protocol.update.CommunityPathAttributeCodecHandler.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.update;

import onl.netfishers.blt.bgp.net.attributes.CommunityMember;
import onl.netfishers.blt.bgp.net.attributes.CommunityPathAttribute;
import onl.netfishers.blt.bgp.netty.BGPv4Constants;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CommunityPathAttributeCodecHandler extends
		PathAttributeCodecHandler<CommunityPathAttribute> {

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.update.PathAttributeCodecHandler#typeCode(onl.netfishers.blt.bgp.netty.protocol.update.PathAttribute)
	 */
	@Override
	public int typeCode(CommunityPathAttribute attr) {
		return BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_COMMUNITIES;
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.update.PathAttributeCodecHandler#valueLength(onl.netfishers.blt.bgp.netty.protocol.update.PathAttribute)
	 */
	@Override
	public int valueLength(CommunityPathAttribute attr) {
		int size = 4;
		
		if(attr.getMembers() != null)
			size += 4*attr.getMembers().size();
		
		return size;
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.update.PathAttributeCodecHandler#encodeValue(onl.netfishers.blt.bgp.netty.protocol.update.PathAttribute)
	 */
	@Override
	public ChannelBuffer encodeValue(CommunityPathAttribute attr) {
		ChannelBuffer buffer = ChannelBuffers.buffer(valueLength(attr));
		
		buffer.writeInt(attr.getCommunity());
		if(attr.getMembers() != null) {
			for(CommunityMember member : attr.getMembers()) {
				buffer.writeShort(member.getAsNumber());
				buffer.writeShort(member.getMemberFlags());
			}
		}
		
		return buffer;
	}

}
