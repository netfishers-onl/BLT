/**
 *  Copyright 2013, 2014 Nitin Bahadur (nitinb@gmail.com)
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

import onl.netfishers.blt.bgp.net.attributes.MultiProtocolNLRIInformation;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * This class is a wrapper class for decoding/encoding NLRIs of type
 * Multi-protocol BGP. Each AFI/SAFI combo should extend this class
 * to define it's own implementation.
 * 
 * @author nitinb
 *
 */
public class MultiProtocolNLRICodec {


	public MultiProtocolNLRIInformation decodeNLRI(ChannelBuffer buffer) {
		return null;
	}


	public int calculateEncodedNLRILength(MultiProtocolNLRIInformation nlri) {
		return 0;
	}

	public ChannelBuffer encodeNLRI(MultiProtocolNLRIInformation nlri) {
		return null;
	}

}
