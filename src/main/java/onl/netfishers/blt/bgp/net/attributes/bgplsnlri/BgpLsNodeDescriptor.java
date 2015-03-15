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

/**
 * This object describes the attributes of node. 
 * These attributes are used to identify a node and used to identify 
 * the end-points of a link. 
 * @author nitinb
 *
 */
public class BgpLsNodeDescriptor {
	private BgpLsType type;
	private long autonomousSystem = 0;
	private long bgpLsIdentifier = 0;
	private long areaId = 0;
	private byte[] igpRouterId;
	//private byte[] pseudoNodeId;
	
	public static final int IGPROUTERID_ISISISONODEID_LENGTH = 6;
	public static final int IGPROUTERID_ISISPSEUDONODE_LENGTH = 7;
	public static final int IGPROUTERID_OSPFROUTERID_LENGTH = 4;
	public static final int IGPROUTERID_OSPFPSEUDONODE_LENGTH = 8;
	
	private boolean validAutonomousSystem = false;
	private boolean validBgpLsIdentifier = false;
	private boolean validAreaId = false;
	
	/**
	 * Sets the type (local or remote) of node descriptor
	 * @param type node descriptor type
	 */
	public BgpLsNodeDescriptor(BgpLsType type) {
		this.type = type;
	}
	
	/**
	 * Gets the node descriptor type (local or remote)
	 * @return node descriptor type
	 */
	public BgpLsType getType() {
		return type;
	}
	
	/**
	 * Checks if the node descriptor is a well formed object. In other words, does
	 * the node descriptor contain it's mandatory attributes.
	 * @return
	 */
	public boolean isValidNodeDescriptor() {
		if (igpRouterId == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Sets the autonomous system of this node descriptor
	 * @param autonomousSystem autonomous system
	 */
	public void setAutonomousSystem(long autonomousSystem) {
		this.autonomousSystem = autonomousSystem;
		validAutonomousSystem = true;
	}
	
	/**
	 * Gets the autonomous system of this node descriptor
	 * @return autonomous system
	 */
	public long getAutonomousSystem() {
		return autonomousSystem;
	}
	
	/**
	 * Returns TRUE if the autonomous system is valid
	 * @return
	 */
	public boolean validAutonomousSystem() {
		return validAutonomousSystem;
	}

	public long getBgpLsIdentifier() {
		return bgpLsIdentifier;
	}

	public void setBgpLsIdentifier(long bgpLsIdentifier) {
		this.bgpLsIdentifier = bgpLsIdentifier;
		this.validBgpLsIdentifier = true;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
		this.validAreaId = true;
	}

	public byte[] getIgpRouterId() {
		return igpRouterId;
	}

	public void setIgpRouterId(byte[] igpRouterId) {
		this.igpRouterId = igpRouterId;
	}
	
/*	public byte[] getPseudoNodeId() {
		return pseudoNodeId;
	}

	public void setPseudoNodeId(byte[] pseudoNodeId) {
		this.pseudoNodeId = pseudoNodeId;
	}

	*//**
	 * Checks if the node is a Pseudo Node
	 * 
	 * @return
	 *//*
	public boolean isPseudoNode() {
		if (pseudoNodeId == null) {
			return false;
		}
		return true;
	}
	
	public void setPseudoNode(boolean isPseudoNode) {
	}*/

	public boolean isValidAutonomousSystem() {
		return validAutonomousSystem;
	}

	public boolean isValidBgpLsIdentifier() {
		return validBgpLsIdentifier;
	}

	public boolean isValidAreaId() {
		return validAreaId;
	}
	
}
