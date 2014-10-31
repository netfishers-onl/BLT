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
 * File: org.blt.bgp.netty.protocol.PathAttributeLengthNotificationPacket.java 
 */
package org.blt.bgp.netty.protocol.update;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AttributeNotificationPacket extends UpdateNotificationPacket {

	private byte[] offendingAttribute;
	
	/**
	 * @param subcode
	 */
	protected AttributeNotificationPacket(int subcode, byte[] offendingAttribute) {
		super(subcode);
		
		this.offendingAttribute = offendingAttribute;
	}

	/* (non-Javadoc)
	 * @see org.blt.bgp.netty.protocol.NotificationPacket#encodeAdditionalPayload()
	 */
	@Override
	protected final ChannelBuffer encodeAdditionalPayload() {
		ChannelBuffer buffer = null;
		
		if(offendingAttribute != null)  {
			buffer = ChannelBuffers.buffer(offendingAttribute.length);
			
			buffer.writeBytes(offendingAttribute);
		}
		
		return buffer;
	}

	/**
	 * @return the offendingAttribute
	 */
	public byte[] getOffendingAttribute() {
		return offendingAttribute;
	}

	/**
	 * @param offendingAttribute the offendingAttribute to set
	 */
	void setOffendingAttribute(byte[] offendingAttribute) {
		this.offendingAttribute = offendingAttribute;
	}
}
