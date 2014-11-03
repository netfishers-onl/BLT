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
 * File: onl.netfishers.blt.bgp.netty.protocol.ConnectionNotSynchronizedException.java 
 */
package onl.netfishers.blt.bgp.netty.protocol;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ConnectionNotSynchronizedException extends
		ProtocolPacketException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 988239317361192202L;

	public ConnectionNotSynchronizedException() {
		// TODO Auto-generated constructor stub
	}

	public ConnectionNotSynchronizedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ConnectionNotSynchronizedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ConnectionNotSynchronizedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.ProtocolPacketFormatException#toNotificationPacket()
	 */
	@Override
	public NotificationPacket toNotificationPacket() {
		return new ConnectionNotSynchronizedNotificationPacket();
	}

}
