package org.blt.bgp.netty.fsm;

public class FireDelayOpenTimerExpired extends FireEventTimeJob {
	public FireDelayOpenTimerExpired() {
		super(FSMEvent.delayOpenTimerExpires());
	}
}
