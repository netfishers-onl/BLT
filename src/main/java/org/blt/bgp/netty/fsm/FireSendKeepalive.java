package org.blt.bgp.netty.fsm;

public class FireSendKeepalive extends FireEventTimeJob {
	public FireSendKeepalive() {
		super(FSMEvent.keepaliveTimerExpires());
	}
}
