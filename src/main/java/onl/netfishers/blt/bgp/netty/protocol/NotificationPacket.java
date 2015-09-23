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
package onl.netfishers.blt.bgp.netty.protocol;

import onl.netfishers.blt.bgp.netty.BGPv4Constants;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NotificationPacket extends BGPv4Packet {
	private int errorCode;
	private int errorSubcode;
	
	protected NotificationPacket(int errorCode, int errorSubcode) {
		this.errorCode = errorCode;
		this.errorSubcode = errorSubcode;
	}
	
	@Override
	protected final ChannelBuffer encodePayload() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);
		
		buffer.writeByte(errorCode);
		buffer.writeByte(errorSubcode);
		
		ChannelBuffer additionalPayload = encodeAdditionalPayload();
		
		if(additionalPayload != null)
			buffer.writeBytes(additionalPayload);
		
		return buffer;
	}

	@Override
	public final int getType() {
		return BGPv4Constants.BGP_PACKET_TYPE_NOTIFICATION;
	}

	/**
	 * @return the errorCode
	 */
	public final int getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the errorSubcode
	 */
	public final int getErrorSubcode() {
		return errorSubcode;
	}	
	
	protected ChannelBuffer encodeAdditionalPayload() {
		return null;
	}
	
	@Override
	public String toString() {
		return (new ToStringBuilder(this))
				.append("type", getType())
				.append("errorCode", errorCode)
				.append("errorSubcode", errorSubcode).toString();
	}
}
