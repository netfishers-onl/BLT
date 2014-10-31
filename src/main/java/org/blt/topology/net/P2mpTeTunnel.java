/*package org.blt.topology.net;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

@XmlRootElement(name = "p2mpTeTunnel")
@XmlAccessorType(XmlAccessType.NONE)
@XmlDiscriminatorValue("P2MPTE")
public class P2mpTeTunnel extends TeTunnel {

	private Set<TeDestination> destinations = new HashSet<TeDestination>();

	public P2mpTeTunnel() {
	}

	@XmlElementWrapper(name = "destinations")
	@XmlElement(name = "destination")
	public Set<TeDestination> getDestinations() {
		return destinations;
	}

	public void setDestinations(Set<TeDestination> destinations) {
		this.destinations = destinations;
	}

	public void addDestination(TeDestination destination) {
		this.destinations.add(destination);
	}


}*/