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
package onl.netfishers.blt.bgp.net.attributes;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import onl.netfishers.blt.bgp.net.AddressFamily;
import onl.netfishers.blt.bgp.net.AddressFamilyKey;
import onl.netfishers.blt.bgp.net.BinaryNextHop;
import onl.netfishers.blt.bgp.net.SubsequentAddressFamily;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The MultiProtocolNLRI is an encapsulator for all content in a MP-BGP NLRI (RFC 4760)
 * The variable length information is represented as a list of NLRIs within this
 * encapsulator
 * @author nitinb
 *
 */
public class MultiProtocolNLRI extends PathAttribute {

	private AddressFamily addressFamily;
	private SubsequentAddressFamily subsequentAddressFamily;
	private BinaryNextHop nextHop;
	private List<MultiProtocolNLRIInformation> nlris = new LinkedList<MultiProtocolNLRIInformation>();
	private PathAttributeType attrType;
	
	/**
	 * 
	 */
	public MultiProtocolNLRI() {
		super(Category.OPTIONAL_NON_TRANSITIVE);
	}

	/**
	 * Gets the BGP address family
	 * @return addressFamily
	 */
	public AddressFamily getAddressFamily() {
		return addressFamily;
	}

	/**
	 * Sets the BGP address family
	 * @param addressFamily addressFamily
	 */
	public void setAddressFamily(AddressFamily addressFamily) {
		this.addressFamily = addressFamily;
	}

	/**
	 * Gets the BGP subsequent address family
	 * @return subsequentAddressFamily
	 */
	public SubsequentAddressFamily getSubsequentAddressFamily() {
		return subsequentAddressFamily;
	}

	/**
	 * Sets the BGP subsequent address family
	 * @param subsequentAddressFamily subsequentAddressFamily
	 */
	public void setSubsequentAddressFamily(
			SubsequentAddressFamily subsequentAddressFamily) {
		this.subsequentAddressFamily = subsequentAddressFamily;
	}

	/**
	 * Returns the path attribute type (Reachable or Unreachable)
	 * @return path attribute type
	 */
	public PathAttributeType getPathAttributeType() {
		return attrType;
	}
	
	/**
	 * Sets the path attribute type (Reachable or Unreachable)
	 * @param path attribute type
	 */
	public void setPathAttributeType(PathAttributeType attrType) {
		this.attrType = attrType;
	}
	
	/**
	 * Gets the nexthop address
	 * @return nexthop address
	 */
	public BinaryNextHop getNextHop() {
		return nextHop;
	}

	/**
	 * Sets the nexthop address, given a byte array
	 * @param nextHopAddress nexthop address
	 */
	public void setNextHopAddress(byte[] nextHopAddress) {
		if(nextHopAddress != null)
			this.nextHop = new BinaryNextHop(nextHopAddress);
		else
			this.nextHop = null;
	}

	/**
	 * Sets the nexthop address, given a binary nexthop
	 * @param nextHopAddress nexthop address
	 */
	public void setNextHop(BinaryNextHop nextHop) {
		this.nextHop = nextHop;
	}

	/**
	 * Gets the list of NLRIs associated with this object
	 * @return list of nlris
	 */
	public List<MultiProtocolNLRIInformation> getNlris() {
		return nlris;
	}

	/**
	 * Set the list of NLRIs associated with this object
	 * @param nlris list of nlris
	 */
	public void setNlris(List<MultiProtocolNLRIInformation> nlris) {
		this.nlris = nlris;
	}

	/**
	 * Returns the address family key associated with this object
	 * @return address family key
	 */
	public AddressFamilyKey addressFamilyKey() {
		return new AddressFamilyKey(getAddressFamily(), getSubsequentAddressFamily());
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.net.attributes.PathAttribute#internalType()
	 */
	@Override
	protected PathAttributeType internalType() {
		return attrType;
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.net.attributes.PathAttribute#subclassEquals(onl.netfishers.blt.bgp.net.attributes.PathAttribute)
	 */
	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		MultiProtocolNLRI o = (MultiProtocolNLRI)obj;
		
		EqualsBuilder builer = (new EqualsBuilder())
				.append(getPathAttributeType(), o.getPathAttributeType())
				.append(getAddressFamily(), o.getAddressFamily())
				.append(getSubsequentAddressFamily(), o.getSubsequentAddressFamily())
				.append(getNextHop(), o.getNextHop())
				.append(getNlris().size(), o.getNlris().size());
		
		if(builer.isEquals()) {
			Iterator<MultiProtocolNLRIInformation> lit = getNlris().iterator();
			Iterator<MultiProtocolNLRIInformation> rit = o.getNlris().iterator();
			
			while(lit.hasNext())
				builer.append(lit.next(), rit.next());
		}
		
		return builer.isEquals();
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.net.attributes.PathAttribute#subclassHashCode()
	 */
	@Override
	protected int subclassHashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder())
				.append(getPathAttributeType())
				.append(getAddressFamily())
				.append(getSubsequentAddressFamily())
				.append(getNextHop());
		Iterator<MultiProtocolNLRIInformation> it = getNlris().iterator();
		
		while(it.hasNext())
			builder.append(it.next());
		
		return builder.toHashCode();
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.net.attributes.PathAttribute#subclassCompareTo(onl.netfishers.blt.bgp.net.attributes.PathAttribute)
	 */
	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		MultiProtocolNLRI o = (MultiProtocolNLRI)obj;
		
		CompareToBuilder builer = (new CompareToBuilder())
				.append(getPathAttributeType(), o.getPathAttributeType())
				.append(getAddressFamily(), o.getAddressFamily())
				.append(getSubsequentAddressFamily(), o.getSubsequentAddressFamily())
				.append(getNextHop(), o.getNextHop())
				.append(getNlris().size(), o.getNlris().size());
		
		if(builer.toComparison() == 0) {
			Iterator<MultiProtocolNLRIInformation> lit = getNlris().iterator();
			Iterator<MultiProtocolNLRIInformation> rit = o.getNlris().iterator();
			
			while(lit.hasNext())
				builer.append(lit.next(), rit.next());
		}
		
		return builer.toComparison();
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.net.attributes.PathAttribute#subclassToString()
	 */
	@Override
	protected ToStringBuilder subclassToString() {
		ToStringBuilder builder = new ToStringBuilder(this)
			.append(attrType)
			.append(addressFamily)
			.append(subsequentAddressFamily)
			.append(nextHop);

		for(MultiProtocolNLRIInformation n : nlris)
			builder.append("nlri", n);

		return builder;
	}


}
