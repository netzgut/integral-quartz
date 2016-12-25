package net.netzgut.integral.quartz.tapestry;

import java.util.function.BiFunction;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import net.netzgut.integral.quartz.JobSchedulingBundle;

public class MycreonQuartzSchedulingBundle implements JobSchedulingBundle {

    private final Class<? extends MycreonQuartzJob>     job;
    private final Trigger                               trigger;
    private final BiFunction<Trigger, Trigger, Boolean> triggerComparator;

    public MycreonQuartzSchedulingBundle(Class<? extends MycreonQuartzJob> job,
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
        JobDetail jobDetail = JobBuilder.newJob(MycreonQuartzJobExecutor.class).withIdentity(jobName).build();
        jobDetail.getJobDataMap().put(MycreonQuartzJobExecutor.JOB_CLAZZNAME_DATAMAP_KEY, jobName);
        return jobDetail;
    }

    @Override
    public BiFunction<Trigger, Trigger, Boolean> getTriggerComparator() {
        return this.triggerComparator;
    }

}
