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
 * File: onl.netfishers.blt.bgp.netty.fsm.InternalFSM.java 
 */
/**
 * Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
 * 
 * License: same as above
 * 
 * Modified to run as an independent java application, one that does not
 * require webserver or app server
 */
package onl.netfishers.blt.bgp.netty.fsm;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import onl.netfishers.blt.bgp.config.nodes.PeerConfiguration;
import onl.netfishers.blt.bgp.netty.FSMState;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Internal FSM to seperate FSM logic from the connection management and message handling code.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class InternalFSM {
	private static final Logger log = LoggerFactory.getLogger(InternalFSM.class);

	
	private FSMState state = FSMState.Idle;
	private PeerConfiguration peerConfiguration;
	private InternalFSMCallbacks callbacks;

	private int connectRetryCounter = 0;
	private boolean canAcceptConnection = false;
	
	private FireEventTimeManager<FireConnectRetryTimerExpired> fireConnectRetryTimerExpired;
	private FireEventTimeManager<FireIdleHoldTimerExpired> fireIdleHoldTimerExpired;
	private FireEventTimeManager<FireDelayOpenTimerExpired> fireDelayOpenTimerExpired;
	private FireEventTimeManager<FireHoldTimerExpired> fireHoldTimerExpired;
	private FireRepeatedEventTimeManager<FireAutomaticStart> fireRepeatedAutomaticStart;
	private FireEventTimeManager<FireSendKeepalive> fireKeepaliveTimerExpired;
	
	private int peerProposedHoldTime = 0;
	private boolean haveFSMError = false;
	private long lastConnectStamp = 0;

	private InternalFSMChannelManager connectedChannelManager;
	private InternalFSMChannelManager activeChannelManager;
	
	InternalFSM() {
		fireConnectRetryTimerExpired = new FireEventTimeManager<FireConnectRetryTimerExpired>();
		fireIdleHoldTimerExpired =  new FireEventTimeManager<FireIdleHoldTimerExpired>();
		fireDelayOpenTimerExpired = new FireEventTimeManager<FireDelayOpenTimerExpired>();
		fireHoldTimerExpired = new FireEventTimeManager<FireHoldTimerExpired>();
		fireRepeatedAutomaticStart = new FireRepeatedEventTimeManager<FireAutomaticStart>();
		fireKeepaliveTimerExpired = new FireEventTimeManager<FireSendKeepalive>();
	}
	
	void setup(PeerConfiguration peerConfiguration, InternalFSMCallbacks callbacks) throws SchedulerException {
		this.peerConfiguration = peerConfiguration;
		this.callbacks = callbacks;
		
		fireConnectRetryTimerExpired.createJobDetail(FireConnectRetryTimerExpired.class, this);
		fireIdleHoldTimerExpired.createJobDetail(FireIdleHoldTimerExpired.class, this);
		fireDelayOpenTimerExpired.createJobDetail(FireDelayOpenTimerExpired.class, this);
		fireHoldTimerExpired.createJobDetail(FireHoldTimerExpired.class, this);
		fireKeepaliveTimerExpired.createJobDetail(FireSendKeepalive.class, this);
		
		fireRepeatedAutomaticStart.createJobDetail(FireAutomaticStart.class, this);
		
		this.connectedChannelManager = new InternalFSMChannelManager(callbacks);
		this.activeChannelManager = new InternalFSMChannelManager(callbacks);
	}
	
	void destroyFSM() {
		try {
			fireConnectRetryTimerExpired.shutdown();
			fireIdleHoldTimerExpired.shutdown();
			fireDelayOpenTimerExpired.shutdown();
			fireHoldTimerExpired.shutdown();
			fireRepeatedAutomaticStart.shutdown();
			fireKeepaliveTimerExpired.shutdown();
		} catch (SchedulerException e) {
			log.error("Internal error: failed to shutdown internal FSM for peer " + peerConfiguration.getPeerName(), e);
		}
	}

	void handleEvent(FSMEvent event) {
		FSMChannel channel = null;
		InternalFSMChannelManager channelManager = null;
		
		if(event instanceof FSMEvent.ChannelFSMEvent) {
			channel = ((FSMEvent.ChannelFSMEvent)event).getChannel();
			
			if(connectedChannelManager.isManagedChannel(channel))
				channelManager = connectedChannelManager;
			else if(activeChannelManager.isManagedChannel(channel))
				channelManager = activeChannelManager;			
		}
		
		switch(event.getType()) {
		case AutomaticStart:
		case ManualStart:
			handleStartEvent(event.getType());
			break;
		case AutomaticStop:
		case ManualStop:
			handleStopEvent(event.getType());
			break;
		case ConnectRetryTimer_Expires:
			handleConnectRetryTimerExpiredEvent();
			break;
		case IdleHoldTimer_Expires:
			handleIdleHoldTimerExpiredEvent();
			break;
		case TcpConnectionConfirmed:
			if(channel != null)
				handleTcpConnectionConfirmed(channel);
			else
				haveFSMError = true;
			break;
		case Tcp_CR_Acked:
			if(channel != null)
				handleTcpConnectionAcked(channel);
			else
				haveFSMError = true;
			break;
		case TcpConnectionFails:
			if(channel != null)
				handleTcpConnectionFails(channel);
			else
				haveFSMError = true;
			break;
		case DelayOpenTimer_Expires:
			handleDelayOpenTimerExpiredEvent();
			break;
		case HoldTimer_Expires:
			handleHoldTimerExpiredEvent();
			break;
		case BGPOpen:
			if(channel != null && channelManager != null)
				handleBgpOpenEvent(channel, channelManager);
			else
				haveFSMError = true;
			break;
		case KeepAliveMsg:
			handleKeepaliveMessageEvent();
			break;
		case KeepaliveTimer_Expires:
			handleKeepaliveTimerExpiresEvent();
			break;
		case NotifyMsg:
			handleNotifyMessageEvent();
			break;
		case NotifyMsgVerErr:
			handleNotifyMessageVersionErrorEvent();
			break;
		case BGPOpenMsgErr:
			handleBGPOpenMessageErrorEvent();
			break;
		case BGPHeaderErr:
			handleBGPHeaderErrorEvent();
			break;
		case UpdateMsg:
			handleUpdateMessageEvent();
			break;
		case UpdateMsgErr:
			handleUpdateMessageErrorEvent();
			break;
		}
		
		if(channelManager != null)
			channelManager.pushInboundFSMEvent(event.getType());

		if(haveFSMError) {
			
			haveFSMError = false;
			
			// sent internal error notification only when in state established or open confirm or open sent 
			switch(state) {
			case Established:
			case OpenConfirm:
			case OpenSent:
				connectedChannelManager.fireSendInternalErrorNotification();
				activeChannelManager.fireSendInternalErrorNotification();
				break;
			default:
				log.error("Don't know what to do with FSMError in state: " + state.name());
				break;
			}
			
			connectRetryCounter++;
			 
			moveStateToIdle();
		}
	}
	
	/**
	 * handle any kind of start event. Unless the FSM is in <code>Idle</code> state the event is ignored
	 * <ul>
	 * <li>If passive TCP establishment is disabled then fire the connect remote peer callback and move to <code>Connect</code> state</li>
	 * <li>If passive TCP establishment is enabled then move to <code>Connect</code> state</li>
	 * </ul>
	 * @param fsmEventType 
	 * @throws SchedulerException 
	 */
	private void handleStartEvent(FSMEventType fsmEventType)  {
		if(state == FSMState.Idle) {
			this.connectRetryCounter = 0;
			canAcceptConnection = true;

			try {
			if(peerConfiguration.isDampPeerOscillation() && fireIdleHoldTimerExpired.isJobScheduled())
				return;
			} catch(SchedulerException e) {
				log.error("cannot query idle hold timer for peer " + peerConfiguration.getPeerName(), e);
				
				haveFSMError = true;
			}
			
			boolean temporaryPassive = false;
			
			if(fsmEventType == FSMEventType.AutomaticStart)
				temporaryPassive = (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastConnectStamp) <  peerConfiguration.getConnectRetryTime());

			if(!peerConfiguration.isPassiveTcpEstablishment() && !temporaryPassive) {
				moveStateToConnect();
			} else {
				moveStateToActive();
			}
			
			try {
				if(fsmEventType == FSMEventType.AutomaticStart && peerConfiguration.isAllowAutomaticStart())
					fireRepeatedAutomaticStart.startRepeatedJob(peerConfiguration.getAutomaticStartInterval()); 
			} catch(SchedulerException e) {
				log.error("failed to start automatic restart timer");
				
				haveFSMError = true;
			}

		}
	}

	/**
	 * handle any kind of stop event
	 */
	private void handleStopEvent(FSMEventType type) {
		switch(type) {
		case AutomaticStop:
			this.connectRetryCounter++;	
			break;
		case ManualStop:
			this.connectRetryCounter = 0;
			break;
		default:
			log.error("handleStopEvent: Got an invalid FSMEventType: " + type.name());
			break;
		}
		
		moveStateToIdle();
	}
	
	/**
	 * handle the connect retry timer fired event.
	 * <ol>
	 * <li>Fire disconnect remote peer callback</li>
	 * <li>perform actions based on current state:
	 * <ul>
	 * <li>If state is <code>Connect</code>:
	 * <ul>
	 * <li>If peer dampening is enabled then restart the idle hold timer and move state to <code>Idle</code></li>
	 * <li>If peer dampening is disabled then restart the connect retry timer then fire the connect remote peer callback and stay <code>Connect</code> state</li>
	 * </ul>
	 * </li>
	 * <li>If the state is <code>Active</code> then fire the connect remote peer callback and  move the state to <code>Connect</code>.
	 * <li>If the state is <code>Idle</code>:<ul>
	 * <li>If passive TCP estalishment is disabled then fire the connect remote peer callback and move to <code>Connect</code> state</li>
	 * <li>If passive TCP estalishment is ensabled then move to <code>Connect</code> state</li>
	 * </ul>
	 * </li>
	 * </ul></li>
	 * </ol>
	 */
	private void handleConnectRetryTimerExpiredEvent() {
		switch(state) {
		case Connect:
			connectedChannelManager.disconnect();
			
			if(peerConfiguration.isDampPeerOscillation()) {
				state = FSMState.Idle;
				
				try {
					fireIdleHoldTimerExpired.scheduleJob(peerConfiguration.getIdleHoldTime() << connectRetryCounter);
				} catch (SchedulerException e) {
					log.error("Interal Error: cannot schedule idle hold timer for peer " + peerConfiguration.getPeerName(), e);
					
					haveFSMError = true;
				}
			} else {
				this.connectRetryCounter++;
				
				moveStateToConnect();
			}
			break;
		case Active:
			this.connectRetryCounter++;
			
			moveStateToConnect();
			break;
		case Idle:
			if(!peerConfiguration.isPassiveTcpEstablishment())
				moveStateToConnect();
			else
				moveStateToActive();			
			break;
		default:
			haveFSMError=true;
			break;
		}
	}
	
	/**
	 * handle the idle hold timer expired event. If the current state is <code>Idle</code> then the machine is moved into state <code>Connect</code>
	 */
	private void handleHoldTimerExpiredEvent() {
		switch(state) {
		case Connect:
		case Active:
			this.connectRetryCounter++;
			moveStateToIdle();		
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			connectedChannelManager.fireSendHoldTimerExpiredNotification();
			activeChannelManager.fireSendHoldTimerExpiredNotification();
			
			this.connectRetryCounter++;
			moveStateToIdle();		
			break;
		case Idle:
			// do nothing
			break;
		}
	}

	/**
	 * handle the idle hold timer expired event. If the current state is <code>Idle</code> then the machine is moved into state <code>Connect</code>
	 */
	private void handleIdleHoldTimerExpiredEvent() {
		switch(state) {
		case Connect:
		case Active:
			this.connectRetryCounter++;
			moveStateToIdle();		
			break;
		case Idle:
			this.connectRetryCounter++;
			moveStateToConnect();
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			haveFSMError = true;
			break;
		}
	}

	/**
	 * handle the delay open timer expired event. Depending on the current state the following actions are performed:
	 * <ul>
	 * <li>If the current state is <code>Connect</code> then send an <code>OPEN</code> message to the peer, set the hold timer to 600 seconds
	 * and move the state to <code>OpenSent</code>
	 * </ul>
	 */
	private void handleDelayOpenTimerExpiredEvent() {
		switch(state) {
		case Connect:
		case Active:
			moveStateToOpenSent();
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			haveFSMError = true;
			break;
		case Idle:
			break;
		}
	}
	
	/**
	 * Handle TCP connections failures. 
	 * <ul>
	 * <li>if the current state is <code>Connect</code> then move to <code>Active</code>
	 * <li>if the current state is <code>Active</code> then move to <code>Idle</code>
	 * </ul>
	 * @param i 
	 */
	private void handleTcpConnectionFails(FSMChannel channel) {
		switch(state) {
		case Connect:
			try {
				if (isDelayOpenTimerRunning()) {
					moveStateToActive();
				} else {
					moveStateToIdle();
				}
			} catch(SchedulerException e) {
				log.error("Internal Error: Failed to query delay open timer for peer " + peerConfiguration.getPeerName());
			
				haveFSMError = true;
			}
			break;
		case Active:
			this.connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
			if(connectedChannelManager.isManagedChannel(channel)) {
				if(!activeChannelManager.hasSeenOutbboundFSMEvent(FSMEventType.BGPOpen))
					moveStateToActive();
				connectedChannelManager.clear();
			} else if(activeChannelManager.isManagedChannel(channel)) {
				if(!connectedChannelManager.hasSeenOutbboundFSMEvent(FSMEventType.BGPOpen)) {
					if(connectedChannelManager.isConnected())
						moveStateToConnect();
					else
						moveStateToActive();
				}
				activeChannelManager.clear();
			}
			break;
		case OpenConfirm:
			if(connectedChannelManager.isManagedChannel(channel)) {
				connectedChannelManager.clear();
				if(!activeChannelManager.hasSeenInboundFSMEvent(FSMEventType.BGPOpen))
					moveStateToIdle();
			} else if(activeChannelManager.isManagedChannel(channel)) {
				activeChannelManager.clear();
				if(!connectedChannelManager.hasSeenInboundFSMEvent(FSMEventType.BGPOpen))
					moveStateToIdle();				
			}
			break;	
		case Established:
			if(connectedChannelManager.isManagedChannel(channel)) {
				if(connectedChannelManager.hasSeenInboundFSMEvent(FSMEventType.BGPOpen))
					moveStateToIdle();
				else
					connectedChannelManager.clear();
			} else if(activeChannelManager.isManagedChannel(channel)) {
				if(activeChannelManager.hasSeenOutbboundFSMEvent(FSMEventType.BGPOpen))
					moveStateToIdle();
				else
					activeChannelManager.clear();
			}
			break;
		case Idle:
			// do nothing
			break;	
		}
	}

	/**
	 * Handle the connection originated by the local peer to the remote peer has being established. 
	 * <ul>
	 * <li>If the current state is <code>Connect</code>:<ul>
	 * <li>If the delay open flag is set then the connect retry timer is canceled, the delay open timer is started with the configured value.
	 * The state stay at <code>Connect</code></li>
	 * <li>If the delay open flag is not set then the connect retry timer is canceled, an <code>OPEN</code> message is sent to the peer and
	 * the state is moved to <code>OpenSent</code></li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * @param channelId the ID of the channel with which the connection was established
	 */
	private void handleTcpConnectionAcked(FSMChannel channel) {
		switch(state) {
		case Connect:
		case Active:
			connectedChannelManager.connect(channel);
			if(peerConfiguration.isDelayOpen()) {
				try {
					fireConnectRetryTimerExpired.cancelJob();
				} catch (SchedulerException e) {
					log.error("Internal Error: cannot cancel connect retry timer for peer " + peerConfiguration.getPeerName(), e);
				}
				
				try {
					fireDelayOpenTimerExpired.cancelJob();
					fireDelayOpenTimerExpired.scheduleJob(peerConfiguration.getDelayOpenTime());
				} catch (SchedulerException e) {
					log.error("Internal Error: cannot schedule open delay timer for peer " + peerConfiguration.getPeerName(), e);
					
					haveFSMError = true;
				}
			} else {
				moveStateToOpenSent();
			}
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			if(connectedChannelManager.isConnected() || !activeChannelManager.isConnected())
				haveFSMError = true;
			else
				connectedChannelManager.connect(channel);
			break;
		case Idle:
			// do nothing
			break;
		}
	}
		
	/**
	 * Handle the connection from the remote peer to the local peer being established. 
	 * <ul>
	 * <li>If the current state is <code>Connect</code>:<ul>
	 * <li>If the delay open flag is set then the connect retry timer is canceled, the delay open timer is started with the configured value.
	 * The state stay at <code>Connect</code></li>
	 * <li>If the delay open flag is not set then the connect retry timer is canceled, an <code>OPEN</code> message is sent to the peer and
	 * the state is moved to <code>OpenSent</code></li>
	 * </ul>
	 * </li>
	 * </ul>
	 * 
	 * @param channelId the ID of the channel with which the connection was established
	 */
	private void handleTcpConnectionConfirmed(FSMChannel channel) {
		switch(state) {
		case Connect:
		case Active:
			activeChannelManager.connect(channel);
			
			if(peerConfiguration.isDelayOpen()) {
				try {
					fireConnectRetryTimerExpired.cancelJob();
				} catch (SchedulerException e) {
					log.error("Internal Error: cannot cancel connect retry timer for peer " + peerConfiguration.getPeerName(), e);
				}
				
				try {
					fireDelayOpenTimerExpired.cancelJob();
					fireDelayOpenTimerExpired.scheduleJob(peerConfiguration.getDelayOpenTime());
				} catch (SchedulerException e) {
					log.error("Internal Error: cannot schedule open delay timer for peer " + peerConfiguration.getPeerName(), e);
					
					haveFSMError = true;
				}
			} else {
				moveStateToOpenSent();
			}
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			if(activeChannelManager.isConnected() && activeChannelManager.hasSeenInboundFSMEvent(FSMEventType.BGPOpen))
				haveFSMError = true;
			else
				activeChannelManager.connect(channel);
			break;
		case Idle:
			// do nothing
			break;
		}
	}
	/**
	 * handle an inbound <code>OPEN</code> mesage from the remote peer
  	 *
	 * @param channelId the ID of the channel with which the connection was established
	 */
	private void handleBgpOpenEvent(FSMChannel channel, InternalFSMChannelManager channelManager) {
		switch(state) {
		case Connect:
		case Active:
			try {
				if(fireDelayOpenTimerExpired.isJobScheduled()) {
					moveStateToOpenConfirm(true);
				} else {
					connectRetryCounter++;
					moveStateToIdle();
				}
			} catch(SchedulerException e) {
				log.error("cannot query delay openn timer for peer " + peerConfiguration.getPeerName(), e);
			
				haveFSMError = true;
			}
			break;
		case OpenSent:
			if(channelManager.hasSeenInboundFSMEvent(FSMEventType.BGPOpen))
				haveFSMError = true;
			else if(connectedChannelManager.isConnected() && activeChannelManager.isConnected()) {				
				if(peerConfiguration.getLocalBgpIdentifier() < peerConfiguration.getRemoteBgpIdentifier()) {
					connectedChannelManager.fireSendCeaseNotification();
					connectedChannelManager.disconnect();
				} else {
					activeChannelManager.fireSendCeaseNotification();
					activeChannelManager.disconnect();
				}
			}
			moveStateToOpenConfirm(false);
			break;
		case OpenConfirm:
			if(channelManager.hasSeenInboundFSMEvent(FSMEventType.BGPOpen))
				haveFSMError = true;
			else if(connectedChannelManager.isConnected() && activeChannelManager.isConnected()) {				
				if(peerConfiguration.getLocalBgpIdentifier() < peerConfiguration.getRemoteBgpIdentifier()) {
					connectedChannelManager.fireSendCeaseNotification();
					connectedChannelManager.disconnect();
				} else {
					activeChannelManager.fireSendCeaseNotification();
					activeChannelManager.disconnect();
				}
			}
			break;
		case Established:
			if(channelManager.hasSeenInboundFSMEvent(FSMEventType.BGPOpen))
				haveFSMError = true;
			else if(connectedChannelManager.isConnected() && activeChannelManager.isConnected()) {				
				if(peerConfiguration.getLocalBgpIdentifier() < peerConfiguration.getRemoteBgpIdentifier()) {
					connectedChannelManager.fireSendCeaseNotification();
					moveStateToIdle();
				} else {
					activeChannelManager.fireSendCeaseNotification();
					activeChannelManager.disconnect();
				}
			}
			break;
		case Idle:
			// do nothing here
			break;
		}
	}
	
	/**
	 * handle an <code>KEEPALIVE</CODE> message sent from the remote peer
	 */
	private void handleKeepaliveMessageEvent() {
		switch(state) {
		case Connect:
		case Active:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
			haveFSMError = true;
			break;
		case OpenConfirm:
			moveStateToEstablished();
			break;
		case Established:
			try {
				fireHoldTimerExpired.cancelJob();
				fireHoldTimerExpired.scheduleJob(getNegotiatedHoldTime());
			} catch (SchedulerException e) {
				log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
				
				haveFSMError = true;
			}
			break;
		case Idle:
			// do nothing
			break;
		}		
	}
	
	/**
	 * handle the expired keepalive timer on the local side
	 */
	private void handleKeepaliveTimerExpiresEvent() {
		switch(state) {
		case Connect:
		case Active:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
			haveFSMError=true;
			break;
		case OpenConfirm:
		case Established:
			if(activeChannelManager.hasSeenInboundFSMEvent(FSMEventType.BGPOpen))
				activeChannelManager.fireSendKeepaliveMessage();
			if(connectedChannelManager.hasSeenInboundFSMEvent(FSMEventType.BGPOpen))
				connectedChannelManager.fireSendKeepaliveMessage();

			try {
				fireKeepaliveTimerExpired.scheduleJob(getSendKeepaliveTime());
			} catch(SchedulerException e) {
				log.error("cannont start send keepalive timer", e);
				
				haveFSMError = true;				
			}
			break;
		case Idle:
			// do nothing
			break;
		}
	}

	/**
	 * handle a <code>NOTIFY</code> message sent from the remote peer
	 */
	private void handleNotifyMessageEvent() {
		switch(state) {
		case Connect:
		case Active:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
		case OpenConfirm:
		case Established:
			haveFSMError = true;
			break;
		case Idle:
			// do nothing
			break;
		}
	}
	
	/**
	 * handle a malformed <code>NOTIFY</code> message sent from the remote peer
	 */
	private void handleNotifyMessageVersionErrorEvent() {
		switch(state) {
		case Connect:
		case Active:
		case OpenSent:
		case OpenConfirm:
		case Established:
			moveStateToIdle();
			break;
		case Idle:
			// do nothing
			break;
		}
	}
	
	/**
	 * handle a malformed <code>OPEN</code> message sent from the remote peer
	 */
	private void handleBGPOpenMessageErrorEvent() {
		switch(state) {
		case Connect:
		case Active:
		case OpenSent:
		case OpenConfirm:
		case Established:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case Idle:
			// do nothing
			break;
		}		
	}
	
	/**
	 * handle a malformed BGP packet where the initial header checks failed
	 */
	private void handleBGPHeaderErrorEvent() {
		switch(state) {
		case Connect:
		case Active:
		case OpenSent:
		case OpenConfirm:
		case Established:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case Idle:
			// do nothing
			break;
		}
	}
	
	/**
	 * handle an <code>UPDATE</code> message sent from the remote peer
	 */
	private void handleUpdateMessageEvent() {
		switch(state) {
		case Connect:
		case Active:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
		case OpenConfirm:
			haveFSMError=true;
			break;
		case Established:
			try {
				fireHoldTimerExpired.cancelJob();
				fireHoldTimerExpired.scheduleJob(getNegotiatedHoldTime());
			} catch (SchedulerException e) {
				log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
				
				haveFSMError = true;
			}
			break;
		case Idle:
			// do nothing
			break;
		}		
	}
	
	/**
	 * handle a malformed <code>UPDATE</code> message sent from the remote peer
	 */
	private void handleUpdateMessageErrorEvent() {
		switch(state) {
		case Connect:
		case Active:
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case OpenSent:
		case OpenConfirm:
			haveFSMError = true;
			break;
		case Established:
			activeChannelManager.fireSendUpdateErrorNotification();
			connectedChannelManager.fireSendUpdateErrorNotification();
			connectRetryCounter++;
			moveStateToIdle();
			break;
		case Idle:
			// do nothing
			break;
		}		
	}

	/**
	 * check if connections can be accepted
	 * 
	 * @return
	 */	
	boolean isCanAcceptConnection() {
		return this.canAcceptConnection;
	}

	/**
	 * @return the state
	 */
	FSMState getState() {
		return state;
	}

	/**
	 * @return the connectRetryCounter
	 */
	int getConnectRetryCounter() {
		return connectRetryCounter;
	}

	/**
	 * check if the connect retry timer is currently running
	 * 
	 * @return true if the timer is running
	 * @throws SchedulerException
	 */
	boolean isConnectRetryTimerRunning() throws SchedulerException {
		return fireConnectRetryTimerExpired.isJobScheduled();
	}
	
	/**
	 * get the date when the connect retry timer will fire
	 * 
	 * @return the date when the timmer will fire
	 * @throws SchedulerException
	 */
	Date getConnectRetryTimerDueWhen() throws SchedulerException {
		return fireConnectRetryTimerExpired.getFiredWhen();
	}

	/**
	 * check if the idle hold timer is currently running
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	boolean isIdleHoldTimerRunning() throws SchedulerException {
		return fireIdleHoldTimerExpired.isJobScheduled();
	}
	
	/**
	 * get the date when then idle hold timer will fire
	 * @return
	 * @throws SchedulerException
	 */
	Date getIdleHoldTimerDueWhen() throws SchedulerException {
		return fireIdleHoldTimerExpired.getFiredWhen();
	}

	/**
	 * check if the delay open timer is currently running
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	boolean isDelayOpenTimerRunning() throws SchedulerException {
		return fireDelayOpenTimerExpired.isJobScheduled();
	}
	
	/**
	 * get the date when the delay open timer will fire
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public Date getDelayOpenTimerDueWhen() throws SchedulerException {
		return fireDelayOpenTimerExpired.getFiredWhen();
	}

	/**
	 * Check if the hold timer is running
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	boolean isHoldTimerRunning() throws SchedulerException {
		return fireHoldTimerExpired.isJobScheduled();
	}
	
	/**
	 * get the date when the hold timer will fire.
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	Date getHoldTimerDueWhen() throws SchedulerException {
		return fireHoldTimerExpired.getFiredWhen();
	}

	/**
	 * check if the send keeplives timer is running 
	 * @return
	 * @throws SchedulerException
	 */
	public boolean isKeepaliveTimerRunning() throws SchedulerException {
		return fireKeepaliveTimerExpired.isJobScheduled();
	}
	
	/**
	 * get the date when the next keepalive packket is to be sent
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public Date getKeepaliveTimerDueWhen() throws SchedulerException {
		return fireKeepaliveTimerExpired.getFiredWhen();
	}
	
	/**
	 * Check if the automatic start event generator is running
	 * 
	 */
	boolean isAutomaticStartRunning() throws SchedulerException {
		return fireRepeatedAutomaticStart.isJobScheduled();
	}
	
	/**
	 * get the date the automatic start timer will fire the next time.
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	Date getAutomaticStartDueWhen() throws SchedulerException {
		return fireRepeatedAutomaticStart.getNextFireWhen();
	}
	
	/**
	 * @return the proposedHoldTimer
	 */
	int getPeerProposedHoldTime() {
		return peerProposedHoldTime;
	}

	/**
	 * @param proposedHoldTimer the proposedHoldTimer to set
	 */
	void setPeerProposedHoldTime(int proposedHoldTime) {
		this.peerProposedHoldTime= proposedHoldTime;
	}

	/**
	 * get the negotiated hold time. This is the minimum of the locally configured hold time and the
	 * hold time value received from the remote peer in the initial open packet. 
	 * It is assured that the negotiated hold time cannot be less than 3 seconds as specified by RFC4271. 
	 *  
	 * @return
	 */
	int getNegotiatedHoldTime() {
		int negotiatedHoldTime = Math.min(peerConfiguration.getHoldTime(), peerProposedHoldTime);
		
		if(negotiatedHoldTime < 3)
			negotiatedHoldTime = 3;
		
		return negotiatedHoldTime;
	}
	
	/**
	 * get the keepalive interval which is 1/3 of the negotiated hold time. It is assured that the interval
	 * cannot be less than 1 second as specified by RFC4271
	 * 
	 * @return
	 */
	private int getSendKeepaliveTime() {
		return Math.max(getNegotiatedHoldTime() / 3, 1);
	}

	/**
	 * Move from any other state to <code>Connect</code> state. It performs the following actions:
	 * <ol>
	 * <li>cancel the idle hold timer</li>
	 * <li>cancel the connect retry timer</li>
	 * <li>restart the connect retry timer with the configured value</li>
	 * <li>fire the connect to remote peer callback</li>
	 * <li>set the state to <code>Connect</code></li>
	 * </ol>
	 */
	private void moveStateToConnect() {
		try {
			fireHoldTimerExpired.cancelJob();
			fireIdleHoldTimerExpired.cancelJob();
			fireConnectRetryTimerExpired.cancelJob();
			
			fireConnectRetryTimerExpired.scheduleJob(peerConfiguration.getConnectRetryTime());
		} catch (SchedulerException e) {
			log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
			
			haveFSMError = true;
		}

		callbacks.fireConnectRemotePeer();
		lastConnectStamp = System.currentTimeMillis();
		
		this.state = FSMState.Connect;
		log.info("FSM for peer " + peerConfiguration.getPeerName() + " moved to " + this.state);
	}

	/**
	 * Move from any other state to <code>Active</code> state. It performs the following actions:
	 * <ol>
	 * <li>cancel the idle hold timer</li>
	 * <li>cancel the connect retry timer</li>
	 * <li>cancel the delay open timer</li>
	 * <li>cancal the hold timer</li>
	 * <li>restart the connect retry timer with the configured value</li>
	 * <li>fire the connect to remote peer callback</li>
	 * <li>set the state to <code>Active</code></li>
	 * </ol>
	 */
	private void moveStateToActive() {
		try {
			fireIdleHoldTimerExpired.cancelJob();
			fireConnectRetryTimerExpired.cancelJob();
			fireDelayOpenTimerExpired.cancelJob();
			fireHoldTimerExpired.cancelJob();
			
			fireConnectRetryTimerExpired.scheduleJob(peerConfiguration.getConnectRetryTime());
		} catch (SchedulerException e) {
			log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
			
			haveFSMError = true;
		}
		
		this.state = FSMState.Active;		
		log.info("FSM for peer " + peerConfiguration.getPeerName() + " moved to " + this.state);
	}

	/**
	 * Move from any other state to <code>Idle</code> state. It performs the following actions:
	 * <ol>
	 * <li>cancel the idle hold timer</li>
	 * <li>cancel the connect retry timer</li>
	 * <li>cancel the delay open timer</li>
	 * <li>cancel the hold timer</li>
	 * <li>cancel the send keepalive timer</li>
	 * <li>release all BGP resources</li>
	 * <li>disconnect the remote peer</li>
	 * <li>restart the connect retry timer with the configured value if peer dampening is disabled</li>
	 * <li>restart the idle hold timer with the configured value if peer dampening is enabled</li>
	 * <li>set the state to <code>Idle</code></li>
	 * </ol>
	 */
	private void moveStateToIdle() {
		try {
			fireIdleHoldTimerExpired.cancelJob();
			fireConnectRetryTimerExpired.cancelJob();
			fireDelayOpenTimerExpired.cancelJob();
			fireHoldTimerExpired.cancelJob();
			fireKeepaliveTimerExpired.cancelJob();
		} catch(SchedulerException e) {
			log.error("Interal Error: cannot cancel timers for peer " + peerConfiguration.getPeerName(), e);			
			
			haveFSMError = true;
		}
		
		callbacks.fireReleaseBGPResources();
		activeChannelManager.disconnect();
		connectedChannelManager.disconnect();
		
		if(peerConfiguration.isDampPeerOscillation()) {
			try {
				fireIdleHoldTimerExpired.scheduleJob(peerConfiguration.getIdleHoldTime() << connectRetryCounter);
			} catch (SchedulerException e) {
				log.error("Interal Error: cannot schedule idle hold timer for peer " + peerConfiguration.getPeerName(), e);
				
				haveFSMError = true;
			}
		} else {
			try {
				fireConnectRetryTimerExpired.scheduleJob(peerConfiguration.getConnectRetryTime());
			} catch (SchedulerException e) {
				log.error("Interal Error: cannot schedule idle hold timer for peer " + peerConfiguration.getPeerName(), e);
				
				haveFSMError = true;
			}
		}
			
		this.state = FSMState.Idle;
		log.info("FSM for peer " + peerConfiguration.getPeerName() + " moved to " + this.state);
	}

	/**
	 * Move from any other state to <code>OpenSent</code> state. It performs the following actions:
	 * <ol>
	 * <li>cancel the idle hold timer</li>
	 * <li>cancel the connect retry timer</li>
	 * <li>start the hold timer with 600 seconds</li>
	 * <li>fire the send <code>OPEN</code> message to remote peer callback</li>
	 * <li>set the state to <code>OpenSent</code></li>
	 * </ol>
	 */
	private void moveStateToOpenSent() {
		try {
			fireIdleHoldTimerExpired.cancelJob();
			fireConnectRetryTimerExpired.cancelJob();
			
			fireHoldTimerExpired.scheduleJob(600);
		} catch (SchedulerException e) {
			log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
			
			haveFSMError = true;
		}
		
		callbacks.fireCompleteBGPLocalInitialization();
		connectedChannelManager.fireSendOpenMessage();
		activeChannelManager.fireSendOpenMessage();
		
		this.state = FSMState.OpenSent;
		log.info("FSM for peer " + peerConfiguration.getPeerName() + " moved to " + this.state);
	}

	/**
	 * Move from any other state to <code>OpenSent</code> state. It performs the following actions:
	 * <ol>
	 * <li>cancel the idle hold timer</li>
	 * <li>cancel the connect retry timer</li>
	 * <li>start the hold timer with 600 seconds</li>
	 * <li>fire the send <code>OPEN</code> message to remote peer callback</li>
	 * <li>set the state to <code>OpenSent</code></li>
	 * </ol>
	 */
	private void moveStateToEstablished() {
		try {
			fireIdleHoldTimerExpired.cancelJob();
			fireConnectRetryTimerExpired.cancelJob();
			
			fireHoldTimerExpired.cancelJob();
			fireHoldTimerExpired.scheduleJob(getNegotiatedHoldTime());
		} catch (SchedulerException e) {
			log.error("Interal Error: cannot schedule connect retry timer for peer " + peerConfiguration.getPeerName(), e);
			
			haveFSMError = true;
		}

		if(!activeChannelManager.hasSeenOutbboundFSMEvent(FSMEventType.KeepAliveMsg))
			activeChannelManager.disconnect();
		if(!connectedChannelManager.hasSeenOutbboundFSMEvent(FSMEventType.KeepAliveMsg))
			connectedChannelManager.disconnect();
		
		this.state = FSMState.Established;		
		log.info("FSM for peer " + peerConfiguration.getPeerName() + " moved to " + this.state);
		
		callbacks.fireEstablished();
	}

	/**
	 * move the state to open confirm.
	 * <ul>
	 * <li>If called from the states <code>CONNECT</code> or <code>ACTIVE</code> then complete BGP initialization and send the peer an <code>OPEN</code> message</li>
	 * <li>If called from state <code>OPEN SENT</code> then do <b>not</b> complete BGP initialization and send the peer an <code>OPEN</code> message</li>
	 * </ul>
	 * @param sendOpenMessage
	 */
	private void moveStateToOpenConfirm(boolean sendOpenMessage) {
		if(sendOpenMessage) {
			callbacks.fireCompleteBGPLocalInitialization();
			activeChannelManager.fireSendOpenMessage();
			connectedChannelManager.fireSendOpenMessage();
		}

		callbacks.fireCompleteBGPPeerInitialization();
		
		activeChannelManager.fireSendKeepaliveMessage();
		connectedChannelManager.fireSendKeepaliveMessage();
		
		try {
			fireConnectRetryTimerExpired.cancelJob();
		}  catch(SchedulerException e) {
			log.error("cannot cancel connect retry timer", e);
			
			haveFSMError = true;
		}
		try {
			fireDelayOpenTimerExpired.cancelJob();
		} catch(SchedulerException e){
			log.error("cannot cancel open delay timer", e);
			
			haveFSMError = true;
		}

		if(!peerConfiguration.isHoldTimerDisabled()) {
			try {
				fireKeepaliveTimerExpired.scheduleJob(getSendKeepaliveTime());
			} catch(SchedulerException e) {
				log.error("cannot start send keepalive timer", e);
				
				haveFSMError = true;				
			}
			
			try {
				fireHoldTimerExpired.cancelJob();
				fireHoldTimerExpired.scheduleJob(getNegotiatedHoldTime());
			} catch(SchedulerException e) {
				
			}
		}
		this.state = FSMState.OpenConfirm;
		log.info("FSM for peer " + peerConfiguration.getPeerName() + " moved to " + this.state);
	}

	public void flagFSMError() {
		haveFSMError = true;
	}
}
