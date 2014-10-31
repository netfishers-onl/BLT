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
package org.blt.bgp.net.attributes;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Superclass for all BGPv4 path attributes
 * 
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public abstract class PathAttribute implements Comparable<PathAttribute> {

	/**
	 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
	 *
	 */
	public enum Category {
		WELL_KNOWN_MANDATORY,
		WELL_KNOWN_DISCRETIONARY,
		OPTIONAL_TRANSITIVE,
		OPTIONAL_NON_TRANSITIVE,
	}

	private boolean optional;
	private boolean transitive;
	private boolean partial;
	private Category category;
	
	protected PathAttribute(Category category) {
		this.category = category;
		
		switch(category) {
		case OPTIONAL_NON_TRANSITIVE:
			setTransitive(false);
			setOptional(true);
			break;
		case OPTIONAL_TRANSITIVE:
			setTransitive(true);
			setOptional(true);
			break;
		case WELL_KNOWN_DISCRETIONARY:
			setTransitive(true);
			setOptional(false);
			break;
		case WELL_KNOWN_MANDATORY:
			setTransitive(true);
			setOptional(false);
			break;
		}
	}
	
	
	/**
	 * @return the partial
	 */
	public boolean isPartial() {
		return partial;
	}

	/**
	 * @param partial the partial to set
	 */
	public void setPartial(boolean partial) {
		this.partial = partial;
	}

	/**
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * @return the optional
	 */
	public boolean isWellKnown() {
		return !isOptional();
	}

	/**
	 * @param optional the optional to set
	 */
	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	/**
	 * @param wellKnown the well known to set
	 */
	protected void setWellKnown(boolean wellKnown) {
		setOptional(!wellKnown);
	}
	
	/**
	 * @return the transitive
	 */
	public boolean isTransitive() {
		return transitive;
	}

	/**
	 * @param transitive the transitive to set
	 */
	public void setTransitive(boolean transitive) {
		this.transitive = transitive;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	public String toString() {
		return subclassToString().append("internalType", internalType())
				.append("category", getCategory())
				.append("option", isOptional())
				.append("partial", isPartial())
				.append("transitive", isTransitive())
				.toString();
	}

	protected abstract ToStringBuilder subclassToString();
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PathAttribute o) {
		CompareToBuilder builder = new CompareToBuilder();
		
		builder.append(internalType(), o.internalType())
			.append(getCategory(), o.getCategory())
			.append(isOptional(), o.isOptional())
			.append(isPartial(), o.isPartial())
			.append(isTransitive(), o.isTransitive());
		
		if(internalType() == o.internalType())
			builder.appendSuper(subclassCompareTo(o));
		
		return builder.toComparison();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
			.append(internalType())
			.append(getCategory())
			.append(isOptional())
			.append(isPartial())
			.append(isTransitive())
			.appendSuper(subclassHashCode())
			.toHashCode();
	}

	public PathAttributeType getType() {
		return internalType();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PathAttribute))
			return false;
		
		PathAttribute o = (PathAttribute)obj;
		
		EqualsBuilder builder = new EqualsBuilder();
		
		builder.append(internalType(), o.internalType())
			.append(getCategory(), o.getCategory())
			.append(isOptional(), o.isOptional())
			.append(isPartial(), o.isPartial())
			.append(isTransitive(), o.isTransitive());
		
		if(internalType() == o.internalType())
			builder.appendSuper(subclassEquals(o));
		
		return builder.isEquals();
	}
	
	/**
	 * get the internal type of the path attribute
	 * 
	 * @return
	 */
	protected abstract PathAttributeType internalType();
	
	/**
	 * handles equals on subclass.
	 * 
	 * @param obj
	 * @return
	 */
	protected abstract boolean subclassEquals(PathAttribute obj);
	
	/**
	 * handle hashCode on subclass
	 * @return
	 */
	protected abstract int subclassHashCode();
	
	/**
	 * handle compareTo on subclass
	 * 
	 * @param o
	 * @return
	 */
	protected abstract int subclassCompareTo(PathAttribute o);
}
