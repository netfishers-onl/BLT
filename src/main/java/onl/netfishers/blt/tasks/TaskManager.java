package onl.netfishers.blt.tasks;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import onl.netfishers.blt.Blt;

import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

public class TaskManager {

	private static Logger logger = LoggerFactory
			.getLogger(TaskManager.class);

	/** The Quartz factory. */
	private static SchedulerFactory factory;

	/** The Quartz scheduler. */
	private static Scheduler scheduler;
	
	private static int MAX_TASKS_IN_MEMORY = 200;
	
	private static Map<Long, Task> tasks = new ConcurrentHashMap<Long, Task>(MAX_TASKS_IN_MEMORY);

	/**
	 * Initializes the task manager.
	 */
	public static void init() {
		Properties params = new Properties();
		params.put(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, "org.quartz.simpl.SimpleThreadPool");
		params.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
		params.setProperty(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK, "true");
		params.put("org.quartz.threadPool.threadCount", Blt.getConfig("blt.tasks.threadcount", "10"));
		try {
			factory = new StdSchedulerFactory(params);
			scheduler = factory.getScheduler();
			scheduler.start();
		}
		catch (Exception e) {
			logger.error(MarkerFactory.getMarker("FATAL"), "Unable to instantiate the Task Manager", e);
			throw new RuntimeException("Unable to instantiate the Task Manager.", e);
		}
	}

	public static Scheduler getScheduler() {
		return scheduler;
	}
	
	public static void addTask(Task task) {
		tasks.remove(task.getId() - MAX_TASKS_IN_MEMORY);
		tasks.put(task.getId(), task);
	}
	
	public static Task getTaskById(long id) {
		return tasks.get(id);
	}
	
	public static Collection<Task> getTasks() {
		return tasks.values();
	}

}
