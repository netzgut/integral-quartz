package net.netzgut.integral.quartz;

import java.util.function.BiFunction;

import org.quartz.JobDetail;
import org.quartz.Trigger;

public interface JobSchedulingBundle {

    JobDetail getJobDetail();

    Trigger getTrigger();

    BiFunction<Trigger, Trigger, Boolean> getTriggerComparator();
}
