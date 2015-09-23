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
 * File: onl.netfishers.blt.bgp.netty.handlers.BgpEventFireChannelFutureListener.java 
 */
package onl.netfishers.blt.bgp.netty.handlers;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.UpstreamMessageEvent;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BgpEventFireChannelFutureListener implements ChannelFutureListener {

	private ChannelHandlerContext upstreamContext;
	private BgpEvent bgpEvent;
	
	BgpEventFireChannelFutureListener(ChannelHandlerContext upstreamContext) {
		this.upstreamContext = upstreamContext;
	}
	
	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelFutureListener#operationComplete(org.jboss.netty.channel.ChannelFuture)
	 */
	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if(upstreamContext != null && bgpEvent != null) {
			upstreamContext.sendUpstream(new UpstreamMessageEvent(upstreamContext.getChannel(), 
					bgpEvent, 
					upstreamContext.getChannel().getRemoteAddress()));
		}
	}

	/**
	 * @return the bgpEvent
	 */
	public BgpEvent getBgpEvent() {
		return bgpEvent;
	}

	/**
	 * @param bgpEvent the bgpEvent to set
	 */
	public void setBgpEvent(BgpEvent bgpEvent) {
		this.bgpEvent = bgpEvent;
	}

}
