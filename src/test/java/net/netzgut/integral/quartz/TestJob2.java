package net.netzgut.integral.quartz;

public class TestJob2 implements IntegralQuartzJob {

    public TestJob2() {
        super();
    }

    @Override
    public void run() {
        System.out.println("Going to fail....");
        throw new RuntimeException("test");
    }

}
