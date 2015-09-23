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
 * File: onl.netfishers.blt.bgp.net.InetAddressNextHop.java 
 */
package onl.netfishers.blt.bgp.net;

import java.net.InetAddress;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class InetAddressNextHop<T extends InetAddress> implements NextHop {

	private T address;
	
	public InetAddressNextHop(T address) {
		this.address = address;
	}
	
	/**
	 * @return the address
	 */
	public T getAddress() {
		return address;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(getAddress()).toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if(!(obj instanceof InetAddressNextHop))
			return false;

		InetAddressNextHop<T> o = (InetAddressNextHop<T>)obj;
		
		return (new EqualsBuilder()).append(getAddress(), o.getAddress()).isEquals();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(NextHop o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getType(), o.getType());
		
		if(o.getType() == Type.InetAddress) {
			builder.append(getAddress().getAddress(), ((InetAddressNextHop<InetAddress>)o).getAddress().getAddress());
		}
		
		return builder.toComparison();
	}

	@Override
	public Type getType() {
		return Type.InetAddress;
	}

	public BinaryNextHop toBinaryNextHop() {
		return new BinaryNextHop(getAddress().getAddress());
	}
}
