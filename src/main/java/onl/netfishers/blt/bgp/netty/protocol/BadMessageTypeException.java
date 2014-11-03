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
package onl.netfishers.blt.bgp.netty.protocol;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BadMessageTypeException extends ProtocolPacketException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1997353015198092763L;
	
	private int type;
	
	/**
	 * 
	 */
	public BadMessageTypeException() {
	}

	/**
	 * 
	 */
	public BadMessageTypeException(int type) {
		this.type = type;
	}

	/**
	 * @param message
	 */
	public BadMessageTypeException(String message, int type) {
		super(message);
		
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	@Override
	public NotificationPacket toNotificationPacket() {
		return new BadMessageTypeNotificationPacket(this.type);
	}

}
