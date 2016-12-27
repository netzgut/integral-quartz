/**
 * Copyright 2016 Netzgut GmbH <info@netzgut.net>
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

@DisallowConcurrentExecution
public class IntegralQuartzJobExecutor implements Job {

    private static final Logger log = LoggerFactory.getLogger(IntegralQuartzJobExecutor.class);

    public IntegralQuartzJobExecutor() {
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

            // resetting integral job context is kind of redundant since the PerthreadManager cleanup resets the
            // service
            integralJobContext.removeRunningJob();
            objectLocator.getService(PerthreadManager.class).cleanup();
        }
    }

    @SuppressWarnings("unchecked")
    private Class<IntegralQuartzJob> loadJobClass(JobExecutionContext context) throws ClassNotFoundException {
        Class<IntegralQuartzJob> jobClazz;
        String clazzName =
            (String) context.getJobDetail().getJobDataMap().get(InternalQuartzConstants.JOB_CLAZZNAME_DATAMAP_KEY);
        log.debug("loading job class {}", clazzName);
        jobClazz = (Class<IntegralQuartzJob>) Thread.currentThread().getContextClassLoader().loadClass(clazzName);
        return jobClazz;
    }

    private ObjectLocator retrieveObjectLocator(JobExecutionContext context) {
        ObjectLocator registry = (ObjectLocator) context.get(InternalQuartzConstants.OBJECT_LOCATOR_JOB_DATA_MAP_KEY);
        return registry;
    }

}
