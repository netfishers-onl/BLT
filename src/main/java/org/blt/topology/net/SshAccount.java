/*package org.blt.topology.net;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class SshAccount implements Comparable<SshAccount> {

	private static long idGenerator = 0;

	private long id = 0;
	private Ipv4Subnet subnet;
	private String username;
	private String password;

	public SshAccount(Ipv4Subnet subnet, String username, String password) {
		this.subnet = subnet;
		this.username = username;
		this.password = password;
	}

	protected SshAccount() {

	}

	@XmlAttribute
	public long getId() {
		if (id == 0) {
			id = ++idGenerator;
		}
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
		if (id > idGenerator) {
			idGenerator = id;
		}
	}

	@XmlElement
	public Ipv4Subnet getSubnet() {
		return subnet;
	}

	public void setSubnet(Ipv4Subnet subnet) {
		this.subnet = subnet;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subnet == null) ? 0 : subnet.hashCode());
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
		SshAccount other = (SshAccount) obj;
		if (subnet == null) {
			if (other.subnet != null)
				return false;
		}
		else if (!subnet.equals(other.subnet))
			return false;
		return true;
	}

	@Override
	public int compareTo(SshAccount o) {
		if (o == null) {
			return 1;
		}
		return (this.getSubnet().compareTo(o.getSubnet()));
	}

}
*/