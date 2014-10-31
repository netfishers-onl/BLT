package org.blt.bgp.net;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PathSegment implements Comparable<PathSegment> {
	private ASType asType;
	private List<Integer> ases = new LinkedList<Integer>(); 
	private PathSegmentType pathSegmentType;
	
	public PathSegment() {}
	
	public PathSegment(ASType asType) {
		this.asType = asType;
	}
	
	public PathSegment(ASType asType, PathSegmentType pathSegmentType, int[] asArray) {
		this(asType);

		this.pathSegmentType = pathSegmentType;
		
		if(asArray != null)
			for(int as : asArray)
				ases.add(as);
	}

	/**
	 * @return the asType
	 */
	public ASType getAsType() {
		return asType;
	}

	/**
	 * @return the ases
	 */
	public List<Integer> getAses() {
		return ases;
	}

	/**
	 * @param ases the ases to set
	 */
	public void setAses(List<Integer> ases) {
		if(ases != null)
			this.ases = ases;
		else
			this.ases = new LinkedList<Integer>();
	}

	/**
	 * @return the type
	 */
	public PathSegmentType getPathSegmentType() {
		return pathSegmentType;
	}

	/**
	 * @param type the type to set
	 */
	public void setPathSegmentType(PathSegmentType type) {
		this.pathSegmentType = type;
	}

	@Override
	public int compareTo(PathSegment o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getAsType(), o.getAsType())
				.append(getPathSegmentType(), o.getPathSegmentType())
				.append(getAses().size(), o.getAses().size());
		
		if(builder.toComparison() == 0) {
			Iterator<Integer> lit = getAses().iterator();
			Iterator<Integer> rit = o.getAses().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.toComparison();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder())
				.append(getAsType())
				.append(getPathSegmentType());
		Iterator<Integer> it = getAses().iterator();
		
		while(it.hasNext())
			builder.append(it.next());
		
		return builder.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PathSegment))
			return false;
		
		PathSegment o = (PathSegment)obj;
		
		EqualsBuilder builder = (new EqualsBuilder())
				.append(getAsType(), o.getAsType())
				.append(getPathSegmentType(), o.getPathSegmentType())
				.append(getAses().size(), o.getAses().size());
		
		if(builder.isEquals()) {
			Iterator<Integer> lit = getAses().iterator();
			Iterator<Integer> rit = o.getAses().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.isEquals();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this)
			.append("asType", asType)
			.append("pathSegmentType", pathSegmentType);
		
		for(int as : ases)
			builder.append("as", as);
		
		return builder.toString();
	}
}
