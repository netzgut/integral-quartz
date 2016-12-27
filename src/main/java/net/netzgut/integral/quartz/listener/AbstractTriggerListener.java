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
package net.netzgut.integral.quartz.listener;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;

public abstract class AbstractTriggerListener implements TriggerListener {

    private final String name;

    public AbstractTriggerListener(String name) {
        super();
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        // NO-OP, override if needed
    }

    /**
     * returns false by default, no veto
     */
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        // NO-OP, override if needed
    }

    @Override
    public void triggerComplete(Trigger trigger,
                                JobExecutionContext context,
                                CompletedExecutionInstruction triggerInstructionCode) {
        // NO-OP, override if needed
    }

}
