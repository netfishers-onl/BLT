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
 * File: onl.netfishers.blt.bgp.netty.protocol.refresh.ORFMatch.java 
 */
package onl.netfishers.blt.bgp.net;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public enum ORFMatch {
	PERMIT,
	DENY;
	
	public int toCode() {
		switch(this) {
		case PERMIT:
			return 0;
		case DENY:
			return 1;
		default:
			throw new IllegalArgumentException("unknown ORF action code: " + this);			
		}
	}
	
	public static ORFMatch fromCode(int code) {
		switch(code) {
		case 0:
			return PERMIT;
		case 1:
			return DENY;
		default:
			throw new IllegalArgumentException("unknown ORF action code: " + code);			
		}
	}
}
