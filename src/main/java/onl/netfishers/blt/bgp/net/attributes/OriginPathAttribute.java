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

import onl.netfishers.blt.bgp.net.Origin;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * ORIGIN (type code 1) BGPv4 path attribute
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class OriginPathAttribute extends PathAttribute {

	private Origin origin;
	
	public OriginPathAttribute() {
		super(Category.WELL_KNOWN_MANDATORY);
		
		origin = Origin.INCOMPLETE;
	}
	
	public OriginPathAttribute(Origin origin) {
		super(Category.WELL_KNOWN_MANDATORY);
		
		this.origin = origin;
	}
	
	/**
	 * @return the origin
	 */
	public Origin getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(Origin origin) {
		this.origin = origin;
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.ORIGIN;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		OriginPathAttribute o = (OriginPathAttribute)obj;
		
		return (new EqualsBuilder()).append(getOrigin(), o.getOrigin()).isEquals();
	}

	@Override
	protected int subclassHashCode() {
		return (new HashCodeBuilder()).append(getOrigin()).toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		OriginPathAttribute o = (OriginPathAttribute)obj;
		
		return (new CompareToBuilder()).append(getOrigin(), o.getOrigin()).toComparison();
	}

	@Override
	protected ToStringBuilder subclassToString() {
		return (new ToStringBuilder(this))
				.append("origin", origin);
	}

}
