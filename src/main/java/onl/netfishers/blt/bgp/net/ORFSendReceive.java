package onl.netfishers.blt.bgp.net;

import org.apache.commons.lang3.StringUtils;

public enum ORFSendReceive {
	RECEIVE,
	SEND,
	BOTH;
	
	public int toCode() {
		switch(this) {
		case RECEIVE:
			return 1;
		case SEND:
			return 2;
		case BOTH:
			return 3;
		default:
			throw new IllegalArgumentException("unknown Send/Receive type " + this);
		}
	}
	
	public static ORFSendReceive fromCode(int code) {
		switch(code) {
		case 1:
			return RECEIVE;
		case 2:
			return SEND;
		case 3:
			return BOTH;
		default:
			throw new IllegalArgumentException("unknown Send/Receive type code " + code);
		}
	}
	
	public static ORFSendReceive fromString(String value) {
		if(StringUtils.equalsIgnoreCase("receive", value)) {
			return RECEIVE;
		} else if(StringUtils.equalsIgnoreCase("send", value)) {
			return SEND;
		} else if(StringUtils.equalsIgnoreCase("both", value)) {
			return BOTH;
		} else
			throw new IllegalArgumentException("unknown Send/Receive type: " + value);			
	}
}
