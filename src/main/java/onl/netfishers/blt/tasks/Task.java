package onl.netfishers.blt.tasks;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement()
@XmlAccessorType(value = XmlAccessType.NONE)
public abstract class Task {

	static Logger logger = LoggerFactory
			.getLogger(Task.class);
	
	private static long idGenerator = 0;
	private String description;

	private long id = 0;
	protected int scheduleId = 0;
	private TaskStatus status = TaskStatus.NEW;
	private List<Long> relatedTasks = new CopyOnWriteArrayList<Long>();
	
	
	public static enum TaskStatus {
		NEW,
		WAITING,
		RUNNING,
		SUCCESS,
		FAILURE,
		CANCELLED
	}

	public static class TaskJob implements Job {

		@Override
		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			
			Task task = (Task) context.getJobDetail().getJobDataMap().get("task");
			

			/*if (!TopologyService.getService().isLicensed()) {
				logger.error("Cancelling task '{}' due to invalid license.", task);
				task.setStatus(TaskStatus.CANCELLED);
				return;
			}*/
			
			task.setStatus(TaskStatus.RUNNING);
			task.startTime = System.currentTimeMillis();
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e) {
			}
			task.execute();
			task.endTime = System.currentTimeMillis();
			if (task.getStatus() == TaskStatus.RUNNING) {
				task.setStatus(TaskStatus.FAILURE);
			}
		}

	}

	protected long creationTime = System.currentTimeMillis();
	protected long startTime;
	protected long endTime;

	public void schedule(int delay) throws SchedulerException {
		logger.info("Scheduling the task '' in {}ms.", delay);
		JobDetail job = JobBuilder.newJob(TaskJob.class)
				.withIdentity(String.format("Task_%d_%d",this.getId(), ++this.scheduleId), "taskgroup")
				.build();
		this.setStatus(TaskStatus.WAITING);
		job.getJobDataMap().put("task", this);
		Trigger trigger;
		if (delay > 0) {
			Calendar when = Calendar.getInstance();
			when.add(Calendar.MILLISECOND, delay);
			trigger = TriggerBuilder.newTrigger()
					.startAt(when.getTime())
					.build();

		}
		else {
			trigger = TriggerBuilder.newTrigger()
					.startNow()
					.build();
		}
		TaskManager.getScheduler().scheduleJob(job, trigger);
	}
	
	public Task(String description) {
		this.description = description;
		TaskManager.addTask(this);
	}
	
	@XmlAttribute
	public long getId() {
		if (id == 0) {
			id = ++idGenerator;
		}
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
		if (id > idGenerator) {
			idGenerator = id;
		}
	}
	
	@XmlElement
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	@XmlElementWrapper
	@XmlElement
	public List<Long> getRelatedTasks() {
		return relatedTasks;
	}
	public void setRelatedTasks(List<Long> relatedTasks) {
		this.relatedTasks = relatedTasks;
	}
	public void addRelatedTask(Long relatedTask) {
		this.relatedTasks.add(relatedTask);
	}
	
	
	@XmlElement
	public long getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	@XmlElement
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	@XmlElement
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public abstract void execute();

	public abstract String toString();

}
