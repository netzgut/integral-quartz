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

    public SchedulerFactory build() throws SchedulerException {
        Properties props = new Properties();
        props.putAll(this.settings);
        return new StdSchedulerFactory(props);
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

    public QuartzSchedulerFactoryBuilder withRamDefaultSettings() {
        return schedulerInstanceName("MasterScheduler").schedulerInstanceId("AUTO")
                                                       .schedulerJobFactoryClass(org.quartz.simpl.SimpleJobFactory.class)
                                                       .threadPoolClass(org.quartz.simpl.SimpleThreadPool.class)
                                                       .threadPoolThreadCount(3)
                                                       .threadPoolThreadPriority(Thread.NORM_PRIORITY)
                                                       .jobStoreMisfireThreshold(60_000)
                                                       .jobStoreClass(org.quartz.simpl.RAMJobStore.class);

    }

    public QuartzSchedulerFactoryBuilder withJdbcDefaultSettings(ConnectionProvider connectionProvider) {
        return withRamDefaultSettings().jobStoreClass(org.quartz.impl.jdbcjobstore.JobStoreTX.class)
                                       .jobStoreDriverDelegateClass(org.quartz.impl.jdbcjobstore.StdJDBCDelegate.class)
                                       .jobStoreUseProperties(true).jobStoreTablePrefix("QRTZ_")
                                       .jobStoreIsClustered(true).schedulerIdleWaitTime(Duration.ofMinutes(1))
                                       .jobStoreClusterCheckinInterval(Duration.ofMinutes(1))
                                       .jobStoreConnectionProvider(connectionProvider);
    }

    // #============================================================================
    // # Configure Main Scheduler Properties
    // #============================================================================

    public QuartzSchedulerFactoryBuilder schedulerInstanceName(String instanceName) {
        return set("org.quartz.scheduler.instanceName", instanceName);
    }

    public QuartzSchedulerFactoryBuilder schedulerInstanceId(String instanceId) {
        return set("org.quartz.scheduler.instanceId", instanceId);
    }

    public QuartzSchedulerFactoryBuilder schedulerJobFactoryClass(Class<? extends JobFactory> jobFactoryClass) {
        return set("org.quartz.scheduler.jobFactory.class", jobFactoryClass.getName());
    }

    // #============================================================================
    // # Configure ThreadPool
    // #============================================================================
    public QuartzSchedulerFactoryBuilder threadPoolClass(Class<? extends ThreadPool> threadPoolClass) {
        return set("org.quartz.threadPool.class", threadPoolClass.getName());
    }

    public QuartzSchedulerFactoryBuilder threadPoolThreadCount(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException(String.format("Thread count (%d) must be greater than zero",
                                                             threadCount));
        }
        return set("org.quartz.threadPool.threadCount", String.valueOf(threadCount));
    }

    public QuartzSchedulerFactoryBuilder threadPoolThreadPriority(int threadPriority) {
        if (threadPriority < Thread.MIN_PRIORITY || threadPriority > Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException(String.format("Thread priority has to be between %d and %d",
                                                             Thread.MIN_PRIORITY,
                                                             Thread.MAX_PRIORITY));
        }
        return set("org.quartz.threadPool.threadPriority", String.valueOf(threadPriority));
    }

    // #============================================================================
    // # Configure JobStore
    // #============================================================================

    public QuartzSchedulerFactoryBuilder jobStoreMisfireThreshold(int misfireThreshold) {
        return set("org.quartz.jobStore.misfireThreshold", String.valueOf(misfireThreshold));
    }

    public QuartzSchedulerFactoryBuilder jobStoreClass(Class<? extends JobStore> jobStore) {
        return set("org.quartz.jobStore.class", jobStore.getName());
    }

    public QuartzSchedulerFactoryBuilder jobStoreDriverDelegateClass(Class<? extends DriverDelegate> driverDelegateClass) {
        return set("org.quartz.jobStore.driverDelegateClass", driverDelegateClass.getName());
    }

    public QuartzSchedulerFactoryBuilder jobStoreUseProperties(boolean useProperties) {
        return set("org.quartz.jobStore.useProperties", String.valueOf(useProperties));
    }

    public QuartzSchedulerFactoryBuilder jobStoreTablePrefix(String tablePrefix) {
        return set("org.quartz.jobStore.tablePrefix", tablePrefix);
    }

    public QuartzSchedulerFactoryBuilder jobStoreIsClustered(boolean isClustered) {
        return set("org.quartz.jobStore.isClustered", String.valueOf(isClustered));
    }

    /**
     * time, before a scheduler with no "job" searches for new jobs
     */
    public QuartzSchedulerFactoryBuilder schedulerIdleWaitTime(Duration idleWaitTime) {
        return set("org.quartz.scheduler.idleWaitTime", String.valueOf(idleWaitTime.toMillis()));
    }

    /**
     * time, before a cluster node says "hello" (affects recovery of misfired jobs)
     */
    public QuartzSchedulerFactoryBuilder jobStoreClusterCheckinInterval(Duration clusterCheckinInterval) {
        return set("org.quartz.jobStore.clusterCheckinInterval", String.valueOf(clusterCheckinInterval.toMillis()));
    }

    // TODO: check if we have to cleanup connection provider when adding more than one data source or after shutdown of cluster / app....?
    public QuartzSchedulerFactoryBuilder jobStoreConnectionProvider(ConnectionProvider connectionProvider) {
        if (connectionProvider == null) {
            throw new IllegalArgumentException("Parameter \"connectionProvider\" of jobStoreConnectionProvider cannot be null");
        }

        String id = UUID.randomUUID().toString();
        DBConnectionManager.getInstance().addConnectionProvider(id, connectionProvider);
        return set("org.quartz.jobStore.dataSource", id);
    }

}
