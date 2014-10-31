package org.blt.bgp.net;

import java.net.InetAddress;
import java.util.Comparator;

public final class InetAddressComparator implements Comparator<InetAddress> {
	public int compare(InetAddress l, InetAddress r) {
		byte[] la = l.getAddress();
		byte[] ra = r.getAddress();
		
		if(la.length < ra.length)
			return -1;
		else if(la.length > ra.length)
			return 1;
		else {
			for(int i=0; i<la.length; i++) {
				if(la[i] < ra[i])
					return -1;
				else if(la[i] > ra[i])
					return 1;
			}
			
			return 0;
		}
	}
}
