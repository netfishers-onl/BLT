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
 * File: onl.netfishers.blt.bgp.netty.protocol.MultiProtocolCapability.java 
 */
package onl.netfishers.blt.bgp.net.capabilities;

import onl.netfishers.blt.bgp.net.AddressFamily;
import onl.netfishers.blt.bgp.net.AddressFamilyKey;
import onl.netfishers.blt.bgp.net.SubsequentAddressFamily;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MultiProtocolCapability extends Capability {

	public MultiProtocolCapability() {}
	
	public MultiProtocolCapability(AddressFamily afi, SubsequentAddressFamily safi) {
		setAfi(afi);
		setSafi(safi);
	}
	
	private AddressFamily afi;
	private SubsequentAddressFamily safi;
	
	/**
	 * @return the afi
	 */
	public AddressFamily getAfi() {
		return afi;
	}

	/**
	 * @param afi the afi to set
	 */
	public void setAfi(AddressFamily afi) {
		this.afi = afi;
	}

	/**
	 * @return the safi
	 */
	public SubsequentAddressFamily getSafi() {
		return safi;
	}

	/**
	 * @param safi the safi to set
	 */
	public void setSafi(SubsequentAddressFamily safi) {
		this.safi = safi;
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.net.Capability#orderNumber()
	 */
	@Override
	protected int orderNumber() {
		return ORDER_NUMBER_MULTI_PROTOCOL_CAPABILITY;
	}

	public AddressFamilyKey toAddressFamilyKey() {
		return new AddressFamilyKey(getAfi(), getSafi());
	}
	
	@Override
	protected boolean equalsSubclass(Capability other) {
		MultiProtocolCapability mp = (MultiProtocolCapability)other;
		
		return (new EqualsBuilder())
				.append(getAfi(), mp.getAfi())
				.append(getSafi(), mp.getSafi())
				.isEquals();
	}

	@Override
	protected int hashCodeSubclass() {
		return (new HashCodeBuilder())
				.append(getAfi())
				.append(getSafi())
				.toHashCode();
	}

	@Override
	protected int compareToSubclass(Capability other) {
		MultiProtocolCapability mp = (MultiProtocolCapability)other;
		
		return (new CompareToBuilder())
				.append(getAfi(), mp.getAfi())
				.append(getSafi(), mp.getSafi())
				.toComparison();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
