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
package org.blt.bgp.netty.protocol.update;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class AttributeException extends UpdatePacketException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8265508454292519918L;
	
	private byte[] offendingAttribute;

	/**
	 * 
	 */
	public AttributeException() {
	}

	/**
	 * 
	 */
	public AttributeException(byte[] offendingAttribute) {
		setOffendingAttribute(offendingAttribute);
	}

	/**
	 * @param message
	 */
	public AttributeException(String message, byte[] offendingAttribute) {
		super(message);

		setOffendingAttribute(offendingAttribute);
	}

	/**
	 * 
	 */
	public AttributeException(ChannelBuffer buffer) {
		setOffendingAttribute(buffer);
	}

	/**
	 * @param message
	 */
	public AttributeException(String message, ChannelBuffer buffer) {
		super(message);

		setOffendingAttribute(buffer);
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
	public void setOffendingAttribute(byte[] offendingAttribute) {
		this.offendingAttribute = offendingAttribute;
	}

	/**
	 * @param offendingAttribute the offendingAttribute to set
	 */
	public void setOffendingAttribute(ChannelBuffer buffer) {
		this.offendingAttribute = new byte[buffer.readableBytes()];
		
		buffer.readBytes(offendingAttribute);
	}
}
