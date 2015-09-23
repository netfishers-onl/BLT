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
 * File: onl.netfishers.blt.bgp.netty.protocol.WellKnownAttributeMissingNotificationPacket.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.update;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MissingWellKnownAttributeNotificationPacket extends UpdateNotificationPacket {

	private int attributeCode;
	
	/**
	 * @param attributeCode 
	 * @param subcode
	 */
	public MissingWellKnownAttributeNotificationPacket(int attributeCode) {
		super(UpdateNotificationPacket.SUBCODE_MISSING_WELL_KNOWN_ATTRIBUTE);
		
		this.attributeCode = attributeCode;
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.NotificationPacket#encodeAdditionalPayload()
	 */
	@Override
	protected ChannelBuffer encodeAdditionalPayload() {
		ChannelBuffer buffer = ChannelBuffers.buffer(1);
		
		buffer.writeByte(attributeCode);
		
		return buffer;
	}

}
