/*
 * Copyright Sylvain Cadilhac 2013
 */
package onl.netfishers.blt.topology.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * An IPv4 address.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Ipv4Subnet implements Comparable<Ipv4Subnet> {

	public static class MalformedIpv4SubnetException extends Exception {
		private static final long serialVersionUID = -752597923167917044L;
		public MalformedIpv4SubnetException() {
			super();
		}
		public MalformedIpv4SubnetException(String message) {
			super(message);
		}
	}

	public static int dottedMaskToPrefixLength(String mask)
			throws MalformedIpv4SubnetException {
		int n = ipToInt(mask);
		return intAddressToPrefixLength(n);
	}

	public static String prefixLengthToDottedMask(int length) {
		return Ipv4Subnet.intToIP(Ipv4Subnet
				.prefixLengthToIntAddress(length));
	}
	
	public static int dottedWildcardToPrefixLength(String wildcard) throws MalformedIpv4SubnetException {
		int n = ipToInt(wildcard);
		return intWildcardToPrefixLength(n);
	}

	public static int prefixLengthToIntAddress(int length) {
		if (length == 0) {
			return 0;
		}
		return 0xFFFFFFFF << (32 - length);
	}
	
	public static int intAddressToPrefixLength(int address) {
		int n = ~address;
		return intWildcardToPrefixLength(n);
	}
	
	public static int intWildcardToPrefixLength(int wildcard) {
		if (wildcard == -1) {
			return 0;
		}
		return (int) Math.round(32 - (Math.log(wildcard + 1) / Math.log(2)));
	}

	public static int inetToIntAddress(Inet4Address address) {
		byte[] buffer = address.getAddress();
		return ByteBuffer.wrap(buffer).getInt();
	}

	public static Inet4Address intToInetAddress(int address)
			throws MalformedIpv4SubnetException {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(address);
		try {
			return (Inet4Address) Inet4Address.getByAddress(buffer.array());
		}
		catch (Exception e) {
			throw new MalformedIpv4SubnetException("Unable to create the InetAddress");
		}
	}

	public static String intToIP(int address) {
		int b1 = address & 0xFF;
		int b2 = (address >> 8) & 0xFF;
		int b3 = (address >> 16) & 0xFF;
		int b4 = (address >> 24) & 0xFF;
		return String.format("%d.%d.%d.%d", b4, b3, b2, b1);
	}

	public static int ipToInt(String address) throws MalformedIpv4SubnetException {
		try {
			InetAddress inetAddress = InetAddress.getByName(address);
			if (inetAddress instanceof Inet4Address) {
				return Ipv4Subnet.inetToIntAddress((Inet4Address) inetAddress);
			}
		}
		catch (Exception e) {
		}
		throw new MalformedIpv4SubnetException("Unable to parse the IPv4 address");
	}


	/** The address. */
	private int address = 0;

	/** The prefix length. */
	private int prefixLength = 0;


	protected Ipv4Subnet() {
	}

	public Ipv4Subnet(Inet4Address address, int prefixLength)
			throws MalformedIpv4SubnetException {
		this.address = inetToIntAddress(address);
		this.setPrefixLength(prefixLength);
	}

	public Ipv4Subnet(String input) throws MalformedIpv4SubnetException {
		String address = input;
		int prefixLength = 32;
		Pattern withPrefixPattern = Pattern.compile("^(?<ip>.*)/(?<len>[0-9]+)$");
		Matcher m = withPrefixPattern.matcher(address);
		if (m.find()) {
			address = m.group("ip");
			prefixLength = Integer.parseInt(m.group("len"));
		}
		this.address = ipToInt(address);
		this.setPrefixLength(prefixLength);
	}


	public Ipv4Subnet(int address, int prefixLength)
			throws MalformedIpv4SubnetException {
		this.address = address;
		this.setPrefixLength(prefixLength);
	}
	
	public Ipv4Subnet(byte[] address, int prefixLength)
			throws MalformedIpv4SubnetException {
		if (address == null || address.length != 4) {
			throw new MalformedIpv4SubnetException("Invalid address bytes");
		}
		this.address = (address[0] & 0xFF) << 24 |
					   (address[1] & 0xFF) << 16 |
					   (address[2] & 0xFF) << 8  |
					   (address[3] & 0xFF);
		this.setPrefixLength(prefixLength);
	}

	public Ipv4Subnet(String address, int prefixLength)
			throws MalformedIpv4SubnetException {
		this.address = Ipv4Subnet.ipToInt(address);
		this.setPrefixLength(prefixLength);
	}

	public Ipv4Subnet(String address, String mask)
			throws MalformedIpv4SubnetException {
		this.address = Ipv4Subnet.ipToInt(address);
		this.prefixLength = Ipv4Subnet.dottedMaskToPrefixLength(mask);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Ipv4Subnet)) {
			return false;
		}
		Ipv4Subnet other = (Ipv4Subnet) obj;
		if (address != other.address || prefixLength != other.prefixLength) {
			return false;
		}
		return true;
	}

	public int getAddress() {
		return address;
	}

	public Inet4Address getInetAddress() {
		try {
			return Ipv4Subnet.intToInetAddress(this.address);
		}
		catch (MalformedIpv4SubnetException e) {
		}
		return null;
	}

	public int getIntAddress() {
		return address;
	}


	@XmlElement
	public String getIp() {
		return intToIP(this.address);
	}

	public void setIp(String ip) throws MalformedIpv4SubnetException {
		this.address = ipToInt(ip);
	}

	public String getPrefix() {
		return intToIP(this.address) + "/" + prefixLength;
	}

	@XmlElement
	public int getPrefixLength() {
		return prefixLength;
	}
	
	public String getDottedMask() {
		return prefixLengthToDottedMask(prefixLength);
	}
	
	public String getWildcardDottedMask() {
		int n = prefixLengthToIntAddress(prefixLength);
		return Ipv4Subnet.intToIP(~n);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + address;
		result = prime * result + prefixLength;
		return result;
	}

	public boolean isBroadcast() {
		return this.address == 0xFFFFFFFF;
	}
	
	public boolean isDirectedBroadcast() {
		return this.address == this.getSubnetMax() && this.prefixLength < 31;
	}
	
	public boolean isNetwork() {
		return this.address == this.getSubnetMin() && this.prefixLength < 31;
	}

	public boolean isLoopback() {
		return ((this.address >> 24) & 255) == 127;
	}
	
	public boolean isZeroSlash8() {
		return ((this.address >> 24) & 255) == 0;
	}

	public boolean isMulticast() {
		return ((this.address >> 28) & 15) == 14;
	}

	public boolean isNormalUnicast() {
		return (!this.isBroadcast() && !this.isLoopback() && !this.isMulticast() && !this
				.isUndefined() && !this.isZeroSlash8() && !this.isDirectedBroadcast() &&
				!this.isNetwork());
	}

	public boolean isUndefined() {
		return (this.address == 0);
	}

	public void setAddress(int address) throws MalformedIpv4SubnetException {
		this.address = address;
	}

	public void setPrefixLength(int prefixLength) throws MalformedIpv4SubnetException {
		if (prefixLength < 0 || prefixLength > 32) {
			throw new MalformedIpv4SubnetException("Invalid prefix length");
		}
		this.prefixLength = prefixLength;
	}

	public String toString() {
		return this.getPrefix();
	}

	public int getSubnetMin() {
		return this.getIntAddress()
				& Ipv4Subnet.prefixLengthToIntAddress(this.getPrefixLength());
	}

	public int getSubnetMax() {
		return this.getSubnetMin()
				| ~Ipv4Subnet.prefixLengthToIntAddress(this.getPrefixLength());
	}

	public boolean contains(Ipv4Subnet otherAddress) {
		return (this.address >> (32 - this.prefixLength)) == (otherAddress
				.getIntAddress() >> (32 - this.prefixLength));
	}

	@Override
	public int compareTo(Ipv4Subnet o) {
		if (o == null) return 1;
		if (this.prefixLength < o.prefixLength) return 1;
		if (this.prefixLength > o.prefixLength) return -1;
		if (this.address == o.address) return 0;
		return (((this.address >= 0) && (o.address < 0)) ? -1 : 1) * ((this.address < o.address) ? -1 : 1);
	}


}
