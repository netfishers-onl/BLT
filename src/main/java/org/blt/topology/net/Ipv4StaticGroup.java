package org.blt.topology.net;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.blt.topology.net.Ipv4Subnet.MalformedIpv4SubnetException;

@XmlRootElement()
@XmlAccessorType(value = XmlAccessType.NONE)
public class Ipv4StaticGroup {

	private static long idGenerator = 0;

	private long id = 0;

	private Ipv4Subnet group;
	private Ipv4Subnet groupMask;
	private int groupCount;
	private int sourceCount;
	private Ipv4Subnet source;

	public Ipv4StaticGroup() {

	}



	public Ipv4StaticGroup(Ipv4Subnet group, Ipv4Subnet groupMask,
			int groupCount, int sourceCount, Ipv4Subnet source) {
		this.group = group;
		this.groupMask = groupMask;
		this.groupCount = groupCount;
		this.sourceCount = sourceCount;
		this.source = source;
	}

	public Ipv4StaticGroup(String group, String groupMask,
			int groupCount, int sourceCount, String source) throws MalformedIpv4SubnetException {
		this.group = new Ipv4Subnet(group);
		if (groupMask != null) {
			this.groupMask = new Ipv4Subnet(groupMask);
		}
		this.groupCount = groupCount;
		this.sourceCount = sourceCount;
		if (source != null) {
			this.source = new Ipv4Subnet(source);
		}
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
	public Ipv4Subnet getGroup() {
		return group;
	}
	public void setGroup(Ipv4Subnet group) {
		this.group = group;
	}
	@XmlElement
	public Ipv4Subnet getGroupMask() {
		return groupMask;
	}
	public void setGroupMask(Ipv4Subnet groupMask) {
		this.groupMask = groupMask;
	}
	@XmlElement
	public int getGroupCount() {
		return groupCount;
	}
	public void setGroupCount(int groupCount) {
		this.groupCount = groupCount;
	}
	@XmlElement
	public int getSourceCount() {
		return sourceCount;
	}
	public void setSourceCount(int sourceCount) {
		this.sourceCount = sourceCount;
	}

	@XmlElement
	public Ipv4Subnet getSource() {
		return source;
	}
	public void setSource(Ipv4Subnet source) {
		this.source = source;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + groupCount;
		result = prime * result + ((groupMask == null) ? 0 : groupMask.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + sourceCount;
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
		Ipv4StaticGroup other = (Ipv4StaticGroup) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		}
		else if (!group.equals(other.group))
			return false;
		if (groupCount != other.groupCount)
			return false;
		if (groupMask == null) {
			if (other.groupMask != null)
				return false;
		}
		else if (!groupMask.equals(other.groupMask))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		}
		else if (!source.equals(other.source))
			return false;
		if (sourceCount != other.sourceCount)
			return false;
		return true;
	}	

}
