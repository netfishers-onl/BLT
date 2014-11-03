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
 * File: onl.netfishers.blt.bgp.rib.RoutingInformationBaseKey.java 
 */
/**
 * Copyright 2013 Nitin Bahadur (nitinb@gmail.com)
 * 
 * License: same as above
 * 
 * Added support for BGP-LS
 */
package onl.netfishers.blt.bgp.net;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AddressFamilyKey implements Comparable<AddressFamilyKey> {

	public static final AddressFamilyKey IPV4_UNICAST_FORWARDING = new AddressFamilyKey(AddressFamily.IPv4, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
	public static final AddressFamilyKey IPV6_UNICAST_FORWARDING = new AddressFamilyKey(AddressFamily.IPv6, SubsequentAddressFamily.NLRI_UNICAST_FORWARDING);
	public static final AddressFamilyKey BGP_LS_TOPOLOGY = new AddressFamilyKey(AddressFamily.BGP_LS, SubsequentAddressFamily.NLRI_BGP_LS);
	public static final AddressFamilyKey BGP_LS_VPN_TOPOLOGY = new AddressFamilyKey(AddressFamily.BGP_LS, SubsequentAddressFamily.NLRI_MPLS_VPN);
	
	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	
	public AddressFamilyKey(AddressFamily addressFamily, SubsequentAddressFamily subsequentAddressFamily) {
		this.addressFamily = addressFamily;
		this.subsequentAddressFamily = subsequentAddressFamily;
	}

	/**
	 * @return the addressFamily
	 */
	public AddressFamily getAddressFamily() {
		return addressFamily;
	}

	/**
	 * @return the subsequentAddressFamily
	 */
	public SubsequentAddressFamily getSubsequentAddressFamily() {
		return subsequentAddressFamily;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(this.addressFamily).append(this.subsequentAddressFamily).toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AddressFamilyKey))
			return false;
		
		AddressFamilyKey o = (AddressFamilyKey)obj;
		
		return (new EqualsBuilder())
				.append(addressFamily, o.addressFamily)
				.append(subsequentAddressFamily, o.subsequentAddressFamily)
				.isEquals();
	}

	public int compareTo(AddressFamilyKey o) {
		return (new CompareToBuilder())
				.append(addressFamily, o.addressFamily)
				.append(subsequentAddressFamily, o.subsequentAddressFamily)
				.toComparison();
	}
	
	public boolean matches(AddressFamily afi, SubsequentAddressFamily safi) {
		return ((getAddressFamily() == afi) && (getSubsequentAddressFamily() == safi));
	}
}
