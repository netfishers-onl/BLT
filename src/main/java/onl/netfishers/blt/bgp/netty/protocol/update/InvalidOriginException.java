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
 */
package onl.netfishers.blt.bgp.netty.protocol.update;

import onl.netfishers.blt.bgp.netty.protocol.NotificationPacket;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class InvalidOriginException extends AttributeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4983725877677879162L;

	/**
	 * 
	 */
	public InvalidOriginException() {
	}

	/**
	 * @param offendingAttribute
	 */
	public InvalidOriginException(byte[] offendingAttribute) {
		super(offendingAttribute);
	}

	/**
	 * @param message
	 */
	public InvalidOriginException(String message, byte[] offendingAttribute) {
		super(message, offendingAttribute);
	}

	@Override
	public NotificationPacket toNotificationPacket() {
		return new InvalidOriginNotificationPacket(getOffendingAttribute());
	}


}
