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
package onl.netfishers.blt.bgp.netty.handlers;

import onl.netfishers.blt.bgp.netty.protocol.BGPv4Packet;
import onl.netfishers.blt.bgp.netty.protocol.BGPv4PacketDecoder;
import onl.netfishers.blt.bgp.netty.protocol.ProtocolPacketException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.DownstreamMessageEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.UpstreamMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Protocol codec which translates between protocol network packets and protocol POJOs 
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4Codec extends SimpleChannelHandler {
	public static final String HANDLER_NAME = "BGP4-Codec";
	private static final Logger log = LoggerFactory.getLogger(BGPv4Codec.class);

	private static BGPv4PacketDecoder packetDecoder = new BGPv4PacketDecoder();

    public BGPv4Codec () {
    }

	/**
	 * Upstream handler which takes care of the network packet to POJO translation
	 * 
	 * @param ctx the channel handler context
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if(e.getMessage() instanceof ChannelBuffer) {
			ChannelBuffer buffer = (ChannelBuffer)e.getMessage();
			
			try {
				BGPv4Packet packet = packetDecoder.decodePacket(buffer);
				
				log.info("received packet " + packet);
				
				if(packet != null) {
					ctx.sendUpstream(new UpstreamMessageEvent(e.getChannel(), packet, e.getRemoteAddress()));
				}
			} catch(ProtocolPacketException ex) {
				log.error("received malformed protocol packet, closing connection", ex);
				
				NotificationHelper.sendNotification(ctx, 
						ex.toNotificationPacket(), 
						new BgpEventFireChannelFutureListener(ctx));
			} catch(Exception ex) {
				log.error("generic decoding exception, closing connection", ex);
				
				ctx.getChannel().close();
			}
		} else {
			log.error("expected a {} message payload, got a {} message payload", 
					ChannelBuffer.class.getName(), 
					e.getMessage().getClass().getName()); 
		}
	}

	/**
	 * Downstream handler which takes care of the POJO to network packet translation
	 */
	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if(e.getMessage() instanceof BGPv4Packet) {
			ChannelBuffer buffer = ((BGPv4Packet)e.getMessage()).encodePacket();
						
			log.info("writing packet " + e.getMessage());

			if(buffer != null) {
				ctx.sendDownstream(new DownstreamMessageEvent(e.getChannel(), e.getFuture(), buffer, e.getRemoteAddress()));
			}
		} else {
			log.error("expected a {} message payload, got a {} message payload", 
					BGPv4Packet.class.getName(), 
					e.getMessage().getClass().getName()); 
		}
	}

}
