package net.netzgut.integral.quartz;

import org.apache.tapestry5.ioc.RegistryBuilder;

public class BasicTest {

    public static void main(String[] args) {
        //BasicConfigurator.configure();
        System.out.println("starting basic test");
        new BasicTest().start();
    }

    private synchronized void start() {

        RegistryBuilder builder = new RegistryBuilder().add(QuartzTestModule.class);
        builder.build().performRegistryStartup();
        System.out.println("registry started");

        System.out.println("done?");
    }

}
