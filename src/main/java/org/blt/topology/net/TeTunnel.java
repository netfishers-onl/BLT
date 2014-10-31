/*package org.blt.topology.net;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlDiscriminatorValue;

@XmlRootElement
@XmlDiscriminatorValue("TE")
@XmlAccessorType(XmlAccessType.NONE)
public abstract class TeTunnel extends RouterInterface {

	public static enum RouterRole {
		HEAD,
		MID,
		TAIL
	}

	protected RouterRole routerRole;
	protected boolean operUp;
	protected long bandwith;
	protected int setupPriority;
	protected int holdPriority;

	protected float signalledBandwidth;

	protected boolean autobwEnabled;
	protected float autobwMin;
	protected float autobwMax;

	protected Ipv4Subnet head;

	public TeTunnel() {

	}





	@XmlRootElement
	@XmlAccessorType(XmlAccessType.NONE)
	public static class TeDestination {
		private Ipv4Subnet destination;
		private Map<Integer, TePath> paths = new HashMap<Integer, TePath>();

		@XmlElement
		public Ipv4Subnet getDestination() {
			return destination;
		}
		public void setDestination(Ipv4Subnet destination) {
			this.destination = destination;
		}

		@XmlElement
		public Map<Integer, TePath> getPaths() {
			return paths;
		}
		public void setPaths(Map<Integer, TePath> paths) {
			this.paths = paths;
		}
		public void addPath(int index, TePath path) {
			this.paths.put(index, path);
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((destination == null) ? 0 : destination.hashCode());
			result = prime * result + ((paths == null) ? 0 : paths.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TeDestination other = (TeDestination) obj;
			if (destination == null) {
				if (other.destination != null)
					return false;
			}
			else if (!destination.equals(other.destination))
				return false;
			if (paths == null) {
				if (other.paths != null)
					return false;
			}
			else if (!paths.equals(other.paths))
				return false;
			return true;
		}
		public TeDestination() {

		}
		public TeDestination(Ipv4Subnet destination, Map<Integer, TePath> paths) {
			super();
			this.destination = destination;
			this.paths = paths;
		}
		@Override
		public String toString() {
			return String.format("TeDestination to %s, via {%s}",
					destination, paths);
		}

	}

	@XmlElement
	public RouterRole getRouterRole() {
		return routerRole;
	}

	public void setRouterRole(RouterRole routerRole) {
		this.routerRole = routerRole;
	}

	@XmlElement
	public boolean isOperUp() {
		return operUp;
	}

	public void setOperUp(boolean operUp) {
		this.operUp = operUp;
	}

	@XmlElement
	public long getBandwith() {
		return bandwith;
	}

	public void setBandwith(long bandwith) {
		this.bandwith = bandwith;
	}

	@XmlElement
	public int getSetupPriority() {
		return setupPriority;
	}

	public void setSetupPriority(int setupPriority) {
		this.setupPriority = setupPriority;
	}

	@XmlElement
	public int getHoldPriority() {
		return holdPriority;
	}

	public void setHoldPriority(int holdPriority) {
		this.holdPriority = holdPriority;
	}

	@XmlElement
	public float getSignalledBandwidth() {
		return signalledBandwidth;
	}

	public void setSignalledBandwidth(float signalledBandwidth) {
		this.signalledBandwidth = signalledBandwidth;
	}

	@XmlElement
	public boolean isAutobwEnabled() {
		return autobwEnabled;
	}

	public void setAutobwEnabled(boolean autobwEnabled) {
		this.autobwEnabled = autobwEnabled;
	}

	@XmlElement
	public float getAutobwMin() {
		return autobwMin;
	}

	public void setAutobwMin(float autobwMin) {
		this.autobwMin = autobwMin;
	}

	@XmlElement
	public float getAutobwMax() {
		return autobwMax;
	}

	public void setAutobwMax(float autobwMax) {
		this.autobwMax = autobwMax;
	}

	@XmlElement
	public Ipv4Subnet getHead() {
		return head;
	}

	public void setHead(Ipv4Subnet head) {
		this.head = head;
	}



}
*/