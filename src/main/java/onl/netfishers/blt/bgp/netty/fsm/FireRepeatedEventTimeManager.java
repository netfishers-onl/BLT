/**
 *  Copyright 2012 Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 * File: onl.netfishers.blt.bgp.netty.fsm.FireEventTimeManager.java 
 */
package onl.netfishers.blt.bgp.netty.fsm;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import onl.netfishers.blt.bgp.BgpService;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

/**
 * @author Rainer Bieniek (Rainer.Bieniek@web.de)
 *
 */
class FireRepeatedEventTimeManager<T extends FireEventTimeJob> {

	private Scheduler scheduler;
	private JobDetail jobDetail;
	private TriggerKey triggerKey;
	private JobKey jobKey;

    public FireRepeatedEventTimeManager () {
        scheduler = (Scheduler)BgpService.getInstance(Scheduler.class.getName());
    }

	void createJobDetail(Class<T> jobClass, InternalFSM fsm, Map<String, Object> additionalJobData) throws SchedulerException {
		JobDataMap map = new JobDataMap();
		
		map.put(FireEventTimeJob.FSM_KEY, fsm);

		if(additionalJobData != null)  {
			for(Entry<String, Object> entry : additionalJobData.entrySet())
				map.put(entry.getKey(), entry.getValue());
		}

		jobKey = new JobKey(UUID.randomUUID().toString());
		jobDetail = JobBuilder.newJob(jobClass).usingJobData(map).withIdentity(jobKey).build();		
	}
	
	void createJobDetail(Class<T> jobClass, InternalFSM fsm) throws SchedulerException {
		createJobDetail(jobClass, fsm, null);
	}
	
	void shutdown() throws SchedulerException {
		cancelJob();
	};
	
	void startRepeatedJob(int repeatInterval) throws SchedulerException {
		if(isJobScheduled())
			cancelJob();

		triggerKey = TriggerKey.triggerKey(UUID.randomUUID().toString());
		
		if(scheduler.checkExists(jobKey)) {
			scheduler.scheduleJob(TriggerBuilder.newTrigger()
			.withIdentity(triggerKey)
			.forJob(jobKey)
			.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(repeatInterval))
			.startAt(new Date(System.currentTimeMillis() + repeatInterval*1000L))
			.build());			
		} else {
			scheduler.scheduleJob(jobDetail, TriggerBuilder.newTrigger()
			.withIdentity(triggerKey)
			.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(repeatInterval))
			.startAt(new Date(System.currentTimeMillis() + repeatInterval*1000L))
			.build());
		}
	}
	
	boolean isJobScheduled() throws SchedulerException {
		if(triggerKey == null)
			return false;
		return scheduler.checkExists(triggerKey);
	}

	public Date getNextFireWhen() throws SchedulerException {
		if(!isJobScheduled())
			return null;
		
		return scheduler.getTrigger(triggerKey).getFireTimeAfter(new Date(System.currentTimeMillis()));
	}
	
	void cancelJob() throws SchedulerException {
		if(triggerKey != null) {
			scheduler.unscheduleJob(triggerKey);
			triggerKey = null;
		}
	}	
}
