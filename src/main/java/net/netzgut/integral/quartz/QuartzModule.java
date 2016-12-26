// Copyright 2016 Netzgut GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package net.netzgut.integral.quartz;

import java.util.List;

import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.ioc.annotations.Scope;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.netzgut.integral.internal.quartz.IntegralQuartzJobContextImpl;
import net.netzgut.integral.internal.quartz.QuartzSchedulerManagerImpl;
import net.netzgut.integral.quartz.builder.QuartzSchedulerFactoryBuilder;

public class QuartzModule {

    private static final Logger log = LoggerFactory.getLogger(QuartzModule.class);

    /**
     * override, if you want to have custom configuration!
     */
    public static SchedulerFactory buildSchedulerFactory() {
        return QuartzSchedulerFactoryBuilder.withRamDefaultSettings().build();
    }

    @EagerLoad
    public QuartzSchedulerManager buildQuartzSchedulerManager(final SchedulerFactory schedulerFactory,
                                                              final List<JobSchedulingBundle> jobSchedulingBundles,
                                                              final ObjectLocator objectLocator) {
        return new QuartzSchedulerManagerImpl(schedulerFactory, jobSchedulingBundles, objectLocator);
    }

    @Scope(ScopeConstants.PERTHREAD)
    public IntegralQuartzJobContext buildIntegralQuartzJobContext() {
        log.debug("building new IntegralQuartzJobContext");
        return new IntegralQuartzJobContextImpl();
    }

    /**
     * Invoked when the registry shuts down, giving services a chance to perform any final operations. Service
     * implementations should not attempt to invoke methods on other services (via proxies) as the service proxies may
     * themselves be shutdown.
     */
    @Startup
    public static void onRegistryShutdownKillSchedulers(SchedulerFactory factory, RegistryShutdownHub shutdownHub) {
        shutdownHub.addRegistryWillShutdownListener(() -> {
            try {
                factory.getAllSchedulers().iterator().forEachRemaining(QuartzModule::shutdownScheduler);
            }
            catch (SchedulerException e) {
                throw new RuntimeException("This will never ever happen.");
            }
        });
    }

    // vvvvv PRIVATE HELPERS vvvvv

    private static void handleSchedulerException(Scheduler scheduler, SchedulerException e) {
        String msg =
            String.format("Exception ocurred while shutting down scheduler '%s'. Ignoring, since registry is going to shut down.",
                          scheduler);
        log.warn(msg, e);
    }

    private static void shutdownScheduler(Scheduler scheduler) {
        try {
            scheduler.shutdown(true);
        }
        catch (SchedulerException e) {
            handleSchedulerException(scheduler, e);
        }
    }
}
