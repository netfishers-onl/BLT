/**
 *  Copyright 2012, 2014 Rainer Bieniek (Rainer.Bieniek@web.de)
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
 * File: onl.netfishers.blt.bgp.netty.protocol.refresh.OutboundRouteFilter.java 
 */
package onl.netfishers.blt.bgp.net;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OutboundRouteFilter {
	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	private ORFRefreshType refreshType;

	private Map<ORFType, List<ORFEntry>> entries = new HashMap<ORFType, List<ORFEntry>>();
	
	public OutboundRouteFilter(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily) {
		setAddressFamily(addressFamily);
		setSubsequentAddressFamily(subsequentAddressFamily);
	}

	public OutboundRouteFilter(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, ORFRefreshType refreshType) {
		this(addressFamily, subsequentAddressFamily);
		
		setRefreshType(refreshType);
	}

	public OutboundRouteFilter(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily, ORFRefreshType refreshType, ORFEntry[] entries) {
		this(addressFamily, subsequentAddressFamily, refreshType);
		
		if(entries != null) {
			for (ORFEntry entry : entries) {
				addORFEntry(entry);
			}
		}
	}

	/**
	 * add an ORF entry to the 
	 * @param entry
	 */
	public void addORFEntry(ORFEntry entry) {
		if(!this.entries.containsKey(entry.getORFType()))
			this.entries.put(entry.getORFType(), new LinkedList<ORFEntry>());
		
		this.entries.get(entry.getORFType()).add(entry);		
	}

	/**
	 * add all ORF entries to the 
	 * @param entry
	 */
	public void addAllORFEntries(Collection<ORFEntry> entries) {
		for(ORFEntry entry : entries)
			addORFEntry(entry);
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

	/**
	 * @return the refreshType
	 */
	public ORFRefreshType getRefreshType() {
		return refreshType;
	}

	/**
	 * @param refreshType the refreshType to set
	 */
	public void setRefreshType(ORFRefreshType refreshType) {
		this.refreshType = refreshType;
	}

	/**
	 * @return the entries
	 */
	public Map<ORFType, List<ORFEntry>> getEntries() {
		return entries;
	}

	/**
	 * @param entries the entries to set
	 */
	public void setEntries(Map<ORFType, List<ORFEntry>> entries) {
		this.entries = entries;
	}
	
	@Override
	public String toString() {
		return (new ToStringBuilder(this))
				.append("addressFamily", addressFamily)
				.append("subsequentAddressFamily", subsequentAddressFamily)
				.append("refreshType", refreshType)
				.toString();
	}
}
