/**
 *  Copyright 2013, 2014 Nitin Bahadur (nitinb@gmail.com)
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
package onl.netfishers.blt.bgp.net.attributes.bgplsnlri;

import onl.netfishers.blt.bgp.net.SubsequentAddressFamily;

/**
 * This object uniquely identifies a node in a network
 * @author nitinb
 *
 */
public class BgpLsNodeNLRI extends BgpLsNLRIInformation {

	private BgpLsProtocolId protocolId;
	private long instanceIdentifier;
	private BgpLsNodeDescriptor localNodeDescriptor;
	
	/**
	 * @param safi BGP subsequent address family
	 */
	public BgpLsNodeNLRI(SubsequentAddressFamily safi) {
		super(safi, BgpLsNLRIType.NODE_NLRI);
	}
	
	/**
	 * Sets the protocol through which this node was learnt
	 * @param protocolId protocol id
	 */
	public void setProtocolId(BgpLsProtocolId protocolId) {
		this.protocolId = protocolId;
	}
	
	/**
	 * Gets the protocol through which this node was learnt
	 * @return
	 */
	public BgpLsProtocolId getProtocolId() {
		return protocolId;
	}
	
	/**
	 * Sets the node descriptors associated with this node
	 * @param localNodeDescriptor node descriptors
	 */
	public void setLocalNodeDescriptor(BgpLsNodeDescriptor localNodeDescriptor) {
		this.localNodeDescriptor = localNodeDescriptor;
	}
	
	/**
	 * Gets the node descriptors associated with this node
	 * @return node descriptors
	 */
	public BgpLsNodeDescriptor getLocalNodeDescriptor() {
		return localNodeDescriptor;
	}

	/**
	 * Gets the instance identifier of this node
	 * @return instance id
	 */
	public long getInstanceIdentifier() {
		return instanceIdentifier;
	}

	/**
	 * Sets the instance identifier of this node
	 * @param instanceId instance id
	 */
	public void setInstanceIdentifier(long instanceIdentifier) {
		this.instanceIdentifier = instanceIdentifier;
	}
}
