/**
 * Copyright 2017 Netzgut GmbH <info@netzgut.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.netzgut.integral.quartz.builder;

public class QuartzSchedulerFactoryConstants {

    public static final String SKIP_UPDATE_CHECK                 = "org.quartz.scheduler.skipUpdateCheck";

    public static final String SCHEDULER_INSTANCE_NAME           = "org.quartz.scheduler.instanceName";
    public static final String SCHEDULER_INSTANCE_ID             = "org.quartz.scheduler.instanceId";
    public static final String SCHEDULER_JOB_FACTORY_CLASS       = "org.quartz.scheduler.jobFactory.class";
    public static final String SCHEDULER_IDLE_WAIT_TIME          = "org.quartz.scheduler.idleWaitTime";

    public static final String THREADPOOL_CLASS                  = "org.quartz.threadPool.class";
    public static final String THREADPOOL_THREAD_COUNT           = "org.quartz.threadPool.threadCount";
    public static final String THREADPOOL_THREAD_PRIORITY        = "org.quartz.threadPool.threadPriority";

    public static final String JOBSTORE_MISFIRE_THRESHOLD        = "org.quartz.jobStore.misfireThreshold";
    public static final String JOBSTORE_CLASS                    = "org.quartz.jobStore.class";
    public static final String JOBSTORE_DRIVER_DELEGATE_CLASS    = "org.quartz.jobStore.driverDelegateClass";
    public static final String JOBSTORE_USE_PROPERTIES           = "org.quartz.jobStore.useProperties";
    public static final String JOBSTORE_TABLE_PREFIX             = "org.quartz.jobStore.tablePrefix";
    public static final String JOBSTORE_IS_CLUSTERED             = "org.quartz.jobStore.isClustered";
    public static final String JOBSTORE_CLUSTER_CHECKIN_INTERVAL = "org.quartz.jobStore.clusterCheckinInterval";
    public static final String JOBSTORE_DATASOURCE               = "org.quartz.jobStore.dataSource";

}
