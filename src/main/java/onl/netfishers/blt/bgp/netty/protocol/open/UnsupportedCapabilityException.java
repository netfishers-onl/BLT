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
 * File: onl.netfishers.blt.bgp.netty.protocol.CapabilityLengthException.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.open;

import java.util.Collection;

import onl.netfishers.blt.bgp.net.capabilities.Capability;
import onl.netfishers.blt.bgp.netty.protocol.NotificationPacket;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UnsupportedCapabilityException extends CapabilityException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1363011101974431668L;

	/**
	 * 
	 */
	public UnsupportedCapabilityException() {
	}

	/**
	 * @param message
	 * @param capability
	 */
	public UnsupportedCapabilityException(String message, byte[] capability) {
		super(message, capability);
	}

	/**
	 * @param message
	 * @param capability
	 */
	public UnsupportedCapabilityException(String message, Capability cap) {
		super(message, cap);
	}

	/**
	 * @param message
	 * @param capability
	 */
	public UnsupportedCapabilityException(String message, Collection<Capability> caps) {
		super(message, caps);
	}

	/**
	 * @param cause
	 * @param capability
	 */
	public UnsupportedCapabilityException(byte[] capability) {
		super(capability);
	}

	/**
	 * @param message
	 * @param capability
	 */
	public UnsupportedCapabilityException(Capability cap) {
		super(cap);
	}

	/**
	 * @param message
	 * @param capability
	 */
	public UnsupportedCapabilityException(Collection<Capability> caps) {
		super(caps);
	}

	@Override
	public NotificationPacket toNotificationPacket() {
		return new ByteArrayUnsupportedCapabilityNotificationPacket(getCapability());
	}
}
