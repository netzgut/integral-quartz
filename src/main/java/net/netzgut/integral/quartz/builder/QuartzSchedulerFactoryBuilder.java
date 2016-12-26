package net.netzgut.integral.quartz.builder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.jdbcjobstore.DriverDelegate;
import org.quartz.spi.JobFactory;
import org.quartz.spi.JobStore;
import org.quartz.spi.ThreadPool;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;

public class QuartzSchedulerFactoryBuilder {

    private final Map<String, String> settings = new HashMap<>();

    public QuartzSchedulerFactoryBuilder() {
        super();
    }

    public SchedulerFactory build() {
        try {
            Properties props = new Properties();
            props.putAll(this.settings);
            return new StdSchedulerFactory(props);
        }
        catch (SchedulerException ex) {
            throw new RuntimeException(ex);

        }
    }

    /**
     * @param propertyName Can't be blank
     * @param value If blank the property will be removed from the builder.
     * @throws IllegalArgumentException If propertyName is null or empty.
     * @return The builder itself for fluent usage.
     */
    public QuartzSchedulerFactoryBuilder set(String propertyName, String value) {
        // We can't do anything without a valid propertyName
        if (InternalUtils.isBlank(propertyName)) {
            throw new IllegalArgumentException("propertyName must not be blank");
        }

        // If we don't get a value we remove the setting
        if (value == null || value.length() == 0) {
            this.settings.remove(propertyName);
            return this;
        }

        this.settings.put(propertyName, value);
        return this;
    }

    public static QuartzSchedulerFactoryBuilder withRamDefaultSettings() {
        return new QuartzSchedulerFactoryBuilder().schedulerInstanceName("MasterScheduler").schedulerInstanceId("AUTO")
                                                  .schedulerJobFactoryClass(org.quartz.simpl.SimpleJobFactory.class)
                                                  .threadPoolClass(org.quartz.simpl.SimpleThreadPool.class)
                                                  .threadPoolThreadCount(3)
                                                  .threadPoolThreadPriority(Thread.NORM_PRIORITY)
                                                  .jobStoreMisfireThreshold(60_000)
                                                  .jobStoreClass(org.quartz.simpl.RAMJobStore.class)
                                                  .skipUpdateCheck(true);

    }

    public static QuartzSchedulerFactoryBuilder withJdbcDefaultSettings(ConnectionProvider connectionProvider) {
        return withRamDefaultSettings().jobStoreClass(org.quartz.impl.jdbcjobstore.JobStoreTX.class)
                                       .jobStoreDriverDelegateClass(org.quartz.impl.jdbcjobstore.StdJDBCDelegate.class)
                                       .jobStoreUseProperties(true).jobStoreTablePrefix("QRTZ_")
                                       .jobStoreIsClustered(true).schedulerIdleWaitTime(Duration.ofMinutes(1))
                                       .jobStoreClusterCheckinInterval(Duration.ofMinutes(1))
                                       .jobStoreConnectionProvider(connectionProvider);
    }

    public QuartzSchedulerFactoryBuilder skipUpdateCheck(boolean skipUpdateCheck) {
        return set(QuartzSchedulerFactoryConstants.SKIP_UPDATE_CHECK, String.valueOf(skipUpdateCheck));
    }

    // #============================================================================
    // # Configure Main Scheduler Properties
    // #============================================================================

    /***
     * <p>Can be any string, and the value has no meaning to the scheduler itself
     * - but rather serves as a mechanism for client code to distinguish
     * schedulers when multiple instances are used within the same program.</p>
     * <p>If you are using the clustering features, you must use the same name for
     * every instance in the cluster that is ‘logically’ the same Scheduler.</p>
     */
    public QuartzSchedulerFactoryBuilder schedulerInstanceName(String instanceName) {
        return set(QuartzSchedulerFactoryConstants.SCHEDULER_INSTANCE_NAME, instanceName);
    }

    /**
     * <p>Can be any string, but must be unique for all schedulers working as if
     * they are the same ‘logical’ Scheduler within a cluster. You may use the
     * value “AUTO” as the instanceId if you wish the Id to be generated for you.</p>
     * <p>Or the value “SYS_PROP” if you want the value to come from the system property
     * <pre>org.quartz.scheduler.instanceId</pre>.</p>
     */
    public QuartzSchedulerFactoryBuilder schedulerInstanceId(String instanceId) {
        return set(QuartzSchedulerFactoryConstants.SCHEDULER_INSTANCE_ID, instanceId);
    }

    /**
     * <p>The class name of the JobFactory to use.</p>
     * <p>The default is <pre>org.quartz.simpl.SimpleJobFactory</pre>, you may like
     * to try <pre>org.quartz.simpl.PropertySettingJobFactory</pre>.</p>
     * <p>A JobFactory is responsible for producing instances of JobClasses.
     * SimpleJobFactory simply calls newInstance() on the class.
     * PropertySettingJobFactory does as well, but also reflectively sets the job’s
     * bean properties using the contents of the SchedulerContext (since 2.0.2) and
     * Job and Trigger JobDataMaps.</p>
     */
    public QuartzSchedulerFactoryBuilder schedulerJobFactoryClass(Class<? extends JobFactory> jobFactoryClass) {
        return set(QuartzSchedulerFactoryConstants.SCHEDULER_JOB_FACTORY_CLASS, jobFactoryClass.getName());
    }

    /**
     * <p>The amount of time in milliseconds that the scheduler will wait before
     * re-queries for available triggers when the scheduler is otherwise idle.</p>
     * <p>Normally you should not have to ‘tune’ this parameter, unless you’re
     * using XA transactions, and are having problems with delayed firings of
     * triggers that should fire immediately.</p>
     * <p>Values less than 5000 ms are not recommended as it will cause excessive
     * database querying. Values less than 1000 are not legal.</p>
     */
    public QuartzSchedulerFactoryBuilder schedulerIdleWaitTime(Duration idleWaitTime) {
        if (idleWaitTime.toMillis() <= 1_000l) {
            throw new IllegalArgumentException(String.format("Idle Wait Time must be greater than or equal 1000ms. Current: '%d'ms",
                                                             idleWaitTime));
        }

        return set(QuartzSchedulerFactoryConstants.SCHEDULER_IDLE_WAIT_TIME, String.valueOf(idleWaitTime.toMillis()));
    }

    // #============================================================================
    // # Configure ThreadPool
    // #============================================================================

    /**
     * <p>Name of the ThreadPool implementation you wish to use.</p>
     * <p>The threadpool that ships with Quartz is <pre>org.quartz.simpl.SimpleThreadPool</pre>,
     * and should meet the needs of nearly every user. It has very simple behavior and
     * is very well tested.</p>
     * <p>It provides a fixed-size pool of threads that ‘live’ the lifetime of the Scheduler.</p>
     */
    public QuartzSchedulerFactoryBuilder threadPoolClass(Class<? extends ThreadPool> threadPoolClass) {
        if (threadPoolClass == null) {
            throw new IllegalArgumentException("Thread Pool Class must not be null");
        }
        return set(QuartzSchedulerFactoryConstants.THREADPOOL_CLASS, threadPoolClass.getName());
    }

    /**
     * <p>This is the number of threads that are available for concurrent execution of jobs.</p>
     * <p>Can be any positive integer, although you should realize that only numbers between 1 and 100 are very practical.</p>
     * <p>If you only have a few jobs that fire a few times a day, then 1 thread is plenty!
     * If you have tens of thousands of jobs, with many firing every minute, then you probably
     * want a thread count more like 50 or 100 (this highly depends on the nature of
     * the work that your jobs perform, and your systems resources!).</p>
     */
    public QuartzSchedulerFactoryBuilder threadPoolThreadCount(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException(String.format("Thread count (%d) must be greater than zero",
                                                             threadCount));
        }
        return set(QuartzSchedulerFactoryConstants.THREADPOOL_THREAD_COUNT, String.valueOf(threadCount));
    }

    /**
     * Can be any int between Thread.MIN_PRIORITY (which is 1) and
     * Thread.MAX_PRIORITY (which is 10). The default is Thread.NORM_PRIORITY (5).
     */
    public QuartzSchedulerFactoryBuilder threadPoolThreadPriority(int threadPriority) {
        if (threadPriority < Thread.MIN_PRIORITY || threadPriority > Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException(String.format("Thread priority has to be between %d and %d",
                                                             Thread.MIN_PRIORITY,
                                                             Thread.MAX_PRIORITY));
        }
        return set(QuartzSchedulerFactoryConstants.THREADPOOL_THREAD_PRIORITY, String.valueOf(threadPriority));
    }

    // #============================================================================
    // # Configure JobStore
    // #============================================================================

    public QuartzSchedulerFactoryBuilder jobStoreMisfireThreshold(int misfireThreshold) {
        return set(QuartzSchedulerFactoryConstants.JOBSTORE_MISFIRE_THRESHOLD, String.valueOf(misfireThreshold));
    }

    public QuartzSchedulerFactoryBuilder jobStoreClass(Class<? extends JobStore> jobStore) {
        return set(QuartzSchedulerFactoryConstants.JOBSTORE_CLASS, jobStore.getName());
    }

    public QuartzSchedulerFactoryBuilder jobStoreDriverDelegateClass(Class<? extends DriverDelegate> driverDelegateClass) {
        return set(QuartzSchedulerFactoryConstants.JOBSTORE_DRIVER_DELEGATE_CLASS, driverDelegateClass.getName());
    }

    public QuartzSchedulerFactoryBuilder jobStoreUseProperties(boolean useProperties) {
        return set(QuartzSchedulerFactoryConstants.JOBSTORE_USE_PROPERTIES, String.valueOf(useProperties));
    }

    public QuartzSchedulerFactoryBuilder jobStoreTablePrefix(String tablePrefix) {
        return set(QuartzSchedulerFactoryConstants.JOBSTORE_TABLE_PREFIX, tablePrefix);
    }

    public QuartzSchedulerFactoryBuilder jobStoreIsClustered(boolean isClustered) {
        return set(QuartzSchedulerFactoryConstants.JOBSTORE_IS_CLUSTERED, String.valueOf(isClustered));
    }

    /**
     * time, before a cluster node says "hello" (affects recovery of misfired jobs)
     */
    public QuartzSchedulerFactoryBuilder jobStoreClusterCheckinInterval(Duration clusterCheckinInterval) {
        return set(QuartzSchedulerFactoryConstants.JOBSTORE_CLUSTER_CHECKIN_INTERVAL,
                   String.valueOf(clusterCheckinInterval.toMillis()));
    }

    // TODO: check if we have to cleanup connection provider when adding more than one data source or after shutdown of cluster / app....?
    public QuartzSchedulerFactoryBuilder jobStoreConnectionProvider(ConnectionProvider connectionProvider) {
        if (connectionProvider == null) {
            throw new IllegalArgumentException("Parameter \"connectionProvider\" of jobStoreConnectionProvider cannot be null");
        }

        String id = UUID.randomUUID().toString();
        DBConnectionManager.getInstance().addConnectionProvider(id, connectionProvider);
        return set(QuartzSchedulerFactoryConstants.JOBSTORE_DATASOURCE, id);
    }

}
