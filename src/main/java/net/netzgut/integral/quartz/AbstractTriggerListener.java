package net.netzgut.integral.quartz;

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
