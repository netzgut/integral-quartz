/**
 * Copyright 2017 Netzgut GmbH <info@netzgut.net>
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
