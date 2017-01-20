# ∫ Integral Quartz

Use the [Quartz scheduler](http://www.quartz-scheduler.org/) easily from Tapestry.

## Why?

We're deeply committed to Tapestry as our daily driver so we like to build some
frameworks to enhance the experience, and now we started to share some of our
stuff under the name _integral_.

## Usage

NOTE: This library isn't released yet on jcenter etc.!

### `build.gradle`:
```groovy
respositories {
  jcenter()
}

dependencies {
    compile "net.netzgut.integral:integral-quartz:0.0.1"
}
```

### Add job

Your job has to implement IntegralQuartzJob. IntegralQuartzJob is like a runnable.

Contribute to QuartzSchedulerManager, use QuartzJobSchedulingBundleBuilder to build trigger and add to configuration:

```java
    @Contribute(QuartzSchedulerManager.class)
    public static void contributeQuartzSchedulerManager(OrderedConfiguration<JobSchedulingBundle> configuration) {

        new QuartzJobSchedulingBundleBuilder() //
                                               .jobClass(TestJob1.class) //
                                               .startDate(new Date()) //
                                               .triggerSecondly(1) //
                                               .build(configuration);

    }
```

Quartz supports multiple triggers per Job. We currently only support one trigger per job.

### Scheduler configuration

Out of the box integral-quartz comes with an RAM-based scheduler with reasonable default settings. If you want to use
quartz in a clustered environemt i.e. with a database JobStore, contribute a service override for the SchedulerFactory
service.

```java
    @Contribute(ServiceOverride.class)
    public static void contributeServiceOverride(@SuppressWarnings("rawtypes") MappedConfiguration<Class, Object> conf,
                                                 @Local SchedulerFactory schedulerFactory) {
        conf.add(SchedulerFactory.class, schedulerFactory);
    }

    public static SchedulerFactory buildTestClusterSchedulerFactory() {
        ConnectionProvider connectionProvider = new ConnectionProvider() {

            @Override
            public void shutdown() throws SQLException {
                // NOOP
            }

            @Override
            public void initialize() throws SQLException {
                // NOOP
            }

            @Override
            public Connection getConnection() throws SQLException {
                Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
                return c;
            }
        };

        return QuartzSchedulerFactoryBuilder.withJdbcDefaultSettings(connectionProvider).build();
    }
```

See QuartzSchedulerFactoryBuilder for more options.

### Failing tasks

When a task fails, it will not refire until the next scheduled trigger fires.

### Nonconcurrent

All jobs are non-concurrent. If a job takes longer than the next firetime, it will not run concurrently.

TODO: check if overlapping triggers are skipped or delayed. 

### Adding / Updating / Removing jobs and triggers

If you use RAM store, the scheduler will build the jobs and triggers every time the registry starts.

If you use JDBC store in clustered mode, the Scheduler will on startup:
 - remove all jobs from JobStore that are not configured any more.
 - remove all jobs from JobStore with a different trigger (see triggerComparator in QuartzJobSchedulingBundleBuilder)
 - add all jobs that are not in the JobStore
So if you want to change a job, you have to change the jobKey or the triggerKey and it will be removed and re-inserted

### Log4J, RequestResponse

TODO: Documentation tasks:
 - Explain possible Log4J configuration for Mapped Diagnostic Context (MDC)
   - MDC.put("requestedUrl", jobClazz.getName());
   - MDC.clear();
 - Explain why Request and Response are not available and why tapestry may crash if services are being used that rely on
   request or response availability. Clear design vs. Mocking
   

## Gradle task uploadArchives

To upload the archives you need to set some project properties:

- snapshot_repository
- snapshot_repository_username
- snapshot_repository_password

The fallbacks are empty strings, so you can build etc. without gradle failing instantly.


## Contribute

If you want to contribute feel free to open issues or provide pull requests. Please read the additional info in the folder `_CONTRIBUTE`.


## License

Apache 2.0 license, see `LICENSE.txt` and `NOTICE.txt` for more details.
