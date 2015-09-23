/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
package onl.netfishers.blt.bgp.netty.fsm;

public class FireHoldTimerExpired extends FireEventTimeJob {
	public FireHoldTimerExpired() {
		super(FSMEvent.holdTimerExpires());
	}
}
