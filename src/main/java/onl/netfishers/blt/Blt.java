/*******************************************************************************
 * Copyright (c) 2015 Netfishers - contact@netfishers.onl
 *******************************************************************************/
package onl.netfishers.blt;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import onl.netfishers.blt.bgp.BgpService;
import onl.netfishers.blt.rest.RestService;
import onl.netfishers.blt.tasks.TaskManager;
import onl.netfishers.blt.topology.TopologyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;

public class Blt {

	/** Blt version. */
	public static final String VERSION = "0.4.5";
	
	public static final String FEATURES = "Basic";

	/** The list of configuration files to look at, in sequence. */
	private static final String[] CONFIG_FILENAMES = new String[] {
		"blt.conf", "/etc/blt.conf" };

	/** The application configuration as retrieved from the configuration file. */
	private static Properties config;

	private static Logger logger = LoggerFactory.getLogger(Blt.class);


	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	public static Properties getConfig() {
		return config;
	}

	/**
	 * Gets the config.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the config
	 */
	public static String getConfig(String key, String defaultValue) {
		return config.getProperty(key, defaultValue);
	}

	/**
	 * Gets the config.
	 *
	 * @param key the key
	 * @return the config
	 */
	public static String getConfig(String key) {
		return config.getProperty(key);
	}

	/**
	 * Read the application configuration from the files.
	 *
	 * @return true, if successful
	 */
	private static boolean initConfig() {
		config = new Properties();
		for (String fileName : CONFIG_FILENAMES) {
			try {
				logger.trace("Trying to load the configuration file {}.", fileName);
				InputStream fileStream = new FileInputStream(fileName);
				config.load(fileStream);
				fileStream.close();
				break;
			}
			catch (Exception e) {
				logger.error("Unable to read the configuration file {}.", fileName);
			}
		}
		if (config.isEmpty()) {
			logger.error(MarkerFactory.getMarker("FATAL"), "No configuration file was found. Exiting.");
			return false;
		}
		return true;
	}

	/**
	 * Initializes the logging.
	 *
	 * @return true, if successful
	 */
	private static boolean initLogging() {
		// Redirect JUL to SLF4J
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		
		ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger)
				LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
		LoggerContext loggerContext = rootLogger.getLoggerContext();
		

		String logFile = Blt.getConfig("blt.log.file", "/var/log/blt/blt.log");
		String aaaLogFile = Blt.getConfig("blt.log.auditfile");
		String pfxLogFile = Blt.getConfig("blt.log.prefixActivityFile");
		String logLevelCfg = Blt.getConfig("blt.log.level", "WARN");
		String logCountCfg = Blt.getConfig("blt.log.count", "5");
		String logMaxSizeCfg = Blt.getConfig("blt.log.maxsize", "2");

		int logCount = 5;
		try {
			logCount = Integer.parseInt(logCountCfg);
		}
		catch (NumberFormatException e1) {
			logger.error("Invalid number of log files (blt.log.count config line). Using {}.", logCount);
		}
		
		int logMaxSize = 5;
		try {
			logMaxSize = Integer.parseInt(logMaxSizeCfg);
			if (logMaxSize < 1) {
				throw new NumberFormatException();
			}
		}
		catch (NumberFormatException e1) {
			logger.error("Invalid max size of log files (blt.log.maxsize config line). Using {}.", logMaxSize);
		}

		Level logLevel = Level.INFO;
		try {
			logLevel = Level.valueOf(logLevelCfg);
		}
		catch (Exception e) {
			logger.error("Invalid log level. Using {}.", logLevel);
		}
		
		OutputStreamAppender<ILoggingEvent> appender;

		if (logFile.equals("CONSOLE")) {
			ConsoleAppender<ILoggingEvent> cAppender = new ConsoleAppender<ILoggingEvent>();
			logger.info("Will go on logging to the console.");
			appender = cAppender;
		}
		else {
			logger.info("Switching to file logging, into {}, level {}, rotation using {} files of max {}MB.",
					logFile, logLevel, logCount, logMaxSize);

			loggerContext.reset();
			try {
				RollingFileAppender<ILoggingEvent> rfAppender = new RollingFileAppender<ILoggingEvent>();
				rfAppender.setContext(loggerContext);
				rfAppender.setFile(logFile);

				FixedWindowRollingPolicy fwRollingPolicy = new FixedWindowRollingPolicy();
				fwRollingPolicy.setContext(loggerContext);
				fwRollingPolicy.setFileNamePattern(logFile + ".%i.gz");
				fwRollingPolicy.setMinIndex(1);
				fwRollingPolicy.setMaxIndex(logCount);
				fwRollingPolicy.setParent(rfAppender);
				fwRollingPolicy.start();

				SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new 
						SizeBasedTriggeringPolicy<ILoggingEvent>();
				triggeringPolicy.setMaxFileSize(String.format("%dMB", logMaxSize));
				triggeringPolicy.start();

				rfAppender.setRollingPolicy(fwRollingPolicy);
				rfAppender.setTriggeringPolicy(triggeringPolicy);
				appender = rfAppender;
				
			}
			catch (Exception e) {
				logger.error(MarkerFactory.getMarker("FATAL"), "Unable to log into file {}. Exiting.", logFile, e);
				return false;
			}
		}
		


		rootLogger.setLevel(logLevel);
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
		encoder.setPattern("%d %-5level [%thread] %logger{0}: %msg%n");
		encoder.start();
		appender.setEncoder(encoder);
		appender.setContext(loggerContext);
		appender.start();
		rootLogger.addAppender(appender);
		
		Pattern logSetting = Pattern.compile("^blt\\.log\\.class\\.(?<class>.*)"); 
		Enumeration<?> propertyNames = Blt.config.propertyNames();
		while (propertyNames.hasMoreElements()) {
			String propertyName = (String) propertyNames.nextElement();
			Matcher matcher = logSetting.matcher(propertyName);
			if (matcher.find()) {
				String propertyValue = Blt.getConfig(propertyName);
				String className = matcher.group("class");
				ch.qos.logback.classic.Logger classLogger = (ch.qos.logback.classic.Logger)
						LoggerFactory.getLogger(className);
				try {
					Level classLevel = Level.valueOf(propertyValue);
					classLogger.setLevel(classLevel);
					logger.info("Assigning level {} to class {}.", classLevel, className);
				}
				catch (Exception e) {
					logger.error("Invalid log level for class {}.", className);
				}
			}
		}
		


		if (aaaLogFile != null) {
			try {
				ch.qos.logback.classic.Logger aaaLogger = (ch.qos.logback.classic.Logger)
						LoggerFactory.getLogger("AAA");
				LoggerContext aaaLoggerContext = aaaLogger.getLoggerContext();
				aaaLogger.setLevel(Level.ALL);
				RollingFileAppender<ILoggingEvent> aaaRfAppender = new RollingFileAppender<ILoggingEvent>();
				aaaRfAppender.setContext(aaaLoggerContext);
				aaaRfAppender.setFile(aaaLogFile);

				FixedWindowRollingPolicy fwRollingPolicy = new FixedWindowRollingPolicy();
				fwRollingPolicy.setContext(aaaLoggerContext);
				fwRollingPolicy.setFileNamePattern(aaaLogFile + ".%i.gz");
				fwRollingPolicy.setMinIndex(1);
				fwRollingPolicy.setMaxIndex(logCount);
				fwRollingPolicy.setParent(aaaRfAppender);
				fwRollingPolicy.start();

				SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new 
						SizeBasedTriggeringPolicy<ILoggingEvent>();
				triggeringPolicy.setMaxFileSize(String.format("%dMB", logMaxSize));
				triggeringPolicy.start();

				aaaRfAppender.setRollingPolicy(fwRollingPolicy);
				aaaRfAppender.setTriggeringPolicy(triggeringPolicy);

				PatternLayoutEncoder aaaEncoder = new PatternLayoutEncoder();
				aaaEncoder.setContext(loggerContext);
				aaaEncoder.setPattern("%d %-5level [%thread] %logger{0}: %msg%n");
				aaaEncoder.start();
				aaaRfAppender.setEncoder(aaaEncoder);
				aaaRfAppender.start();
				aaaLogger.addAppender(aaaRfAppender);
			}
			catch (Exception e) {
				logger.error("Unable to log AAA data into file {}. Exiting.", aaaLogFile, e);
			}
		}
		
		if (pfxLogFile != null) {
			try {
				ch.qos.logback.classic.Logger pfxLogger = (ch.qos.logback.classic.Logger)
						LoggerFactory.getLogger("PFX");
				LoggerContext pfxLoggerContext = pfxLogger.getLoggerContext();
				pfxLogger.setLevel(Level.ALL);
				RollingFileAppender<ILoggingEvent> pfxRfAppender = new RollingFileAppender<ILoggingEvent>();
				pfxRfAppender.setContext(pfxLoggerContext);
				pfxRfAppender.setFile(pfxLogFile);

				FixedWindowRollingPolicy fwRollingPolicy = new FixedWindowRollingPolicy();
				fwRollingPolicy.setContext(pfxLoggerContext);
				fwRollingPolicy.setFileNamePattern(pfxLogFile + ".%i.gz");
				fwRollingPolicy.setMinIndex(1);
				fwRollingPolicy.setMaxIndex(logCount);
				fwRollingPolicy.setParent(pfxRfAppender);
				fwRollingPolicy.start();

				SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new 
						SizeBasedTriggeringPolicy<ILoggingEvent>();
				triggeringPolicy.setMaxFileSize(String.format("%dMB", logMaxSize));
				triggeringPolicy.start();

				pfxRfAppender.setRollingPolicy(fwRollingPolicy);
				pfxRfAppender.setTriggeringPolicy(triggeringPolicy);

				PatternLayoutEncoder pfxEncoder = new PatternLayoutEncoder();
				pfxEncoder.setContext(loggerContext);
				pfxEncoder.setPattern("%d %-5level [%thread] %logger{0}: %msg%n");
				pfxEncoder.start();
				pfxRfAppender.setEncoder(pfxEncoder);
				pfxRfAppender.start();
				pfxLogger.addAppender(pfxRfAppender);
			}
			catch (Exception e) {
				logger.error("Unable to log pfx data into file {}. Exiting.", pfxLogFile, e);
			}
		}

		return true;
	}
	
	public static Thread.UncaughtExceptionHandler exceptioHandler = new Thread.UncaughtExceptionHandler() {
		public void uncaughtException(Thread th, Throwable ex) {
			System.err.println("BLT FATAL ERROR");
			ex.printStackTrace();
			System.exit(1);
		}
	};

	public static void main(String[] args) {

		System.out.println(String.format("Starting Blt version %s.",
				Blt.VERSION));
		logger.info("Starting Blt");

		if (!Blt.initConfig()) {
			System.exit(1);
		}
		if (!Blt.initLogging()) {
			System.exit(1);
		}

		try {
			logger.info("Starting the task manager");
			TaskManager.init();
			logger.info("Starting the BGP service");
			BgpService.init();
			logger.info("Starting the topology service");
			TopologyService.init();
			logger.info("Starting the REST service");
			RestService.init();
		}
		catch (Exception e) {
			System.err.println("BLT FATAL ERROR");
			e.printStackTrace();
			System.exit(1);
		}
	}

}
