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
 * File: onl.netfishers.blt.bgp.netty.protocol.update.CommunitiesPathAttribute.java 
 */
package onl.netfishers.blt.bgp.net.attributes;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
public class CommunityPathAttribute extends PathAttribute {

	private int community;
	private List<CommunityMember> members = new LinkedList<CommunityMember>();

	public CommunityPathAttribute() {
		super(Category.OPTIONAL_TRANSITIVE);
	}

	public CommunityPathAttribute(int community) {
		super(Category.OPTIONAL_TRANSITIVE);
		
		this.community = community;
	}

	public CommunityPathAttribute(int community, List<CommunityMember> members) {
		this(community);
		
		if(members != null)
			this.members = new LinkedList<CommunityMember>(members);
	}
	/**
	 * @return the community
	 */
	public int getCommunity() {
		return community;
	}

	/**
	 * @param community the community to set
	 */
	public void setCommunity(int community) {
		this.community = community;
	}

	/**
	 * @return the members
	 */
	public List<CommunityMember> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(List<CommunityMember> members) {
		if(members != null)
			this.members = members;
		else
			this.members = new LinkedList<CommunityMember>();
	}

	@Override
	protected PathAttributeType internalType() {
		return PathAttributeType.COMMUNITY;
	}

	@Override
	protected boolean subclassEquals(PathAttribute obj) {
		CommunityPathAttribute o = (CommunityPathAttribute)obj;
		
		EqualsBuilder builder = (new EqualsBuilder())
			.append(getCommunity(), o.getCommunity())
			.append(getMembers().size(), o.getMembers().size());
		
		if(builder.isEquals()) {
			Iterator<CommunityMember> lit = getMembers().iterator();
			Iterator<CommunityMember> rit = o.getMembers().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.isEquals();
	}

	@Override
	protected int subclassHashCode() {
		HashCodeBuilder builder = (new HashCodeBuilder())
				.append(getCommunity());
		Iterator<CommunityMember> it = getMembers().iterator();
		
		while(it.hasNext())
			builder.append(it.next());
		
		return builder.toHashCode();
	}

	@Override
	protected int subclassCompareTo(PathAttribute obj) {
		CommunityPathAttribute o = (CommunityPathAttribute)obj;
		CompareToBuilder builder = (new CompareToBuilder())
			.append(getCommunity(), o.getCommunity())
			.append(getMembers().size(), o.getMembers().size());
		
		if(builder.toComparison() == 0) {
			Iterator<CommunityMember> lit = getMembers().iterator();
			Iterator<CommunityMember> rit = o.getMembers().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.toComparison();
	}

	@Override
	protected ToStringBuilder subclassToString() {
		ToStringBuilder builder = new ToStringBuilder(this)
			.append("community", community);
		
		for(CommunityMember c : members)
			builder.append("member", c);
		
		return builder;
	}

}
