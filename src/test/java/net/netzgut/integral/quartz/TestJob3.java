package net.netzgut.integral.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJob3 implements IntegralQuartzJob {

    private static final Logger log = LoggerFactory.getLogger(TestJob3.class);

    public TestJob3() {
        super();
    }

    @Override
    public void run() {
        log.info("MARK");
    }

}
