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
 * File: onl.netfishers.blt.bgp.netty.protocol.update.AggregatorPathAttributeCodecHandler.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.update;

import onl.netfishers.blt.bgp.net.attributes.AggregatorPathAttribute;
import onl.netfishers.blt.bgp.netty.BGPv4Constants;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AggregatorPathAttributeCodecHandler extends PathAttributeCodecHandler<AggregatorPathAttribute> {

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.update.PathAttributeCodecHandler#typeCode(onl.netfishers.blt.bgp.netty.protocol.update.PathAttribute)
	 */
	@Override
	public int typeCode(AggregatorPathAttribute attr) {
		return (attr.isFourByteASNumber() 
				? BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AS4_AGGREGATOR 
						: BGPv4Constants.BGP_PATH_ATTRIBUTE_TYPE_AGGREGATOR);
	}

	@Override
	public int valueLength(AggregatorPathAttribute attr) {
		return (attr.isFourByteASNumber() ? 8 : 6);
	}

	@Override
	public ChannelBuffer encodeValue(AggregatorPathAttribute attr) {
		ChannelBuffer buffer = ChannelBuffers.buffer(valueLength(attr));
		
		if(attr.isFourByteASNumber())
			buffer.writeInt(attr.getAsNumber());
		else
			buffer.writeShort(attr.getAsNumber());
		
		buffer.writeBytes(attr.getAggregator().getAddress());
		
		return buffer;
	}

}
