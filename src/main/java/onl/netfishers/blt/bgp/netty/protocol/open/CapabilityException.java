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
 * File: onl.netfishers.blt.bgp.netty.protocol.CapabilityException.java 
 */
package onl.netfishers.blt.bgp.netty.protocol.open;

import java.util.Collection;

import onl.netfishers.blt.bgp.net.capabilities.Capability;
import onl.netfishers.blt.bgp.netty.BGPv4Constants;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class CapabilityException extends OpenPacketException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5564369816036195511L;

	private byte[] capability;
	
	protected CapabilityException() {
		super();
	}

	protected CapabilityException(byte[] capability) {
		this.capability = capability;
	}

	protected CapabilityException(Capability cap) {
		this.capability = encodeCapability(cap);
	}

	protected CapabilityException(Collection<Capability> caps) {
		this.capability = encodeCapabilities(caps);
	}

	protected CapabilityException(String message, byte[] capability) {
		super(message);
		
		this.capability = capability;
	}

	protected CapabilityException(String message, Capability cap) {
		super(message);
		
		this.capability = encodeCapability(cap);
	}

	protected CapabilityException(String message, Collection<Capability> caps) {
		super(message);
		
		this.capability = encodeCapabilities(caps);
	}

	/**
	 * @return the capability
	 */
	public byte[] getCapability() {
		return capability;
	}

	/**
	 * @param capability the capability to set
	 */
	public void setCapability(byte[] capability) {
		this.capability = capability;
	}

	private byte[] encodeCapability(Capability cap) {
		ChannelBuffer buffer = CapabilityCodec.encodeCapability(cap);
		byte[] packet  = new byte[buffer.readableBytes()];
		
		buffer.readBytes(packet);
		
		return packet;
	}

	private byte[] encodeCapabilities(Collection<Capability> caps) {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);
		
		for(Capability cap : caps)
			buffer.writeBytes(CapabilityCodec.encodeCapability(cap));
		
		byte[] packet  = new byte[buffer.readableBytes()];
		
		buffer.readBytes(packet);
		
		return packet;
	}
}
