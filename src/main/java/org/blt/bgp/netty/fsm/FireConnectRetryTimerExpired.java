package org.blt.bgp.netty.fsm;

public class FireConnectRetryTimerExpired extends FireEventTimeJob {
	public FireConnectRetryTimerExpired() {
		super(FSMEvent.connectRetryTimerExpires());
	}
}
