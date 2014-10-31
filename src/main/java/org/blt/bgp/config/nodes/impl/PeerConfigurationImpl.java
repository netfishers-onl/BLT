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
 * File: org.bgp4.config.nodes.impl.PeerConfigurationImpl.java 
 */
/**
 * Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
 * 
 * License: same as above
 * 
 * Modified to run as an independent java application, one that does not
 * require webserver or app server
 */
package org.blt.bgp.config.nodes.impl;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.blt.bgp.config.nodes.Capabilities;
import org.blt.bgp.config.nodes.CapabilitiesList;
import org.blt.bgp.config.nodes.ClientConfiguration;
import org.blt.bgp.config.nodes.PeerConfiguration;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class PeerConfigurationImpl implements PeerConfiguration {

	private ClientConfiguration clientConfig;
	private int localAS;
	private int remoteAS;
	private String peerName;
	private long localBgpIdentifier;
	private long remoteBgpIdentifier; 
	private int holdTime;
	private boolean holdTimerDisabled;
	private int idleHoldTime;
	private boolean allowAutomaticStart;
	private boolean allowAutomaticStop;
	private boolean collisionDetectEstablishedState;
	private boolean dampPeerOscillation;
	private boolean delayOpen;
	private boolean passiveTcpEstablishment;
	private int delayOpenTime;
	private int connectRetryTime;
	private int automaticStartInterval;
	private Capabilities capabilities = new CapabilitiesList();
	
	public PeerConfigurationImpl() {
		
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig) throws ConfigurationException {
		setPeerName(peerName);
		setClientConfig(clientConfig);
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS) throws ConfigurationException {
		this(peerName, clientConfig);
		setLocalAS(localAS);
		setRemoteAS(remoteAS);
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS);
		
		setLocalBgpIdentifier(localBgpIdentifier);
		setRemoteBgpIdentifier(remoteBgpIdentifier);
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier, int connectRetryTime) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS, localBgpIdentifier, remoteBgpIdentifier);

		this.connectRetryTime = connectRetryTime;
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier, int connectRetryTime, int holdTime, boolean holdTimerDisabled, int idleHoldTime) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS, localBgpIdentifier, remoteBgpIdentifier, connectRetryTime);

		setHoldTime(holdTime);
		setHoldTimerDisabled(holdTimerDisabled);
		setIdleHoldTime(idleHoldTime);
	}
	
	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier, int connectRetryTime, int holdTime, boolean holdTimerDisabled, int idleHoldTime,
			boolean allowAutomaticStart, boolean allowAutomaticStop, int automaticStartInterval, boolean dampPeerOscillation) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS, localBgpIdentifier, remoteBgpIdentifier, connectRetryTime, holdTime, holdTimerDisabled, idleHoldTime);
		
		setAllowAutomaticStart(allowAutomaticStart);
		setAllowAutomaticStop(allowAutomaticStop);
		setAutomaticStartInterval(automaticStartInterval);
		setDampPeerOscillation(dampPeerOscillation);
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier, int connectRetryTime, int holdTime, boolean holdTimerDisabled, int idleHoldTime,
			boolean allowAutomaticStart, boolean allowAutomaticStop, int automaticStartInterval, boolean dampPeerOscillation,
			boolean passiveTcpEstablishment, boolean delayOpen, int delayOpenTime) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS, localBgpIdentifier, remoteBgpIdentifier, connectRetryTime, holdTime, holdTimerDisabled, idleHoldTime,
				allowAutomaticStart, allowAutomaticStop, automaticStartInterval, dampPeerOscillation);
		
		setPassiveTcpEstablishment(passiveTcpEstablishment);
		setDelayOpen(delayOpen);
		setDelayOpenTime(delayOpenTime);
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier, int connectRetryTime, int holdTime, boolean holdTimerDisabled, int idleHoldTime,
			boolean allowAutomaticStart, boolean allowAutomaticStop, int automaticStartInterval, boolean dampPeerOscillation,
			boolean passiveTcpEstablishment, boolean delayOpen, int delayOpenTime,
			boolean collisionDetectEstablishedState) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS, localBgpIdentifier, remoteBgpIdentifier, connectRetryTime, holdTime, holdTimerDisabled, idleHoldTime,
				allowAutomaticStart, allowAutomaticStop, automaticStartInterval ,dampPeerOscillation, passiveTcpEstablishment, delayOpen, delayOpenTime);
		
		setCollisionDetectEstablishedState(collisionDetectEstablishedState);
	}

	public PeerConfigurationImpl(String peerName, ClientConfiguration clientConfig, int localAS, int remoteAS, 
			long localBgpIdentifier, long remoteBgpIdentifier, int connectRetryTime, int holdTime, boolean holdTimerDisabled, int idleHoldTime,
			boolean allowAutomaticStart, boolean allowAutomaticStop, int automaticStartInterval, boolean dampPeerOscillation,
			boolean passiveTcpEstablishment, boolean delayOpen, int delayOpenTime,
			boolean collisionDetectEstablishedState, Capabilities capabilities) throws ConfigurationException {
		this(peerName, clientConfig, localAS, remoteAS, localBgpIdentifier, remoteBgpIdentifier, connectRetryTime, holdTime, holdTimerDisabled, idleHoldTime,
				allowAutomaticStart, allowAutomaticStop, automaticStartInterval ,dampPeerOscillation, passiveTcpEstablishment, delayOpen, delayOpenTime);
		
		setCollisionDetectEstablishedState(collisionDetectEstablishedState);

		if(capabilities != null)
			setCapabilities(capabilities);
	}

	@Override
	public ClientConfiguration getClientConfig() {
		return clientConfig;
	}

	@Override
	public int getLocalAS() {
		return localAS;
	}

	@Override
	public int getRemoteAS() {
		return remoteAS;
	}

	/**
	 * @param clientConfig the clientConfig to set
	 * @throws ConfigurationException 
	 */
	void setClientConfig(ClientConfiguration clientConfig) throws ConfigurationException {
		if(clientConfig == null)
			throw new ConfigurationException("null client configuration not allowed");
		
		if(!(clientConfig instanceof BgpClientConfigurationImpl))
			clientConfig = new BgpClientConfigurationImpl(clientConfig);
		
		this.clientConfig = clientConfig;
	}


	/**
	 * @param localAS the localAS to set
	 */
	void setLocalAS(int localAS) throws ConfigurationException {
		if(localAS <= 0)
			throw new ConfigurationException("negative AS number not allowed");
		
		this.localAS = localAS;
	}

	/**
	 * @param remoteAS the remoteAS to set
	 * @throws ConfigurationException 
	 */
	void setRemoteAS(int remoteAS) throws ConfigurationException {
		if(remoteAS <= 0)
			throw new ConfigurationException("negative AS number not allowed");

		this.remoteAS = remoteAS;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getPeerName() {
		return peerName;
	}

	/**
	 * @param name the name to set
	 * @throws ConfigurationException 
	 */
	void setPeerName(String name) throws ConfigurationException {
		if(StringUtils.isBlank(name))
			throw new ConfigurationException("blank name not allowed");
		
		this.peerName = name;
	}

	/**
	 * @return the localBgpIdentifier
	 */
	public long getLocalBgpIdentifier() {
		return localBgpIdentifier;
	}

	/**
	 * @param localBgpIdentifier the localBgpIdentifier to set
	 */
	void setLocalBgpIdentifier(long localBgpIdentifier) throws ConfigurationException  {
		if(localBgpIdentifier <= 0)
			throw new ConfigurationException("Illegal local BGP identifier: " + localBgpIdentifier);
		this.localBgpIdentifier = localBgpIdentifier;
	}

	/**
	 * @return the remoteBgpIdentifier
	 */
	public long getRemoteBgpIdentifier() {
		return remoteBgpIdentifier;
	}

	/**
	 * @param remoteBgpIdentifier the remoteBgpIdentifier to set
	 */
	void setRemoteBgpIdentifier(long remoteBgpIdentifier) throws ConfigurationException  {
		if(remoteBgpIdentifier <= 0)
			throw new ConfigurationException("Illegal remote BGP identifier: " + remoteBgpIdentifier);
		this.remoteBgpIdentifier = remoteBgpIdentifier;
	}

	/**
	 * @return the holdTime
	 */
	public int getHoldTime() {
		return holdTime;
	}

	/**
	 * @param holdTime the holdTime to set
	 */
	public void setHoldTime(int holdTime)  throws ConfigurationException {
		if(holdTime < 0)
			throw new ConfigurationException("Illegal hold time given: " + holdTime);
		
		this.holdTime = holdTime;
	}

	/**
	 * @return the connectRetryInterval
	 */
	public int getIdleHoldTime() {
		return idleHoldTime;
	}

	/**
	 * @param connectRetryInterval the connectRetryInterval to set
	 */
	public void setIdleHoldTime(int connectRetryInterval)  throws ConfigurationException {
		if(connectRetryInterval < 0)
			throw new ConfigurationException("Illegal connect retry interval given: " + connectRetryInterval);
		
		this.idleHoldTime = connectRetryInterval;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(allowAutomaticStart)
				.append(allowAutomaticStop)
				.append(automaticStartInterval)
				.append(capabilities)
				.append(clientConfig)
				.append(collisionDetectEstablishedState)
				.append(connectRetryTime)
				.append(dampPeerOscillation)
				.append(delayOpen)
				.append(delayOpenTime)
				.append(holdTime)
				.append(holdTimerDisabled)
				.append(idleHoldTime)
				.append(localAS)
				.append(localBgpIdentifier)
				.append(passiveTcpEstablishment)
				.append(peerName)
				.append(remoteAS)
				.append(remoteBgpIdentifier)
				.toHashCode();
				
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		/*
		if(getClass() != obj.getClass())
			return false;
		
		PeerConfigurationImpl o = (PeerConfigurationImpl)obj;
		
		return (new EqualsBuilder())
				.append(allowAutomaticStart, o.allowAutomaticStart)
				.append(allowAutomaticStop, o.allowAutomaticStop)
				.append(automaticStartInterval, o.automaticStartInterval)
				.append(capabilities, o.capabilities)
				.append(clientConfig, o.clientConfig)
				.append(collisionDetectEstablishedState, o.collisionDetectEstablishedState)
				.append(connectRetryTime, o.connectRetryTime)
				.append(dampPeerOscillation, o.dampPeerOscillation)
				.append(delayOpen, o.delayOpen)
				.append(delayOpenTime, o.delayOpenTime)
				.append(holdTime, o.holdTime)
				.append(holdTimerDisabled, o.holdTimerDisabled)
				.append(idleHoldTime, o.idleHoldTime)	
				.append(localAS, o.localAS)
				.append(localBgpIdentifier, o.localBgpIdentifier)
				.append(passiveTcpEstablishment, o.passiveTcpEstablishment)
				.append(peerName, o.peerName)
				.append(remoteAS, o.remoteAS)
				.append(remoteBgpIdentifier, o.remoteBgpIdentifier)
				.isEquals();
				*/
		if(!PeerConfiguration.class.isAssignableFrom(obj.getClass()))
			return false;
		
		PeerConfiguration o = (PeerConfiguration)obj;
		
		return (new EqualsBuilder())
				.append(allowAutomaticStart, o.isAllowAutomaticStart())
				.append(allowAutomaticStop, o.isAllowAutomaticStop())
				.append(automaticStartInterval, o.getAutomaticStartInterval())
				.append(capabilities, o.getCapabilities())
				.append(clientConfig, o.getClientConfig())
				.append(collisionDetectEstablishedState, o.isCollisionDetectEstablishedState())
				.append(connectRetryTime, o.getConnectRetryTime())
				.append(dampPeerOscillation, o.isDampPeerOscillation())
				.append(delayOpen, o.isDelayOpen())
				.append(delayOpenTime, o.getDelayOpenTime())
				.append(holdTime, o.getHoldTime())
				.append(holdTimerDisabled, o.isHoldTimerDisabled())
				.append(idleHoldTime, o.getIdleHoldTime())	
				.append(localAS, o.getLocalAS())
				.append(localBgpIdentifier, o.getLocalBgpIdentifier())
				.append(passiveTcpEstablishment, o.isPassiveTcpEstablishment())
				.append(peerName, o.getPeerName())
				.append(remoteAS, o.getRemoteAS())
				.append(remoteBgpIdentifier, o.getRemoteBgpIdentifier())
				.isEquals();
	}

	/**
	 * @return the allowAutomaticStart
	 */
	public boolean isAllowAutomaticStart() {
		return allowAutomaticStart;
	}

	/**
	 * @param allowAutomaticStart the allowAutomaticStart to set
	 */
	void setAllowAutomaticStart(boolean allowAutomaticStart) {
		this.allowAutomaticStart = allowAutomaticStart;
	}

	/**
	 * @return the allowAutomaticStop
	 */
	public boolean isAllowAutomaticStop() {
		return allowAutomaticStop;
	}

	/**
	 * @param allowAutomaticStop the allowAutomaticStop to set
	 */
	void setAllowAutomaticStop(boolean allowAutomaticStop) {
		this.allowAutomaticStop = allowAutomaticStop;
	}

	/**
	 * @return the collisionDetectEstablishedEnabledState
	 */
	public boolean isCollisionDetectEstablishedState() {
		return collisionDetectEstablishedState;
	}

	/**
	 * @param collisionDetectEstablishedEnabledState the collisionDetectEstablishedEnabledState to set
	 */
	void setCollisionDetectEstablishedState(
			boolean collisionDetectEstablishedEnabledState) {
		this.collisionDetectEstablishedState = collisionDetectEstablishedEnabledState;
	}

	/**
	 * @return the dampPeerOscillation
	 */
	public boolean isDampPeerOscillation() {
		return dampPeerOscillation;
	}

	/**
	 * @param dampPeerOscillation the dampPeerOscillation to set
	 */
	void setDampPeerOscillation(boolean dampPeerOscillation) {
		this.dampPeerOscillation = dampPeerOscillation;
	}

	/**
	 * @return the delayOpen
	 */
	public boolean isDelayOpen() {
		return delayOpen;
	}

	/**
	 * @param delayOpen the delayOpen to set
	 */
	void setDelayOpen(boolean delayOpen) {
		this.delayOpen = delayOpen;
	}

	/**
	 * @return the passiveTcpEstablishment
	 */
	public boolean isPassiveTcpEstablishment() {
		return passiveTcpEstablishment;
	}

	/**
	 * @param passiveTcpEstablishment the passiveTcpEstablishment to set
	 */
	void setPassiveTcpEstablishment(boolean passiveTcpEstablishment) {
		this.passiveTcpEstablishment = passiveTcpEstablishment;
	}

	/**
	 * @return the delayOpenTime
	 */
	public int getDelayOpenTime() {
		return delayOpenTime;
	}

	/**
	 * @param delayOpenTime the delayOpenTime to set
	 * @throws ConfigurationException 
	 */
	public void setDelayOpenTime(int delayOpenTime) throws ConfigurationException {
		if(delayOpenTime < 0)
			throw new ConfigurationException("Illegal delay open time given: " + delayOpenTime);

		this.delayOpenTime = delayOpenTime;
	}

	/**
	 * @return the connectRetryTime
	 */
	public int getConnectRetryTime() {
		return connectRetryTime;
	}

	/**
	 * @param connectRetryTime the connectRetryTime to set
	 */
	public void setConnectRetryTime(int connectRetryTime) throws ConfigurationException {
		if(connectRetryTime < 0)
			throw new ConfigurationException("Illegal connect retry time given: " + connectRetryTime);
		
		this.connectRetryTime = connectRetryTime;
	}

	/**
	 * @return the automaticStartInterval
	 */
	public int getAutomaticStartInterval() {
		return automaticStartInterval;
	}

	/**
	 * @param automaticStartInterval the automaticStartInterval to set
	 */
	public void setAutomaticStartInterval(int automaticStartInterval) throws ConfigurationException {
		if(automaticStartInterval < 0)
			throw new ConfigurationException("Illegal automatic start interval given: " + connectRetryTime);

		this.automaticStartInterval = automaticStartInterval;
	}

	/**
	 * @return the holdTimerDisabled
	 */
	public boolean isHoldTimerDisabled() {
		return holdTimerDisabled;
	}

	/**
	 * @param holdTimerDisabled the holdTimerDisabled to set
	 */
	void setHoldTimerDisabled(boolean holdTimerDisabled) {
		this.holdTimerDisabled = holdTimerDisabled;
	}

	/**
	 * @return the capabilities
	 */
	public Capabilities getCapabilities() {
		return capabilities;
	}

	/**
	 * @param capabilities the capabilities to set
	 */
	public void setCapabilities(Capabilities capabilities) {
		this.capabilities = capabilities;
	}

}
