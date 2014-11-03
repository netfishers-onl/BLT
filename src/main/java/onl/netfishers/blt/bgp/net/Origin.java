package onl.netfishers.blt.bgp.net;

import org.apache.commons.lang3.StringUtils;

/**
 * Discrete origin types as specified in RFC 4271
 * 
 * @author rainer
 *
 */
public enum Origin {
	
	/** NLRI is interior to the originating AS (RFC 4271) */
	IGP,

	/** NLRI learned via EGP protocol (RFC 4271, RFC 904) */
	EGP,
	
	/** NLRI learned by some other means (RFC 4271)*/
	INCOMPLETE;
	
	public static Origin fromString(String value) {
		if(StringUtils.equalsIgnoreCase("igp", value))
			return IGP;
		else if(StringUtils.equalsIgnoreCase("egp", value))
			return EGP;
		if(StringUtils.equalsIgnoreCase("incomplete", value))
			return INCOMPLETE;
		else 
			throw new IllegalArgumentException("Illegal Origin type: " + value);
	}
}
