package net.netzgut.integral.quartz.tapestry;

import java.time.Duration;

import org.apache.log4j.MDC;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.netzgut.integral.quartz.QuartzConstants;

@DisallowConcurrentExecution
public class MycreonQuartzJobExecutor implements Job {

    private static final Logger                              log                       =
        LoggerFactory.getLogger(MycreonQuartzJobExecutor.class);

    public static final String                               JOB_CLAZZNAME_DATAMAP_KEY = "mycreon-quartz-job-classname";

    public static final ThreadLocal<Class<MycreonQuartzJob>> currentJobClazz           = new ThreadLocal<>();

    public MycreonQuartzJobExecutor() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void execute(JobExecutionContext context) throws JobExecutionException {

        ObjectLocator objectLocator = retrieveObjectLocator(context);
        //RequestGlobals requestGlobals = objectLocator.getService(RequestGlobals.class);
        Class<MycreonQuartzJob> jobClazz = null;
        boolean currentJobClazzRewritten = false;
        Class<MycreonQuartzJob> oldCurrentJobClazzValue = null;

        try {
            MycreonQuartzJobRequestMockImplementation request = new MycreonQuartzJobRequestMockImplementation();
            MycreonQuartzJobResponseMockImplementation response = new MycreonQuartzJobResponseMockImplementation();

            // some services need the Request and Response services to be available
            // we use here some mock objects since in the quarz execution context
            // there is naturally no request and response object set
            // requestGlobals.storeRequestResponse(request, response);
            try {
                String clazzName = (String) context.getJobDetail().getJobDataMap().get(JOB_CLAZZNAME_DATAMAP_KEY);
                jobClazz = (Class<MycreonQuartzJob>) Class.forName(clazzName);
                MDC.put("requestedUrl", jobClazz.getName());
            }
            catch (ClassNotFoundException e) {
                throw new JobExecutionException(e);
            }

            // Inform the others that we are actually in a Quartz job so they can change their behaviour if needed
            oldCurrentJobClazzValue = currentJobClazz.get();
            currentJobClazz.set(jobClazz);
            currentJobClazzRewritten = true;
            MycreonQuartzJob autobuild = objectLocator.autobuild(jobClazz);
            autobuild.run();
        }
        catch (Exception ex) {
            // FIXME: job re-execution should be tested and hardened (FG, 2014-04-03)
            log.error("An error occured executing quarz job " + jobClazz + ". Refiring in 10 Minutes", ex);
            JobExecutionException e = new JobExecutionException(ex);
            try {
                // refire
                Thread.sleep(Duration.ofMinutes(10).toMillis());
                e.setRefireImmediately(true);
                throw e;
            }
            catch (InterruptedException ignore) {
                // IGNORE
            }
        }
        finally {
            MDC.clear();
            //requestGlobals.storeRequestResponse(null, null); // QUESTION: this should not be necessary with perthread manager cleanup afterwards?
            objectLocator.getService(PerthreadManager.class).cleanup();

            if (currentJobClazzRewritten) {
                currentJobClazz.set(oldCurrentJobClazzValue);
            }
        }
    }

    private ObjectLocator retrieveObjectLocator(JobExecutionContext context) {
        ObjectLocator registry = (ObjectLocator) context.get(QuartzConstants.OBJECT_LOCATOR_JOB_DATA_MAP_KEY);
        return registry;
    }

}
