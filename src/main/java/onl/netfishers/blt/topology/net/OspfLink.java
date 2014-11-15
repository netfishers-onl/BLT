/**
 * 
 */
package onl.netfishers.blt.topology.net;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author amoretti
 *
 */
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.NONE)
public class OspfLink extends Link {
	private Ipv4Subnet localAddress;
	private Ipv4Subnet remoteAddress;
		
	public OspfLink() {

	}
	
	@XmlElement
	public Ipv4Subnet getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(Ipv4Subnet localAddress) {
		this.localAddress = localAddress;
	}

	@XmlElement
	public Ipv4Subnet getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(Ipv4Subnet remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((localAddress == null) ? 0 : localAddress.hashCode());
		result = prime * result
				+ ((remoteAddress == null) ? 0 : remoteAddress.hashCode());
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
		OspfLink other = (OspfLink) obj;
		if (localAddress == null) {
			if (other.localAddress != null)
				return false;
		}
		else if (!localAddress.equals(other.localAddress))
			return false;
		/*if (localRouter == null) {
			if (other.localRouter != null)
				return false;
		}
		else if (!localRouter.equals(other.localRouter))
			return false;
		*/
		if (remoteAddress == null) {
			if (other.remoteAddress != null)
				return false;
		}
		else if (!remoteAddress.equals(other.remoteAddress))
			return false;
		/*if (remoteRouter == null) {
			if (other.remoteRouter != null)
				return false;
		}
		else if (!remoteRouter.equals(other.remoteRouter))
			return false;
		*/
		return true;
	}

}
