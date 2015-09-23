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
 * File: onl.netfishers.blt.bgp.netty.protocol.refresh.ORFEntryCodec.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.refresh;

import onl.netfishers.blt.bgp.BgpService;
import onl.netfishers.blt.bgp.net.AddressPrefixBasedORFEntry;
import onl.netfishers.blt.bgp.net.ORFAction;
import onl.netfishers.blt.bgp.net.ORFEntry;
import onl.netfishers.blt.bgp.netty.NLRICodec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ORFEntryCodec {

	private static NLRICodec codec = (NLRICodec)BgpService.getInstance(NLRICodec.class.getName());

	/**
	 * get the length of the encoded ORF entry in octets
	 * 
	 * @return
	 */
	public static final int calculateEncodingLength(ORFEntry entry) {
		return 1 + calculateORFPayloadEncodingLength(entry);
	}

	private static int calculateORFPayloadEncodingLength(ORFEntry entry) {
		if(entry instanceof AddressPrefixBasedORFEntry)
			return calculateAddressPrefixBasedORFPayloadEncodingLength((AddressPrefixBasedORFEntry)entry);
		else
			throw new IllegalArgumentException("cannot handle ORFEntry of type " + entry.getClass().getName());
	}
	
	/**
	 * encode the ORF entry
	 * 
	 * @return
	 */
	public static final ChannelBuffer encodeORFEntry(ORFEntry entry) {
		ChannelBuffer buffer = ChannelBuffers.buffer(calculateEncodingLength(entry));
		ChannelBuffer payload = encodeORFPayload(entry);
		
		buffer.writeByte(entry.getAction().toCode() << 6 | entry.getMatch().toCode() << 5);

		if(payload != null)
			buffer.writeBytes(payload);
		
		return buffer;
	}

	private static ChannelBuffer encodeORFPayload(ORFEntry entry) {
		if(entry instanceof AddressPrefixBasedORFEntry)
			return encodeAddressPrefixBasedORFPayload((AddressPrefixBasedORFEntry)entry);
		else
			throw new IllegalArgumentException("cannot handle ORFEntry of type " + entry.getClass().getName());
	}

	private static int calculateAddressPrefixBasedORFPayloadEncodingLength(AddressPrefixBasedORFEntry entry) {
		int size = 0;
		
		if(entry.getAction() != ORFAction.REMOVE_ALL)
			size += 6 + codec.calculateEncodedNLRILength(entry.getPrefix()); // 4 octet sequence + 1 octet min length + 1 octet max length + prefix length
		
		return size;
	}

	private static ChannelBuffer encodeAddressPrefixBasedORFPayload(AddressPrefixBasedORFEntry entry) {
		ChannelBuffer buffer = null;
		
		if(entry.getAction() != ORFAction.REMOVE_ALL) {
			buffer = ChannelBuffers.buffer(calculateAddressPrefixBasedORFPayloadEncodingLength(entry));

			buffer.writeInt(entry.getSequence());
			buffer.writeByte(entry.getMinLength());
			buffer.writeByte(entry.getMaxLength());
			buffer.writeBytes(codec.encodeNLRI(entry.getPrefix()));
		}
		
		return buffer;
	}

}
