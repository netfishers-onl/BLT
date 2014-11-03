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
 * File: onl.netfishers.blt.bgp.netty.protocol.update.PathSegmentTypeCodec.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.update;

import onl.netfishers.blt.bgp.net.PathSegmentType;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PathSegmentTypeCodec {

	private static final int AS_SET_CODE = 1;
	private static final int AS_SEQUENCE_CODE = 2;
	private static final int AS_CONFED_SEQUENCE_CODE = 3;
	private static final int AS_CONFED_SET_CODE = 4;	
	

	static PathSegmentType fromCode(int code) {
		switch(code) {
		case AS_SET_CODE:
			return PathSegmentType.AS_SET;
		case AS_SEQUENCE_CODE:
			return PathSegmentType.AS_SEQUENCE;
		case AS_CONFED_SEQUENCE_CODE:
			return PathSegmentType.AS_CONFED_SEQUENCE;
		case AS_CONFED_SET_CODE:
			return PathSegmentType.AS_CONFED_SET;
		default:
			throw new IllegalArgumentException("illegal AS_PATH type" + code);				
		}
	}

	// unordered set of ASes in a confederation a route in the UPDATE message has traversed
	
	static int toCode(PathSegmentType type) {
		switch(type) {
		case AS_SET:
			return AS_SET_CODE;
		case AS_SEQUENCE:
			return AS_SEQUENCE_CODE;
		case AS_CONFED_SEQUENCE:
			return AS_CONFED_SEQUENCE_CODE;
		case AS_CONFED_SET:
			return AS_CONFED_SET_CODE;
		default:
			throw new IllegalArgumentException("illegal AS_PATH type" + type);
		}
	}

}
