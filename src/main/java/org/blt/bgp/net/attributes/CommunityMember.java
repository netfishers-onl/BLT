package org.blt.bgp.net.attributes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CommunityMember implements Comparable<CommunityMember> {
	private int asNumber;
	private int memberFlags;
	
	public CommunityMember() {}
	
	public CommunityMember(int asNumber, int memberFlags) {
		this.asNumber = asNumber;
		this.memberFlags = memberFlags;
	}
	
	/**
	 * @return the asNumber
	 */
	public int getAsNumber() {
		return asNumber;
	}
	/**
	 * @param asNumber the asNumber to set
	 */
	public void setAsNumber(int asNumber) {
		this.asNumber = asNumber;
	}
	/**
	 * @return the memberFlags
	 */
	public int getMemberFlags() {
		return memberFlags;
	}
	/**
	 * @param memberFlags the memberFlags to set
	 */
	public void setMemberFlags(int memberFlags) {
		this.memberFlags = memberFlags;
	}
	
	@Override
	public int compareTo(CommunityMember o) {
		return (new CompareToBuilder())
			.append(getAsNumber(), o.getAsNumber())
			.append(getMemberFlags(), o.getMemberFlags())
			.toComparison();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
			.append(getAsNumber())
			.append(getMemberFlags())
			.toHashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof CommunityMember))
			return false;
		
		CommunityMember o = (CommunityMember)obj;
		
		return (new EqualsBuilder())
			.append(getAsNumber(), o.getAsNumber())
			.append(getMemberFlags(), o.getMemberFlags())
			.isEquals();
	}
	
	public String toString() {
		return (new ToStringBuilder(this))
				.append("asNumber", asNumber)
				.append("memberFlags", memberFlags)
				.toString();
	}
}
