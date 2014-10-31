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
 * File: org.bgp4.config.global.ApplicationConfiguration.java 
 */
/**
 * Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
 * 
 * License: same as above
 * 
 * Modified to run as an independent java application, one that does not
 * require webserver or app server
 */
package org.blt.bgp.config.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//import javax.enterprise.event.Event;


import org.blt.bgp.config.Configuration;
import org.blt.bgp.config.ModifiableConfiguration;
import org.blt.bgp.config.nodes.PeerConfiguration;
import org.blt.bgp.config.nodes.RoutingProcessorConfiguration;
import org.blt.bgp.config.nodes.ServerConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ApplicationConfiguration implements ModifiableConfiguration {

	private ServerConfiguration bgpServerConfiguration;
	private ServerConfiguration httpServerConfiguration;
	private RoutingProcessorConfiguration routingProcessorConfiguration;
	private Map<String, PeerConfiguration> peers = new HashMap<String, PeerConfiguration>();
	
	/* TODO: Support events
	private Event<ServerConfigurationEvent> bgpServerConfigurationEvent;
	private Event<ServerConfigurationEvent> httpServerConfigurationEvent;
	private Event<PeerConfigurationEvent> peerConfigurationEvent; */
    
    public ApplicationConfiguration () {
    }

	void resetConfiguration() {
		this.bgpServerConfiguration = null;
		this.httpServerConfiguration = null;
		this.routingProcessorConfiguration = null;
		this.peers = new HashMap<String, PeerConfiguration>();
	}
	
	public void importConfiguration(Configuration configuration) {
		setBgpServerConfiguration(configuration.getBgpServerConfiguration());
		setHttpServerConfiguration(configuration.getHttpServerConfiguration());
		setRoutingProcessorConfiguration(configuration.getRoutingProcessorConfiguration());
		
		for(PeerConfiguration peer : configuration.listPeerConfigurations())
			putPeer(peer);
	}
	
	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#getBgpServerConfiguration()
	 */
	@Override
	public ServerConfiguration getBgpServerConfiguration() {
		return bgpServerConfiguration;
	}
	
	@Override
	public void setBgpServerConfiguration(ServerConfiguration serverConfiguration) {
		EventType type = EventType.determineEvent(this.bgpServerConfiguration, serverConfiguration);
		
		this.bgpServerConfiguration = serverConfiguration;

		if(type != null) {
			//TODO: Fire an event
			//bgpServerConfigurationEvent.fire(new ServerConfigurationEvent(type, this.bgpServerConfiguration));
		}
	}

	public ServerConfiguration getHttpServerConfiguration() {
		return httpServerConfiguration;
	}

	public void setHttpServerConfiguration(ServerConfiguration httpServerConfiguration) {
		EventType type = EventType.determineEvent(this.httpServerConfiguration, httpServerConfiguration);

		this.httpServerConfiguration = httpServerConfiguration;
		
		if(type != null) {
			// TODO: Fire an event
			//httpServerConfigurationEvent.fire(new ServerConfigurationEvent(type, this.httpServerConfiguration));
		}
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#listPeerNames()
	 */
	@Override
	public Set<String> listPeerNames() {
		return Collections.unmodifiableSet(peers.keySet());
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#listPeerConfigurations()
	 */
	@Override
	public List<PeerConfiguration> listPeerConfigurations() {
		List<PeerConfiguration> entries = new ArrayList<PeerConfiguration>(peers.size());
		
		for(Entry<String, PeerConfiguration> entry : peers.entrySet())
			entries.add(entry.getValue());
		
		return Collections.unmodifiableList(entries);
	}

	/* (non-Javadoc)
	 * @see org.bgp4.config.Configuration#getPeer(java.lang.String)
	 */
	@Override
	public PeerConfiguration getPeer(String peerName) {
		return peers.get(peerName);
	}

	public void putPeer(PeerConfiguration peer) {
		PeerConfiguration former = getPeer(peer.getPeerName());
		EventType type = EventType.determineEvent(former, peer);
		
		peers.put(peer.getPeerName(), peer);
		
		if(type != null) {
			//TODO: Fire an event
			//peerConfigurationEvent.fire(new PeerConfigurationEvent(type, former, peer));
		}
	}
	
	public void removePeer(String peerName) {
		PeerConfiguration peer = peers.remove(peerName);
		
		if(peer != null) {
			//TODO: Fire an event
			//peerConfigurationEvent.fire(new PeerConfigurationEvent(EventType.CONFIGURATION_REMOVED, peer, null));
		}
	}

	@Override
	public RoutingProcessorConfiguration getRoutingProcessorConfiguration() {
		return routingProcessorConfiguration;
	}

	/**
	 * @param routingProcessorConfiguration the routingProcessorConfiguration to set
	 */
	public void setRoutingProcessorConfiguration(RoutingProcessorConfiguration routingProcessorConfiguration) {
		this.routingProcessorConfiguration = routingProcessorConfiguration;
	}
}
