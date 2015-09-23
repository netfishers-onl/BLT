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
 */
package onl.netfishers.blt.bgp.netty.protocol.open;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ByteArrayUnsupportedCapabilityNotificationPacket extends UnsupportedCapabilityNotificationPacket {

	private byte[] capability;
	
	public ByteArrayUnsupportedCapabilityNotificationPacket(byte[] capability) {
		this.capability = capability;
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.NotificationPacket#encodeAdditionalPayload()
	 */
	@Override
	protected ChannelBuffer encodeAdditionalPayload() {
		ChannelBuffer buffer = ChannelBuffers.buffer(capability.length);
		
		buffer.writeBytes(capability);
		
		return buffer;
	}

}
