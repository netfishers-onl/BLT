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
 */
package org.blt.bgp.net.capabilities;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.blt.bgp.net.AddressFamily;
import org.blt.bgp.net.ORFSendReceive;
import org.blt.bgp.net.ORFType;
import org.blt.bgp.net.SubsequentAddressFamily;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OutboundRouteFilteringCapability extends Capability {

	public OutboundRouteFilteringCapability() { }
	
	public OutboundRouteFilteringCapability(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily) {
		this.addressFamily = addressFamily;
		this.subsequentAddressFamily = subsequentAddressFamily;
	}

	public OutboundRouteFilteringCapability(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, Map<ORFType, ORFSendReceive> filters) {
		this(addressFamily, subsequentAddressFamily);

		setFilters(filters);
	}

	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	private Map<ORFType, ORFSendReceive> filters = new TreeMap<ORFType, ORFSendReceive>();
	
	/* (non-Javadoc)
	 * @see org.blt.bgp.netty.protocol.Capability#encodeParameterValue()
	 */

	/**
	 * @return the filters
	 */
	public Map<ORFType, ORFSendReceive> getFilters() {
		return filters;
	}

	/**
	 * @param filters the filters to set
	 */
	public void setFilters(Map<ORFType, ORFSendReceive> filters) {
		this.filters.clear();
		
		if(filters != null)
			this.filters.putAll(filters);
	}

	/**
	 * @return the addressFamily
	 */
	public AddressFamily getAddressFamily() {
		return addressFamily;
	}

	/**
	 * @param addressFamily the addressFamily to set
	 */
	public void setAddressFamily(AddressFamily addressFamily) {
		this.addressFamily = addressFamily;
	}

	/**
	 * @return the subsequentAddressFamily
	 */
	public SubsequentAddressFamily getSubsequentAddressFamily() {
		return subsequentAddressFamily;
	}

	/**
	 * @param subsequentAddressFamily the subsequentAddressFamily to set
	 */
	public void setSubsequentAddressFamily(
			SubsequentAddressFamily subsequentAddressFamily) {
		this.subsequentAddressFamily = subsequentAddressFamily;
	}

	/* (non-Javadoc)
	 * @see org.blt.bgp.net.Capability#orderNumber()
	 */
	@Override
	protected int orderNumber() {
		return ORDER_NUMBER_OUTBOUND_ROUTE_FILTERING_CAPABILITY;
	}

	@Override
	protected boolean equalsSubclass(Capability other) {
		OutboundRouteFilteringCapability orc = (OutboundRouteFilteringCapability)other;
		EqualsBuilder builder = new EqualsBuilder();
		
		builder.append(getAddressFamily(), orc.getAddressFamily())
			.append(getSubsequentAddressFamily(), orc.getSubsequentAddressFamily())
			.append(getFilters().size(), orc.getFilters()
					.size());
		
		if(!builder.isEquals())
			return false;

		Iterator<ORFType> hSet = getFilters().keySet().iterator();
		Iterator<ORFType> oSet = orc.getFilters().keySet().iterator();
		
		while(hSet.hasNext()) {
			ORFType hType = hSet.next();
			ORFType oType = oSet.next();
			
			builder.append(hType, oType).append(getFilters().get(hType), orc.getFilters().get(oType));
		}
		
		return builder.isEquals();
	}

	@Override
	protected int hashCodeSubclass() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		
		hcb.append(getAddressFamily()).append(getSubsequentAddressFamily());
		
		for(ORFType orfType : getFilters().keySet())
			hcb.append(orfType.toCode()).append(getFilters().get(orfType).toCode());
		
		return hcb.toHashCode();
	}

	@Override
	protected int compareToSubclass(Capability other) {
		OutboundRouteFilteringCapability orc = (OutboundRouteFilteringCapability)other;
		CompareToBuilder builder = new CompareToBuilder();
		
		builder.append(getAddressFamily(), orc.getAddressFamily())
			.append(getSubsequentAddressFamily(), orc.getSubsequentAddressFamily())
			.append(getFilters().size(), orc.getFilters()
					.size());
		
		if(builder.toComparison() != 0)
			return builder.toComparison();

		Iterator<ORFType> hSet = getFilters().keySet().iterator();
		Iterator<ORFType> oSet = orc.getFilters().keySet().iterator();
		
		while(hSet.hasNext()) {
			ORFType hType = hSet.next();
			ORFType oType = oSet.next();
			
			builder.append(hType, oType).append(getFilters().get(hType), orc.getFilters().get(oType));
		}
		
		return builder.toComparison();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
