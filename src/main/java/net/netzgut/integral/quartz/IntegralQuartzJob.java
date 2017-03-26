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
package net.netzgut.integral.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Extend your jobs from this interface. <br />
 * The Job will be autobuild each time it is executed, you can use constructor or field based injection.
 * Please take care, that you don't use services that rely on Response/Request objects, since we are not running
 * in an HTTP request.
 */
public interface IntegralQuartzJob extends Job, Runnable {

    @Override
    default void execute(JobExecutionContext context) throws JobExecutionException {
        run();
    }

    @Override
    default void run() {
        // NOOP
    }
}
