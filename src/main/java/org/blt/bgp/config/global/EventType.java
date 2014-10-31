package org.blt.bgp.config.global;

public enum EventType {
	CONFIGURATION_ADDED,
	CONFIGURATION_CHANGED,
	CONFIGURATION_REMOVED;
	
	public static EventType determineEvent(Object former, Object current) {
		if(former == null && current != null)
			return CONFIGURATION_ADDED;
		else if(former != null && current == null)
			return CONFIGURATION_REMOVED;
		else if(former != null && current != null && !former.equals(current))
			return CONFIGURATION_CHANGED;
		else
			return null;
	}
}
