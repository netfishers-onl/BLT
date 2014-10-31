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
package org.blt.bgp.net.attributes;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author nitinb
 * 
 */
public class LinkStateAttribute extends PathAttribute {

	public LinkStateAttribute() {
		super(Category.OPTIONAL_NON_TRANSITIVE);
		setSharedRiskLinkGroups(new LinkedList<Long>());
	}

	private int[] multiTopologyId;
	private short nodeFlagBits;
	private byte[] opaqueNodeProperties;
	private String nodeName;
	private List<byte[]> isisAreaIdentifiers = new LinkedList<byte[]>();
	private List<byte[]> localNodeIPv4RouterIDs = new LinkedList<byte[]>();
	private List<byte[]> localNodeIPv6RouterIDs = new LinkedList<byte[]>();

	private List<byte[]> remoteNodeIPv4RouterIDs = new LinkedList<byte[]>();
	private List<byte[]> remoteNodeIPv6RouterIDs = new LinkedList<byte[]>();
	private long adminGroup;
	private float maxLinkBandwidth;
	private float maxReservableLinkBandwidth;
	private float[] unreservedBandwidth;
	private int teDefaultMetric;
	private short linkProtectionType;
	private short mplsProtocolMask;
	private int metric;
	private List<Long> sharedRiskLinkGroups = new LinkedList<Long>();
	private byte[] opaqueLinkAttribute;
	private String linkNameAttribute;
	
	
	private short igpFlags;
	private long routeTag;
	private byte[] extendedTag;
	private long prefixMetric;
	private byte[] ospfForwardingAddress;
	private byte[] opaquePrefixAttribute;

	private boolean validNodeFlagBits = false;
	private boolean validAdminGroup = false;
	private boolean validMaxLinkBandwidth = false;
	private boolean validMaxReservableLinkBandwidth = false;
	private boolean validTeDefaultMetric = false;
	private boolean validLinkProtectionType = false;
	private boolean validMplsProtocolMask = false;
	private boolean validMetric = false;
	private boolean validIgpFlags = false;
	private boolean validRouteTag = false;
	private boolean validPrefixMetric = false;

	private boolean validISISAreaIdentifiers = false;
	private boolean validLocalNodeIPv4RouterIDs = false;
	private boolean validLocalNodeIPv6RouterIDs = false;
	private boolean validRemoteNodeIPv4RouterIDs = false;
	private boolean validRemoteNodeIPv6RouterIDs = false;
	private boolean validSharedRiskLinkGroups = false;

	public int[] getMultiTopologyId() {
		return multiTopologyId;
	}

	public void setMultiTopologyId(int[] multiTopologyId) {
		this.multiTopologyId = multiTopologyId;
	}

	public short getNodeFlagBits() {
		return nodeFlagBits;
	}

	public void setNodeFlagBits(short nodeFlagBits) {
		this.nodeFlagBits = nodeFlagBits;
		this.validNodeFlagBits = true;
	}

	public byte[] getOpaqueNodeProperties() {
		return opaqueNodeProperties;
	}

	public void setOpaqueNodeProperties(byte[] opaqueNodeProperties) {
		this.opaqueNodeProperties = opaqueNodeProperties;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public List<byte[]> getIsisAreaIdentifiers() {
		return isisAreaIdentifiers;
	}

	public void setIsisAreaIdentifiers(List<byte[]> isisAreaIdentifiers) {
		this.isisAreaIdentifiers = isisAreaIdentifiers;
		this.validISISAreaIdentifiers = true;
	}



	public List<byte[]> getLocalNodeIPv4RouterIDs() {
		return localNodeIPv4RouterIDs;
	}

	public void setLocalNodeIPv4RouterIDs(List<byte[]> localNodeIPv4RouterIDs) {
		this.localNodeIPv4RouterIDs = localNodeIPv4RouterIDs;
		this.validLocalNodeIPv4RouterIDs = true;
	}
	
	public void addLocalNodeIPv4RouterID(byte[] localNodeIPv4RouterID) {
		this.localNodeIPv4RouterIDs.add(localNodeIPv4RouterID);
		this.validLocalNodeIPv4RouterIDs = true;
	}

	public List<byte[]> getLocalNodeIPv6RouterIDs() {
		return localNodeIPv6RouterIDs;
	}

	public void setLocalNodeIPv6RouterIDs(List<byte[]> localNodeIPv6RouterIDs) {
		this.localNodeIPv6RouterIDs = localNodeIPv6RouterIDs;
		this.validLocalNodeIPv6RouterIDs = true;
	}
	
	public void addLocalNodeIPv6RouterID(byte[] localNodeIPv6RouterID) {
		this.localNodeIPv6RouterIDs.add(localNodeIPv6RouterID);
		this.validLocalNodeIPv6RouterIDs = true;
	}

	public List<byte[]> getRemoteNodeIPv4RouterIDs() {
		return remoteNodeIPv4RouterIDs;
	}

	public void setRemoteNodeIPv4RouterIDs(List<byte[]> remoteNodeIPv4RouterIDs) {
		this.remoteNodeIPv4RouterIDs = remoteNodeIPv4RouterIDs;
		this.validRemoteNodeIPv4RouterIDs = true;
	}
	
	public void addRemoteNodeIPv4RouterID(byte[] remoteNodeIPv4RouterID) {
		this.remoteNodeIPv4RouterIDs.add(remoteNodeIPv4RouterID);
		this.validRemoteNodeIPv4RouterIDs = true;
	}

	public List<byte[]> getRemoteNodeIPv6RouterIDs() {
		return remoteNodeIPv6RouterIDs;
	}

	public void setRemoteNodeIPv6RouterIDs(List<byte[]> remoteNodeIPv6RouterIDs) {
		this.remoteNodeIPv6RouterIDs = remoteNodeIPv6RouterIDs;
		this.validRemoteNodeIPv6RouterIDs = true;
	}
	
	public void addRemoteNodeIPv6RouterID(byte[] remoteNodeIPv6RouterID) {
		this.remoteNodeIPv6RouterIDs.add(remoteNodeIPv6RouterID);
		this.validRemoteNodeIPv6RouterIDs = true;
	}

	public long getAdminGroup() {
		return adminGroup;
	}

	public void setAdminGroup(long adminGroup) {
		this.adminGroup = adminGroup;
		this.validAdminGroup = true;
	}

	public float getMaxLinkBandwidth() {
		return maxLinkBandwidth;
	}

	public void setMaxLinkBandwidth(float maxLinkBandwidth) {
		this.maxLinkBandwidth = maxLinkBandwidth;
		this.validMaxLinkBandwidth = true;
	}

	public float getMaxReservableLinkBandwidth() {
		return maxReservableLinkBandwidth;
	}

	public void setMaxReservableLinkBandwidth(float maxReservableLinkBandwidth) {
		this.maxReservableLinkBandwidth = maxReservableLinkBandwidth;
		this.validMaxReservableLinkBandwidth = true;
	}

	public float[] getUnreservedBandwidth() {
		return unreservedBandwidth;
	}

	public void setUnreservedBandwidth(float[] unreservedBandwidth) {
		this.unreservedBandwidth = unreservedBandwidth;
	}

	public int getTeDefaultMetric() {
		return teDefaultMetric;
	}

	public void setTeDefaultMetric(int teDefaultMetric) {
		this.teDefaultMetric = teDefaultMetric;
		this.validTeDefaultMetric = true;
	}

	public short getLinkProtectionType() {
		return linkProtectionType;
	}

	public void setLinkProtectionType(short linkProtectionType) {
		this.linkProtectionType = linkProtectionType;
		this.validLinkProtectionType = true;
	}

	public short getMplsProtocolMask() {
		return mplsProtocolMask;
	}

	public void setMplsProtocolMask(short mplsProtocolMask) {
		this.mplsProtocolMask = mplsProtocolMask;
		this.validMplsProtocolMask = true;
	}

	public int getMetric() {
		return metric;
	}

	public void setMetric(int metric) {
		this.metric = metric;
		this.validMetric = true;
	}

	public List<Long> getSharedRiskLinkGroups() {
		return sharedRiskLinkGroups;
	}

	public void setSharedRiskLinkGroups(List<Long> sharedRiskLinkGroups) {
		this.sharedRiskLinkGroups = sharedRiskLinkGroups;
		this.validSharedRiskLinkGroups = true;
	}

	public byte[] getOpaqueLinkAttribute() {
		return opaqueLinkAttribute;
	}

	public void setOpaqueLinkAttribute(byte[] opaqueLinkAttribute) {
		this.opaqueLinkAttribute = opaqueLinkAttribute;
	}

	public String getLinkNameAttribute() {
		return linkNameAttribute;
	}

	public void setLinkNameAttribute(String linkNameAttribute) {
		this.linkNameAttribute = linkNameAttribute;
	}

	public short getIgpFlags() {
		return igpFlags;
	}

	public void setIgpFlags(short igpFlags) {
		this.igpFlags = igpFlags;
		this.validIgpFlags = true;
	}

	public long getRouteTag() {
		return routeTag;
	}

	public void setRouteTag(long routeTag) {
		this.routeTag = routeTag;
		this.validRouteTag = true;
	}

	public byte[] getExtendedTag() {
		return extendedTag;
	}

	public void setExtendedTag(byte[] extendedTag) {
		this.extendedTag = extendedTag;
	}

	public long getPrefixMetric() {
		return prefixMetric;
	}

	public void setPrefixMetric(long prefixMetric) {
		this.prefixMetric = prefixMetric;
		this.validPrefixMetric = true;
	}

	public byte[] getOspfForwardingAddress() {
		return ospfForwardingAddress;
	}

	public void setOspfForwardingAddress(byte[] ospfForwardingAddress) {
		this.ospfForwardingAddress = ospfForwardingAddress;
	}

	public byte[] getOpaquePrefixAttribute() {
		return opaquePrefixAttribute;
	}

	public void setOpaquePrefixAttribute(byte[] opaquePrefixAttribute) {
		this.opaquePrefixAttribute = opaquePrefixAttribute;
	}

	public boolean isValidNodeFlagBits() {
		return validNodeFlagBits;
	}

	public boolean isValidAdminGroup() {
		return validAdminGroup;
	}

	public boolean isValidMaxLinkBandwidth() {
		return validMaxLinkBandwidth;
	}

	public boolean isValidMaxReservableLinkBandwidth() {
		return validMaxReservableLinkBandwidth;
	}

	public boolean isValidTeDefaultMetric() {
		return validTeDefaultMetric;
	}

	public boolean isValidLinkProtectionType() {
		return validLinkProtectionType;
	}

	public boolean isValidMplsProtocolMask() {
		return validMplsProtocolMask;
	}

	public boolean isValidMetric() {
		return validMetric;
	}

	public boolean isValidIgpFlags() {
		return validIgpFlags;
	}

	public boolean isValidRouteTag() {
		return validRouteTag;
	}

	public boolean isValidPrefixMetric() {
		return validPrefixMetric;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LinkStateAttribute [");
		if (multiTopologyId != null) {
			builder.append("multiTopologyId=");
			builder.append(Arrays.toString(multiTopologyId));
		}
		if (validNodeFlagBits) {
			builder.append(", nodeFlagBits=");
			builder.append(nodeFlagBits);
		}
		if (opaqueNodeProperties != null) {
			builder.append(", opaqueNodeProperties=");
			builder.append(Arrays.toString(opaqueNodeProperties));
		}
		if (nodeName != null) {
			builder.append(", nodeName=");
			builder.append(nodeName);
		}
		if (isisAreaIdentifiers.size() > 0) {
			builder.append(", isisAreaIdentifiers=");
			builder.append(isisAreaIdentifiers);
		}
		if (localNodeIPv4RouterIDs.size() > 0) {
			builder.append(", localNodeIPv4RouterID=");
			builder.append(localNodeIPv4RouterIDs);
		}
		if (localNodeIPv6RouterIDs.size() > 0) {
			builder.append(", localNodeIPv6RouterID=");
			builder.append(localNodeIPv6RouterIDs);
		}
		if (remoteNodeIPv4RouterIDs.size() > 0) {
			builder.append(", remoteNodeIPv4RouterID=");
			builder.append(remoteNodeIPv4RouterIDs);
		}
		if (remoteNodeIPv6RouterIDs.size() > 0) {
			builder.append(", remoteNodeIPv6RouterID=");
			builder.append(remoteNodeIPv6RouterIDs);
		}
		if (validAdminGroup) {
			builder.append(", adminGroup=");
			builder.append(adminGroup);
		}
		if (validMaxLinkBandwidth) {
			builder.append(", maxLinkBandwidth=");
			builder.append(maxLinkBandwidth);
		}
		if (validMaxReservableLinkBandwidth) {
			builder.append(", maxReservableLinkBandwidth=");
			builder.append(maxReservableLinkBandwidth);
		}
		if (unreservedBandwidth != null) {
			builder.append(", unreservedBandwidth=");
			builder.append(Arrays.toString(unreservedBandwidth));
		}
		if (validTeDefaultMetric) {
			builder.append(", teDefaultMetric=");
			builder.append(teDefaultMetric);
		}
		if (validLinkProtectionType) {
			builder.append(", linkProtectionType=");
			builder.append(linkProtectionType);
		}
		if (validMplsProtocolMask) {
			builder.append(", mplsProtocolMask=");
			builder.append(mplsProtocolMask);
		}
		if (validMetric) {
			builder.append(", metric=");
			builder.append(metric);
		}
		if (sharedRiskLinkGroups.size() > 0) {
			builder.append(", sharedRiskLinkGroups=");
			builder.append(sharedRiskLinkGroups);
		}
		if (opaqueLinkAttribute != null) {
			builder.append(", opaqueLinkAttribute=");
			builder.append(Arrays.toString(opaqueLinkAttribute));
		}
		if (linkNameAttribute != null) {
			builder.append(", linkNameAttribute=");
			builder.append(linkNameAttribute);
		}
		if (validIgpFlags) {
			builder.append(", igpFlags=");
			builder.append(igpFlags);
		}
		if (validRouteTag) {
			builder.append(", routeTag=");
			builder.append(routeTag);
		}
		if (extendedTag != null) {
			builder.append(", extendedTag=");
			builder.append(Arrays.toString(extendedTag));
		}
		if (validPrefixMetric) {
			builder.append(", prefixMetric=");
			builder.append(prefixMetric);
		}
		if (ospfForwardingAddress != null) {
			builder.append(", ospfForwardingAddress=");
			builder.append(Arrays.toString(ospfForwardingAddress));
		}
		if (opaquePrefixAttribute != null) {
			builder.append(", opaquePrefixAttribute=");
			builder.append(Arrays.toString(opaquePrefixAttribute));
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
  protected ToStringBuilder subclassToString() {
	  // TODO Auto-generated method stub
	  return null;
  }

	@Override
  protected PathAttributeType internalType() {
		return PathAttributeType.LINK_STATE;
  }

	@Override
  protected boolean subclassEquals(PathAttribute obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkStateAttribute other = (LinkStateAttribute) obj;
		if (adminGroup != other.adminGroup)
			return false;
		if (!Arrays.equals(extendedTag, other.extendedTag))
			return false;
		if (igpFlags != other.igpFlags)
			return false;
		if (isisAreaIdentifiers == null) {
			if (other.isisAreaIdentifiers != null)
				return false;
		}
		else if (!isisAreaIdentifiers.equals(other.isisAreaIdentifiers))
			return false;
		if (linkNameAttribute == null) {
			if (other.linkNameAttribute != null)
				return false;
		}
		else if (!linkNameAttribute.equals(other.linkNameAttribute))
			return false;
		if (linkProtectionType != other.linkProtectionType)
			return false;
		if (Float.floatToIntBits(maxLinkBandwidth) != Float
		    .floatToIntBits(other.maxLinkBandwidth))
			return false;
		if (Float.floatToIntBits(maxReservableLinkBandwidth) != Float
		    .floatToIntBits(other.maxReservableLinkBandwidth))
			return false;
		if (metric != other.metric)
			return false;
		if (mplsProtocolMask != other.mplsProtocolMask)
			return false;
		if (!Arrays.equals(multiTopologyId, other.multiTopologyId))
			return false;
		if (nodeFlagBits != other.nodeFlagBits)
			return false;
		if (nodeName == null) {
			if (other.nodeName != null)
				return false;
		}
		else if (!nodeName.equals(other.nodeName))
			return false;
		if (!Arrays.equals(opaqueLinkAttribute, other.opaqueLinkAttribute))
			return false;
		if (!Arrays.equals(opaqueNodeProperties, other.opaqueNodeProperties))
			return false;
		if (!Arrays.equals(opaquePrefixAttribute, other.opaquePrefixAttribute))
			return false;
		if (!Arrays.equals(ospfForwardingAddress, other.ospfForwardingAddress))
			return false;
		if (prefixMetric != other.prefixMetric)
			return false;
		if (routeTag != other.routeTag)
			return false;
		if (sharedRiskLinkGroups == null) {
			if (other.sharedRiskLinkGroups != null)
				return false;
		}
		else if (!sharedRiskLinkGroups.equals(other.sharedRiskLinkGroups))
			return false;
		if (teDefaultMetric != other.teDefaultMetric)
			return false;
		if (!Arrays.equals(unreservedBandwidth, other.unreservedBandwidth))
			return false;
		return true;
  }

	@Override
  protected int subclassHashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (adminGroup ^ (adminGroup >>> 32));
		result = prime * result + Arrays.hashCode(extendedTag);
		result = prime * result + igpFlags;
		result = prime * result
		    + ((isisAreaIdentifiers == null) ? 0 : isisAreaIdentifiers.hashCode());
		result = prime * result
		    + ((linkNameAttribute == null) ? 0 : linkNameAttribute.hashCode());
		result = prime * result + linkProtectionType;
		result = prime * result + Float.floatToIntBits(maxLinkBandwidth);
		result = prime * result + Float.floatToIntBits(maxReservableLinkBandwidth);
		result = prime * result + metric;
		result = prime * result + teDefaultMetric;
		result = prime * result + mplsProtocolMask;
		result = prime * result + Arrays.hashCode(multiTopologyId);
		result = prime * result + nodeFlagBits;
		result = prime * result + ((nodeName == null) ? 0 : nodeName.hashCode());
		result = prime * result + Arrays.hashCode(opaqueLinkAttribute);
		result = prime * result + Arrays.hashCode(opaqueNodeProperties);
		result = prime * result + Arrays.hashCode(opaquePrefixAttribute);
		result = prime * result + Arrays.hashCode(ospfForwardingAddress);
		result = prime * result + (int) (prefixMetric ^ (prefixMetric >>> 32));
		result = prime * result + (int) (routeTag ^ (routeTag >>> 32));
		result = prime
		    * result
		    + ((sharedRiskLinkGroups == null) ? 0 : sharedRiskLinkGroups.hashCode());
		result = prime * result + Arrays.hashCode(unreservedBandwidth);
		return result;
  }

	@Override
  protected int subclassCompareTo(PathAttribute obj) {
		LinkStateAttribute o = (LinkStateAttribute)obj;
		return (new CompareToBuilder())
				.append(getAdminGroup(), o.getAdminGroup())
				.append(getMaxLinkBandwidth(), o.getMaxLinkBandwidth())
				.append(getMaxReservableLinkBandwidth(), o.getMaxReservableLinkBandwidth())
				.append(getUnreservedBandwidth(), o.getUnreservedBandwidth())
				.append(getTeDefaultMetric(), o.getTeDefaultMetric())
				.append(getLinkProtectionType(), o.getLinkProtectionType())
				.append(getMplsProtocolMask(), o.getMplsProtocolMask())
				.append(getMetric(), o.getMetric())
				.append(getSharedRiskLinkGroups(), o.getSharedRiskLinkGroups())
				.append(getNodeFlagBits(), o.getNodeFlagBits())
				.append(getIgpFlags(),  o.getIgpFlags())
				.append(getRouteTag(), o.getRouteTag())
				.append(getExtendedTag(), o.getExtendedTag())
				.append(getPrefixMetric(), o.getPrefixMetric())
				.append(getOspfForwardingAddress(), o.getOspfForwardingAddress())
				.toComparison();
  }
	
	public void addSharedRiskLinkGroup(long value) {
		this.sharedRiskLinkGroups.add(value);
		this.validSharedRiskLinkGroups = true;
	}

	public boolean isValidISISAreaIdentifiers() {
		return validISISAreaIdentifiers;
	}

	public boolean isValidLocalNodeIPv4RouterIDs() {
		return validLocalNodeIPv4RouterIDs;
	}

	public boolean isValidLocalNodeIPv6RouterIDs() {
		return validLocalNodeIPv6RouterIDs;
	}

	public boolean isValidRemoteNodeIPv4RouterIDs() {
		return validRemoteNodeIPv4RouterIDs;
	}

	public boolean isValidRemoteNodeIPv6RouterIDs() {
		return validRemoteNodeIPv6RouterIDs;
	}

	public boolean isValidSharedRiskLinkGroups() {
		return validSharedRiskLinkGroups;
	}

}
