package net.netzgut.integral.internal.quartz;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.tapestry5.ioc.ObjectLocator;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.netzgut.integral.internal.quartz.utils.SchedulerUtils;
import net.netzgut.integral.quartz.JobSchedulingBundle;
import net.netzgut.integral.quartz.QuartzConstants;
import net.netzgut.integral.quartz.QuartzSchedulerManager;
import net.netzgut.integral.quartz.listener.AbstractTriggerListener;

public class QuartzSchedulerManagerImpl implements QuartzSchedulerManager {

    private static final Logger    log = LoggerFactory.getLogger(QuartzSchedulerManagerImpl.class);

    private final SchedulerFactory schedulerFactory;

    public QuartzSchedulerManagerImpl(SchedulerFactory schedulerFactory,
                                      Collection<JobSchedulingBundle> jobSchedulingBundles,
                                      final ObjectLocator objectLocator) {
        this.schedulerFactory = schedulerFactory;

        try {
            // get all existing jobs
            Set<JobKey> existingJobs = getScheduler().getJobKeys(GroupMatcher.jobGroupStartsWith(""));

            // formatter: off
            Set<JobKey> incomingJobKeys =
                jobSchedulingBundles
                    .stream()
                    .map(JobSchedulingBundle::getJobDetail)
                    .map(JobDetail::getKey)
                    .collect(Collectors.toSet());

            // remove jobs from scheduler that are not in new jobScheduling bundle!
            existingJobs
                .stream()
                .filter(jobKey -> incomingJobKeys.contains(jobKey) == false)
                .forEach(jobKey -> SchedulerUtils.deleteJobQuietly(getScheduler(), jobKey));
            // formatter: on

            // replace different and add new jobs

            for (JobSchedulingBundle bundle : jobSchedulingBundles) {

                boolean found = false;
                for (JobKey jobKey : existingJobs) {
                    if (jobKey.equals(bundle.getJobDetail().getKey())) {
                        found = true;

                        List<? extends Trigger> existingTrigger = getScheduler().getTriggersOfJob(jobKey);

                        if (existingTrigger.size() != 1) {
                            // todo: remove and reschedule job seems better solution here
                            throw new RuntimeException("More than 1 existing trigger of job "
                                                       + jobKey
                                                       + " found, not supported");
                        }

                        if (bundle.getTriggerComparator().apply(existingTrigger.get(0), bundle.getTrigger()) == false) {
                            found = false;
                            SchedulerUtils.deleteJobQuietly(getScheduler(), jobKey);
                        }

                    }
                }

                // new job
                if (found == false) {
                    addBundleToScheduler(bundle);
                }
            }

            getScheduler().getListenerManager()
                          .addTriggerListener(new AbstractTriggerListener("object locator injector") {

                              @Override
                              public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
                                  jobExecutionContext.put(QuartzConstants.OBJECT_LOCATOR_JOB_DATA_MAP_KEY,
                                                          objectLocator);
                              }
                          });

            getScheduler().start();

        }
        catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Scheduler getScheduler() {
        try {
            return this.schedulerFactory.getScheduler();
        }
        catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * adding a job detail/trigger bundle to the named or default scheduler.
     *
     * @param jobSchedulingBundle the job detail/trigger bundle
     */
    private void addBundleToScheduler(JobSchedulingBundle jobSchedulingBundle) throws SchedulerException {
        JobDetail jobDetail = jobSchedulingBundle.getJobDetail();
        Trigger trigger = jobSchedulingBundle.getTrigger();

        Scheduler scheduler = getScheduler();

        String schedulerId = "default";

        String jobName = jobDetail.getKey().getName();

        if (trigger != null) {

            if (log.isInfoEnabled()) {
                String triggerName = trigger.getKey().getName();
                if (trigger instanceof CronTrigger) {
                    triggerName += " (" + ((CronTrigger) trigger).getCronExpression() + ")";
                }

                log.info("schedule job '{}' with trigger '{}' to scheduler '{}'",
                         new Object[] { jobName, triggerName, schedulerId });
            }
            scheduler.scheduleJob(jobDetail, trigger);
        }
        else {
            if (log.isInfoEnabled()) {
                log.info("add job '{}' to scheduler '{}'", jobName, schedulerId);
            }

            scheduler.addJob(jobDetail, true);
        }
    }
}
