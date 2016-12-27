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

import net.netzgut.integral.quartz.IntegralQuartzJob;
import net.netzgut.integral.quartz.IntegralQuartzJobContext;

public class IntegralQuartzJobContextImpl implements IntegralQuartzJobContext {

    /**
     * when running inside quartz job, this is set to the current job class
     */
    private Class<IntegralQuartzJob> currentJobClazz = null;

    public IntegralQuartzJobContextImpl() {
        super();
    }

    @Override
    public boolean isRunning() {
        return this.currentJobClazz != null;
    }

    @Override
    public void setRunningJob(Class<IntegralQuartzJob> jobClass) {
        this.currentJobClazz = jobClass;
    }

    @Override
    public void removeRunningJob() {
        this.currentJobClazz = null;
    }

}
