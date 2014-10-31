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
 * File: org.blt.bgp.netty.protocol.UnacceptableHoldTimerException.java 
 */
package org.blt.bgp.netty.protocol.open;

import org.blt.bgp.netty.protocol.NotificationPacket;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UnacceptableHoldTimerException extends OpenPacketException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2100244665501484800L;

	/**
	 * @param message
	 * @param cause
	 */
	public UnacceptableHoldTimerException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public UnacceptableHoldTimerException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	public UnacceptableHoldTimerException() {
	}

	/**
	 * @param message
	 */
	public UnacceptableHoldTimerException(String message) {
		super(message);
	}

	/* (non-Javadoc)
	 * @see org.blt.bgp.netty.protocol.ProtocolPacketFormatException#toNotificationPacket()
	 */
	@Override
	public NotificationPacket toNotificationPacket() {
		return new UnacceptableHoldTimerNotificationPacket();
	}

}
