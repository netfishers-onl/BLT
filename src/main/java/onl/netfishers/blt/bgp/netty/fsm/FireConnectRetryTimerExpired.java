package onl.netfishers.blt.bgp.netty.fsm;

public class FireConnectRetryTimerExpired extends FireEventTimeJob {
	public FireConnectRetryTimerExpired() {
		super(FSMEvent.connectRetryTimerExpires());
	}
}
