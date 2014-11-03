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
 * File: onl.netfishers.blt.bgp.netty.protocol.refresh.AddressPrefixORFEntry.java 
 */
package onl.netfishers.blt.bgp.net;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class AddressPrefixBasedORFEntry extends ORFEntry {

	private int sequence;
	private int minLength;
	private int maxLength;
	private NetworkLayerReachabilityInformation prefix;

	public AddressPrefixBasedORFEntry(ORFAction action, ORFMatch match) {
		super(action, match);
	}

	public AddressPrefixBasedORFEntry(ORFAction action, ORFMatch match, int sequence, int minLength, int maxLength, NetworkLayerReachabilityInformation prefix) {
		super(action, match);
		
		this.sequence = sequence;
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.prefix = prefix;
	}

	@Override
	public ORFType getORFType() {
		return ORFType.ADDRESS_PREFIX_BASED;
	}

	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the minLength
	 */
	public int getMinLength() {
		return minLength;
	}

	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	/**
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return the prefix
	 */
	public NetworkLayerReachabilityInformation getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(NetworkLayerReachabilityInformation prefix) {
		this.prefix = prefix;
	}


}
