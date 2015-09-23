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
 * File: onl.netfishers.blt.bgp.netty.fsm.FSMChannelManager.java 
 */
package onl.netfishers.blt.bgp.netty.fsm;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
class InternalFSMChannelManager {

	private FSMChannel managedChannel;
	private InternalFSMCallbacks callbacks;
	private List<FSMEventType> inboundEventStream = new LinkedList<FSMEventType>();
	private List<FSMEventType> outboundEventStream = new LinkedList<FSMEventType>();
	
	InternalFSMChannelManager(InternalFSMCallbacks callbacks) {
		this.callbacks = callbacks;  
	}
	
	void connect(FSMChannel managedChannel) {
		disconnect();
		
		this.managedChannel = managedChannel;
	}
	
	void disconnect() {
		if(isConnected())
			fireDisconnectRemotePeer();
		clear();
	}
	
	/**
	 * The remote connection to the peer (if established) shall be disconnected and closed
	 */
	void fireDisconnectRemotePeer() {
		if(managedChannel != null)
			callbacks.fireDisconnectRemotePeer(managedChannel);
	}

	/**
	 * Sent an <code>OPEN</code> message to the remote peer.
	 */
	void fireSendOpenMessage() {
		if(managedChannel != null) {
			callbacks.fireSendOpenMessage(managedChannel);
			this.outboundEventStream.add(FSMEventType.BGPOpen);
		}
	}

	/**
	 * send an FSM error notification to the remote peer
	 */
	void fireSendInternalErrorNotification() {
		if(managedChannel != null) {
			callbacks.fireSendInternalErrorNotification(managedChannel);
			this.outboundEventStream.add(FSMEventType.NotifyMsg);
		}
	}

	/**
	 * send a CEASE notification to the remote peer
	 */
	void fireSendCeaseNotification() {
		if(managedChannel != null) {
			callbacks.fireSendCeaseNotification(managedChannel);
			this.outboundEventStream.add(FSMEventType.NotifyMsg);
		}
	}

	/**
	 * send a keepalive message to the remote peer
	 */
	void fireSendKeepaliveMessage() {
		if(managedChannel != null) {
			callbacks.fireSendKeepaliveMessage(managedChannel);
			this.outboundEventStream.add(FSMEventType.KeepAliveMsg);
		}
	}

	/**
	 * fire a notification to the peer that the hold timer expired
	 */
	void fireSendHoldTimerExpiredNotification() {
		if(managedChannel != null) {
			callbacks.fireSendHoldTimerExpiredNotification(managedChannel);
			this.outboundEventStream.add(FSMEventType.NotifyMsg);
		}
	}

	/**
	 * fire an notification to the peer that it sent a bad update
	 */
	void fireSendUpdateErrorNotification() {
		if(managedChannel != null) {
			callbacks.fireSendUpdateErrorNotification(managedChannel);
			this.outboundEventStream.add(FSMEventType.NotifyMsg);
		}
	}

	boolean isConnected() {
		return (this.managedChannel != null);
	}

	boolean isManagedChannel(FSMChannel channel) {
		return (this.managedChannel == channel);
	}

	void clear() {
		this.managedChannel = null;
		this.inboundEventStream.clear();
		this.outboundEventStream.clear();
	}
	
	boolean hasSeenInboundFSMEvent(FSMEventType event) {
		return this.inboundEventStream.contains(event);
	}
	
	boolean hasSeenOutbboundFSMEvent(FSMEventType event) {
		return this.outboundEventStream.contains(event);
	}
	
	void pushInboundFSMEvent(FSMEventType event) {
		if(isConnected())
			this.inboundEventStream.add(event);
	}
}
