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

/**
 * Identifier object that identifies a BGP link-state object in the network
 * @author nitinb
 *
 */
public class BgpLsIdentifier {
	
	//private static final BgpLsType type = BgpLsType.Identifier; draft v2
	private byte[] instanceId;
	private byte[] domainId;
	private byte[] areaIdentifier;
	private short ospfRouteType;
	private int multiTopologyId;
	
	BgpLsIdentifier() {}
	
	/* draft v2
	public BgpLsType getType() {
		return type;
	}*/
	
	/**
	 * Returns TRUE if this is a valid identifier
	 * @return
	 */
	public boolean isValidIdentifier() {
		if (instanceId == null) {
			return false;
		}	
		return true;
	}
	
	/**
	 * Sets the instance id
	 * @param instanceId instance id to set
	 */
	public void setInstanceIdentifier (byte[] instanceId) {
		this.instanceId = instanceId;
	}
	
	/**
	 * Gets the instance id
	 * @return instance id
	 */
	public byte[] getInstanceIdentifier() {
		return instanceId;
	}
	
	/**
	 * Sets the domain identifier
	 * @param domainId domain id to set
	 */
	public void setDomainIdentifier (byte[] domainId) {
		this.domainId = domainId;
	}
	
	/**
	 * Gets the domain id
	 * @return domain id
	 */
	public byte[] getDomainIdentifier () {
		return domainId;
	}
	
	/**
	 * Sets the area id
	 * @param areaIdentifier area id to set
	 */
	public void setAreaIdentifier (byte[] areaIdentifier) {
		this.areaIdentifier = areaIdentifier;
	}
	
	/**
	 * Gets the area id
	 * @return area id
	 */
	public byte[] getAreaIdentifier() {
		return areaIdentifier;
	}
	
	/**
	 * Sets the OSPF route type
	 * @param ospfRouteType ospf route type
	 */
	public void setOspfRouteType(short ospfRouteType) {
		this.ospfRouteType = ospfRouteType;
	}
	
	/**
	 * Gets the OSPF route type
	 * @return ospf route type
	 */
	public short getOspfRouteType() {
		return ospfRouteType;
	}
	
	/**
	 * Sets the multi-topology id
	 * @param multiTopologyId multi-topology id to set
	 */
	public void setMultiTopologyId(int multiTopologyId) {
		this.multiTopologyId = multiTopologyId;
	}
	
	/**
	 * Gets the multi-topology id
	 * @return multi-topology id
	 */
	public int getMultiTopologyId() {
		return multiTopologyId;
	}
}
