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
