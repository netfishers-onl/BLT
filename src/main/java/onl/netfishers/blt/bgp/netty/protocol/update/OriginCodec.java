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
 * File: onl.netfishers.blt.bgp.netty.protocol.update.OriginCodec.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.update;

import onl.netfishers.blt.bgp.net.Origin;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OriginCodec {

	static Origin fromCode(int code) {
		switch(code) {
		case 0:
			return Origin.IGP;
		case 1:
			return Origin.EGP;
		case 2:
			return Origin.INCOMPLETE;
		default:
			throw new IllegalArgumentException("unknown origin code: " + code);
		}
	}

	static int toCode(Origin origin) {
		switch(origin) {
			case IGP:
				return 0;
			case EGP:
				return 1;
			case INCOMPLETE:
				return 2;
			default:
				throw new IllegalArgumentException("unknown origin code: " +origin);
		}
	}

}
