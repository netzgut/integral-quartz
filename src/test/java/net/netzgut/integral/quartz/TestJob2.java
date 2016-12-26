package net.netzgut.integral.quartz;

public class TestJob2 implements IntegralQuartzJob {

    public TestJob2() {
        super();
    }

    @Override
    public void run() {
        throw new RuntimeException("test");
    }

}
