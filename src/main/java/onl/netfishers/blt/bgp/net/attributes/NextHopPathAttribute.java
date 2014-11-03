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
package onl.netfishers.blt.bgp.net.attributes;

import java.net.Inet4Address;

import onl.netfishers.blt.bgp.net.InetAddressNextHop;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class NextHopPathAttribute extends PathAttribute {

	public NextHopPathAttribute() {
		super(Category.WELL_KNOWN_MANDATORY);
	}

	public NextHopPathAttribute(Inet4Address nextHop) {
		super(Category.WELL_KNOWN_MANDATORY);
		
		setNextHop(new InetAddressNextHop<Inet4Address>(nextHop));
	}

	public NextHopPathAttribute(InetAddressNextHop<Inet4Address> nextHop) {
		super(Category.WELL_KNOWN_MANDATORY);
		
		setNextHop(nextHop);
	}

	private InetAddressNextHop<Inet4Address> nextHop;
	
	/**
	 * @return the nextHop
	 */
	public InetAddressNextHop<Inet4Address> getNextHop() {
		return nextHop;
	}

	/**
	 * set the next hop. If the next hop is semantically invalid, an exception is raised.
	 * 
	 * @param nextHop the nextHop to set, MUST NOT be an IP multicast address
	 * @throws IllegalArgumentException next hop address is a multicast address.
	 */
	public void setNextHop(InetAddressNextHop<Inet4Address> nextHop) {
		if(nextHop.getAddress().isMulticastAddress())
			throw new IllegalArgumentException();
		
		this.nextHop = nextHop;
	}

	/**
	 * set the next hop. If the next hop is semantically invalid, an exception is raised.
	 * 
	 * @param nextHop the nextHop to set, MUST NOT be an IP multicast address
	 * @throws IllegalArgumentException next hop address is a multicast address.
	 */
	public void setNextHop(Inet4Address nextHop) {
		if(nextHop.isMulticastAddress())
			throw new IllegalArgumentException();
		
		this.nextHop = new InetAddressNextHop<Inet4Address>(nextHop);
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.NEXT_HOP;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		NextHopPathAttribute o = (NextHopPathAttribute)obj;
		
		return (new EqualsBuilder()).append(getNextHop(), o.getNextHop()).isEquals();
	}

	@Override
	protected int subclassHashCode() {
		return (new HashCodeBuilder()).append(getNextHop()).toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		NextHopPathAttribute o = (NextHopPathAttribute)obj;
		
		return (new CompareToBuilder()).append(getNextHop(), o.getNextHop()).toComparison();
	}

	@Override
	protected ToStringBuilder subclassToString() {
		return (new ToStringBuilder(this))
				.append("nextHop", nextHop);
	}

}
