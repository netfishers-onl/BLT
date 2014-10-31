package org.blt.bgp.netty.fsm;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

class FireEventTimeJob implements Job {
	final static String FSM_KEY = "fsm";
	
	private FSMEvent event;
	
	protected FireEventTimeJob(FSMEvent event) {
		this.event = event;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		((InternalFSM)context.getMergedJobDataMap().get(FSM_KEY)).handleEvent(event);
	}
}
