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
 * This enumerator contains the identifier values for various objects and
 * attributes associated with BGP link-state data. 
 * The TLV/sub-tlv types are based on draft-ietf-idr-ls-distribution-01
 * @author nitinb
 *
 */
public enum BgpLsType {
	LocalNodeDescriptors,
	RemoteNodeDescriptors,
	LinkLocalRemoteIdentifiers,
	IPv4InterfaceAddress,
	IPv4NeighborAddress,
	IPv6InterfaceAddress,
	IPv6NeighborAddress,
	MultiTopologyID,
	OSPFRouteType,
	IPReachabilityInformation,
	AutonomousSystem,
	BGPLSIdentifier,
	AreaID,
	IGPRouterID,
	NodeFlagBits,
	OpaqueNodeProperties,
	NodeName,
	ISISAreaIdentifier,
	LocalNodeIPv4RouterID,
	LocalNodeIPv6RouterID,
	RemoteNodeIPv4RouterID,
	RemoteNodeIPv6RouterID,
	AdministrativeGroup,
	MaximumLinkBandwith,
	MaxReservableLinkBandwidth,
	UnreservedBandwidth,
	TEDefaultMetric,
	LinkProtectionType,
	MPLSProtocolMask,
	Metric,
	SharedRiskLinkGroup,
	OpaqueLinkAttribute,
	LinkNameAttribute,
	IGPFlags,
	RouteTag,
	ExtendedTag,
	PrefixMetric,
	OSPFForwardingAddress,
	OpaquePrefixAttribute,
	Unknown;
	
	/**
	 * Convert the enumerator into an integer value
	 * @return integer value
	 */
	public int toCode() {
		switch(this) {
		case LocalNodeDescriptors:
			return 256;
		case RemoteNodeDescriptors:
			return 257;
		case LinkLocalRemoteIdentifiers:
			return 258;
		case IPv4InterfaceAddress:
			return 259;
		case IPv4NeighborAddress:
			return 260;
		case IPv6InterfaceAddress:
			return 261;
		case IPv6NeighborAddress:
			return 262;
		case MultiTopologyID:
			return 263;
		case OSPFRouteType:
			return 264;
		case IPReachabilityInformation:
			return 265;
		case AutonomousSystem:
			return 512;
		case BGPLSIdentifier:
			return 513;
		case AreaID:
			return 514;
		case IGPRouterID:
			return 515;
		case NodeFlagBits:
			return 1024;
		case OpaqueNodeProperties:
			return 1025;
		case NodeName:
			return 1026;
		case ISISAreaIdentifier:
			return 1027;
		case LocalNodeIPv4RouterID:
			return 1028;
		case LocalNodeIPv6RouterID:
			return 1029;
		case RemoteNodeIPv4RouterID:
			return 1030;
		case RemoteNodeIPv6RouterID:
			return 1031;
		case AdministrativeGroup:
			return 1088;
		case MaximumLinkBandwith:
			return 1089;
		case MaxReservableLinkBandwidth:
			return 1090;
		case UnreservedBandwidth:
			return 1091;
		case TEDefaultMetric:
			return 1092;
		case LinkProtectionType:
			return 1093;
		case MPLSProtocolMask:
			return 1094;
		case Metric:
			return 1095;
		case SharedRiskLinkGroup:
			return 1096;
		case OpaqueLinkAttribute:
			return 1097;
		case LinkNameAttribute:
			return 1098;
		case IGPFlags:
			return 1152;
		case RouteTag:
			return 1153;
		case ExtendedTag:
			return 1154;
		case PrefixMetric:
			return 1155;
		case OSPFForwardingAddress:
			return 1156;
		case OpaquePrefixAttribute:
			return 1157;
		default:
			throw new IllegalArgumentException("unknown type: " + this);
		}
	}
	
	/**
	 * Gets a enumerator from an integer value
	 * @param code integer value
	 * @return the enumerator
	 */
	public static BgpLsType fromCode(int code) {
		switch(code) {
		case 256:
			return LocalNodeDescriptors;
		case 257:
			return RemoteNodeDescriptors;
		case 258:
			return LinkLocalRemoteIdentifiers;
		case 259:
			return IPv4InterfaceAddress;
		case 260:
			return IPv4NeighborAddress;
		case 261:
			return IPv6InterfaceAddress;
		case 262:
			return IPv6NeighborAddress;
		case 263:
			return MultiTopologyID;
		case 264:
			return OSPFRouteType;
		case 265:
			return IPReachabilityInformation;
		case 512:
			return AutonomousSystem;
		case 513:
			return BGPLSIdentifier;
		case 514:
			return AreaID;
		case 515:
			return IGPRouterID;
		case 1024:
			return NodeFlagBits;
		case 1025:
			return OpaqueNodeProperties;
		case 1026:
			return NodeName;
		case 1027:
			return ISISAreaIdentifier;
		case 1028:
			return LocalNodeIPv4RouterID;
		case 1029:
			return LocalNodeIPv6RouterID;
		case 1030:
			return RemoteNodeIPv4RouterID;
		case 1031:
			return RemoteNodeIPv6RouterID;
		case 1088:
			return AdministrativeGroup;
		case 1089:
			return MaximumLinkBandwith;
		case 1090:
			return MaxReservableLinkBandwidth;
		case 1091:
			return UnreservedBandwidth;
		case 1092:
			return TEDefaultMetric;
		case 1093:
			return LinkProtectionType;
		case 1094:
			return MPLSProtocolMask;
		case 1095:
			return Metric;
		case 1096:
			return SharedRiskLinkGroup;
		case 1097:
			return OpaqueLinkAttribute;
		case 1098:
			return LinkNameAttribute;
		case 1152:
			return IGPFlags;
		case 1153:
			return RouteTag;
		case 1154:
			return ExtendedTag;
		case 1155:
			return PrefixMetric;
		case 1156:
			return OSPFForwardingAddress;
		case 1157:
			return OpaquePrefixAttribute;
		default:
			return Unknown;
		}
	}
	
	/**
	 * Converts the enumerator to a string
	 * @return String form of enumerator
	 */
	public String toString() {
		switch(this) {
		case LocalNodeDescriptors:
			return "Local Router Descriptors";
		case RemoteNodeDescriptors:
			return "Remote Router Descriptors";
		case LinkLocalRemoteIdentifiers:
			return "Link Local/Remote Identifiers";
		case IPv4InterfaceAddress:
			return "IPv4 interface address";
		case IPv4NeighborAddress:
			return "IPv4 neighbor address";
		case IPv6InterfaceAddress:
			return "IPv6 interface address";
		case IPv6NeighborAddress:
			return "IPv6 neighbor address";
		case MultiTopologyID:
			return "Multi-Topology ID";
		case OSPFRouteType:
			return "OSPF Route Type";
		case IPReachabilityInformation:
			return "IP Reachability Information";
		case AutonomousSystem:
			return "Autonomous System";
		case BGPLSIdentifier:
			return "BGP-LS Identifier";
		case AreaID:
			return "Area ID";
		case IGPRouterID:
			return "IGP Router-ID";
		case NodeFlagBits:
			return "Router Flag Bits";
		case OpaqueNodeProperties:
			return "Opaque Router Properties";
		case NodeName:
			return "Router Name";
		case ISISAreaIdentifier:
			return "IS-IS Area Identifier";
		case LocalNodeIPv4RouterID:
			return "IPv4 Router-ID of Local Router";
		case LocalNodeIPv6RouterID:
			return "IPv6 Router-ID of Local Router";
		case RemoteNodeIPv4RouterID:
			return "IPv4 Router-ID of Remote Router";
		case RemoteNodeIPv6RouterID:
			return "IPv6 Router-ID of Remote Router";
		case AdministrativeGroup:
			return "Administrative group (color)";
		case MaximumLinkBandwith:
			return "Maximum link bandwidth";
		case MaxReservableLinkBandwidth:
			return "Max. reservable link bandwidth";
		case UnreservedBandwidth:
			return "Unreserved bandwidth";
		case TEDefaultMetric:
			return "TE Default Metric";
		case LinkProtectionType:
			return "Link Protection Type";
		case MPLSProtocolMask:
			return "MPLS Protocol Mask";
		case Metric:
			return "Metric";
		case SharedRiskLinkGroup:
			return "Shared Risk Link Group";
		case OpaqueLinkAttribute:
			return "Opaque link attribute";
		case LinkNameAttribute:
			return "Link Name attribute";
		case IGPFlags:
			return "IGP Flags";
		case RouteTag:
			return "Route Tag";
		case ExtendedTag:
			return "Extended Tag";
		case PrefixMetric:
			return "Prefix Metric";
		case OSPFForwardingAddress:
			return "OSPF Forwarding Address";
		case OpaquePrefixAttribute:
			return "Opaque Prefix Attribute";
		
		default:
			return "Unknown";
		}
	}
}
