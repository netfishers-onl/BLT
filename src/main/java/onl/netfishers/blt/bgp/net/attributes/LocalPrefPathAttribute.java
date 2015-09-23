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
 * File: onl.netfishers.blt.bgp.netty.protocol.update.MultiExitDiscPathAttribute.java 
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
public class LocalPrefPathAttribute extends PathAttribute {

	public LocalPrefPathAttribute() {
		super(Category.WELL_KNOWN_DISCRETIONARY);
	}

	public LocalPrefPathAttribute(int localPreference) {
		super(Category.WELL_KNOWN_DISCRETIONARY);
		
		this.localPreference = localPreference;
	}

	private int localPreference;
	
	/**
	 * @return the discriminator
	 */
	public int getLocalPreference() {
		return localPreference;
	}

	/**
	 * @param discriminator the discriminator to set
	 */
	public void setLocalPreference(int discriminator) {
		this.localPreference = discriminator;
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.LOCAL_PREF;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		LocalPrefPathAttribute  o = (LocalPrefPathAttribute)obj;
		
		return (new EqualsBuilder()).append(getLocalPreference(), o.getLocalPreference()).isEquals();
	}

	@Override
	protected int subclassHashCode() {
		return (new HashCodeBuilder()).append(getLocalPreference()).toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		LocalPrefPathAttribute  o = (LocalPrefPathAttribute)obj;
		
		return (new CompareToBuilder()).append(getLocalPreference(), o.getLocalPreference()).toComparison();
	}

	@Override
	protected ToStringBuilder subclassToString() {
		return (new ToStringBuilder(this))
				.append("localPreference", localPreference);
	}

}
