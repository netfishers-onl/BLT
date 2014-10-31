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
 * File: org.blt.bgp.netty.protocol.refresh.ORFAction.java 
 */
package org.blt.bgp.net;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public enum ORFAction {
	ADD,
	REMOVE,
	REMOVE_ALL;
	
	public int toCode() {
		switch(this) {
			case ADD:
				return 0;
			case REMOVE:
				return 1;
			case REMOVE_ALL:
				return 2;
			default:
				throw new IllegalArgumentException("unknown ORF action: " + this);
		}
	}
	
	public static ORFAction fromCode(int code) {
		switch(code) {
		case 0:
			return ADD;
		case 1:
			return REMOVE;
		case 2:
			return REMOVE_ALL;
		default:
			throw new IllegalArgumentException("unknown ORF action code: " + code);
		}
	}
}
