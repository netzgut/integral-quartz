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
