/**
 * 
 */
package onl.netfishers.blt.bgp.config.nodes.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import onl.netfishers.blt.bgp.config.nodes.PrefixRoutingFilterConfiguration;
import onl.netfishers.blt.bgp.config.nodes.RoutingFilterConfiguration;
import onl.netfishers.blt.bgp.net.NetworkLayerReachabilityInformation;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author rainer
 *
 */
public class PrefixRoutingFilterConfigurationImpl extends RoutingFilterConfigurationImpl implements	PrefixRoutingFilterConfiguration {

	private Set<NetworkLayerReachabilityInformation> filterPrefixes = new TreeSet<NetworkLayerReachabilityInformation>();
	
	public PrefixRoutingFilterConfigurationImpl() {}

	public PrefixRoutingFilterConfigurationImpl(String name, Collection<NetworkLayerReachabilityInformation> filterPrefixes) {
		super(name);
		
		this.filterPrefixes.addAll(filterPrefixes);
	}
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.config.nodes.PrefixRoutingFilterConfiguration#getFilterPrefixes()
	 */
	@Override
	public Set<NetworkLayerReachabilityInformation> getFilterPrefixes() {
		return filterPrefixes;
	}

	void setFilterPrefixes(Set<NetworkLayerReachabilityInformation> filterPrefixes) {
		this.filterPrefixes.clear();
		
		if(filterPrefixes != null)
			this.filterPrefixes.addAll(filterPrefixes);
	}
	
	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.config.nodes.impl.RoutingFilterConfigurationImpl#getType()
	 */
	@Override
	protected RoutingFilterType getType() {
		return RoutingFilterType.PREFIX;
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.config.nodes.impl.RoutingFilterConfigurationImpl#subclassCompareTo(org.apache.commons.lang3.builder.CompareToBuilder, onl.netfishers.blt.bgp.config.nodes.RoutingFilterConfiguration)
	 */
	@Override
	protected void subclassCompareTo(CompareToBuilder builder, RoutingFilterConfiguration o) {
		PrefixRoutingFilterConfiguration p = (PrefixRoutingFilterConfiguration)o; 
		
		builder.append(getFilterPrefixes().size(), p.getFilterPrefixes().size());
		
		if(builder.toComparison() == 0) {
			Iterator<NetworkLayerReachabilityInformation> lit = getFilterPrefixes().iterator();
			Iterator<NetworkLayerReachabilityInformation> rit = p.getFilterPrefixes().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.config.nodes.impl.RoutingFilterConfigurationImpl#subclassEquals(org.apache.commons.lang3.builder.EqualsBuilder, onl.netfishers.blt.bgp.config.nodes.RoutingFilterConfiguration)
	 */
	@Override
	protected void subclassEquals(EqualsBuilder builder, RoutingFilterConfiguration o) {
		PrefixRoutingFilterConfiguration p = (PrefixRoutingFilterConfiguration)o; 
		
		builder.append(getFilterPrefixes().size(), p.getFilterPrefixes().size());
		
		if(builder.isEquals()) {
			Iterator<NetworkLayerReachabilityInformation> lit = getFilterPrefixes().iterator();
			Iterator<NetworkLayerReachabilityInformation> rit = p.getFilterPrefixes().iterator();
			
			while(lit.hasNext())
				builder.append(lit.next(), rit.next());
		}
	}

	/* (non-Javadoc)
	 * @see onl.netfishers.blt.bgp.config.nodes.impl.RoutingFilterConfigurationImpl#subclassHashCode(org.apache.commons.lang3.builder.HashCodeBuilder)
	 */
	@Override
	protected void subclassHashCode(HashCodeBuilder builder) {
		for(NetworkLayerReachabilityInformation nlri : getFilterPrefixes())
			builder.append(nlri);
	}

}
