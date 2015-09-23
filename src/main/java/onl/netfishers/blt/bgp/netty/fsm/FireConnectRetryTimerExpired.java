/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
package onl.netfishers.blt.bgp.netty.fsm;

public class FireConnectRetryTimerExpired extends FireEventTimeJob {
	public FireConnectRetryTimerExpired() {
		super(FSMEvent.connectRetryTimerExpires());
	}
}
