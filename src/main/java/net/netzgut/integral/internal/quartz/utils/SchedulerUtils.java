package net.netzgut.integral.internal.quartz.utils;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerUtils {

    private static final Logger log = LoggerFactory.getLogger(SchedulerUtils.class);

    public static void deleteJobQuietly(Scheduler scheduler, JobKey jobKey) {
        if (scheduler == null) {
            log.debug("Scheduler was null for Key '{}'", jobKey);
            return;
        }

        if (jobKey == null) {
            log.debug("JobKey was null for Scheduler '{}'", scheduler);
            return;
        }

        try {
            scheduler.deleteJob(jobKey);
        }
        catch (SchedulerException e) {
            log.debug(String.format("Couldn't delete Job '%s' on Scheduler '%s'",
                                    jobKey.toString(),
                                    scheduler.toString()),
                      e);
        }
    }

}
