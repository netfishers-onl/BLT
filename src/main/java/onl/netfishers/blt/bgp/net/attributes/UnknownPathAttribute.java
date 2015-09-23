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
public class UnknownPathAttribute extends PathAttribute {

	private int typeCode;
	private byte[] value;
	
	public UnknownPathAttribute(int typeCode, byte[] value) {
		super(Category.OPTIONAL_TRANSITIVE);
		
		this.typeCode = typeCode;
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public byte[] getValue() {
		return value;
	}

	/**
	 * @return the typeCode
	 */
	public int getTypeCode() {
		return typeCode;
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.UNKNOWN;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		UnknownPathAttribute o =(UnknownPathAttribute)obj;
		
		return (new EqualsBuilder())
				.append(getTypeCode(), o.getTypeCode())
				.append(getValue(), o.getValue())
				.isEquals();
	}

	@Override
	protected int subclassHashCode() {
		return (new HashCodeBuilder())
			.append(getTypeCode())
			.append(getValue())
			.toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		UnknownPathAttribute o =(UnknownPathAttribute)obj;
		
		return (new CompareToBuilder())
				.append(getTypeCode(), o.getTypeCode())
				.append(getValue(), o.getValue())
				.toComparison();
	}

	@Override
	protected ToStringBuilder subclassToString() {
		return (new ToStringBuilder(this))
				.append("typeCode", typeCode)
				.append("value", value);
	}

}
