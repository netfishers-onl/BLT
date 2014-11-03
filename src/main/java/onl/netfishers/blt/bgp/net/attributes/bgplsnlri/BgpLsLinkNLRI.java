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
package onl.netfishers.blt.bgp.net.attributes.bgplsnlri;

import onl.netfishers.blt.bgp.net.SubsequentAddressFamily;

/**
 * Link NLRI object uniquely identifier the 2 end-points of a link and associated 
 * properties of the link
 * @author nitinb
 *
 */
public class BgpLsLinkNLRI extends BgpLsNLRIInformation {

	private BgpLsProtocolId protocolId;
	private long instanceIdentifier;
	private BgpLsNodeDescriptor localNodeDescriptors;
	private BgpLsNodeDescriptor remoteNodeDescriptors;
	private BgpLsLinkDescriptor linkDescriptors;
	
	/**
	 * @param safi Subsequent address family type
	 */
	public BgpLsLinkNLRI(SubsequentAddressFamily safi) {
		super(safi, BgpLsNLRIType.LINK_NLRI);
	}
	
	/**
	 * Checks if the object is well formed and all mandatory fields are set
	 * @return TRUE if the object is well formed
	 */
	public boolean isValidLinkNLRI() {
		// check for validity of node descriptors
		if (!localNodeDescriptors.isValidNodeDescriptor() ||
			!remoteNodeDescriptors.isValidNodeDescriptor()) {
			return false;
		}
		
		// check for a common router-id in local & remote descriptor array
		/*if (localNodeDescriptors == null || remoteNodeDescriptors == null
				|| localNodeDescriptors.lookupCommonRouterId(localNodeDescriptors, remoteNodeDescriptors) == BgpLsType.Unknown) {
			return false;
		}
		*/
		return true;
	}
	
	/**
	 * Sets the protocol which originated the link information
	 * @param protocolId protocol identifier
	 */
	public void setProtocolId(BgpLsProtocolId protocolId) {
		this.protocolId = protocolId;
	}
	
	/**
	 * Gets the protocol which originated the link information
	 * @return protocol id
	 */
	public BgpLsProtocolId getProtocolId() {
		return protocolId;
	}
	
	/**
	 * Sets the local node descriptors for this link
	 * @param localNodeDescriptors local node descriptors
	 */
	public void setLocalNodeDescriptors(BgpLsNodeDescriptor localNodeDescriptors) {
		this.localNodeDescriptors = localNodeDescriptors;
	}
	
	/**
	 * Gets the local node descriptors for this link
	 * @return node descriptors
	 */
	public BgpLsNodeDescriptor getLocalNodeDescriptors() {
		return localNodeDescriptors;
	}
	
	/**
	 * Sets the remote node descriptors for this link
	 * @param remoteNodeDescriptors remote node descriptors
	 */
	public void setRemoteNodeDescriptors(BgpLsNodeDescriptor remoteNodeDescriptors) {
		this.remoteNodeDescriptors = remoteNodeDescriptors;
	}
	
	/**
	 * Gets the remote node descriptors for this link
	 * @return node descriptors
	 */
	public BgpLsNodeDescriptor getRemoteNodeDescriptors() {
		return this.remoteNodeDescriptors;
	}
	
	/**
	 * Sets the link descriptors for this link
	 * @param linkDescriptors link descriptors
	 */
	public void setLinkDescriptors(BgpLsLinkDescriptor linkDescriptors) {
		this.linkDescriptors = linkDescriptors;
	}
	
	/**
	 * Gets the link descriptors for this link
	 * @return link descriptors
	 */
	public BgpLsLinkDescriptor getLinkDescriptors() {
		return this.linkDescriptors;
	}
	

	/**
	 * Gets the instance identifier associated with the link
	 * @return instance Identifier
	 */
	public long getInstanceIdentifier() {
		return instanceIdentifier;
	}

	/**
	 * Sets the instance identifier for this link
	 * @param instanceIdentifier instance identifier to set
	 */
	public void setInstanceIdentifier(long instanceIdentifier) {
		this.instanceIdentifier = instanceIdentifier;
	}
}
