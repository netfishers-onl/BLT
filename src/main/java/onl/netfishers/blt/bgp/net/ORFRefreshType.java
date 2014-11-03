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
 * File: onl.netfishers.blt.bgp.netty.protocol.refresh.ORFRefreshType.java 
 */
package onl.netfishers.blt.bgp.net;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public enum ORFRefreshType {
	IMMEDIATE,
	DEFER;
	
	public int toCode() {
		switch(this)  {
		case IMMEDIATE:
			return 1;
		case DEFER:
			return 2;
		default:
			throw new IllegalArgumentException("unknown OutboundRouteFilter refresh type " + this);
		}
	}
	
	public static ORFRefreshType fromCode(int code) {
		switch(code) {
		case 1:
			return IMMEDIATE;
		case 2:
			return DEFER;
		default:
			throw new IllegalArgumentException("unknown OutboundRouteFilter refresh type code " + code);
		}
	}
}
