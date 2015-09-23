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
 * File: onl.netfishers.blt.bgp.netty.fsm.FSMEvent.java 
 */
package onl.netfishers.blt.bgp.netty.fsm;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class FSMEvent {

	static class ChannelFSMEvent extends FSMEvent {
		private FSMChannel channel;
		
		public ChannelFSMEvent(FSMEventType type, FSMChannel channel) {
			super(type);
			
			this.channel = channel;
		}

		/**
		 * @return the channel
		 */
		public FSMChannel getChannel() {
			return channel;
		}

	}
	
	private FSMEventType type;
	
	protected FSMEvent(FSMEventType type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public FSMEventType getType() {
		return type;
	}

	// Adminstrative events
	public static FSMEvent manualStart() {
		return new FSMEvent(FSMEventType.ManualStart);  
	}

	public static FSMEvent manualStop() {
		return new FSMEvent(FSMEventType.ManualStop);
	}
	
	public static FSMEvent automaticStart() {
		return new FSMEvent(FSMEventType.AutomaticStart);
	}
	
	public static FSMEvent automaticStop() {
		return new FSMEvent(FSMEventType.AutomaticStop);
	}

	// Timer events
	public static final FSMEvent connectRetryTimerExpires() {
		return new FSMEvent(FSMEventType.ConnectRetryTimer_Expires);
	}
	
	public static final FSMEvent holdTimerExpires() {
		return new FSMEvent(FSMEventType.HoldTimer_Expires);
	}
	
	public static final FSMEvent keepaliveTimerExpires() {
		return new FSMEvent(FSMEventType.KeepaliveTimer_Expires);
	}
	
	public static final FSMEvent delayOpenTimerExpires() {
		return new FSMEvent(FSMEventType.DelayOpenTimer_Expires);
	}
	
	public static final FSMEvent idleHoldTimerExpires() {
		return new FSMEvent(FSMEventType.IdleHoldTimer_Expires);
	}
	
	// TCP connection-based events
	public static final FSMEvent tcpConnectionConfirmed(FSMChannel channel) {
		return new ChannelFSMEvent(FSMEventType.TcpConnectionConfirmed, channel);
	}
	
	public static final FSMEvent tcpConnectionRequestAcked(FSMChannel channel) {
		return new ChannelFSMEvent(FSMEventType.Tcp_CR_Acked, channel);
	}
	
	public static final FSMEvent tcpConnectionFails(FSMChannel channel) {
		return new ChannelFSMEvent(FSMEventType.TcpConnectionFails, channel);
	}
	
	// BGP Message-based events
	public static final FSMEvent bgpOpen(FSMChannel channel) {
		return new ChannelFSMEvent(FSMEventType.BGPOpen, channel);
	}

	public static final FSMEvent bgpHeaderError() {
		return new FSMEvent(FSMEventType.BGPHeaderErr);
	}

	public static final FSMEvent bgpOpenMessageError() {
		return new FSMEvent(FSMEventType.BGPOpenMsgErr);
	}

	public static final FSMEvent notifyMessageVersionError() {
		return new FSMEvent(FSMEventType.NotifyMsgVerErr);
	}
	
	public static final FSMEvent notifyMessage() {
		return new FSMEvent(FSMEventType.NotifyMsg);
	}

	public static final FSMEvent keepAliveMessage() {
		return new FSMEvent(FSMEventType.KeepAliveMsg);
	}
	
	public static final FSMEvent updateMessage() {
		return new FSMEvent(FSMEventType.UpdateMsg);
	}

	public static final FSMEvent updateMessageError() {
		return new FSMEvent(FSMEventType.UpdateMsgErr);
	}
}
