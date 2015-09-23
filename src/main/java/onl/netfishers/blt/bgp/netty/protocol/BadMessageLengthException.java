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
 * File: onl.netfishers.blt.bgp.netty.protocol.BadMessageLengthException.java 
 */
package onl.netfishers.blt.bgp.netty.protocol;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BadMessageLengthException extends ProtocolPacketException {

	private int length;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4333191817393984978L;

	/**
	 * @param message
	 * @param cause
	 */
	public BadMessageLengthException(int length) {
		this.length = length;
	}

	/**
	 * 
	 */
	public BadMessageLengthException() {
	}

	/**
	 * @param message
	 */
	public BadMessageLengthException(String message, int length) {
		super(message);
		
		this.length = length;
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.ProtocolPacketFormatException#toNotificationPacket()
	 */
	@Override
	public NotificationPacket toNotificationPacket() {
		return new BadMessageLengthNotificationPacket(this.length);
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

}
