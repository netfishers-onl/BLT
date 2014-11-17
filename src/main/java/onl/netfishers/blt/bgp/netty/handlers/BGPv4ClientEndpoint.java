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
/**
 * Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
 * 
 * License: same as above
 * 
 * Modified to run as an independent java application, one that does not
 * require webserver or app server
 */
package onl.netfishers.blt.bgp.netty.handlers;

import java.net.InetSocketAddress;

import onl.netfishers.blt.bgp.BgpService;
import onl.netfishers.blt.bgp.netty.PeerConnectionInformation;
import onl.netfishers.blt.bgp.netty.PeerConnectionInformationAware;
import onl.netfishers.blt.bgp.netty.fsm.BGPv4FSM;
import onl.netfishers.blt.bgp.netty.protocol.BGPv4Packet;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This handler acts as the client side pipeline end. It attaches the peer connection info to the channel context of all insterested 
 * handlers when the channel is connected. Each message it receives is forwarded to the appropiate finite state machine instance.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4ClientEndpoint extends SimpleChannelHandler {
	public static final String HANDLER_NAME ="BGP4-ClientEndpoint";

	private static final Logger log = LoggerFactory.getLogger(BGPv4ClientEndpoint.class.getName());

	
    public BGPv4ClientEndpoint () {
    }

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#messageReceived(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.MessageEvent)
	 */
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		
		BGPv4FSM fsm = BgpService.lookupFSM((InetSocketAddress)e.getRemoteAddress());
			
		if(fsm == null) {
			log.error("Internal Error: client for address " + e.getRemoteAddress() + " is unknown");
			
			ctx.getChannel().close();
		} else {
			if(e.getMessage() instanceof BGPv4Packet) {
				fsm.handleMessage(ctx.getChannel(), (BGPv4Packet)e.getMessage());
			} else if(e.getMessage() instanceof BgpEvent) {
				fsm.handleEvent(ctx.getChannel(), (BgpEvent)e.getMessage());
			} else {
				log.error("unknown payload class " + e.getMessage().getClass().getName() + " received for peer " + e.getRemoteAddress());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelConnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		log.info("connected to client " + e.getChannel().getRemoteAddress());
		
		BGPv4FSM fsm = BgpService.lookupFSM((InetSocketAddress)e.getChannel().getRemoteAddress());
		
		if(fsm == null) {
			log.error("Internal Error: client for address " + e.getChannel().getRemoteAddress() + " is unknown");
			
			ctx.getChannel().close();
		} else {
			ChannelPipeline pipeline = ctx.getPipeline();
			PeerConnectionInformation pci = fsm.getPeerConnectionInformation();
			
			for(String handlerName : pipeline.getNames()) {
				ChannelHandler handler = pipeline.get(handlerName);

				if(handler.getClass().isAnnotationPresent(PeerConnectionInformationAware.class)) {
					log.info("attaching peer connection information " + pci + " to handler " + handlerName + " for client " + e.getChannel().getRemoteAddress());
					
					pipeline.getContext(handlerName).setAttachment(pci);
				}
			}
			
			fsm.handleClientConnected(e.getChannel());
			ctx.sendUpstream(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelConnected(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		log.info("disconnected from client " + e.getChannel().getRemoteAddress());
		
		BGPv4FSM fsm = BgpService.lookupFSM((InetSocketAddress)e.getChannel().getRemoteAddress());
		
		if(fsm == null) {
			log.error("Internal Error: client for address " + e.getChannel().getRemoteAddress() + " is unknown");
			
			ctx.getChannel().close();
		} else {
			
			fsm.handleDisconnected(e.getChannel());
			ctx.sendUpstream(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.SimpleChannelHandler#channelClosed(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.ChannelStateEvent)
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		
		BGPv4FSM fsm;
		
		// if we weren't connected at all, then let's try to find a peer to
		// which we were trying to connect to
		if (e.getChannel().getRemoteAddress() == null) {
			fsm = BgpService.lookupFSM();
			log.info("closed channel to client {}", fsm.getRemotePeerAddress());
			
		} else {
			log.info("closed channel to client {}", e.getChannel().getRemoteAddress());
			fsm = BgpService.lookupFSM((InetSocketAddress)e.getChannel().getRemoteAddress());
		}
		
		if(fsm == null) {
			log.error("Internal Error: client for address " + e.getChannel().getRemoteAddress() + " is unknown");
		} else {
			fsm.handleClosed(e.getChannel());
			ctx.sendUpstream(e);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		log.error("Internal Error: Caught exception " + e.toString());
	}
}
