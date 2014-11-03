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
 * File: onl.netfishers.blt.bgp.netty.protocol.MissingWellKnownAttributeException.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.update;

import onl.netfishers.blt.bgp.netty.protocol.NotificationPacket;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MissingWellKnownAttributeException extends UpdatePacketException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6006146463252407190L;

	private int attributeCode;
	
	public MissingWellKnownAttributeException() {
	}

	public MissingWellKnownAttributeException(int attributeCode) {
		this.attributeCode = attributeCode;
	}

	public MissingWellKnownAttributeException(String message, int attributeCode) {
		super(message);

		this.attributeCode = attributeCode;
	}

	@Override
	public NotificationPacket toNotificationPacket() {
		return new MissingWellKnownAttributeNotificationPacket(this.attributeCode);
	}

	/**
	 * @return the attributeCode
	 */
	public int getAttributeCode() {
		return attributeCode;
	}

}
