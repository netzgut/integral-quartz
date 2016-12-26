package net.netzgut.integral.quartz;

public interface IntegralQuartzJobContext {

    boolean isRunning();

    void setRunningJob(Class<IntegralQuartzJob> jobClass);

    void removeRunningJob();
}
