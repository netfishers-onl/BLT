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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class UnknownCapability extends Capability {
	private int capabilityType;
	private byte[] value;
	
	public UnknownCapability() {
		
	}
	
	public UnknownCapability(int capabilityType, byte[] value) {
		setCapabilityType(capabilityType);
		setValue(value);
	}
	
	/**
	 * @return the value
	 */
	public byte[] getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(byte[] value) {
		this.value = value;
	}

	/**
	 * @param capabilityType the capabilityType to set
	 */
	public void setCapabilityType(int capabilityType) {
		this.capabilityType = capabilityType;
	}

	/**
	 * @return the capabilityType
	 */
	public int getCapabilityType() {
		return capabilityType;
	}

	/* (non-Javadoc)
	 * @see org.blt.bgp.net.Capability#orderNumber()
	 */
	@Override
	protected int orderNumber() {
		return ORDER_NUMBER_UNKNOWN_CAPABILITY;
	}

	@Override
	protected boolean equalsSubclass(Capability other) {
		UnknownCapability uc = (UnknownCapability)other;
		
		return (new EqualsBuilder())
				.append(getCapabilityType(), uc.getCapabilityType())
				.append(getValue(), uc.getValue())
				.isEquals();
	}

	@Override
	protected int hashCodeSubclass() {
		return (new HashCodeBuilder())
				.append(getValue())
				.append(getCapabilityType())
				.toHashCode();
	}

	@Override
	protected int compareToSubclass(Capability other) {
		UnknownCapability uc = (UnknownCapability)other;
		
		return (new CompareToBuilder())
				.append(getCapabilityType(), uc.getCapabilityType())
				.append(getValue(), uc.getValue())
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
