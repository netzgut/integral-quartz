package net.netzgut.integral.quartz;

import java.util.Date;

import org.quartz.SchedulerException;

public class TestJob1 implements IntegralQuartzJob {

    private final QuartzSchedulerManager manager;

    public TestJob1(QuartzSchedulerManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void run() {
        try {
            System.out.println("Hello it's "
                               + new Date()
                               + " and quartz is started: "
                               + this.manager.getScheduler().isStarted());
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

}
