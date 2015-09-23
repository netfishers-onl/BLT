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
 * File: onl.netfishers.blt.bgp.netty.protocol.update.AggregatorPathAttribute.java 
 */
package onl.netfishers.blt.bgp.net.attributes;

import java.net.Inet4Address;

import onl.netfishers.blt.bgp.net.ASType;
import onl.netfishers.blt.bgp.net.ASTypeAware;
import onl.netfishers.blt.bgp.net.InetAddressComparator;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AggregatorPathAttribute extends PathAttribute implements ASTypeAware {

	private ASType asType;
	private int asNumber;
	private Inet4Address aggregator;

	public AggregatorPathAttribute(ASType asType) {
		super(Category.OPTIONAL_TRANSITIVE);

		this.asType = asType;
	}

	public AggregatorPathAttribute(ASType asType, int asNumber, Inet4Address aggregator) {
		this(asType);
		
		this.asNumber = asNumber;
		this.aggregator = aggregator;
	}

	/**
	 * @return the fourByteASNumber
	 */
	public boolean isFourByteASNumber() {
		return (this.asType == ASType.AS_NUMBER_4OCTETS);
	}

	/**
	 * @return the asType
	 */
	public ASType getAsType() {
		return asType;
	}

	/**
	 * @return the asNumber
	 */
	public int getAsNumber() {
		return asNumber;
	}

	/**
	 * @param asNumber the asNumber to set
	 */
	public void setAsNumber(int asNumber) {
		this.asNumber = asNumber;
	}

	/**
	 * @return the aggregator
	 */
	public Inet4Address getAggregator() {
		return aggregator;
	}

	/**
	 * @param aggregator the aggregator to set
	 */
	public void setAggregator(Inet4Address aggregator) {
		this.aggregator = aggregator;
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.AGGREGATOR;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		AggregatorPathAttribute o = (AggregatorPathAttribute)obj;
		
		return (new EqualsBuilder())
			.append(getAsNumber(), o.getAsNumber())
			.append(getAggregator(), o.getAggregator())
			.append(getAsType(), o.getAsType())
			.isEquals();
	}

	@Override
	protected int subclassHashCode() {
		return (new HashCodeBuilder())
			.append(getAsNumber())
			.append(getAggregator())
			.append(getAsType())
			.toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		AggregatorPathAttribute o = (AggregatorPathAttribute)obj;
		
		return (new CompareToBuilder())
			.append(getAsNumber(), o.getAsNumber())
			.append(getAggregator(), o.getAggregator(), new InetAddressComparator())
			.append(getAsType(), o.getAsType())
			.toComparison();
	}

	@Override
	protected ToStringBuilder subclassToString() {
		return (new ToStringBuilder(this))
				.append("asNumber", this.asNumber)
				.append("aggregator", this.aggregator)
				.append("asType", this.asType);
	}

}
