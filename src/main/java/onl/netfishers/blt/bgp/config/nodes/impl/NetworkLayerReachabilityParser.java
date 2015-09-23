/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
/**
 * 
 */
package onl.netfishers.blt.bgp.config.nodes.impl;

import onl.netfishers.blt.bgp.net.NetworkLayerReachabilityInformation;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrTokenizer;

/**
 * @author rainer
 *
 */
public class NetworkLayerReachabilityParser {

	NetworkLayerReachabilityInformation parseNlri(String rep) throws ConfigurationException {
		if(StringUtils.startsWith(rep, "ipv4:"))
			return parseIPv4Nlri(StringUtils.substring(rep, 5));
		else if(StringUtils.startsWith(rep, "ipv6:"))
			return parseIPv6MNlri(StringUtils.substring(rep, 5));
		else if(StringUtils.startsWith(rep, "binary:"))
			return parseBinaryNlri(StringUtils.substring(rep, 7));
		else
			throw new ConfigurationException("cannot parser NLRI: " + rep);
	}

	private NetworkLayerReachabilityInformation parseBinaryNlri(String rep) throws ConfigurationException {
		int idx = StringUtils.indexOf(rep, "/");
		int prefixLength;
		
		if(idx <= 0)
			throw new ConfigurationException("malformed IPv6 prefix");
		
		try {
			prefixLength = Integer.parseInt(StringUtils.substring(rep, idx+1));
		} catch(NumberFormatException e) {
			throw new ConfigurationException("Illiegal bits given", e);
		}
		if(prefixLength < 0)
			throw new ConfigurationException("Illegal bits given: " + prefixLength);
		
		int octetSize = NetworkLayerReachabilityInformation.calculateOctetsForPrefixLength(prefixLength);
		String prefixRep = StringUtils.lowerCase(StringUtils.substring(rep, 0, idx));
		
		if(StringUtils.length(prefixRep) != 2*octetSize)
			throw new ConfigurationException("Invalid prefix: " + prefixRep);
		
		byte[] prefix = new byte[octetSize];
		
		for(int i=0; i<octetSize; i++) {
			String part = StringUtils.substring(prefixRep, 2*i, 2*i+2);
			
			try {
				prefix[i] = (byte)Integer.parseInt(part, 16);
			} catch(NumberFormatException e) {
				throw new ConfigurationException("Invalid prefix: " + prefixRep + ", part: " + part);
			}
		}
		
		return new NetworkLayerReachabilityInformation(prefixLength, prefix);
	}

	private NetworkLayerReachabilityInformation parseIPv6MNlri(String rep) throws ConfigurationException {
		int idx = StringUtils.indexOf(rep, "/");
		int prefixLength;
		
		if(idx <= 0)
			throw new ConfigurationException("malformed IPv6 prefix");
		
		try {
			prefixLength = Integer.parseInt(StringUtils.substring(rep, idx+1));
		} catch(NumberFormatException e) {
			throw new ConfigurationException("Illiegal bits given", e);
		}
		if(prefixLength < 0 || prefixLength > 128)
			throw new ConfigurationException("Illegal bits given: " + prefixLength);

		String ipv6Addr = StringUtils.substring(rep, 0, idx);
		int doubleColons = StringUtils.countMatches(ipv6Addr, "::");

		if(doubleColons == 1) {
			// inflate address
			int colons = 7 - (StringUtils.countMatches(ipv6Addr, ":") - 1);
			
			if(colons <= 0)
				throw new ConfigurationException("malformed IPv6 address: " + ipv6Addr);
			
			StringBuilder replacement = new StringBuilder(":");
			
			replacement.append(StringUtils.repeat("0:", colons));
			
			ipv6Addr = StringUtils.replace(ipv6Addr, "::", replacement.toString());
			
			if(StringUtils.startsWith(ipv6Addr, ":"))
				ipv6Addr = "0" + ipv6Addr;
			if(StringUtils.endsWith(ipv6Addr, ":"))
				ipv6Addr = ipv6Addr + "0";
		} else if(doubleColons > 1) {
			throw new ConfigurationException("malformed IPv6 address: " + ipv6Addr);
		}
		
		StrTokenizer tokenizer = new StrTokenizer(ipv6Addr, ":");

		if(tokenizer.size() != 8)
			throw new ConfigurationException("Invalid IP address given: " + ipv6Addr);
		
		int[] ipParts = new int[8];
		
		for(int i=0; i<8; i++) {
			String token = tokenizer.next();
			
			if(token.length() > 4)
				throw new ConfigurationException("malformed IPv6 address: " + ipv6Addr);
			try {
				ipParts[i] = Integer.parseInt(token, 16);
			} catch(NumberFormatException e) {
				throw new ConfigurationException("Invalid IP address given: " + ipv6Addr, e);				
			}
			
			if(ipParts[i] < 0 || ipParts[i] > 65535)
				throw new ConfigurationException("Invalid part of IP address given: " + ipParts);
		}
		
		int octetSize = NetworkLayerReachabilityInformation.calculateOctetsForPrefixLength(prefixLength);
		
		byte[] prefix = new byte[octetSize];
		
		for(int i=0; i<octetSize; i++) {
			if(i % 2 == 0)
				prefix[i] = (byte)((ipParts[i/2] >> 8) & 0x0ff);
			else
				prefix[i] = (byte)(ipParts[i/2] & 0x0ff);
		}
		
		return new NetworkLayerReachabilityInformation(prefixLength, prefix);
	}

	/**
	 * Parse a prefix in PIv4 CIDR notation a.b.c.d/bits(0-32)
	 * 
	 * @param rep
	 * @return
	 * @throws ConfigurationException
	 */
	private NetworkLayerReachabilityInformation parseIPv4Nlri(String rep) throws ConfigurationException {
		int idx = StringUtils.indexOf(rep, "/");
		int prefixLength;
		
		if(idx <= 0)
			throw new ConfigurationException("malformed IPv4 prefix");
		
		try {
			prefixLength = Integer.parseInt(StringUtils.substring(rep, idx+1));
		} catch(NumberFormatException e) {
			throw new ConfigurationException("Illiegal bits given", e);
		}
		if(prefixLength < 0 || prefixLength > 32)
			throw new ConfigurationException("Illegal bits given: " + prefixLength);

		StrTokenizer tokenizer = new StrTokenizer(StringUtils.substring(rep, 0, idx), ".");
		
		if(tokenizer.size() != 4)
			throw new ConfigurationException("Invalid IP address given: " + StringUtils.substring(rep, 0, idx));
		
		int[] ipParts = new int[4];
		
		for(int i=0; i<4; i++) {
			try {
				ipParts[i] = Integer.parseInt(tokenizer.next());
			} catch(NumberFormatException e) {
				throw new ConfigurationException("Invalid IP address given: " + StringUtils.substring(rep, 0, idx), e);				
			}
			
			if(ipParts[i] < 0 || ipParts[i] > 255)
				throw new ConfigurationException("Invalid part of IP address given: " + ipParts);
		}
		
		int octetSize = NetworkLayerReachabilityInformation.calculateOctetsForPrefixLength(prefixLength);
		
		byte[] prefix = new byte[octetSize];
		
		for(int i=0; i<octetSize; i++) {
			prefix[i] = (byte)ipParts[i];
		}
		
		return new NetworkLayerReachabilityInformation(prefixLength, prefix);
	}

}
