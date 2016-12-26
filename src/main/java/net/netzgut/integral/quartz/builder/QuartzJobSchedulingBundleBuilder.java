package net.netzgut.integral.quartz.builder;

import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.quartz.CronScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;

import net.netzgut.integral.internal.quartz.IntegralQuartzSchedulingBundle;
import net.netzgut.integral.quartz.IntegralQuartzJob;
import net.netzgut.integral.quartz.JobSchedulingBundle;

public class QuartzJobSchedulingBundleBuilder {

    private Class<? extends IntegralQuartzJob>    jobClass;
    private Supplier<Trigger>                     triggerBuilder;
    private Date                                  triggerStartDate = new Date();
    private BiFunction<Trigger, Trigger, Boolean> triggerComparator;

    public QuartzJobSchedulingBundleBuilder() {
        super();
        simpleTriggerKeyDiff(); // default mode
    }

    public QuartzJobSchedulingBundleBuilder jobClass(Class<? extends IntegralQuartzJob> jobClass) {
        this.jobClass = jobClass;
        return this;
    }

    public QuartzJobSchedulingBundleBuilder triggerHourly() {
        return triggerHourly(1);
    }

    public QuartzJobSchedulingBundleBuilder triggerHourly(int hours) {
        this.triggerBuilder = () -> TriggerBuilder.newTrigger() //
                                                  .withIdentity(buildIdentity(this.jobClass, "triggerHourly", hours))
                                                  .startAt(this.triggerStartDate) //
                                                  .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(hours)
                                                                                     .withMisfireHandlingInstructionFireNow())
                                                  .build();
        return this;
    }

    public QuartzJobSchedulingBundleBuilder triggerMinutely() {
        return triggerMinutely(1);
    }

    public QuartzJobSchedulingBundleBuilder triggerMinutely(int minutes) {
        this.triggerBuilder = () -> TriggerBuilder.newTrigger() //
                                                  .withIdentity(buildIdentity(this.jobClass,
                                                                              "triggerMinutely",
                                                                              minutes))
                                                  .startAt(this.triggerStartDate) //
                                                  .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(minutes)
                                                                                     .withMisfireHandlingInstructionFireNow())
                                                  .build();
        return this;
    }

    public QuartzJobSchedulingBundleBuilder triggerSecondly(int seconds) {
        this.triggerBuilder = () -> TriggerBuilder.newTrigger() //
                                                  .withIdentity(buildIdentity(this.jobClass,
                                                                              "triggerSecondly",
                                                                              seconds))
                                                  .startAt(this.triggerStartDate) //
                                                  .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(seconds)
                                                                                     .withMisfireHandlingInstructionFireNow())
                                                  .build();
        return this;
    }

    public QuartzJobSchedulingBundleBuilder trigger(CronScheduleBuilder cronScheduleBuilder) {
        this.triggerBuilder = () -> {
            CronTriggerImpl trigger = (CronTriggerImpl) cronScheduleBuilder.build();
            trigger.setKey(new TriggerKey(buildIdentity(this.jobClass, trigger.getCronExpression(), 0)));
            return trigger;
        };

        return this;
    }

    public QuartzJobSchedulingBundleBuilder simpleTriggerKeyDiff() {

        this.triggerComparator = (l, r) -> {
            if (l == null || r == null) {
                if (l == null && r == null) {
                    return true;
                }
                return false;
            }
            return l.getKey().equals(r.getKey());
        };

        return this;
    }

    public QuartzJobSchedulingBundleBuilder startDate(Date startDate) {
        this.triggerStartDate = startDate;
        return this;
    }

    public void build(OrderedConfiguration<JobSchedulingBundle> configuration) {
        if (this.jobClass == null) {
            throw new RuntimeException("Missing job class when building "
                                       + IntegralQuartzSchedulingBundle.class.getName());
        }
        if (this.triggerBuilder == null) {
            throw new RuntimeException("Missing trigger when building "
                                       + IntegralQuartzSchedulingBundle.class.getName());
        }
        configuration.add(this.jobClass.getName(),
                          new IntegralQuartzSchedulingBundle(this.jobClass,
                                                             this.triggerBuilder.get(),
                                                             this.triggerComparator));
    }

    private String buildIdentity(Class<? extends IntegralQuartzJob> jobClass, String type, int value) {
        return String.format("%s:%s:%d", jobClass.getName(), type, value);
    }

}
