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
 * File: onl.netfishers.blt.bgp.netty.fsm.CapabilitesNegotiator.java 
 */
package onl.netfishers.blt.bgp.netty.fsm;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import onl.netfishers.blt.bgp.config.nodes.PeerConfiguration;
import onl.netfishers.blt.bgp.net.capabilities.AutonomousSystem4Capability;
import onl.netfishers.blt.bgp.net.capabilities.Capability;
import onl.netfishers.blt.bgp.netty.BGPv4Constants;
import onl.netfishers.blt.bgp.netty.protocol.open.OpenPacket;

/**
 * This class negotiates the supported capabilities with a peer. According to RFC 5492 an intersection
 * of the locally configured capabilities and the capabilities announced by the remote peer can be build
 * which forms the capability "working set" per connection.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CapabilitesNegotiator {

	private PeerConfiguration peerConfiguration;
	private List<Capability> remoteCapabilities = new LinkedList<Capability>();
	
	void setup(PeerConfiguration peerCofiguration) {
		this.peerConfiguration = peerCofiguration;
	}
	
	/**
	 * Insert the currently negotiated capabilities into the OPEN packet.
	 * 
	 * If the capabilities contain an AS number larger than 65536 then the 
	 * AS number in the OPEN packet is changed to AS_TRANS (23456)
	 * 
	 * @param packet
	 * @see BGPv4Constants#BGP_AS_TRANS
	 */
	void insertLocalCapabilities(OpenPacket packet) {
		packet.getCapabilities().clear();
		
		insertLocalCapabilities(packet, peerConfiguration.getCapabilities().getRequiredCapabilities());
		insertLocalCapabilities(packet, peerConfiguration.getCapabilities().getOptionalCapabilities());
	}

	/**
	 * record the capabilites passed in from the peer
	 * 
	 * @param packet
	 */
	void recordPeerCapabilities(OpenPacket packet) {
		remoteCapabilities.clear();
		
		if(packet.getCapabilities() != null) {
			remoteCapabilities.addAll(packet.getCapabilities());
		}
	}

	/**
	 * build an intersection of the locally configured capabilities and the capabilites passed in from the peer.
	 * 
	 * @return
	 */
	public Set<Capability> intersectLocalAndRemoteCapabilities() {
		Set<Capability> caps = new TreeSet<Capability>();
		
		for(Capability cap : peerConfiguration.getCapabilities().getRequiredCapabilities())
			if(remoteCapabilities.contains(cap))
				caps.add(cap);
		for(Capability cap : peerConfiguration.getCapabilities().getOptionalCapabilities())
			if(remoteCapabilities.contains(cap))
				caps.add(cap);
		
		return caps;
	}

	/**
	 * build an intersection of the locally configured capabilities and the capabilites passed in from the peer.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Capability> Set<T> intersectLocalAndRemoteCapabilities(Class<T> capClass) {
		Set<T> caps = new TreeSet<T>();
		
		for(Capability cap : listLocalCapabilities(capClass))
			if(remoteCapabilities.contains(cap))
				caps.add((T)cap);
		
		return caps;
	}

	/**
	 * build a set of capabilities which are marked as required in the local configuration and which
	 * are not supported by the peer.
	 * The local speaker may build an Unsupported Capabilities notification message based on that info
	 * 
	 * @return
	 */
	public LinkedList<Capability> missingRequiredCapabilities() {
		LinkedList<Capability> caps = new LinkedList<Capability>();
		
		for(Capability cap : peerConfiguration.getCapabilities().getRequiredCapabilities())
			if(!remoteCapabilities.contains(cap))
				caps.add(cap);

		return caps;
	}
	
	/**
	 * List all locally configured capabilities of a given type
	 * 
	 * @param capClass
	 * @return
	 */
	public <T extends Capability> Set<T> listLocalCapabilities(Class<T> capClass) {
		Set<T> caps = new HashSet<T>();

		caps.addAll(listCapabilities(peerConfiguration.getCapabilities().getRequiredCapabilities(), capClass));
		caps.addAll(listCapabilities(peerConfiguration.getCapabilities().getOptionalCapabilities(), capClass));
		
		return caps;
	}

	/**
	 * list all capabilities sent by the remote peer of a given type
	 * @param capClass
	 * @return
	 */
	public <T extends Capability> Set<T> listRemoteCapabilities(Class<T> capClass) {
		Set<T> caps = new HashSet<T>();

		caps.addAll(listCapabilities(remoteCapabilities, capClass));
		
		return caps;
	}
	
	/**
	 * Insert the currently negotiated capabilities into the OPEN packet.
	 * 
	 * If the capabilities contain an AS number larger than 65536 then the 
	 * AS number in the OPEN packet is changed to AS_TRANS (23456)
	 * 
	 * @param packet
	 * @param capabilities
	 * @see BGPv4Constants#BGP_AS_TRANS
	 */
	private void insertLocalCapabilities(OpenPacket packet, Collection<Capability> capabilities) {
		for(Capability cap : capabilities) {
			if(cap instanceof AutonomousSystem4Capability) {
				AutonomousSystem4Capability as4cap = (AutonomousSystem4Capability)cap;
				
				if(as4cap.getAutonomousSystem() > 65536) {
					packet.setAutonomousSystem(BGPv4Constants.BGP_AS_TRANS);
				}
			}
			
			packet.getCapabilities().add(cap);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Capability> Set<T> listCapabilities(Collection<Capability> src, Class<T> capClass) {
		Set<T> caps = new HashSet<T>();

		for(Capability cap : src) {
			if(cap.getClass().equals(capClass))
				caps.add((T)cap);
		}
		
		return caps;
	}
}
