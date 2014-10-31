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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.blt.bgp.net.ASType;
import org.blt.bgp.net.ASTypeAware;
import org.blt.bgp.net.PathSegment;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class ASPathAttribute extends PathAttribute implements ASTypeAware {

	private ASType asType;
	private List<PathSegment> pathSegments = new LinkedList<PathSegment>(); 

	public ASPathAttribute(ASType asType) {
		super(Category.WELL_KNOWN_MANDATORY);
		
		this.asType = asType;
	}
	
	public ASPathAttribute(ASType asType, PathSegment[] segs) {
		this(asType);
		
		if(segs != null) {
			for(PathSegment seg : segs) {
				this.pathSegments.add(seg);
			}
		}
	}

	public ASPathAttribute(ASType asType, List<PathSegment> pathSegments) {
		this(asType);

		if(pathSegments != null)
			this.pathSegments = new LinkedList<PathSegment>(pathSegments);
	}

	/**
	 * @return the fourByteASNumber
	 */
	public boolean isFourByteASNumber() {
		return (this.asType == ASType.AS_NUMBER_4OCTETS);
	}

	/**
	 * @return the pathSegments
	 */
	public List<PathSegment> getPathSegments() {
		return pathSegments;
	}

	/**
	 * @param pathSegments the pathSegments to set
	 */
	public void setPathSegments(List<PathSegment> pathSegments) {
		if(pathSegments != null)
			this.pathSegments = pathSegments;
		else
			this.pathSegments = new LinkedList<PathSegment>();
	}

	/* (non-Javadoc)
	 * @see org.blt.bgp.netty.protocol.update.ASTypeAware#getAsType()
	 */
	@Override
	public ASType getAsType() {
		return asType;
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.AS_PATH;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		ASPathAttribute o = (ASPathAttribute)obj;

		EqualsBuilder builder = (new EqualsBuilder())
				.append(getAsType(), o.getAsType())
				.append(getPathSegments().size(), o.getPathSegments().size());
		
		if(builder.isEquals()) {
			Iterator<PathSegment> lit = getPathSegments().iterator();
			Iterator<PathSegment> rit = o.getPathSegments().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.isEquals();
	}

	@Override
	protected int subclassHashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder())
				.append(getAsType());
		Iterator<PathSegment> it = getPathSegments().iterator();
		
		while(it.hasNext())
			builder.append(it.next());
		
		return builder.toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		ASPathAttribute o = (ASPathAttribute)obj;

		CompareToBuilder builder = (new CompareToBuilder())
				.append(getAsType(), o.getAsType())
				.append(getPathSegments().size(), o.getPathSegments().size());
		
		if(builder.toComparison() == 0) {
			Iterator<PathSegment> lit = getPathSegments().iterator();
			Iterator<PathSegment> rit = o.getPathSegments().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.toComparison();
	}

	@Override
	protected ToStringBuilder subclassToString() {
		ToStringBuilder builder = new ToStringBuilder(this).append("asType", asType);
		
		for(PathSegment ps : this.pathSegments)
			builder.append("pathSegment", ps);
		
		return builder;
	}
}
