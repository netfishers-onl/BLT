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
 * File: onl.netfishers.blt.bgp.netty.protocol.UnspecificOpenPacketException.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.open;

import onl.netfishers.blt.bgp.netty.protocol.NotificationPacket;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UnspecificOpenPacketException extends OpenPacketException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2310805310691844533L;

	/**
	 * @param message
	 * @param cause
	 */
	public UnspecificOpenPacketException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public UnspecificOpenPacketException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	public UnspecificOpenPacketException() {
	}

	/**
	 * @param message
	 */
	public UnspecificOpenPacketException(String message) {
		super(message);
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.ProtocolPacketFormatException#toNotificationPacket()
	 */
	@Override
	public NotificationPacket toNotificationPacket() {
		return new UnspecificOpenNotificationPacket();
	}

}
