/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
/**
 * 
 */
package onl.netfishers.blt.bgp.config.nodes.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import onl.netfishers.blt.bgp.config.nodes.AddressFamilyRoutingPeerConfiguration;
import onl.netfishers.blt.bgp.config.nodes.PathAttributeConfiguration;
import onl.netfishers.blt.bgp.config.nodes.RoutingFilterConfiguration;
import onl.netfishers.blt.bgp.net.AddressFamilyKey;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


/**
 * @author rainer
 *
 */
public class AddressFamilyRoutingPeerConfigurationImpl implements AddressFamilyRoutingPeerConfiguration {

	private AddressFamilyKey addressFamilyKey;
	private Set<RoutingFilterConfiguration> localRoutingFilters = new TreeSet<RoutingFilterConfiguration>();
	private Set<RoutingFilterConfiguration> remoteRoutingFilters = new TreeSet<RoutingFilterConfiguration>();
	private PathAttributeConfiguration localDefaultPathAttributes = new PathAttributeConfigurationImpl(); 
	private PathAttributeConfiguration remoteDefaultPathAttributes = new PathAttributeConfigurationImpl();
	
	AddressFamilyRoutingPeerConfigurationImpl() {}
	
	AddressFamilyRoutingPeerConfigurationImpl(AddressFamilyKey addressFamilyKey, 
			Collection<RoutingFilterConfiguration> localRoutingFilters,
			Collection<RoutingFilterConfiguration> remoteRoutingFilters, 
			PathAttributeConfiguration localDefaultPathAttributes,
			PathAttributeConfiguration remoteDefaultPathAttributes) {
		setAddressFamilyKey(addressFamilyKey);
		if(localRoutingFilters != null)
			this.localRoutingFilters.addAll(localRoutingFilters);
		if(remoteRoutingFilters != null)
			this.remoteRoutingFilters.addAll(remoteRoutingFilters);
		setLocalDefaultPathAttributes(localDefaultPathAttributes);
		setRemoteDefaultPathAttributes(remoteDefaultPathAttributes);
	}
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.config.nodes.AddressFamilyRoutingPeerConfiguration#getAddressFamilyKey()
	 */
	@Override
	public AddressFamilyKey getAddressFamilyKey() {
		return addressFamilyKey;
	}

	void setAddressFamilyKey(AddressFamilyKey addressFamilyKey) {
		this.addressFamilyKey = addressFamilyKey;
	}
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.config.nodes.AddressFamilyRoutingPeerConfiguration#getLocalDefaultPathAttributes()
	 */
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.config.nodes.AddressFamilyRoutingPeerConfiguration#getLocalRoutingFilters()
	 */
	@Override
	public Set<RoutingFilterConfiguration> getLocalRoutingFilters() {
		return localRoutingFilters;
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.config.nodes.AddressFamilyRoutingPeerConfiguration#getRemoteRoutingFilters()
	 */
	@Override
	public Set<RoutingFilterConfiguration> getRemoteRoutingFilters() {
		return remoteRoutingFilters;
	}


	@Override
	public PathAttributeConfiguration getLocalDefaultPathAttributes() {
		return localDefaultPathAttributes;
	}

	@Override
	public PathAttributeConfiguration getRemoteDefaultPathAttributes() {
		return remoteDefaultPathAttributes;
	}

	/**
	 * @param localRoutingFilters the localRoutingFilters to set
	 */
	void setLocalRoutingFilters(Set<RoutingFilterConfiguration> localRoutingFilters) {
		this.localRoutingFilters.clear();
		
		if(localRoutingFilters != null)
			this.localRoutingFilters.addAll(localRoutingFilters);
	}

	/**
	 * @param remoteRoutingFilters the remoteRoutingFilters to set
	 */
	void setRemoteRoutingFilters(Set<RoutingFilterConfiguration> remoteRoutingFilters) {
		this.remoteRoutingFilters.clear();
		
		if(remoteRoutingFilters != null)
			this.remoteRoutingFilters.addAll(remoteRoutingFilters);
	}

	/**
	 * @param localDefaultPathAttributes the localDefaultPathAttributes to set
	 */
	void setLocalDefaultPathAttributes(PathAttributeConfiguration localDefaultPathAttributes) {
		if(localDefaultPathAttributes != null)
			this.localDefaultPathAttributes = localDefaultPathAttributes;
		else
			this.localDefaultPathAttributes = new PathAttributeConfigurationImpl();
	}

	/**
	 * @param remoteDefaultPathAttributes the remoteDefaultPathAttributes to set
	 */
	void setRemoteDefaultPathAttributes(PathAttributeConfiguration remoteDefaultPathAttributes) {
		if(remoteDefaultPathAttributes != null)
			this.remoteDefaultPathAttributes = remoteDefaultPathAttributes;
		else
			this.remoteDefaultPathAttributes = new PathAttributeConfigurationImpl();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AddressFamilyRoutingPeerConfiguration o) {
		CompareToBuilder builder = (new CompareToBuilder())
				.append(getAddressFamilyKey(), o.getAddressFamilyKey())
				.append(getLocalDefaultPathAttributes(), o.getLocalDefaultPathAttributes())
				.append(getRemoteDefaultPathAttributes(), o.getRemoteDefaultPathAttributes())
				.append(getLocalRoutingFilters().size(), o.getLocalRoutingFilters().size())
				.append(getRemoteRoutingFilters().size(), o.getRemoteRoutingFilters().size());
		
		if(builder.toComparison() == 0) {
			Iterator<RoutingFilterConfiguration> lit = getLocalRoutingFilters().iterator();
			Iterator<RoutingFilterConfiguration> rit = o.getLocalRoutingFilters().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		if(builder.toComparison() == 0) {
			Iterator<RoutingFilterConfiguration> lit = getRemoteRoutingFilters().iterator();
			Iterator<RoutingFilterConfiguration> rit = o.getRemoteRoutingFilters().iterator();
			
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
				.append(getAddressFamilyKey())
				.append(getLocalDefaultPathAttributes())
				.append(getLocalRoutingFilters())
				.append(getRemoteDefaultPathAttributes())
				.append(getRemoteRoutingFilters());
		
		for(RoutingFilterConfiguration filter : getLocalRoutingFilters())
			builder.append(filter);
		for(RoutingFilterConfiguration filter : getRemoteRoutingFilters())
			builder.append(filter);
		
		return builder.toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AddressFamilyRoutingPeerConfiguration))
			return false;
		
		AddressFamilyRoutingPeerConfiguration o = (AddressFamilyRoutingPeerConfiguration)obj;
		
		EqualsBuilder builder = (new EqualsBuilder())
				.append(getAddressFamilyKey(), o.getAddressFamilyKey())
				.append(getLocalDefaultPathAttributes(), o.getLocalDefaultPathAttributes())
				.append(getRemoteDefaultPathAttributes(), o.getRemoteDefaultPathAttributes())
				.append(getLocalRoutingFilters(), o.getLocalRoutingFilters())
				.append(getRemoteRoutingFilters(), o.getRemoteRoutingFilters());
		
		if(builder.isEquals()) {
			Iterator<RoutingFilterConfiguration> lit = getLocalRoutingFilters().iterator();
			Iterator<RoutingFilterConfiguration> rit = o.getLocalRoutingFilters().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		if(builder.isEquals()) {
			Iterator<RoutingFilterConfiguration> lit = getRemoteRoutingFilters().iterator();
			Iterator<RoutingFilterConfiguration> rit = o.getRemoteRoutingFilters().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
		
		return builder.isEquals();
	}

}
