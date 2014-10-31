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
 * Added support for capability negotiation handling
 */
package org.blt.bgp.netty.fsm;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.jboss.netty.channel.Channel;
import org.blt.bgp.BgpService;
import org.blt.bgp.config.nodes.PeerConfiguration;
import org.blt.bgp.net.ASType;
import org.blt.bgp.net.capabilities.Capability;
import org.blt.bgp.netty.BGPv4Constants;
import org.blt.bgp.netty.FSMState;
import org.blt.bgp.netty.PeerConnectionInformation;
import org.blt.bgp.netty.handlers.BgpEvent;
import org.blt.bgp.netty.handlers.NotificationEvent;
import org.blt.bgp.netty.protocol.BGPv4Packet;
import org.blt.bgp.netty.protocol.FiniteStateMachineErrorNotificationPacket;
import org.blt.bgp.netty.protocol.HoldTimerExpiredNotificationPacket;
import org.blt.bgp.netty.protocol.KeepalivePacket;
import org.blt.bgp.netty.protocol.NotificationPacket;
import org.blt.bgp.netty.protocol.UnspecifiedCeaseNotificationPacket;
import org.blt.bgp.netty.protocol.open.CapabilityListUnsupportedCapabilityNotificationPacket;
import org.blt.bgp.netty.protocol.open.OpenNotificationPacket;
import org.blt.bgp.netty.protocol.open.OpenPacket;
import org.blt.bgp.netty.protocol.open.UnsupportedVersionNumberNotificationPacket;
import org.blt.bgp.netty.protocol.update.UpdateNotificationPacket;
import org.blt.bgp.netty.protocol.update.UpdatePacket;
import org.blt.bgp.netty.service.BGPv4Client;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class BGPv4FSM {

	private class FSMChannelImpl implements FSMChannel {
		private Channel channel;

		public FSMChannelImpl(Channel channel) {
			this.channel = channel;
		}
		
		/**
		 * @return the channel
		 */
		private Channel getChannel() {
			return channel;
		}

	}
	
	/**
	 * Internal proxy class to expose the peer connection information to interested handlers
	 * 
	 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
	 *
	 */
	private class PeerConnectionInformationImpl implements PeerConnectionInformation {
		
		public ASType getAsTypeInUse() {
			return asTypeInUse;
		}

		/**
		 * 
		 * @return
		 */
		public int getLocalAS() {
			return peerConfig.getLocalAS();
		}
		
		/**
		 * 
		 * @return
		 */
		public int getRemoteAS() {
			return peerConfig.getRemoteAS();
		}
		
		/**
		 * Test if the connection describes an IBGP connection (peers in the same AS)
		 * 
		 * @return <code>true</code> if IBGP connection, <code>false</code> otherwise
		 */
		public boolean isIBGPConnection() {
			return (getRemoteAS() == getLocalAS());
		}

		/**
		 * Test if the connection describes an EBGP connection (peers in the same AS)
		 * 
		 * @return <code>true</code> if EBGP connection, <code>false</code> otherwise
		 */
		public boolean isEBGPConnection() {
			return (getRemoteAS() != getLocalAS());
		}
		
		/**
		 * Test if this connection uses 4 octet AS numbers
		 * 
		 * @return
		 */
		public boolean isAS4OctetsInUse() {
			return (asTypeInUse == ASType.AS_NUMBER_4OCTETS);
		}

		/**
		 * @return the localBgpIdentifier
		 */
		public long getLocalBgpIdentifier() {
			return peerConfig.getLocalBgpIdentifier();
		}

		/**
		 * @return the remoteBgpIdentifier
		 */
		public long getRemoteBgpIdentifier() {
			return peerConfig.getRemoteBgpIdentifier();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("PeerConnectionInformation [localAS=").append(getLocalAS())
					.append(", remoteAS=").append(getRemoteAS())
					.append(", localBgpIdentifier=").append(getLocalBgpIdentifier())
					.append(", remoteBgpIdentifier=").append(getRemoteBgpIdentifier())
					.append(", ");
			if (getAsTypeInUse() != null)
				builder.append("asTypeInUse=").append(getAsTypeInUse());
			builder.append("]");
			return builder.toString();
		}
	}
	
	/**
	 * Internal class to bind callbacks from the internal state machine to concrete actions 
	 * 
	 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
	 *
	 */
	private class InternalFSMCallbacksImpl implements InternalFSMCallbacks {

		@Override
		public void fireConnectRemotePeer() {
			BGPv4Client client = new BGPv4Client();
			managedChannels.add(new FSMChannelImpl(client.startClient(peerConfig).getChannel()));
		}

		@Override
		public void fireDisconnectRemotePeer(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				((FSMChannelImpl)channel).getChannel().close();
				managedChannels.remove(channel);
			}			
			
		}

		@Override
		public void fireSendOpenMessage(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				OpenPacket packet = new OpenPacket();
				
				packet.setAutonomousSystem(peerConfig.getLocalAS());
				packet.setBgpIdentifier(peerConfig.getLocalBgpIdentifier());
				packet.setHoldTime(peerConfig.getHoldTime());
				packet.setProtocolVersion(BGPv4Constants.BGP_VERSION);
				
				capabilitiesNegotiator.insertLocalCapabilities(packet);
				
				((FSMChannelImpl)channel).getChannel().write(packet);
			}			
		}

		@Override
		public void fireSendInternalErrorNotification(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				((FSMChannelImpl)channel).getChannel().write(new FiniteStateMachineErrorNotificationPacket());
			}			
		}

		@Override
		public void fireSendCeaseNotification(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				((FSMChannelImpl)channel).getChannel().write(new UnspecifiedCeaseNotificationPacket());
			}			
		}

		@Override
		public void fireSendKeepaliveMessage(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				((FSMChannelImpl)channel).getChannel().write(new KeepalivePacket());
			}			
		}

		@Override
		public void fireReleaseBGPResources() {
		}

		@Override
		public void fireCompleteBGPLocalInitialization() {			
		}

		@Override
		public void fireCompleteBGPPeerInitialization() {
		}

		@Override
		public void fireSendHoldTimerExpiredNotification(FSMChannel channel) {
			if(managedChannels.contains(channel)) {
				((FSMChannelImpl)channel).getChannel().write(new HoldTimerExpiredNotificationPacket());
			}			
		}

		@Override
		public void fireSendUpdateErrorNotification(FSMChannel channel) {
		}

		@Override
		public void fireEstablished() {
		}


	}
	
	private static final Logger log = LoggerFactory.getLogger(BGPv4FSM.class);

	private PeerConfiguration peerConfig;
	private ASType asTypeInUse = ASType.AS_NUMBER_2OCTETS;

	private InternalFSM internalFsm;
	private CapabilitesNegotiator capabilitiesNegotiator;
	
	
	private Set<FSMChannelImpl> managedChannels = new HashSet<FSMChannelImpl>();
	
	/**
	 * 
	 */
	public BGPv4FSM() {
        internalFsm = new InternalFSM();
        capabilitiesNegotiator = new CapabilitesNegotiator();
	}

	public void configure(PeerConfiguration peerConfig) throws SchedulerException {
		this.peerConfig = peerConfig;
		
		internalFsm.setup(peerConfig, new InternalFSMCallbacksImpl());
		capabilitiesNegotiator.setup(peerConfig);
	}

	public InetSocketAddress getRemotePeerAddress() {
		return peerConfig.getClientConfig().getRemoteAddress();
	}

	public PeerConnectionInformation getPeerConnectionInformation() {
		return new PeerConnectionInformationImpl();
	}
	
	public void startFSMAutomatic() {
		internalFsm.handleEvent(FSMEvent.automaticStart());
	}

	public void startFSMManual() {
		internalFsm.handleEvent(FSMEvent.manualStart());
	}

	public void stopFSM() {
		internalFsm.handleEvent(FSMEvent.automaticStop());
	}
	
	public void destroyFSM() {
		internalFsm.destroyFSM();
	}

	public void handleMessage(Channel channel, BGPv4Packet message) {
		log.info("received message " + message);

		if(message instanceof OpenPacket) {
			internalFsm.setPeerProposedHoldTime(((OpenPacket) message).getHoldTime());
			
			capabilitiesNegotiator.recordPeerCapabilities((OpenPacket)message);
			
			if(capabilitiesNegotiator.missingRequiredCapabilities().size() > 0) {
				for(Capability cap : capabilitiesNegotiator.missingRequiredCapabilities())
					log.error("Missing required capability: " + cap);
				
				fireCapabilityNegotiationErrorNotification(channel);
				internalFsm.handleEvent(FSMEvent.bgpOpenMessageError());
			} else
				internalFsm.handleEvent(FSMEvent.bgpOpen(findWrapperForChannel(channel)));
		} else if(message instanceof KeepalivePacket) {
			internalFsm.handleEvent(FSMEvent.keepAliveMessage());
		} else if(message instanceof UpdatePacket) {
			internalFsm.handleEvent(FSMEvent.updateMessage());
			
			try {
				processRemoteUpdate((UpdatePacket)message);
			} catch(Exception e) {
				log.error("error processing UPDATE packet from peer: " + peerConfig.getPeerName());

				internalFsm.handleEvent(FSMEvent.updateMessageError());
			}
		} else if(message instanceof UnsupportedVersionNumberNotificationPacket) {
			internalFsm.handleEvent(FSMEvent.notifyMessageVersionError());
		} else if(message instanceof OpenNotificationPacket) {
			internalFsm.handleEvent(FSMEvent.bgpOpenMessageError());
		} else if(message instanceof UpdateNotificationPacket) {
			internalFsm.handleEvent(FSMEvent.updateMessageError());
		} else if(message instanceof NotificationPacket) {
			internalFsm.handleEvent(FSMEvent.notifyMessage());
		}
	}

	public void handleEvent(Channel channel, BgpEvent message) {
		log.info("received event " + message);

		if(message instanceof NotificationEvent) {
			for(NotificationPacket packet :((NotificationEvent)message).getNotifications()) {
				if(packet instanceof UnsupportedVersionNumberNotificationPacket) {
					internalFsm.handleEvent(FSMEvent.notifyMessageVersionError());
				} else if(packet instanceof OpenNotificationPacket) {
					internalFsm.handleEvent(FSMEvent.bgpOpenMessageError());
				} else if(packet instanceof UpdateNotificationPacket) {
					internalFsm.handleEvent(FSMEvent.updateMessageError());
				} else
					internalFsm.handleEvent(FSMEvent.notifyMessage());
			}
		}
	}

	public void handleClientConnected(Channel channel) {
		FSMChannelImpl wrapper = findWrapperForChannel(channel);
		
		if(wrapper != null)
			internalFsm.handleEvent(FSMEvent.tcpConnectionRequestAcked(wrapper));
	}
	
	public void handleServerOpened(Channel channel) {
		FSMChannelImpl wrapper= new FSMChannelImpl(channel);
		
		managedChannels.add(wrapper);
		internalFsm.handleEvent(FSMEvent.tcpConnectionConfirmed(wrapper));
	}

	public void handleClosed(Channel channel) {
		FSMChannel wrapper = findWrapperForChannel(channel);
		
		if(wrapper != null)
			internalFsm.handleEvent(FSMEvent.tcpConnectionFails(wrapper));
	}

	public void handleDisconnected(Channel channel) {
		// (Sylv) Trying to free up the channels
		FSMChannel wrapper = findWrapperForChannel(channel);
		
		if (wrapper != null) {
			managedChannels.remove(wrapper);
		}
	}

	public boolean isCanAcceptConnection() {
		return internalFsm.isCanAcceptConnection();
	}
	
	public FSMState getState() {
		return internalFsm.getState();
	}
		
	private FSMChannelImpl findWrapperForChannel(Channel channel) {
		FSMChannelImpl wrapper = null;
		
		for(FSMChannelImpl impl : managedChannels) {
			if(impl.getChannel().equals(channel)) {
				wrapper = impl;
				break;
			}
		}
		
		return wrapper;
	}
	
	/**
	 * process the UPDATE packet received from the remote peer
	 * 
	 * @param message
	 */
	private void processRemoteUpdate(UpdatePacket message) {
		BgpService.processUpdate(this, message);
	}

	/**
	 * Sends off a Open Notification error message to peer indicating that
	 * one or more required capabilities were unsupported by the peer
	 * @param channel
	 */
	private void fireCapabilityNegotiationErrorNotification(Channel channel) {
		LinkedList<Capability>caps = capabilitiesNegotiator.missingRequiredCapabilities();
		if(caps.size() > 0) {
			CapabilityListUnsupportedCapabilityNotificationPacket notifyPacket = new CapabilityListUnsupportedCapabilityNotificationPacket(caps);
			channel.write(notifyPacket);
		}
	}
}
