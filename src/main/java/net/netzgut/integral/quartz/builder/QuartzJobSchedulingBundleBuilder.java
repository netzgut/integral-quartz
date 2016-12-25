package net.netzgut.integral.quartz.builder;

import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import net.netzgut.integral.quartz.JobSchedulingBundle;
import net.netzgut.integral.quartz.tapestry.MycreonQuartzJob;
import net.netzgut.integral.quartz.tapestry.MycreonQuartzSchedulingBundle;

public class QuartzJobSchedulingBundleBuilder {

    private Class<? extends MycreonQuartzJob>     jobClass;
    private Supplier<Trigger>                     triggerBuilder;
    private Date                                  triggerStartDate = new Date();
    private BiFunction<Trigger, Trigger, Boolean> triggerComparator;

    public QuartzJobSchedulingBundleBuilder() {
        super();
        simpleTriggerKeyDiff(); // default mode
    }

    public QuartzJobSchedulingBundleBuilder jobClass(Class<? extends MycreonQuartzJob> jobClass) {
        this.jobClass = jobClass;
        return this;
    }

    public QuartzJobSchedulingBundleBuilder triggerHourly(int hours) {
        this.triggerBuilder =
            () -> TriggerBuilder.newTrigger() //
                                .withIdentity(buildIdentity(this.jobClass, "triggerHourly", hours))
                                .startAt(this.triggerStartDate) //
                                .withSchedule(SimpleScheduleBuilder.repeatHourlyForever(hours)).build();
        return this;
    }

    public QuartzJobSchedulingBundleBuilder triggerMinutely(int minutes) {
        this.triggerBuilder = () -> TriggerBuilder.newTrigger() //
                                                  .withIdentity(buildIdentity(this.jobClass,
                                                                              "triggerMinutely",
                                                                              minutes))
                                                  .startAt(this.triggerStartDate) //
                                                  .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(minutes))
                                                  .build();
        return this;
    }

    public QuartzJobSchedulingBundleBuilder triggerSecondly(int seconds) {
        this.triggerBuilder = () -> TriggerBuilder.newTrigger() //
                                                  .withIdentity(buildIdentity(this.jobClass,
                                                                              "triggerSecondly",
                                                                              seconds))
                                                  .startAt(this.triggerStartDate) //
                                                  .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(seconds))
                                                  .build();
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
                                       + MycreonQuartzSchedulingBundle.class.getName());
        }
        if (this.triggerBuilder == null) {
            throw new RuntimeException("Missing trigger when building "
                                       + MycreonQuartzSchedulingBundle.class.getName());
        }
        configuration.add(this.jobClass.getName(),
                          new MycreonQuartzSchedulingBundle(this.jobClass,
                                                            this.triggerBuilder.get(),
                                                            this.triggerComparator));
    }

    private String buildIdentity(Class<? extends MycreonQuartzJob> jobClass, String type, int value) {
        return String.format("%s:%s:%d", jobClass.getName(), type, value);
    }

}
