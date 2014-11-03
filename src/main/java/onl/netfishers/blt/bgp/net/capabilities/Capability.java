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
package onl.netfishers.blt.bgp.net.capabilities;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;



/**
 * base class for all optional BGP4 protocol capabilities
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class Capability implements Comparable<Capability> {

	protected static final int ORDER_NUMBER_AS4_CAPABILITY = 1;
	protected static final int ORDER_NUMBER_MULTI_PROTOCOL_CAPABILITY = 2;
	protected static final int ORDER_NUMBER_OUTBOUND_ROUTE_FILTERING_CAPABILITY = 3;
	protected static final int ORDER_NUMBER_ROUTE_REFRESH_CAPABILITY = 4;
	protected static final int ORDER_NUMBER_UNKNOWN_CAPABILITY = 5;
	
	@Override
	public final boolean equals(Object o) {
		if(!(o instanceof Capability))
			return false;
		
		Capability other = (Capability)o;
		
		if(orderNumber() != other.orderNumber())
			return false;
		
		return equalsSubclass(other);
	}

	@Override
	public final int hashCode() {
		return (new HashCodeBuilder()).append(orderNumber()).append(hashCodeSubclass()).toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public final int compareTo(Capability other) {
		int val = (new Integer(orderNumber())).compareTo(other.orderNumber());
		
		if(val != 0)
			return val;
		
		return compareToSubclass(other);
	}

	protected abstract int orderNumber();
	
	protected boolean equalsSubclass(Capability other) {
		return true;
	}
	
	protected int hashCodeSubclass() {
		return 0;
	}
	
	protected int compareToSubclass(Capability other) {
		return 0;
	}
}
