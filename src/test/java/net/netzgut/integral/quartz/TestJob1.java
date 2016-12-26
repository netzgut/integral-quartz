package net.netzgut.integral.quartz;

import java.util.Date;

import org.quartz.SchedulerException;

public class TestJob1 implements IntegralQuartzJob {

    public static boolean                RUN = false;

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
            RUN = true;
        }
        catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

}
