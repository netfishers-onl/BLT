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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import onl.netfishers.blt.bgp.BgpService;
import onl.netfishers.blt.bgp.net.NetworkLayerReachabilityInformation;
import onl.netfishers.blt.bgp.net.attributes.PathAttribute;
import onl.netfishers.blt.bgp.netty.BGPv4Constants;
import onl.netfishers.blt.bgp.netty.NLRICodec;
import onl.netfishers.blt.bgp.netty.protocol.BGPv4Packet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UpdatePacket extends BGPv4Packet {

	private List<NetworkLayerReachabilityInformation> withdrawnRoutes = new LinkedList<NetworkLayerReachabilityInformation>();
	private List<NetworkLayerReachabilityInformation> nlris = new LinkedList<NetworkLayerReachabilityInformation>();
	private List<PathAttribute> pathAttributes = new LinkedList<PathAttribute>();
	private static NLRICodec codec = (NLRICodec)BgpService.getInstance(NLRICodec.class.getName());

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.BGPv4Packet#encodePayload()
	 */
	@Override
	protected ChannelBuffer encodePayload() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);
		ChannelBuffer withdrawnBuffer = encodeWithdrawnRoutes();
		ChannelBuffer pathAttributesBuffer = encodePathAttributes();
		
		buffer.writeShort(withdrawnBuffer.readableBytes());
		buffer.writeBytes(withdrawnBuffer);
		buffer.writeShort(pathAttributesBuffer.readableBytes());
		buffer.writeBytes(pathAttributesBuffer);
		buffer.writeBytes(encodeNlris());
		
		return buffer;
	}

	public int calculatePacketSize() {
		int size = BGPv4Constants.BGP_PACKET_MIN_SIZE_UPDATE;

		size += calculateSizeWithdrawnRoutes();
		size += calculateSizePathAttributes();
		size += calculateSizeNlris();
		
		return size;
	}
	
	private ChannelBuffer encodeWithdrawnRoutes() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);

		if(this.withdrawnRoutes != null) {
			for (NetworkLayerReachabilityInformation route : withdrawnRoutes) {
				buffer.writeBytes(codec.encodeNLRI(route));
			}
		}
		
		return buffer;
	}

	private ChannelBuffer encodePathAttributes() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);

		if(this.pathAttributes != null) {
			for(PathAttribute pathAttribute : pathAttributes) {
				buffer.writeBytes(PathAttributeCodec.encodePathAttribute(pathAttribute));
			}
		}
		
		return buffer;
	}
	
	private ChannelBuffer encodeNlris() {
		ChannelBuffer buffer = ChannelBuffers.buffer(BGPv4Constants.BGP_PACKET_MAX_LENGTH);

		if(this.nlris != null) {
			for (NetworkLayerReachabilityInformation nlri : nlris) {
				buffer.writeBytes(codec.encodeNLRI(nlri));
			}
		}
		
		return buffer;
	}

	private int calculateSizeWithdrawnRoutes() {
		int size = 0;

		if(this.withdrawnRoutes != null) {
			for (NetworkLayerReachabilityInformation route : withdrawnRoutes) {
				size += codec.calculateEncodedNLRILength(route);
			}
		}

		return size;
	}
	
	private int calculateSizeNlris() {
		int size = 0;

		if(this.nlris != null) {
			for (NetworkLayerReachabilityInformation nlri : nlris) {
				size += codec.calculateEncodedNLRILength(nlri);
			}
		}

		return size;
	}
	
	private int calculateSizePathAttributes() {
		int size = 0;
		
		if(this.pathAttributes != null) {
			for(PathAttribute  attr : pathAttributes)
				size += PathAttributeCodec.calculateEncodedPathAttributeLength(attr);
		}
		
		return size;
	}
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.netty.protocol.BGPv4Packet#getType()
	 */
	@Override
	public int getType() {
		return BGPv4Constants.BGP_PACKET_TYPE_UPDATE;
	}

	/**
	 * @return the withdrawnRoutes
	 */
	public List<NetworkLayerReachabilityInformation> getWithdrawnRoutes() {
		return withdrawnRoutes;
	}

	/**
	 * @param withdrawnRoutes the withdrawnRoutes to set
	 */
	public void setWithdrawnRoutes(List<NetworkLayerReachabilityInformation> withdrawnRoutes) {
		this.withdrawnRoutes = withdrawnRoutes;
	}

	/**
	 * @return the nlris
	 */
	public List<NetworkLayerReachabilityInformation> getNlris() {
		return nlris;
	}

	/**
	 * @param nlris the nlris to set
	 */
	public void setNlris(List<NetworkLayerReachabilityInformation> nlris) {
		this.nlris = nlris;
	}

	/**
	 * @return the pathAttributes
	 */
	public List<PathAttribute> getPathAttributes() {
		return pathAttributes;
	}

	/**
	 * @param pathAttributes the pathAttributes to set
	 */
	public void setPathAttributes(List<PathAttribute> pathAttributes) {
		this.pathAttributes = pathAttributes;
	}

	/**
	 * look up path attributes of a given type passed in this update packet
	 */
	@SuppressWarnings("unchecked")
	public <T extends PathAttribute> Set<T> lookupPathAttributes(Class<T> paClass) {
		Set<T> result = new HashSet<T>();
		
		for(PathAttribute pa : pathAttributes) {
			if(pa.getClass().equals(paClass))
				result.add((T)pa);
		}
		
		return result;
	}
	
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		
		for(NetworkLayerReachabilityInformation n : withdrawnRoutes)
			builder.append("withdrawnRoute", n);
		
		for(NetworkLayerReachabilityInformation n : nlris)
			builder.append("nlri", n);

		for(PathAttribute a : pathAttributes)
			builder.append("pathAttribute", a);
		
		return builder.toString();
	}
}
