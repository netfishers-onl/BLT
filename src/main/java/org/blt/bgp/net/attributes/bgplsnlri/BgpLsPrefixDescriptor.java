package org.blt.bgp.net.attributes.bgplsnlri;

import java.util.LinkedList;
import java.util.List;

public class BgpLsPrefixDescriptor {

	public enum OspfRouteType {
		IntraArea,
		InterArea,
		External1,
		External2,
		NSSA1,
		NSSA2,
		Unknown;
		
		public int toCode() {
			switch (this) {
			case IntraArea:
				return 1;
			case InterArea:
				return 2;
			case External1:
				return 3;
			case External2:
				return 4;
			case NSSA1:
				return 5;
			case NSSA2:
				return 6;
			default:
				throw new IllegalArgumentException("unknown type: " + this);
			}
		}
		
		public static OspfRouteType fromCode(int code) {
			switch (code) {
			case 1:
				return IntraArea;
			case 2:
				return InterArea;
			case 3:
				return External1;
			case 4:
				return External2;
			case 5:
				return NSSA1;
			case 6:
				return NSSA2;
			default:
				return Unknown;
			}
		}
		
		public String toString() {
			switch (this) {
			case IntraArea:
				return "Intra-Area";
			case InterArea:
				return "Inter-Area";
			case External1:
				return "External 1";
			case External2:
				return "External 2";
			case NSSA1:
				return "NSSA 1";
			case NSSA2:
				return "NSSA 2";
			default:
				return "Unknown";
			}
		}
		
	}
	
	private int multiTopologyId = 0;
	private OspfRouteType ospfRouteType;
	private List<IPPrefix> prefixList = new LinkedList<IPPrefix>();
	
	private boolean validMultiTopologyId = false;
	
	/**
	 * Checks if this is a well formed object
	 * @return
	 */
	public boolean isValidPrefixNLRI() {
		// must contain at least 1 prefix
		if (prefixList.size() == 0) {
			return false;
		}
		
		// each prefix must be well formed
		for(IPPrefix prefix: prefixList) {
			if (!prefix.isValidPrefix()) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Sets the list of prefixes
	 * @param prefixList prefix list
	 */
	public void setPrefixList(List<IPPrefix> prefixList) {
		this.prefixList = prefixList;
	}
	
	/**
	 * Gets the list of prefixes
	 * @return prefix list
	 */
	public List<IPPrefix> getPrefixList() {
		return prefixList;
	}
	
	/**
	 * Adds a prefix to the prefix list
	 * @param prefix IP prefix
	 */
	public void addPrefix(IPPrefix prefix) {
		prefixList.add(prefix);
	}

	public int getMultiTopologyId() {
		return multiTopologyId;
	}

	public void setMultiTopologyId(int multiTopologyId) {
		this.multiTopologyId = multiTopologyId;
		this.validMultiTopologyId = true;
	}

	public OspfRouteType getOspfRouteType() {
		return ospfRouteType;
	}

	public void setOspfRouteType(OspfRouteType ospfRouteType) {
		this.ospfRouteType = ospfRouteType;
	}

	public boolean isValidMultiTopologyId() {
		return validMultiTopologyId;
	}

	public boolean isValidOspfRouteType() {
		return ospfRouteType != null;
	}
	
	
	
}
