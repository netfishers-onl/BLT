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
package onl.netfishers.blt.bgp.netty.service;

import java.util.concurrent.Executors;

import onl.netfishers.blt.bgp.BgpService;
import onl.netfishers.blt.bgp.config.nodes.PeerConfiguration;
import onl.netfishers.blt.bgp.netty.handlers.BGPv4ClientEndpoint;
import onl.netfishers.blt.bgp.netty.handlers.BGPv4Codec;
import onl.netfishers.blt.bgp.netty.handlers.BGPv4Reframer;
import onl.netfishers.blt.bgp.netty.handlers.InboundOpenCapabilitiesProcessor;
import onl.netfishers.blt.bgp.netty.handlers.ValidateServerIdentifier;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4Client {
	private static final Logger log = LoggerFactory.getLogger(BGPv4Client.class);

	private BGPv4ClientEndpoint clientEndpoint;
	private BGPv4Codec codec;
	private InboundOpenCapabilitiesProcessor inboundOpenCapProcessor;
	private ValidateServerIdentifier validateServer;
	private BGPv4Reframer reframer;
	private ChannelFactory channelFactory;
	
	private Channel clientChannel;

    public BGPv4Client () {
        codec = (BGPv4Codec)BgpService.getInstance(BGPv4Codec.class.getName());       
        clientEndpoint = (BGPv4ClientEndpoint)BgpService.getInstance(BGPv4ClientEndpoint.class.getName());
        inboundOpenCapProcessor = (InboundOpenCapabilitiesProcessor)BgpService.getInstance(InboundOpenCapabilitiesProcessor.class.getName());
        validateServer = (ValidateServerIdentifier)BgpService.getInstance(ValidateServerIdentifier.class.getName());
        reframer = new BGPv4Reframer();
        channelFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
    }

	public ChannelFuture startClient(PeerConfiguration peerConfiguration) {
		ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);

		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				
				pipeline.addLast(BGPv4Reframer.HANDLER_NAME, reframer);
				pipeline.addLast(BGPv4Codec.HANDLER_NAME, codec);
				pipeline.addLast(InboundOpenCapabilitiesProcessor.HANDLER_NAME, inboundOpenCapProcessor);
				pipeline.addLast(ValidateServerIdentifier.HANDLER_NAME, validateServer);
				pipeline.addLast(BGPv4ClientEndpoint.HANDLER_NAME, clientEndpoint);
				
				return pipeline;
			}
		});

		bootstrap.setOption("tcpnoDelay", true);
		bootstrap.setOption("keepAlive", true);
		
		log.info("connecting remote peer " + peerConfiguration.getPeerName() 
				+ " with address " + peerConfiguration.getClientConfig().getRemoteAddress());
		
		return bootstrap.connect(peerConfiguration.getClientConfig().getRemoteAddress());
	}

	public void stopClient() {
		if(clientChannel != null) {
			clientChannel.close();
			this.clientChannel = null;
		}
	}

	/**
	 * @return the clientChannel
	 */
	public Channel getClientChannel() {
		return clientChannel;
	}	
}
