/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
package onl.netfishers.blt.bgp.netty.fsm;

public class FireSendKeepalive extends FireEventTimeJob {
	public FireSendKeepalive() {
		super(FSMEvent.keepaliveTimerExpires());
	}
}
