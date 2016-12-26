package net.netzgut.integral.internal.quartz;

import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.netzgut.integral.quartz.IntegralQuartzJob;
import net.netzgut.integral.quartz.IntegralQuartzJobContext;
import net.netzgut.integral.quartz.QuartzConstants;

@DisallowConcurrentExecution
public class MycreonQuartzJobExecutor implements Job {

    private static final Logger log = LoggerFactory.getLogger(MycreonQuartzJobExecutor.class);

    public MycreonQuartzJobExecutor() {
        super();
    }

    @Override
    public final void execute(JobExecutionContext context) throws JobExecutionException {
        log.debug("retrieving object locator");
        ObjectLocator objectLocator = retrieveObjectLocator(context);

        log.debug("retrieving integral quartz job context");
        IntegralQuartzJobContext integralJobContext = objectLocator.getObject(IntegralQuartzJobContext.class, null);

        Class<IntegralQuartzJob> jobClazz = null;

        try {
            try {
                jobClazz = loadJobClass(context);
            }
            catch (ClassNotFoundException e) {
                throw new JobExecutionException(e);
            }
            log.debug("updating integral job context");
            integralJobContext.setRunningJob(jobClazz);

            log.debug("autobuilding job clazz '{}'", jobClazz);

            // Inform the others that we are actually in a Quartz job so they can change their behaviour if needed
            IntegralQuartzJob autobuild = objectLocator.autobuild(jobClazz);

            // run the job
            log.debug("executing job: '{}'", jobClazz);
            autobuild.run();
        }
        catch (Exception ex) {
            log.warn("an error occured executing job " + jobClazz + ": " + ex, ex);
            throw new JobExecutionException(ex);
        }
        finally {
            log.debug("cleanup after job execution: '{}'", jobClazz);
            integralJobContext.removeRunningJob();
            objectLocator.getService(PerthreadManager.class).cleanup();
        }
    }

    @SuppressWarnings("unchecked")
    private Class<IntegralQuartzJob> loadJobClass(JobExecutionContext context) throws ClassNotFoundException {
        Class<IntegralQuartzJob> jobClazz;
        String clazzName =
            (String) context.getJobDetail().getJobDataMap().get(QuartzConstants.JOB_CLAZZNAME_DATAMAP_KEY);
        log.debug("loading job class {}", clazzName);
        jobClazz = (Class<IntegralQuartzJob>) Thread.currentThread().getContextClassLoader().loadClass(clazzName);
        return jobClazz;
    }

    private ObjectLocator retrieveObjectLocator(JobExecutionContext context) {
        ObjectLocator registry = (ObjectLocator) context.get(QuartzConstants.OBJECT_LOCATOR_JOB_DATA_MAP_KEY);
        return registry;
    }

}
