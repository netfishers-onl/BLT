package onl.netfishers.blt.bgp.net;

public enum PathSegmentType {
	AS_SET,             // unordered set of ASes a route in the UPDATE message has traversed
	AS_SEQUENCE,        // ordered set of ASes a route in the UPDATE message has traversed
	AS_CONFED_SET,      // unordered set of ASes in a confederation a route in the UPDATE message has traversed
	AS_CONFED_SEQUENCE, // ordered set of ASes in a confederation a route in the UPDATE message has traversed
}
