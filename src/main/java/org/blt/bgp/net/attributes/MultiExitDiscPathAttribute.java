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
 * File: org.blt.bgp.netty.protocol.update.MultiExitDiscPathAttribute.java 
 */
package org.blt.bgp.net.attributes;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class MultiExitDiscPathAttribute extends PathAttribute {

	public MultiExitDiscPathAttribute() {
		super(Category.OPTIONAL_NON_TRANSITIVE);
	}

	public MultiExitDiscPathAttribute(int discriminator) {
		super(Category.OPTIONAL_NON_TRANSITIVE);
		
		this.discriminator = discriminator;
	}

	private int discriminator;
	
	/**
	 * @return the discriminator
	 */
	public int getDiscriminator() {
		return discriminator;
	}

	/**
	 * @param discriminator the discriminator to set
	 */
	public void setDiscriminator(int discriminator) {
		this.discriminator = discriminator;
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.MULTI_EXIT_DISC;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		MultiExitDiscPathAttribute o = (MultiExitDiscPathAttribute)obj;
		
		return (new EqualsBuilder()).append(getDiscriminator(), o.getDiscriminator()).isEquals();
	}

	@Override
	protected int subclassHashCode() {
		return (new HashCodeBuilder()).append(getDiscriminator()).toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		MultiExitDiscPathAttribute o = (MultiExitDiscPathAttribute)obj;
		
		return (new CompareToBuilder()).append(getDiscriminator(), o.getDiscriminator()).toComparison();
	}

	@Override
	protected ToStringBuilder subclassToString() {
		return (new ToStringBuilder(this))
				.append("discriminator", discriminator);
	}
}
