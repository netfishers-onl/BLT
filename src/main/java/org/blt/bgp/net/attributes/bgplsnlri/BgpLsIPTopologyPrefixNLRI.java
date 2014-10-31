/**
 *  Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
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
package org.blt.bgp.net.attributes.bgplsnlri;

import org.blt.bgp.net.SubsequentAddressFamily;

/**
 * This object represents a list of IP prefixes associated with a local node
 * @author nitinb
 *
 */
public class BgpLsIPTopologyPrefixNLRI extends BgpLsNLRIInformation {
	
	private BgpLsProtocolId protocolId;
	private long instanceIdentifier;
	private BgpLsNodeDescriptor localNodeDescriptors;
	private BgpLsPrefixDescriptor prefixDescriptor;
	
	/**
	 * @param safi BGP subsequent address family
	 * @param nlriType BGP NLRI type
	 */
	public BgpLsIPTopologyPrefixNLRI(SubsequentAddressFamily safi, BgpLsNLRIType nlriType) {
		super(safi, nlriType);
	}
	

	
	/**
	 * Sets the protocol type through which the prefixes are learnt
	 * @param protocolId protocol id
	 */
	public void setProtocolId(BgpLsProtocolId protocolId) {
		this.protocolId = protocolId;
	}
	
	/**
	 * Gets the protocol type through which the prefixes are learnt
	 * @return protocol id
	 */
	public BgpLsProtocolId getProtocolId() {
		return protocolId;
	}
		
	/**
	 * Sets the local node descriptors associated with this prefix list
	 * @param localNodeDescriptors local node descriptors
	 */
	public void setLocalNodeDescriptors(BgpLsNodeDescriptor localNodeDescriptors) {
		this.localNodeDescriptors = localNodeDescriptors;
	}
	
	/**
	 * Gets the local node descriptors associated with this prefix list
	 * @return node descriptors
	 */
	public BgpLsNodeDescriptor getLocalNodeDescriptors() {
		return localNodeDescriptors;
	}

	/**
	 * Gets the instance identifier of this prefix list
	 * @return instance identifier
	 */
	public long getInstanceIdentifier() {
		return instanceIdentifier;
	}

	/**
	 * Sets the instance identifier of this prefix list
	 * @param instanceIdentifier instance identifier to set
	 */
	public void setInstanceIdentifier(long instanceIdentifier) {
		this.instanceIdentifier = instanceIdentifier;
	}



	public BgpLsPrefixDescriptor getPrefixDescriptor() {
		return prefixDescriptor;
	}



	public void setPrefixDescriptor(BgpLsPrefixDescriptor prefixDescriptor) {
		this.prefixDescriptor = prefixDescriptor;
	}
}
