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

import onl.netfishers.blt.bgp.netty.BGPv4Constants;
import onl.netfishers.blt.bgp.netty.protocol.NotificationPacket;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdateNotificationPacket extends NotificationPacket {

	protected static final int SUBCODE_MALFORMED_ATTRIBUTE_LIST = 1;
	protected static final int SUBCODE_UNRECOGNIZED_WELL_KNOWN_ATTRIBUTE = 2;
	protected static final int SUBCODE_MISSING_WELL_KNOWN_ATTRIBUTE = 3;
	protected static final int SUBCODE_ATTRIBUTE_FLAGS_ERROR = 4;
	protected static final int SUBCODE_ATTRIBUTE_LENGTH_ERROR = 5;
	protected static final int SUBCODE_INVALID_ORIGIN_ATTRIBUTE = 6;
	protected static final int SUBCODE_INVALID_NEXT_HOP_ATTRIBUTE = 8;
	protected static final int SUBCODE_OPTIONAL_ATTRIBUTE_ERROR = 9;
	protected static final int SUBCODE_INVALID_NETWORK_FIELD = 10;
	protected static final int SUBCODE_MALFORMED_AS_PATH = 11;

	protected UpdateNotificationPacket(int subcode) {
		super(BGPv4Constants.BGP_ERROR_CODE_UPDATE, subcode);
	}
}
