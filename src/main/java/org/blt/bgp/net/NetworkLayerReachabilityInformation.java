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
package org.blt.bgp.net;

import java.io.Serializable;
import java.net.InetAddress;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * This class models the generic network layer reachabibility information as defined in RFC 4271 and RFC 2858.
 * 
 * The Network Layer Reachability Information (NLRI) is a variable length bit field. 
 * 
 * On the network layer it is encondig as a leading length (1 octet) and a varaiable number of of octets 
 * each carrying an 8-bit part of the NLRI. The rightmost octet is filled up with trailing bits which a re ignored by the receiver and should
 * be set to 0 by the sender.  
 * If the prefix length is zero then the number of trailing NLRI octets is zero as well. 
 * 
 * Then number of octets need to carry the NLRI can be calculated from this formula: number of octets = (prefix length / 8) + (prefix length % 8 > 0 ? 1 : 0)
 * 
 * NB: This class is also used to model the IPv4 withdrawn routes information in UPDATE (type 2) packets.
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
@XmlRootElement(name="nlri")
@XmlAccessorType(XmlAccessType.FIELD)
public class NetworkLayerReachabilityInformation implements Serializable, Comparable<NetworkLayerReachabilityInformation>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8319262066302848737L;
	
	private int prefixLength;
	private byte[] prefix;
	
	public NetworkLayerReachabilityInformation() {}
	
	public NetworkLayerReachabilityInformation(int prefixLength, byte[] prefix) {
		setPrefix(prefixLength, prefix);
	}
	
	public NetworkLayerReachabilityInformation(InetAddress source) {
		byte[] raw = source.getAddress();
		
		setPrefix(8*raw.length, raw);
	}
	
	/**
	 * @return the prefixLength
	 */
	public int getPrefixLength() {
		return prefixLength;
	}
	
	/**
	 * @return the prefix
	 */
	public byte[] getPrefix() {
		return prefix;
	}	

	/**
	 * set the prefix length and value in one step. The prefix value length in octets is checked against the prefix length
	 * and the number of octets calculated from the prefix length. The trailing bits are masked out to 0. 
	 * 
	 * @param prefixLength
	 * @param prefix
	 */
	public void setPrefix(int prefixLength, byte[] prefix) {
		this.prefixLength = prefixLength;
		this.prefix = prefix;

		if(prefix == null) {
			if(prefixLength != 0)
				throw new IllegalArgumentException("cannot set null prefix if prefix length greater 0");
			this.prefix = new byte[] {0};
		} else if(prefixLength == 0) {
			this.prefix = new byte[] {0};
		} else {			
			int prefixSize = calculateOctetsForPrefixLength(this.prefixLength);
			
			if(prefix.length != prefixSize)
				throw new IllegalArgumentException("expected a prefix with " + prefixSize + " octets but got " + prefix.length + " octets");

			// skip masking unless prefix length > 0
			if(prefixLength > 0) {
				// mask out trailing bits
				int trailingBits = (8*prefixSize - prefixLength);
				
				if(trailingBits > 0) {
					for(int bit = 0; bit < trailingBits; bit++)
						prefix[prefixSize-1] &= ~(1<<bit);
				}
			}
		}
	}
	
	/**
	 * check if this instance if a prefix of the given other prefix. The criteria for being a prefix are:
	 * - The prefix length of the other NLRI is longer than this NLRI length
	 * - The prefix bits match up to the shorter prefix length
	 * 
	 * If the other NLRI equals this NLRI then this NLRI is NOT a prefix of the other NLRI. If this NLRI has a 
	 * zero-length prefix length it is considered to be a prefix of any other NLRI except the other NLRI also has a 
	 * zero-length prefix length
	 * 
	 * @param other
	 * @return
	 */
	public boolean isPrefixOf(NetworkLayerReachabilityInformation other) {
		boolean isPrefix = false;
		
		if(prefixLength > 0) {
			if (other.prefixLength > 0
					&& other.prefixLength > this.prefixLength) {
				int byteLength = calculateOctetsForPrefixLength(this.prefixLength);
				boolean match = true;

				// test the full prefix octets
				for (int i = 0; i < byteLength - 1; i++) {
					if (this.prefix[i] != other.prefix[i]) {
						match = false;
						break;
					}
				}

				// prefix octets match, check remaining bits
				if (match) {
					int bitsToCheck = this.prefixLength % 8;

					if (bitsToCheck == 0) {
						match = (this.prefix[byteLength - 1] == other.prefix[byteLength - 1]);
					} else {
						for (int i = 0; i < bitsToCheck; i++) {
							int mask = 1 << (7 - i);

							if ((this.prefix[byteLength - 1] & mask) != (other.prefix[byteLength - 1] & mask)) {
								match = false;
								break;
							}
						}
					}
				}

				isPrefix = match;
			}
		} else {
			isPrefix = (other.prefixLength > 0);
		}
		
		return isPrefix;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(getPrefixLength())
				.append(getPrefix())
				.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof NetworkLayerReachabilityInformation))
			return false;
		
		NetworkLayerReachabilityInformation o =(NetworkLayerReachabilityInformation)obj;
		
		return (new EqualsBuilder())
				.append(getPrefixLength(), o.getPrefixLength())
				.append(getPrefix(), o.getPrefix())
				.isEquals();
	}

	@Override
	public int compareTo(NetworkLayerReachabilityInformation other) {
		CompareToBuilder builder = new CompareToBuilder();
		
		builder.append(getPrefixLength(), other.getPrefixLength());
		
		if(builder.toComparison() == 0) {
			int byteLen = calculateOctetsForPrefixLength(this.prefixLength);
			int otherByteLen = calculateOctetsForPrefixLength(other.prefixLength);
			int commonByteLen = (byteLen > otherByteLen) ? otherByteLen : byteLen;
			int commonPrefixLen = (prefixLength > other.prefixLength) ? other.prefixLength : prefixLength;
			
			for (int i = 0; i < commonByteLen - 1; i++) {
				builder.append(getPrefix()[i], other.getPrefix()[i]);
			}
			
			if(builder.toComparison() == 0) {
				int bitsToCheck = commonPrefixLen% 8;

				if (bitsToCheck == 0) {
					if(commonByteLen > 0)
						builder.append(getPrefix()[commonByteLen-1], other.getPrefix()[commonByteLen-1]);
				} else {
					for (int i = 0; i < bitsToCheck; i++) {
						int mask = 1 << (7 - i);

						builder.append(getPrefix()[commonByteLen - 1] & mask, other.getPrefix()[commonByteLen - 1] & mask);
						if(builder.toComparison() != 0)
							break;
					}
				}
				
				if(builder.toComparison() == 0) {
					builder.append(getPrefixLength(), other.getPrefixLength());
				}
			}

		}
		
		return builder.toComparison();
	}

	public static final int calculateOctetsForPrefixLength(int prefixLength) {
		return (prefixLength / 8) + (prefixLength % 8 > 0 ? 1 :0);		
	}

	private static final char[] chars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i<prefix.length; i++) {
			builder.append(chars[(prefix[i]/ 16) & 0x0f]);
			builder.append(chars[(prefix[i] % 16) & 0x0f]);
		}
		builder.append('/');
		builder.append(Integer.toString(prefixLength));
		
		return builder.toString();
	}
	
}
