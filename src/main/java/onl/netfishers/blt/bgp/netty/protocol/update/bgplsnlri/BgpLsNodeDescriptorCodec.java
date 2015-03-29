/**
 *  Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
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
package onl.netfishers.blt.bgp.netty.protocol.update.bgplsnlri;

import onl.netfishers.blt.bgp.net.attributes.bgplsnlri.BgpLsNodeDescriptor;
import onl.netfishers.blt.bgp.net.attributes.bgplsnlri.BgpLsType;
import onl.netfishers.blt.bgp.netty.protocol.update.OptionalAttributeErrorException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decodes a node descriptor tlv
 * @author nitinb
 *
 */
public class BgpLsNodeDescriptorCodec {

	private static final Logger log = LoggerFactory.getLogger(BgpLsNodeDescriptorCodec.class);

	/**
	 * Decodes a node descriptor tlv
	 * @param buffer Data stream containing the tlv
	 * @param nd node descriptor to update
	 */
	public static void decodeNodeDescriptor(ChannelBuffer buffer, BgpLsNodeDescriptor nd) {

		while(buffer.readable()) {
			int subType = buffer.readUnsignedShort();
			int valueLength = buffer.readUnsignedShort();
			
			ChannelBuffer valueBuffer = ChannelBuffers.buffer(valueLength);
			buffer.readBytes(valueBuffer);
			
			switch(BgpLsType.fromCode(subType)) {
			case AutonomousSystem:
				decodeAutonomousSystem(valueBuffer, nd);
				break;
			case BGPLSIdentifier:
				decodeBgpLsIdentifier(valueBuffer, nd);
				break;
			case AreaID:
				decodeAreaId(valueBuffer, nd);
				break;
			case IGPRouterID:
				decodeIgpRouterId(valueBuffer, nd);
				break;
			default:
				log.error("Unsupported descriptor type " + subType);
				break;
			}
		}
	}
	
	/**
	 * Decodes autonomous system sub-tlv
	 * @param buffer Data stream containing the sub-tlv
	 * @param nd node descriptor to update
	 */
	private static void decodeAutonomousSystem(ChannelBuffer buffer,
			BgpLsNodeDescriptor nd) {
		try {
			if (buffer.readableBytes() != 4) {
				log.error("Invalid length (" + buffer.readableBytes() + ") for autonomous system");
				throw new OptionalAttributeErrorException();
			}
			long autonomousSystem = buffer.readUnsignedInt();
			nd.setAutonomousSystem(autonomousSystem);
			
		} catch(RuntimeException e) {
			log.error("failed to decode Autonomous system for node descriptor", e);
			throw new OptionalAttributeErrorException();
		}				
	}


	private static void decodeBgpLsIdentifier(ChannelBuffer buffer,
	    BgpLsNodeDescriptor nd) {
		try {
			if (buffer.readableBytes() != 4) {
				log.error("Invalid length (" + buffer.readableBytes()
				    + ") for BGP LS Identifier");
				throw new OptionalAttributeErrorException();
			}
			long bgpLsId = buffer.readUnsignedInt();
			nd.setBgpLsIdentifier(bgpLsId);

		}
		catch (RuntimeException e) {
			log.error("failed to decode BGP LS Identifier for node descriptor", e);
			throw new OptionalAttributeErrorException();
		}
	}



	private static void decodeAreaId(ChannelBuffer buffer,
	    BgpLsNodeDescriptor nd) {
		try {
			if (buffer.readableBytes() != 4) {
				log.error("Invalid length (" + buffer.readableBytes()
				    + ") for Area ID");
				throw new OptionalAttributeErrorException();
			}
			long areaId = buffer.readUnsignedInt();
			nd.setAreaId(areaId);

		}
		catch (RuntimeException e) {
			log.error("failed to decode Area ID for node descriptor", e);
			throw new OptionalAttributeErrorException();
		}
	}


	private static void decodeIgpRouterId(ChannelBuffer buffer,
	    BgpLsNodeDescriptor nd) {
		try {
			if (buffer.readableBytes() != BgpLsNodeDescriptor.IGPROUTERID_ISISISONODEID_LENGTH &&
					buffer.readableBytes() != BgpLsNodeDescriptor.IGPROUTERID_ISISPSEUDONODE_LENGTH &&
					buffer.readableBytes() != BgpLsNodeDescriptor.IGPROUTERID_OSPFPSEUDONODE_LENGTH &&
					buffer.readableBytes() != BgpLsNodeDescriptor.IGPROUTERID_OSPFROUTERID_LENGTH) {
				log.error("Invalid length (" + buffer.readableBytes()
				    + ") for IGP Router ID");
				throw new OptionalAttributeErrorException();
			}
			byte[] igpRouterId = new byte[buffer.readableBytes()];
			buffer.readBytes(igpRouterId);
			nd.setIgpRouterId(igpRouterId);
			
		}
		catch (RuntimeException e) {
			log.error("failed to decode IGP Router ID for node descriptor", e);
			throw new OptionalAttributeErrorException();
		}
	}
}
