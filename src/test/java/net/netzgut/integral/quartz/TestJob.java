package net.netzgut.integral.quartz;

import java.util.Date;

import org.quartz.SchedulerException;

import net.netzgut.integral.quartz.tapestry.MycreonQuartzJob;

public class TestJob implements MycreonQuartzJob {

    private final QuartzSchedulerManager manager;

    public TestJob(QuartzSchedulerManager manager) {
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
