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

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OriginatorIDPathAttribute extends PathAttribute {

	public OriginatorIDPathAttribute() {
		super(Category.OPTIONAL_NON_TRANSITIVE);
	}

	public OriginatorIDPathAttribute(int originatorID) {
		super(Category.OPTIONAL_NON_TRANSITIVE);
		
		setOriginatorID(originatorID);
	}

	private int originatorID;
	
	/**
	 * @return the nextHop
	 */
	public int getOriginatorID() {
		return originatorID;
	}

	/**
	 * 
	 * @param nextHop
	 */
	public void setOriginatorID(int nextHop) {
		this.originatorID = nextHop;
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.ORIGINATOR_ID;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		OriginatorIDPathAttribute o = (OriginatorIDPathAttribute)obj;
		
		return (new EqualsBuilder()).append(getOriginatorID(), o.getOriginatorID()).isEquals();
	}

	@Override
	protected int subclassHashCode() {
		return (new HashCodeBuilder()).append(getOriginatorID()).toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		OriginatorIDPathAttribute o = (OriginatorIDPathAttribute)obj;
		
		return (new CompareToBuilder()).append(getOriginatorID(), o.getOriginatorID()).toComparison();
	}

	@Override
	protected ToStringBuilder subclassToString() {
		return (new ToStringBuilder(this))
				.append("originatorID", originatorID);
	}

}
