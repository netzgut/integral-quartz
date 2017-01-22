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
package net.netzgut.integral.internal.quartz;

import java.util.function.BiFunction;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import net.netzgut.integral.quartz.IntegralQuartzJob;
import net.netzgut.integral.quartz.JobSchedulingBundle;

public class IntegralQuartzSchedulingBundle implements JobSchedulingBundle {

    private final Class<? extends IntegralQuartzJob>    job;
    private final Trigger                               trigger;
    private final BiFunction<Trigger, Trigger, Boolean> triggerComparator;

    public IntegralQuartzSchedulingBundle(Class<? extends IntegralQuartzJob> job,
                                          Trigger trigger,
                                          BiFunction<Trigger, Trigger, Boolean> triggerComparator) {
        this.job = job;
        this.trigger = trigger;
        this.triggerComparator = triggerComparator;
    }

    @Override
    public Trigger getTrigger() {
        return this.trigger;
    }

    @Override
    public JobDetail getJobDetail() {
        String jobName = this.job.getName();
        JobDetail jobDetail = JobBuilder.newJob(IntegralQuartzJobExecutor.class).withIdentity(jobName).build();
        jobDetail.getJobDataMap().put(InternalQuartzConstants.JOB_CLAZZNAME_DATAMAP_KEY, jobName);
        return jobDetail;
    }

    @Override
    public BiFunction<Trigger, Trigger, Boolean> getTriggerComparator() {
        return this.triggerComparator;
    }

}
