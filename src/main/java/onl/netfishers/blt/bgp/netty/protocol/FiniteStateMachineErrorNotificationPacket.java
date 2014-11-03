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

import onl.netfishers.blt.bgp.netty.BGPv4Constants;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class FiniteStateMachineErrorNotificationPacket extends NotificationPacket {

	/**
	 * @param errorCode
	 * @param errorSubcode
	 */
	public FiniteStateMachineErrorNotificationPacket() {
		super(BGPv4Constants.BGP_ERROR_CODE_CEASE, 0);
	}

}
